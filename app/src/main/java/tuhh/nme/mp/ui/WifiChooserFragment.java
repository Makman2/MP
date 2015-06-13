package tuhh.nme.mp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import tuhh.nme.mp.R;
import tuhh.nme.mp.broadcasts.WifiBroadcastReceiver;
import tuhh.nme.mp.remote.WifiConnector;
import tuhh.nme.mp.ui.controls.adapters.ScanResultAdapter;


/**
 * Displays and let's the user choose the remote module he wants to connect to.
 */
public class WifiChooserFragment extends Fragment
{
    /**
     * Instantiates a new WifiChooserFragment.
     */
    public WifiChooserFragment()
    {
        super();

        m_OnWifiScanResultsAvailableListener = new OnWifiScanResultsAvailableListener();
    }

    /**
     * A listener that listens for new ScanResult's.
     */
    private class OnWifiScanResultsAvailableListener implements
        tuhh.nme.mp.broadcasts.OnWifiScanResultsAvailableListener
    {
        // Inherited documentation.
        @Override
        public void onWifiScanResultsAvailable()
        {
            // Update ScanResult list.
            m_ScanResultAdapter.clear();
            m_ScanResultAdapter.addAll(WifiConnector.getAvailableDevices());
            m_ScanResultAdapter.notifyDataSetChanged();
        }
    }

    // Inherited documentation.
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.wifi_chooser_fragment, container, false);
        initializeView(view);
        return view;
    }

    // Inherited documentation.
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        WifiBroadcastReceiver.attach(m_OnWifiScanResultsAvailableListener);
    }

    // Inherited documentation.
    @Override
    public void onDetach()
    {
        super.onDetach();

        WifiBroadcastReceiver.detach(m_OnWifiScanResultsAvailableListener);
    }

    /**
     * Initializes the states of all Views on this fragment.
     *
     * @param view The fragment View where all other View's reside.
     */
    private void initializeView(View view)
    {
        ListView list = (ListView)view.findViewById(R.id.WifiChooserFragment_scan_result_list);
        m_ScanResultAdapter = new ScanResultAdapter(this.getActivity(),
                                                    WifiConnector.getAvailableDevices());

        list.setAdapter(m_ScanResultAdapter);
    }

    /**
     * The adapter that is responsible for presenting the ScanResult's from the last WiFi scan.
     */
    private ScanResultAdapter m_ScanResultAdapter;
    /**
     * The listener that listens for new WiFi scans available.
     */
    private OnWifiScanResultsAvailableListener m_OnWifiScanResultsAvailableListener;
}
