package tuhh.nme.mp.broadcasts;

import tuhh.nme.mp.components.Event;


/**
 * An event that raises listeners when WiFi was disabled.
 */
public class OnWifiDisabledEvent extends Event<OnWifiDisabledListener>
{
    // Inherited documentation.
    @Override
    protected boolean isInvocationValid(Object... params)
    {
        return params.length == 0;
    }

    // Inherited documentation.
    @Override
    protected void onRaise(OnWifiDisabledListener listener, Object... params)
    {
        listener.onWifiDisabled();
    }
}
