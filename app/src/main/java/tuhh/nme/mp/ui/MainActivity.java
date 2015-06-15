package tuhh.nme.mp.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import tuhh.nme.mp.R;
import tuhh.nme.mp.remote.WifiConnector;
import tuhh.nme.mp.remote.WifiState;


/**
 * The MainActivity presented at startup. It shall display the possible remotes to connect to and
 * makes it available to enable WiFi by the user if disabled.
 */
public class MainActivity extends ActionBarActivity
{
    /**
     * Instantiates a new MainActivity.
     */
    public MainActivity()
    {
        m_WifiChooserFragment = new WifiChooserFragmentOnDemandObject();
        m_WifiIsDisabledFragment = new WifiIsDisabledFragmentOnDemandObject();
    }

    // Inherited documentation.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Set startup fragment.
        Fragment fragment;
        if (WifiConnector.getWifiState() == WifiState.ENABLED)
        {
            fragment = m_WifiChooserFragment.get();
        }
        else
        {
            fragment = m_WifiIsDisabledFragment.get();
        }

        getSupportFragmentManager().beginTransaction().add(
            R.id.MainActivity_fragment_frame,
            fragment).commit();
    }

    // Inherited documentation.
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Inherited documentation.
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.MainMenu_history:
                return true;

            case R.id.MainMenu_settings:
                return true;

            case R.id.MainMenu_about:
                return true;

            case R.id.MainMenu_refresh:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Inherited documentation.
    @Override
    protected void onPause()
    {
        super.onPause();
    }

    // Inherited documentation.
    @Override
    protected void onResume()
    {
        super.onResume();

        // If WiFi was already enabled, make a fragment transition.
        if (WifiConnector.getWifiState() == WifiState.ENABLED)
        {
            changeFragment(m_WifiChooserFragment.get());
        }
    }

    /**
     * Changes the main fragment on this activity by replacing it.
     *
     * @param fragment The new fragment to display.
     */
    private void changeFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(
            R.id.MainActivity_fragment_frame,
            fragment).commit();
    }

    /**
     * The WifiChooserFragment that gets displayed.
     */
    private WifiChooserFragmentOnDemandObject m_WifiChooserFragment;
    /**
     * The WifiIsDisabledFragment that gets displayed.
     */
    private WifiIsDisabledFragmentOnDemandObject m_WifiIsDisabledFragment;
}
