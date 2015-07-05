package tuhh.nme.mp.ui.controls.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

import tuhh.nme.mp.R;
import tuhh.nme.mp.data.HighPrecisionDate;


/**
 * The adapter that displays saved data records.
 */
public class HistoryFileAdapter extends ArrayAdapter<HighPrecisionDate>
{
    /**
     * Instantiates a new HistoryFileAdapter.
     *
     * @param context The context to operate on.
     */
    public HistoryFileAdapter(Context context)
    {
        super(context, R.layout.controls_adapters_history_file_adapter_item);
    }

    /**
     * Instantiates a new HistoryFileAdapter.
     *
     * @param context The context to operate on.
     * @param values  The data that should be displayed as list items.
     */
    public HistoryFileAdapter(Context context, List<HighPrecisionDate> values)
    {
        super(context, R.layout.controls_adapters_history_file_adapter_item, values);
    }

    // Inherited documentation.
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String present_text = getItem(position).toDate().toString();

        if (convertView == null)
        {
            // Inflate design.
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.controls_adapters_history_file_adapter_item,
                                         parent,
                                         false);

            // Associate UI elements.
            TextView timestamp = (TextView)view.findViewById(
                R.id.controls_adapters_HistoryFileAdapterItem_timestamp);

            // Sets display properties.
            timestamp.setText(present_text);

            return view;
        }
        else
        {
            // Associate old UI elements.
            TextView timestamp = (TextView)convertView.findViewById(
                R.id.controls_adapters_HistoryFileAdapterItem_timestamp);

            // Reset display attributes.
            timestamp.setText(present_text);

            return convertView;
        }
    }
}
