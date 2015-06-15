package tuhh.nme.mp.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import tuhh.nme.mp.R;
import tuhh.nme.mp.broadcasts.WifiBroadcastReceiver;
import tuhh.nme.mp.components.Bottleneck;
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

        m_OnWifiEnabledListener = new OnWifiEnabledListener();
        m_OnWifiDisabledListener = new OnWifiDisabledListener();

        m_ChangeFragmentBottleneck = new ChangeFragmentBottleneck();
    }

    /**
     * The Bottleneck used for committing fragment replace transactions.
     *
     * Since transactions cannot be committed while the app is paused or stopped, we bottleneck the
     * calls.
     */
    private class ChangeFragmentBottleneck extends Bottleneck
    {
        /**
         * Commits a fragment replace.
         *
         * @param fragment The fragment to replace with.
         */
        public void process(Fragment fragment)
        {
            super.process(fragment);
        }

        // Inherited documentation.
        @Override
        protected void onProcess(Object object) throws Error
        {
            getSupportFragmentManager().beginTransaction().replace(
                R.id.MainActivity_fragment_frame,
                (Fragment)object).commit();
        }
    }

    // Inherited documentation.
    private class OnWifiEnabledListener implements tuhh.nme.mp.broadcasts.OnWifiEnabledListener
    {
        // Inherited documentation.
        @Override
        public void onWifiEnabled()
        {
            changeFragment(m_WifiChooserFragment.get());
        }
    }

    // Inherited documentation.
    private class OnWifiDisabledListener implements tuhh.nme.mp.broadcasts.OnWifiDisabledListener
    {
        // Inherited documentation.
        @Override
        public void onWifiDisabled()
        {
            changeFragment(m_WifiIsDisabledFragment.get());
        }
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

        WifiBroadcastReceiver.attach(m_OnWifiDisabledListener);
        WifiBroadcastReceiver.attach(m_OnWifiEnabledListener);
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
                // TODO: Implement animation for refresh action menu button.
                WifiConnector.triggerRescan();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Inherited documentation.
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        WifiBroadcastReceiver.detach(m_OnWifiEnabledListener);
        WifiBroadcastReceiver.detach(m_OnWifiDisabledListener);
    }

    // Inherited documentation.
    @Override
    protected void onPause()
    {
        super.onPause();
        m_ChangeFragmentBottleneck.setStall(true);
    }

    // Inherited documentation.
    @Override
    protected void onResume()
    {
        super.onResume();
        m_ChangeFragmentBottleneck.setStall(false);

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
        m_ChangeFragmentBottleneck.process(fragment);
        supportInvalidateOptionsMenu();
    }

    /**
     * The WifiChooserFragment that gets displayed.
     */
    private WifiChooserFragmentOnDemandObject m_WifiChooserFragment;
    /**
     * The WifiIsDisabledFragment that gets displayed.
     */
    private WifiIsDisabledFragmentOnDemandObject m_WifiIsDisabledFragment;

    /**
     * The Bottleneck used to stall transaction commits while in pause or stop mode.
     */
    private ChangeFragmentBottleneck m_ChangeFragmentBottleneck;

    /**
     * The listener that listens for WiFi activation.
     */
    private OnWifiEnabledListener m_OnWifiEnabledListener;
    /**
     * The listener that listens for WiFi deactivation.
     */
    private OnWifiDisabledListener m_OnWifiDisabledListener;
}
