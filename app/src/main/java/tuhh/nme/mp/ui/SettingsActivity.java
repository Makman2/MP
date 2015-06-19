package tuhh.nme.mp.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import tuhh.nme.mp.R;


/**
 * This activity handles the setting-related fragments.
 */
public class SettingsActivity extends ActionBarActivity
{
    // Inherited documentation.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        getFragmentManager().beginTransaction().replace(
            R.id.SettingsActivity_fragment_frame,
            new SettingsFragment()).commit();
    }
}
