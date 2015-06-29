package tuhh.nme.mp.ui;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import tuhh.nme.mp.R;
import tuhh.nme.mp.Settings;
import tuhh.nme.mp.data.HighPrecisionDate;
import tuhh.nme.mp.data.HighPrecisionDatedDataFrame;
import tuhh.nme.mp.data.plotting.PressurePlotData;
import tuhh.nme.mp.remote.RemoteModuleClient;
import tuhh.nme.mp.remote.RemoteModuleDataFetchAsyncTask;
import tuhh.nme.mp.remote.SocketCommandHandlingException;
import tuhh.nme.mp.ui.dialogs.AlertDialogFragment;


/**
 * The fragment that actually displays live data from remote module.
 */
public class LiveDataFragment extends Fragment
{
    /**
     * The task that fetches data in background.
     */
    private class FetchDataAsyncTask extends RemoteModuleDataFetchAsyncTask
    {
        public FetchDataAsyncTask()
        {
            super(
                m_Client,
                Integer.valueOf(
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(
                        Settings.data_fetch_rate, Settings.Default.data_fetch_rate)));
        }

        // Inherited documentation.
        @Override
        protected void onIncomingData(HighPrecisionDatedDataFrame<Short> data)
        {
            if (data != null)
            {
                for (Entry elem : m_PlotData.add(data))
                {
                    m_Chart.getLineData().addEntry(elem, 0);
                }

                m_Chart.notifyDataSetChanged();
                m_Chart.invalidate();
            }
        }
    }

    /**
     * Instantiates a new LiveDataFragment.
     */
    public LiveDataFragment()
    {
        super();

        m_PlotData = new PressurePlotData();
        m_PlotData.setTimeScaleAndStartingDate(BigDecimal.valueOf(0.0000001), HighPrecisionDate.now());
    }

    // Inherited documentation.
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.live_data_fragment, container, false);
        m_Chart = (LineChart)view.findViewById(R.id.LiveDataFragment_chart);
        prepareChart();
        return view;
    }

    // Inherited documentation.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        connectToRemote();

        // TODO: Don't try to reconnect when user rotates screen. Especially when the connection
        // TODO. failed this is annoying to click on more than one dialog every time.

        // Instantiate FetchDataAsyncTask here because it loads settings that can only be accessed
        // if the attaching activity is known.
        m_FetchDataAsyncTask = new FetchDataAsyncTask();
        m_FetchDataAsyncTask.start();
    }

    // Inherited documentation.
    @Override
    public void onDestroy()
    {
        super.onDestroy();

        m_FetchDataAsyncTask.stop();

        if (m_Client != null)
        {
            try
            {
                m_Client.close();
                Log.d(LiveDataFragment.class.getName(), "Client closed.");
            }
            catch (SocketCommandHandlingException ex)
            {
                Log.e(LiveDataFragment.class.getName(),
                      "Error closing the remote module client service.",
                      ex);
            }
        }
    }

    /**
     * Sets up the MPAndroidChart LineChart correctly so it can dynamically display data.
     */
    private void prepareChart()
    {
        // Initialize an empty plot.
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0.0f, 0));
        LineDataSet dataset = new LineDataSet(entries, "");

        ArrayList<LineDataSet> datas = new ArrayList<>();
        datas.add(dataset);

        ArrayList<String> labels = new ArrayList<>();

        for (int i = 0; i <= 1000; i++)
        {
            labels.add(Integer.toString(i));
        }

        LineData ld = new LineData(labels, datas);

        ld.setDrawValues(false);

        m_Chart.setData(ld);
        m_Chart.invalidate();
    }

    /**
     * Attempts a connect to the remote device with user settings.
     */
    private void connectToRemote()
    {
        // TODO: Make connection threaded over an AsyncTask and show meanwhile a progress
        // TODO: dialog.

        String address = PreferenceManager.getDefaultSharedPreferences(
            getActivity()).getString(Settings.device_address, Settings.Default.device_address);
        int port = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(
            getActivity()).getString(Settings.device_port, Settings.Default.device_port));

        try
        {
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

            // TODO: Close the containing activity after the user clicked the OK button of an error
            // TODO: dialog. Maybe let this function throw an
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

            // TODO: Implement instant close of parent activity here also.
        }
    }

    /**
     * The chart that displays the data.
     */
    private LineChart m_Chart;

    /**
     * The connected RemoteModuleClient that is connected to the remote module.
     */
    private RemoteModuleClient m_Client;

    /**
     * The asynchronous task that fetches data from remote device in background.
     */
    private FetchDataAsyncTask m_FetchDataAsyncTask;
    /**
     * The complete fetched data. Used to organize the plot.
     */
    private final PressurePlotData m_PlotData;
}
