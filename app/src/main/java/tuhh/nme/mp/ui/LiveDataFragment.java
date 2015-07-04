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
import tuhh.nme.mp.remote.RemoteModuleDataFetchAsyncTask;
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
                m_Client.connect();
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

                if (getActivity() != null)
                {
                    if (!getActivity().isDestroyed())
                    {
                        m_DataFetchAsyncTask = new DataFetchAsyncTask();
                        m_DataFetchAsyncTask.start();
                    }
                }
            }
            else
            {
                m_Client = null;

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

                if (ex instanceof ConnectException)
                {
                    Log.d(LiveDataFragment.class.getName(), "Connection refused.", ex);
                    dialog.setMessage(R.string.LiveDataFragment_connection_refused_dialog_message);
                }
                else
                {
                    Log.e(LiveDataFragment.class.getName(),
                          "Failed to connect to remote device.",
                          ex);
                    dialog.setMessage(R.string.LiveDataFragment_connection_fail_dialog_message);
                }

                if (getActivity() != null)
                {
                    dialog.show(getActivity().getFragmentManager(), null);
                }
            }
        }

        /**
         * The progress dialog that informs the user about connection attempt.
         */
        private IndeterminateProgressDialogFragment m_ProgressDialog;
    }

    /**
     * The AsyncTask that fetches continuously data from remote module in background.
     */
    private class DataFetchAsyncTask extends RemoteModuleDataFetchAsyncTask
    {
        /**
         * Instantiates a new DataFetchAsyncTask.
         */
        public DataFetchAsyncTask()
        {
            super(m_Client, getDataFetchRate());
        }

        // Inherited documentation.
        @Override
        protected void onIncomingData(HighPrecisionDatedDataFrame<Short> data)
        {
            // TODO: Update plot data here.
        }
    }

    /**
     * Instantiates a new LiveDataFragment.
     */
    public LiveDataFragment()
    {
        super();

        m_DataFetchAsyncTask = null;
    }

    // Inherited documentation.
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.live_data_fragment, container, false);
    }

    // Inherited documentation.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        Activity activity = getActivity();

        String address;
        int port;

        if (activity == null)
        {
            Log.w(LiveDataFragment.class.getName(),
                  "Can't access preferences, manually loading defaults.");

            address = Settings.Default.device_address;
            port = Integer.valueOf(Settings.Default.device_port);
        }
        else
        {
            SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(activity);

            address = preferences.getString(Settings.device_address,
                                            Settings.Default.device_address);
            port = Integer.valueOf(preferences.getString(Settings.device_port,
                                                         Settings.Default.device_port));
        }

        try
        {
            m_Client = new RemoteModuleClient(InetAddress.getByName(address), port);
            // Connect to remote module.
            new ConnectAsyncTask().execute();
        }
        catch (UnknownHostException ex)
        {
            Log.d(LiveDataFragment.class.getName(), "Unknown host.", ex);

            if (activity != null)
            {
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
                dialog.setMessage(R.string.LiveDataFragment_invalid_host_dialog_message);
                dialog.show(activity.getFragmentManager(), null);
            }
        }
    }

    // Inherited documentation.
    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if (m_DataFetchAsyncTask != null)
        {
            m_DataFetchAsyncTask.stop();
        }

        try
        {
            // TODO: Wait for close asynchronously for a constant amount of time. If still
            // TODO: blocking, then use terminate().
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

    /**
     * Retrieves the setting "data_fetch_rate".
     *
     * @return The data fetch rate.
     */
    private int getDataFetchRate()
    {
        Activity activity = getActivity();

        if (activity == null)
        {
            Log.w(LiveDataFragment.class.getName(),
                  "Can't access preferences, manually loading defaults.");

            return Integer.valueOf(Settings.Default.data_fetch_rate);
        }
        else
        {
            return Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(activity)
                .getString(Settings.data_fetch_rate, Settings.Default.data_fetch_rate));
        }
    }

    /**
     * The connected RemoteModuleClient that is connected to the remote module.
     */
    private RemoteModuleClient m_Client;

    /**
     * The background and continuous data fetch AsyncTask.
     */
    private DataFetchAsyncTask m_DataFetchAsyncTask;
}
