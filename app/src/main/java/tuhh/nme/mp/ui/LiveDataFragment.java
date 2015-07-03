package tuhh.nme.mp.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import tuhh.nme.mp.R;
import tuhh.nme.mp.Settings;
import tuhh.nme.mp.remote.RemoteModuleClient;
import tuhh.nme.mp.ui.dialogs.AlertDialogFragment;
import tuhh.nme.mp.ui.dialogs.IndeterminateProgressDialogFragment;


/**
 * The fragment that actually displays live data from remote module.
 */
public class LiveDataFragment extends Fragment
{
    /**
     * The asynchronous connect task.
     */
    private class ConnectAsyncTask extends AsyncTask<Void, Void, Throwable>
    {
        // Inherited documentation.
        @Override
        protected void onPreExecute()
        {
            Activity activity = getActivity();

            if (activity == null)
            {
                Log.w(LiveDataFragment.class.getName(),
                      "Can't access preferences, manually loading defaults.");

                m_Address = Settings.Default.device_address;
                m_Port = Integer.valueOf(Settings.Default.device_port);
            }
            else
            {
                SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(activity);

                m_Address = preferences.getString(Settings.device_address,
                                                  Settings.Default.device_address);
                m_Port = Integer.valueOf(preferences.getString(Settings.device_port,
                                                               Settings.Default.device_port));
            }

            m_ProgressDialog = new IndeterminateProgressDialogFragment();
            m_ProgressDialog.setRetainInstance(true);
            m_ProgressDialog.setCancelable(true);
            m_ProgressDialog.setMessage(R.string.LiveDataFragment_connecting_dialog_message);
            m_ProgressDialog.setOnCancelListener(
                new DialogInterface.OnCancelListener()
                {
                    @Override
                    public void onCancel(DialogInterface dialog)
                    {
                        Activity activity = getActivity();
                        if (activity != null)
                        {
                            getActivity().finish();
                        }
                    }
                });

            m_ProgressDialog.show(getActivity().getFragmentManager(), null);
        }

        // Inherited documentation.
        @Override
        protected Throwable doInBackground(Void... params)
        {
            try
            {
                m_Client = new RemoteModuleClient(InetAddress.getByName(m_Address), m_Port);
            }
            catch (Throwable ex)
            {
                return ex;
            }

            return null;
        }

        // Inherited documentation.
        @Override
        protected void onPostExecute(Throwable ex)
        {
            if (m_ProgressDialog.getActivity() != null)
            {
                m_ProgressDialog.dismiss();
            }

            if (ex == null)
            {
                Log.d(LiveDataFragment.class.getName(), "Client successfully connected.");

                // TODO: Start here the data fetch.
            }
            else
            {
                m_Client = null;
                String error_message;

                AlertDialogFragment dialog = new AlertDialogFragment();
                dialog.setRetainInstance(true);
                dialog.setCancelable(true);
                dialog.setOnDismissListener(
                    new DialogInterface.OnDismissListener()
                    {
                        @Override
                        public void onDismiss(DialogInterface dialog)
                        {
                            Activity activity = getActivity();
                            if (activity != null)
                            {
                                getActivity().finish();
                            }
                        }
                    });

                if (ex instanceof UnknownHostException)
                {
                    error_message = "Unknown host.";
                    dialog.setMessage(R.string.LiveDataFragment_invalid_host_dialog_message);
                }
                else if(ex instanceof ConnectException)
                {
                    error_message = "Connection refused.";
                    dialog.setMessage(R.string.LiveDataFragment_connection_refused_dialog_message);
                }
                else
                {
                    error_message = "Failed to connect to remote device.";
                    dialog.setMessage(R.string.LiveDataFragment_connection_fail_dialog_message);
                }

                Log.e(LiveDataFragment.class.getName(), error_message, ex);

                if (getActivity() != null)
                {
                    dialog.show(getActivity().getFragmentManager(), null);
                }
            }
        }

        /**
         * The address to connect to.
         */
        private String m_Address;
        /**
         * The port to connect to.
         */
        private int m_Port;

        /**
         * The progress dialog that informs the user about connection attempt.
         */
        private IndeterminateProgressDialogFragment m_ProgressDialog;
    }

    /**
     * Instantiates a new LiveDataFragment.
     */
    public LiveDataFragment()
    {
        super();
    }

    // Inherited documentation.
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.live_data_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // Connect to remote module.
        new ConnectAsyncTask().execute();
    }

    // Inherited documentation.
    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (m_Client != null)
        {
            try
            {
                m_Client.close();
                Log.d(LiveDataFragment.class.getName(), "Client closed.");
            }
            catch (Exception ex)
            {
                Log.e(LiveDataFragment.class.getName(),
                      "Error closing the remote module client service.",
                      ex);
            }
        }
    }

    /**
     * The connected RemoteModuleClient that is connected to the remote module.
     */
    RemoteModuleClient m_Client;
}
