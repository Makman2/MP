package tuhh.nme.mp.broadcasts;

import tuhh.nme.mp.components.Event;


/**
 * An event that raises listeners when WiFi is currently enabling.
 */
public class OnWifiEnablingEvent extends Event<OnWifiEnablingListener>
{
    // Inherited documentation.
    @Override
    protected boolean isInvocationValid(Object... params)
    {
        return params.length == 0;
    }

    // Inherited documentation.
    @Override
    protected void onRaise(OnWifiEnablingListener listener, Object... params)
    {
        listener.onWifiEnabling();
    }
}
