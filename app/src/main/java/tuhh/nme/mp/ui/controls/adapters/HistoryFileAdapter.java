package tuhh.nme.mp.ui.controls.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;

import tuhh.nme.mp.R;
import tuhh.nme.mp.data.HighPrecisionDate;
import tuhh.nme.mp.data.storage.DataFrameFileManager;


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
        HighPrecisionDate item = getItem(position);
        String present_text = item.toDate().toString();
        String size_text;
        try
        {
            size_text = String.format("%.2f", DataFrameFileManager.size(item) / 1024.0f) + " KB";
        }
        catch (IOException ex)
        {
            size_text = getContext().getResources().getString(
                R.string.controls_adapters_HistoryFileAdapterItem_unknown_size);
        }

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
            TextView size = (TextView)view.findViewById(
                R.id.controls_adapters_HistoryFileAdapterItem_file_size);

            // Sets display properties.
            timestamp.setText(present_text);
            size.setText(size_text);

            return view;
        }
        else
        {
            // Associate old UI elements.
            TextView timestamp = (TextView)convertView.findViewById(
                R.id.controls_adapters_HistoryFileAdapterItem_timestamp);
            TextView size = (TextView)convertView.findViewById(
                R.id.controls_adapters_HistoryFileAdapterItem_file_size);

            // Reset display attributes.
            timestamp.setText(present_text);
            size.setText(size_text);

            return convertView;
        }
    }
}
