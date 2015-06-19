package tuhh.nme.mp.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import tuhh.nme.mp.R;


/**
 * The about dialog of this app.
 *
 * This class does not use fragments, since the about dialog is completely static and has no
 * interacting elements.
 */
public class AboutActivity extends ActionBarActivity
{
    // Inherited documentation.
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
    }
}
