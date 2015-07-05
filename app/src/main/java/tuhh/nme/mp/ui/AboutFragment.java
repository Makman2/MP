package tuhh.nme.mp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tuhh.nme.mp.R;


/**
 * The fragment that displays the credits.
 */
public class AboutFragment extends Fragment
{
    // Inherited documentation.
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.about_fragment, container, false);
    }
}
