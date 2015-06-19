package tuhh.nme.mp.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import tuhh.nme.mp.R;


/**
 * Displays the app-wide settings.
 */
public class SettingsFragment extends PreferenceFragment
{
    // Inherited documentation.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }

    // TODO: Add validations for text settings like IP address.
}
