package tuhh.nme.mp.ui;

import android.app.Activity;
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
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import tuhh.nme.mp.IntentExtra;
import tuhh.nme.mp.R;
import tuhh.nme.mp.Settings;
import tuhh.nme.mp.data.DataPoint;
import tuhh.nme.mp.data.HighPrecisionDate;
import tuhh.nme.mp.data.plotting.PressurePlotData;
import tuhh.nme.mp.data.storage.DataFrameFileManager;
import tuhh.nme.mp.data.storage.MPDFFormatException;
import tuhh.nme.mp.data.storage.PressureDataFrameReader;


/**
 * Displays the chart for data preview.
 */
public class HistoryPreviewFragment extends Fragment
{
    /**
     * The AsyncTask that loads the data in background.
     */
    private class LoadDataAsyncTask extends AsyncTask<Void, Integer, LineData>
    {
        /**
         * Compares the x-indices of two Entry's.
         */
        private class DataPointXHighPrecisionDateComparator
            implements Comparator<DataPoint<HighPrecisionDate, ?>>
        {
            // Inherited documentation.
            @Override
            public int compare(DataPoint<HighPrecisionDate, ?> lhs,
                               DataPoint<HighPrecisionDate, ?> rhs)
            {
                if (lhs.X.less(rhs.X))
                {
                    return -1;
                }
                else if (lhs.X.greater(rhs.X))
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
        }

        // Inherited documentation.
        @Override
        protected void onPreExecute()
        {
            // TODO: Spawn a progress dialog.

            m_Error = null;
            m_LoadParameter = getActivity().getIntent().getParcelableExtra(
                IntentExtra.history_preview_activity_timestamp);
        }

        // Inherited documentation.
        @Override
        protected LineData doInBackground(Void... params)
        {
            PressureDataFrameReader reader = new PressureDataFrameReader();

            ArrayList<DataPoint<HighPrecisionDate, Short>> values;
            try
            {
                InputStream fstream = DataFrameFileManager.openForRead(m_LoadParameter);
                values = new ArrayList<>(reader.read(fstream).getData());
            }
            catch (IOException | MPDFFormatException ex)
            {
                m_Error = ex;
                return null;
            }

            // Remove duplicate x-entries, MPAndroidChart has problems when having more Entry's than
            // labels which can happen if we allow duplicate entries.
            Iterator<DataPoint<HighPrecisionDate, Short>> it = values.iterator();
            // Today is not epoch any more, so use this as an initial value.
            HighPrecisionDate current_loop_value = HighPrecisionDate.epoch;
            while (it.hasNext())
            {
                HighPrecisionDate x_compare = it.next().X;
                if (current_loop_value.equals(x_compare))
                {
                    it.remove();
                }
                else
                {
                    current_loop_value = x_compare;
                }
            }

            Collections.sort(values, new DataPointXHighPrecisionDateComparator());

            HighPrecisionDate min_x_time = values.get(0).X;

            ArrayList<Entry> y = new ArrayList<>();
            PressurePlotData plot_data = new PressurePlotData();
            plot_data.setTimeScaleAndStartingDate(getChartXScale(), min_x_time);
            y.addAll(plot_data.add(values));

            int max_x_index = y.get(y.size() - 1).getXIndex();

            ArrayList<String> labels = new ArrayList<>();
            for (int i = 0; i <= max_x_index; i++)
            {
                labels.add(Integer.toString(i));
            }

            LineDataSet set = new LineDataSet(y, "Data");
            set.setColor(getResources().getColor(R.color.purple));
            set.setCircleColor(getResources().getColor(R.color.purple));

            return new LineData(labels, set);
        }

        // Inherited documentation.
        @Override
        protected void onPostExecute(LineData data)
        {
            if (m_Error == null)
            {
                m_Chart.setData(data);
                m_Chart.getLineData().setDrawValues(false);

                m_Chart.setDescription("");
                m_Chart.getLegend().setEnabled(false);
                m_Chart.getXAxis().setSpaceBetweenLabels(8);

                m_Chart.invalidate();
            }
            else
            {
                if (m_Error instanceof IOException)
                {
                    Log.e(HistoryPreviewFragment.class.getName(),
                          "Can't open history file. Timestamp: " + m_LoadParameter.toString());

                    // TODO: Show an AlertDialog and close the Activity.
                }
                else if (m_Error instanceof MPDFFormatException)
                {
                    Log.e(HistoryPreviewFragment.class.getName(),
                          "Encountered corrupted history file. Timestamp: "
                              + m_LoadParameter.toString());

                    // TODO: Show here also an AlertDialog and close Activity.
                }
            }
        }

        private Throwable m_Error;
        private HighPrecisionDate m_LoadParameter;
    }

    // Inherited documentation.
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.history_preview_fragment, container, false);

        m_Chart = (LineChart)view.findViewById(R.id.HistoryPreviewFragment_chart);

        // Load data.
        new LoadDataAsyncTask().execute();

        return view;
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
     * The chart that displays the data.
     */
    private LineChart m_Chart;
}
