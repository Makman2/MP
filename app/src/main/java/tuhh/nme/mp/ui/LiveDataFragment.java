package tuhh.nme.mp.ui;

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


/**
 * The fragment that actually displays live data from remote module.
 */
public class LiveDataFragment extends Fragment
{
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

        String address = PreferenceManager.getDefaultSharedPreferences(
            getActivity()).getString(Settings.device_address,
                                     Settings.Default.device_address);
        int port = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(
            getActivity()).getString(Settings.device_port,
                                     Settings.Default.device_port));

        try
        {
            // TODO: Make connection threaded over an AsyncTask and show meanwhile a progress
            // TODO: dialog.
            m_Client = new RemoteModuleClient(InetAddress.getByName(address), port);

            Log.d(LiveDataFragment.class.getName(), "Client successfully connected.");
        }
        catch (UnknownHostException ex)
        {
            m_Client = null;

            AlertDialogFragment dialog = new AlertDialogFragment();
            dialog.setMessage(R.string.ManualConnectActivity_invalid_host_dialog_message);
            dialog.show(getActivity().getFragmentManager(), null);
        }
        catch (ConnectException ex)
        {
            m_Client = null;

            Log.e(ManualConnectActivity.class.getName(),
                  "Connection refused.",
                  ex);

            AlertDialogFragment dialog = new AlertDialogFragment();
            dialog.setMessage(R.string.ManualConnectActivity_connection_refused_dialog_message);
            dialog.show(getActivity().getFragmentManager(), null);
        }
        catch (Throwable ex)
        {
            m_Client = null;

            Log.e(ManualConnectActivity.class.getName(),
                  "Failed to connect to remote device.",
                  ex);

            AlertDialogFragment dialog = new AlertDialogFragment();
            dialog.setMessage(R.string.ManualConnectActivity_connection_fail_dialog_message);
            dialog.show(getActivity().getFragmentManager(), null);
        }

        // TODO: Close the containing activity after the user clicked the OK button of an error
        // TODO: dialog.
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
