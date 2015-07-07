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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import tuhh.nme.mp.R;
import tuhh.nme.mp.Settings;
import tuhh.nme.mp.data.DataPoint;
import tuhh.nme.mp.data.HighPrecisionDate;
import tuhh.nme.mp.data.HighPrecisionDatedDataFrame;
import tuhh.nme.mp.data.plotting.HighPrecisionDatedPlotData;
import tuhh.nme.mp.data.plotting.PressurePlotData;
import tuhh.nme.mp.data.storage.DataFrameFileManager;
import tuhh.nme.mp.data.storage.MPDFFormatException;
import tuhh.nme.mp.data.storage.PressureDataFrameWriter;
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
     * Performs an asynchronous client close and terminates if the normal close takes too long.
     */
    private class ConnectionCloseAsyncTask extends AsyncTask<Integer, Void, Void>
    {
        /**
         * Internal close AsyncTask that invokes normal close.
         */
        private class InternalCloseAsyncTask extends AsyncTask<Void, Void, Void>
        {
            // Inherited documentation.
            @Override
            protected Void doInBackground(Void... params)
            {
                // A SocketCommandHandlingException shall lead to an ExecutionException and should
                // be immediately terminated.
                m_Client.close();
                Log.d(LiveDataFragment.class.getName(), "Client closed.");
                return null;
            }
        }

        // Inherited documentation.
        @Override
        protected void onPreExecute()
        {
            m_InternalCloseAsyncTask = new InternalCloseAsyncTask();
            m_InternalCloseAsyncTask.executeOnExecutor(THREAD_POOL_EXECUTOR);
        }

        /**
         * Waits a specific amount of time.
         *
         * @param params The time to sleep in milliseconds.
         */
        @Override
        protected Void doInBackground(Integer... params)
        {
            try
            {
                m_InternalCloseAsyncTask.get(params[0], TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException ex)
            {
                Log.w(LiveDataFragment.class.getName(),
                      "Close attempt interrupted. Terminating client.",
                      ex);

                m_Client.terminate();
            }
            catch (ExecutionException ex)
            {
                Log.w(LiveDataFragment.class.getName(),
                      "Socket raised exception. Terminating client.",
                      ex);

                m_Client.terminate();
            }
            catch (TimeoutException ex)
            {
                Log.w(LiveDataFragment.class.getName(),
                      "Waited for " + Integer.toString(params[0]) +
                          "ms, but peer didn't respond. Terminating client.",
                      ex);

                m_Client.terminate();
            }

            return null;
        }

        private InternalCloseAsyncTask m_InternalCloseAsyncTask;
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
            if (data == null)
            {
                return;
            }

            Entry elem = null;
            Iterator<Entry> it = m_PlotData.add(data).iterator();
            // Don't use a for-each loop since we want to keep the last processed value later.
            while (it.hasNext())
            {
                elem = it.next();
                m_Chart.getLineData().addEntry(elem, 0);
            }

            int xvals = m_Chart.getLineData().getXValCount();

            // Add labels so the last fetched value can be displayed.
            // elem can't be null since we get every time data if it is not null itself.
            for (int i = xvals; i < elem.getXIndex(); i++)
            {
                m_Chart.getLineData().addXValue(Integer.toString(i));
            }

            m_Chart.notifyDataSetChanged();

            // Move view to new incoming values.
            m_Chart.setVisibleXRange(m_VisibleChartXRange);
            m_Chart.moveViewToX(xvals - m_VisibleChartXRange);
            m_Chart.fitScreen();

            m_Chart.invalidate();
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

        // Wait for 10s until we terminate the client thread.
        // We rely on parallel thread execution since the normal close blocks.
        if (m_Client != null)
        {
            new ConnectionCloseAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 10000);
        }

        if (m_PlotData.size() != 0)
        {
            PressureDataFrameWriter writer = new PressureDataFrameWriter();

            for (Iterable<DataPoint<HighPrecisionDate, Short>> frame : m_PlotData.inputs())
            {
                ArrayList<DataPoint<HighPrecisionDate, Short>> data_points = new ArrayList<>();
                for (DataPoint<HighPrecisionDate, Short> point : frame)
                {
                    data_points.add(point);
                }
                writer.add(new HighPrecisionDatedDataFrame<>(data_points));
            }

            try
            {
                writer.write(DataFrameFileManager.create(m_PlotData.getStartingDate()));
            }
            catch (IOException ex)
            {
                // TODO: Display alert dialog.
                Log.w(LiveDataFragment.class.getName(),
                      "Can't save history data for timestamp " +
                          m_PlotData.getStartingDate().toString() + "!",
                      ex);
            }
            catch (MPDFFormatException ex)
            {
                // Can't happen technically since the MPDF-writer writes fixed size data below
                // maximum length.
                Log.e(LiveDataFragment.class.getName(),
                      "Fatal error in " + PressureDataFrameWriter.class.getSimpleName() + ".",
                      ex);
            }
        }
    }

    /**
     * Prepares the chart for live data-preview.
     */
    private void prepareChart()
    {
        // Setup the only data-set.
        ArrayList<Entry> y = new ArrayList<>();
        // Insert a dummy entry since MPAndroidChart crashes with NullPointerException when having
        // a LineDataSet without any Entry. This renders (0,0), but there's no other choice for now.
        y.add(new Entry(0.0f, 0));

        LineDataSet set = new LineDataSet(y, "Data");
        set.setColor(getResources().getColor(R.color.red));
        set.setCircleColor(getResources().getColor(R.color.red));

        // Add the first labels visible in the initial view-x-range.
        m_VisibleChartXRange = getLivescrollingXRange().multiply(new BigDecimal("1000000000"))
                                   .multiply(getChartXScale()).floatValue();

        ArrayList<String> labels = new ArrayList<>();
        int visible_x_range = (int)m_VisibleChartXRange;
        labels.ensureCapacity(visible_x_range);
        for (int i = 0; i < visible_x_range; i++)
        {
            labels.add(Integer.toString(i));
        }

        // Apply data-set and adjust plot properties.
        m_Chart.setData(new LineData(labels, set));
        m_Chart.getLineData().setDrawValues(false);

        m_Chart.setTouchEnabled(false);
        m_Chart.setDescription("");
        m_Chart.getLegend().setEnabled(false);
        m_Chart.getXAxis().setSpaceBetweenLabels(8);
        m_Chart.getAxisLeft().setStartAtZero(false);
        m_Chart.getAxisRight().setEnabled(false);

        // Set up plot data that automatically converts the sent values to Entry's.
        m_PlotData = new PressurePlotData();
        m_PlotData.setTimeScaleAndStartingDate(getChartXScale(), HighPrecisionDate.now());
    }

    /**
     * Retrieves the setting "data_fetch_rate".
     *
     * @return The data fetch rate.
     */
    private RemoteModuleClient.TransferDensity getDataFetchRate()
    {
        Activity activity = getActivity();

        if (activity == null)
        {
            Log.w(LiveDataFragment.class.getName(),
                  "Can't access preferences, manually loading defaults.");

            return RemoteModuleClient.TransferDensity.valueOf(Settings.Default.data_fetch_rate);
        }
        else
        {
            return RemoteModuleClient.TransferDensity.valueOf(
                PreferenceManager.getDefaultSharedPreferences(activity)
                    .getString(Settings.data_fetch_rate, Settings.Default.data_fetch_rate));
        }
    }

    /**
     * Retrieves the setting "chart_x_scale".
     *
     * @return The x-axis scaling for data chart.
     */
    private BigDecimal getChartXScale()
    {
        Activity activity = getActivity();

        if (activity == null)
        {
            Log.w(LiveDataFragment.class.getName(),
                  "Can't access preferences, manually loading defaults.");

            return new BigDecimal(Settings.Default.chart_x_scale);
        }
        else
        {
            return new BigDecimal(PreferenceManager.getDefaultSharedPreferences(activity)
                .getString(Settings.chart_x_scale, Settings.Default.chart_x_scale));
        }
    }

    /**
     * Retrieves the setting "chart_livescroll_x_range_time".
     *
     * @return The visible x-range while livescrolling.
     */
    private BigDecimal getLivescrollingXRange()
    {
        Activity activity = getActivity();

        if (activity == null)
        {
            Log.w(LiveDataFragment.class.getName(),
                  "Can't access preferences, manually loading defaults.");

            return new BigDecimal(Settings.Default.chart_livescroll_x_range_time);
        }
        else
        {
            return new BigDecimal(PreferenceManager.getDefaultSharedPreferences(activity)
                .getString(Settings.chart_livescroll_x_range_time,
                           Settings.Default.chart_livescroll_x_range_time));
        }
    }

    /**
     * Retrieves the setting "auto_save".
     *
     * @return Whether to save live-data.
     */
    public boolean getAutoSave()
    {
        Activity activity = getActivity();

        if (activity == null)
        {
            Log.w(LiveDataFragment.class.getName(),
                  "Can't access preferences, manually loading defaults.");

            return Settings.Default.auto_save;
        }
        else
        {
            return PreferenceManager.getDefaultSharedPreferences(activity)
                .getBoolean(Settings.auto_save, Settings.Default.auto_save);
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

    /**
     * The chart that displays the data.
     */
    private LineChart m_Chart;

    /**
     * The plot data that contains the received DataFrame's.
     */
    private HighPrecisionDatedPlotData<Short> m_PlotData;
    /**
     * The visible chart x-range while livescrolling.
     */
    private float m_VisibleChartXRange;
}
