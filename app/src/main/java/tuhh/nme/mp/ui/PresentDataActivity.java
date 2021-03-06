package tuhh.nme.mp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import tuhh.nme.mp.IntentExtra;
import tuhh.nme.mp.R;
import tuhh.nme.mp.remote.WifiConnector;


/**
 * This Activity presents the data to the user.
 */
public class PresentDataActivity extends ActionBarActivity
{
    /**
     * Instantiates a new PresentDataActivity.
     */
    public PresentDataActivity()
    {
        super();
    }

    // Inherited documentation.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.present_data_activity);

        m_DisconnectFromWifi = getIntent().getBooleanExtra(
            IntentExtra.present_data_activity_app_wifi_connected,
            false);

        getSupportFragmentManager().beginTransaction().replace(
            R.id.PresentDataActivity_fragment_frame,
            new LiveDataFragment()).commit();
    }

    // Inherited documentation.
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.present_data_menu, menu);
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
            case R.id.PresentDataMenu_history:
                startActivity(new Intent(this, HistoryActivity.class));
                return true;

            case R.id.PresentDataMenu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.PresentDataMenu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Inherited documentation.
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (m_DisconnectFromWifi)
        {
            WifiConnector.disconnect();
        }
    }

    /**
     * Whether to disconnect from user specific WLAN when destroying activity.
     */
    private boolean m_DisconnectFromWifi;
}
