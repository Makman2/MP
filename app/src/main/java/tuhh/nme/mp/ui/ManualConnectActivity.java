package tuhh.nme.mp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import tuhh.nme.mp.R;


/**
 * The activity that allows to use the current network. This activity is mostly for debugging and
 * demonstration purposes.
 *
 * It is also useful for devices that connect to an existing network.
 */
public class ManualConnectActivity extends ActionBarActivity
{
    private class OnClickConnectListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            startActivity(new Intent(ManualConnectActivity.this, PresentDataActivity.class));
        }
    }

    // Inherited documentation.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_connect_activity);

        Button btn = (Button)findViewById(R.id.ManualConnectActivity_connect_button);
        btn.setOnClickListener(new OnClickConnectListener());
    }

    // Inherited documentation.
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manual_connect_menu, menu);
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
            case R.id.ManualConnectMenu_history:
                // TODO: Load history activity here.
                return true;

            case R.id.ManualConnectMenu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.ManualConnectMenu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
