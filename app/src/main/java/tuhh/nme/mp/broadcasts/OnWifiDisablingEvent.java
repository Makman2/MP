package tuhh.nme.mp.broadcasts;

import tuhh.nme.mp.components.Event;


/**
 * An event that raises listeners when WiFi is currently disabling.
 */
public class OnWifiDisablingEvent extends Event<OnWifiDisablingListener>
{
    // Inherited documentation.
    @Override
    protected boolean isInvocationValid(Object... params)
    {
        return params.length == 0;
    }

    // Inherited documentation.
    @Override
    protected void onRaise(OnWifiDisablingListener listener, Object... params)
    {
        listener.onWifiDisabling();
    }
}
