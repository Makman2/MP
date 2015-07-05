package tuhh.nme.mp.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import java.io.IOException;

import tuhh.nme.mp.R;
import tuhh.nme.mp.data.storage.DataFrameFileManager;
import tuhh.nme.mp.ui.dialogs.AlertDialogFragment;
import tuhh.nme.mp.ui.dialogs.YesNoDialogFragment;


/**
 * The Activity that presents the history.
 */
public class HistoryActivity extends ActionBarActivity
{
    // Inherited documentation.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
    }

    // Inherited documentation.
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history_menu, menu);
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
            case R.id.HistoryMenu_wipe:
                YesNoDialogFragment dialog = new YesNoDialogFragment();
                dialog.setMessage(R.string.HistoryActivity_wipe_question);
                dialog.setCancelable(true);
                dialog.setOnYesListener(
                    new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            try
                            {
                                DataFrameFileManager.wipe();
                            }
                            catch (IOException ex)
                            {
                                AlertDialogFragment fail_dialog = new AlertDialogFragment();
                                fail_dialog.setMessage(
                                    R.string.HistoryActivity_wipe_failed_message);
                                fail_dialog.setCancelable(true);
                                fail_dialog.show(getFragmentManager(), null);

                                Log.e(HistoryActivity.class.getName(),
                                      "Wiping all records failed.",
                                      ex);
                            }

                            // Update file list of contained fragment.
                            Fragment fragment = getSupportFragmentManager().findFragmentById(
                                R.id.HistoryActivity_fragment);
                            if (fragment instanceof HistoryBrowserFragment)
                            {
                                HistoryBrowserFragment converted_fragment =
                                    (HistoryBrowserFragment)fragment;
                                converted_fragment.updateList();
                            }
                        }
                    });

                dialog.setOnNoListener(null);
                dialog.show(getFragmentManager(), null);

                return true;

            case R.id.HistoryMenu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.HistoryMenu_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
