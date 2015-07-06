package tuhh.nme.mp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.LineChart;

import tuhh.nme.mp.R;


/**
 * Displays the chart for data preview.
 */
public class HistoryPreviewFragment extends Fragment
{
    // Inherited documentation.
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.history_preview_fragment, container, false);

        m_Chart = (LineChart)view.findViewById(R.id.HistoryPreviewFragment_chart);

        return view;
    }

    /**
     * The chart that displays the data.
     */
    private LineChart m_Chart;
}
