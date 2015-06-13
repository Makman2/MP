package tuhh.nme.mp.broadcasts;

import android.net.NetworkInfo;

import tuhh.nme.mp.components.Event;


/**
 * An event that raises when the WiFi network connectivity state changed.
 */
public class OnWifiConnectivityChangedEvent extends Event<OnWifiConnectivityChangedListener>
{
    // Inherited documentation.
    @Override
    protected boolean isInvocationValid(Object... params)
    {
        return params.length == 1;
    }

    // Inherited documentation.
    @Override
    protected void onRaise(OnWifiConnectivityChangedListener listener, Object... params)
        throws Error
    {
        listener.onWifiConnectivityChanged((NetworkInfo)params[0]);
    }
}
