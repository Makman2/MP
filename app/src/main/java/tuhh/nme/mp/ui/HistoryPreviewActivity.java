package tuhh.nme.mp.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import tuhh.nme.mp.IntentExtra;
import tuhh.nme.mp.R;
import tuhh.nme.mp.data.HighPrecisionDate;


/**
 * Displays the chart for data preview.
 */
public class HistoryPreviewActivity extends ActionBarActivity
{
    // Inherited documentation.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_preview_activity);

        HighPrecisionDate load_parameter = getIntent().getParcelableExtra(
            IntentExtra.history_preview_activity_timestamp);
        setTitle(load_parameter.toDate().toString());
    }
}
