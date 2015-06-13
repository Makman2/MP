package tuhh.nme.mp.broadcasts;

import tuhh.nme.mp.components.Event;


/**
 * An event that raises listeners when WiFi gets enabled.
 */
public class OnWifiEnabledEvent extends Event<OnWifiEnabledListener>
{
    // Inherited documentation.
    @Override
    protected boolean isInvocationValid(Object... params)
    {
        return params.length == 0;
    }

    // Inherited documentation.
    @Override
    protected void onRaise(OnWifiEnabledListener listener, Object... params)
    {
        listener.onWifiEnabled();
    }
}
