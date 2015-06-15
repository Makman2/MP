package tuhh.nme.mp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import tuhh.nme.mp.R;
import tuhh.nme.mp.broadcasts.WifiBroadcastReceiver;
import tuhh.nme.mp.remote.WifiConnector;
import tuhh.nme.mp.remote.WifiState;


/**
 * The fragment that is shown when the user has his WiFi disabled, so he can turn it on directly.
 */
public class WifiIsDisabledFragment extends Fragment
{
    /**
     * Instantiates a new WifiIsDisabledFragment.
     */
    public WifiIsDisabledFragment()
    {
        super();

        // The listeners are class bound and used once. So instantiate them in here in constructor.
        m_OnWifiEnabledListener = new OnWifiEnabledListener();
        m_OnWifiEnablingListener = new OnWifiEnablingListener();
        m_OnWifiDisabledListener = new OnWifiDisabledListener();
        m_OnWifiDisablingListener = new OnWifiDisablingListener();
    }

    /**
     * Listener that handles the OnCheckedChange event of the switch.
     */
    private class SwitchOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener
    {
        /**
         * Triggered when the checked-state changed.
         *
         * @param buttonView The object that triggered the event.
         * @param isChecked  Whether the switch is now checked or not.
         */
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            if (isChecked)
            {
                WifiConnector.enableWiFi();
            }
            else
            {
                WifiConnector.disableWiFi();
            }
        }
    }

    /**
     * Listener that listens for WiFi enabling.
     */
    private class OnWifiEnabledListener implements tuhh.nme.mp.broadcasts.OnWifiEnabledListener
    {
        // Inherited documentation.
        @Override
        public void onWifiEnabled()
        {
            setSwitchFromState(WifiState.ENABLED);
        }
    }

    /**
     * Listener that listens for current WiFi enabling.
     */
    private class OnWifiEnablingListener implements tuhh.nme.mp.broadcasts.OnWifiEnablingListener
    {
        // Inherited documentation.
        @Override
        public void onWifiEnabling()
        {
            setSwitchFromState(WifiState.ENABLING);
        }
    }

    /**
     * Listener that listens for WiFi disabling.
     */
    private class OnWifiDisabledListener implements tuhh.nme.mp.broadcasts.OnWifiDisabledListener
    {
        // Inherited documentation.
        @Override
        public void onWifiDisabled()
        {
            setSwitchFromState(WifiState.DISABLED);
        }
    }

    /**
     * Listener that listens for current WiFi disabling.
     */
    private class OnWifiDisablingListener implements tuhh.nme.mp.broadcasts.OnWifiDisablingListener
    {
        // Inherited documentation.
        @Override
        public void onWifiDisabling()
        {
            setSwitchFromState(WifiState.DISABLING);
        }
    }

    /**
     * Triggered when the layout needs to be created.
     *
     * @param inflater           The inflater that should be used for layout inflation.
     * @param container          The parent container where the contents get displayed.
     * @param savedInstanceState The saved state after a suspend.
     * @return                   The View to show.
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        // We want to manipulate the menu, so we need to catch onPrepareOptionsMenu().
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment.
        View view = inflater.inflate(R.layout.wifi_is_disabled_fragment, container, false);
        initializeView(view);
        return view;
    }

    /**
     * Initializes the states of all Views on this fragment.
     *
     * @param view The fragment View where all other View's reside.
     */
    private void initializeView(View view)
    {
        m_Switch = (Switch)view.findViewById(R.id.WifiIsDisabledFragment_switch);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState)
    {
        super.onViewStateRestored(savedInstanceState);

        setSwitchFromState(WifiConnector.getWifiState());
        m_Switch.setOnCheckedChangeListener(new SwitchOnCheckedChangeListener());
    }

    /**
     * Triggered when this fragment gets attached to an activity.
     *
     * @param activity The activity to attach to.
     */
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        // Attach to Wifi broadcasts.
        WifiBroadcastReceiver.attach(m_OnWifiEnabledListener);
        WifiBroadcastReceiver.attach(m_OnWifiEnablingListener);
        WifiBroadcastReceiver.attach(m_OnWifiDisabledListener);
        WifiBroadcastReceiver.attach(m_OnWifiDisablingListener);
    }

    /**
     * Triggered when this fragment gets detached from its activity.
     */
    @Override
    public void onDetach()
    {
        super.onDetach();

        // Detach from Wifi broadcasts.
        WifiBroadcastReceiver.detach(m_OnWifiEnabledListener);
        WifiBroadcastReceiver.detach(m_OnWifiEnablingListener);
        WifiBroadcastReceiver.detach(m_OnWifiDisabledListener);
        WifiBroadcastReceiver.detach(m_OnWifiDisablingListener);
    }

    // Inherited documentation.
    @Override
    public void onPrepareOptionsMenu(Menu menu)
    {
        super.onPrepareOptionsMenu(menu);

        // Hide the "refresh" action button.
        try
        {
            menu.findItem(R.id.MainMenu_refresh).setVisible(false);
        }
        catch (NullPointerException ex)
        {
            Log.w(WifiIsDisabledFragment.class.getName(), "Fragment seems not to be used in " +
                  "intended activity.");
        }
    }

    /**
     * Sets the switch state according to the given WifiState.
     *
     * @param state The WiFi state.
     */
    private void setSwitchFromState(WifiState state)
    {
        // The pair represents the state the switch should be set to. The first entry is the checked
        // state and the second one the enabled state.
        Pair<Boolean, Boolean> switch_state_tuple = new Pair<>(null, null);

        switch (state)
        {
            case ENABLED:
                switch_state_tuple = new Pair<>(true, true);
                break;

            case ENABLING:
                switch_state_tuple = new Pair<>(true, false);
                break;

            case DISABLED:
                switch_state_tuple = new Pair<>(false, true);
                break;

            case DISABLING:
                switch_state_tuple = new Pair<>(false, false);
                break;
        }

        if (switch_state_tuple.first != null)
        {
            m_Switch.setChecked(switch_state_tuple.first);
        }

        if (switch_state_tuple.second != null)
        {
            m_Switch.setEnabled(switch_state_tuple.second);
        }
    }

    /**
     * The switch that represents the WiFi state.
     */
    private Switch m_Switch;
    /**
     * The listener for WiFi enabling.
     */
    private OnWifiEnabledListener m_OnWifiEnabledListener;
    /**
     * The listener for current WiFi enabling.
     */
    private OnWifiEnablingListener m_OnWifiEnablingListener;
    /**
     * The listener for WiFi disabling.
     */
    private OnWifiDisabledListener m_OnWifiDisabledListener;
    /**
     * The listener for current WiFi disabling.
     */
    private OnWifiDisablingListener m_OnWifiDisablingListener;
}
