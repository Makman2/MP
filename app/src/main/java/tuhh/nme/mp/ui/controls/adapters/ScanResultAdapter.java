package tuhh.nme.mp.ui.controls.adapters;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

import tuhh.nme.mp.R;


/**
 * The adapter that displays Wifi ScanResult's.
 */
public class ScanResultAdapter extends ArrayAdapter<ScanResult>
{
    /**
     * Instantiates a new ScanResultAdapter without a displayed list item.
     *
     * @param context The context to operate on.
     */
    public ScanResultAdapter(Context context)
    {
        super(context, R.layout.controls_adapters_scan_result_adapter_item);
    }

    /**
     * Instantiates a new ScanResultAdapter.
     *
     * @param context The context to operate on.
     * @param values  The data that should be displayed as list items.
     */
    public ScanResultAdapter(Context context, List<ScanResult> values)
    {
        super(context, R.layout.controls_adapters_scan_result_adapter_item, values);
    }

    /**
     * Returns the View that is created for display from data.
     *
     * @param position    The position of item inside the internal list that should be displayed.
     * @param convertView An already existing View that can be used to speed up render/scrolling.
     * @param parent      The parent ViewGroup that shall contain the items.
     * @return            The View to display.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            // Inflate design.
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.controls_adapters_scan_result_adapter_item,
                                         parent,
                                         false);

            // Associate UI elements.
            TextView wifi =
                (TextView)view.findViewById(R.id.controls_adapters_ScanResultAdapterItem_wifi_text);

            // Sets display properties.
            wifi.setText(getItem(position).SSID);

            return view;
        }
        else
        {
            // Associate old UI elements.
            TextView wifi = (TextView)convertView.findViewById(
                R.id.controls_adapters_ScanResultAdapterItem_wifi_text);

            // Reset display attributes.
            wifi.setText(getItem(position).toString());

            return convertView;
        }
    }
}
