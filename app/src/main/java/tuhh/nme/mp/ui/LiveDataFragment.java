package tuhh.nme.mp.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tuhh.nme.mp.R;

/**
 * The fragment that actually displays live data from remote module.
 */
public class LiveDataFragment extends Fragment
{
    /**
     * Instantiates a new LiveDataFragment.
     */
    public LiveDataFragment()
    {
        super();
    }

    // Inherited documentation.
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.live_data_fragment, container, false);
    }
}
