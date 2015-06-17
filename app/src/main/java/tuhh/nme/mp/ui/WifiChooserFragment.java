package tuhh.nme.mp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
            ScanResultAdapter adapter = (ScanResultAdapter)m_ListView.getAdapter();
            adapter.clear();
            adapter.addAll(WifiConnector.getAvailableDevices());
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * The listener that listens for item clicks in the ListView.
     *
     * This listener wraps the public listener WifiChooserFragmentOnItemClickListener.
     */
    private class OnItemClickListener implements AdapterView.OnItemClickListener
    {
        /**
         * Creates a new OnItemClickListener.
         *
         * @param callback The WifiChooserFragmentOnItemClickListener that gets called from this
         *                 listener.
         */
        public OnItemClickListener(@NonNull WifiChooserFragmentOnItemClickListener callback)
        {
            m_Callback = callback;
        }

        // Inherited documentation.
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            m_Callback.onItemClick(parent.getItemAtPosition(position));
        }

        /**
         * Returns the registered callback listener.
         *
         * @return The callback listener.
         */
        public WifiChooserFragmentOnItemClickListener getCallback()
        {
            return m_Callback;
        }

        /**
         * The background listener that is called implicitly when invoking onItemClick().
         */
        private WifiChooserFragmentOnItemClickListener m_Callback;
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
        m_ListView = (ListView)view.findViewById(R.id.WifiChooserFragment_scan_result_list);
        m_ListView.setAdapter(new ScanResultAdapter(this.getActivity(),
                                                    WifiConnector.getAvailableDevices()));
        m_ListView.setOnItemClickListener(m_OnItemClickListener);
    }

    /**
     * Sets the WifiChooserFragmentOnItemClickListener that shall listen to item clicks on the
     * list.
     *
     * @param listener The listener to register. Passing null means no listener gets registered.
     */
    public void setOnItemClickListener(WifiChooserFragmentOnItemClickListener listener)
    {
        if (listener == null)
        {
            m_OnItemClickListener = null;
        }
        else
        {
            m_OnItemClickListener = new OnItemClickListener(listener);
        }

        if (m_ListView != null)
            m_ListView.setOnItemClickListener(m_OnItemClickListener);
    }

    /**
     * Returns the registered WifiChooserFragmentOnItemClickListener that is responsible for
     * handling ListView item clicks.
     *
     * @return The registered WifiChooserFragmentOnItemClickListener. If none registered, returns
     *         null.
     */
    public WifiChooserFragmentOnItemClickListener getOnItemClickListener()
    {
        if (m_OnItemClickListener == null)
        {
            return null;
        }
        else
        {
            return m_OnItemClickListener.getCallback();
        }
    }

    /**
     * The ListView that displays all possible remote modules the user can connect to.
     */
    private ListView m_ListView;

    /**
     * The listener that listens for new WiFi scans available.
     */
    private OnWifiScanResultsAvailableListener m_OnWifiScanResultsAvailableListener;
    /**
     * The listener that attaches to the list view item click.
     */
    private OnItemClickListener m_OnItemClickListener;
}
