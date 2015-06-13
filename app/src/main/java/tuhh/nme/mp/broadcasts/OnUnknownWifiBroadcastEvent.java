package tuhh.nme.mp.broadcasts;

import android.content.Intent;

import tuhh.nme.mp.components.Event;


/**
 * An event that raises listeners when an unknown WiFi-related Intent was sent.
 */
public class OnUnknownWifiBroadcastEvent extends Event<OnUnknownWifiBroadcastListener>
{
    // Inherited documentation.
    @Override
    protected boolean isInvocationValid(Object... params)
    {
        return params.length == 1;
    }

    // Inherited documentation.
    @Override
    protected void onRaise(OnUnknownWifiBroadcastListener listener, Object... params) throws Error
    {
        listener.onUnknownWifiBroadcast((Intent)params[0]);
    }
}
