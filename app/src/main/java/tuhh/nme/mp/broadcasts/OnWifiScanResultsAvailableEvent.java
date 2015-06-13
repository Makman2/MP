package tuhh.nme.mp.broadcasts;

import tuhh.nme.mp.components.Event;


/**
 * An event that raises when the device recently scanned for available WiFi networks.
 */
public class OnWifiScanResultsAvailableEvent extends Event<OnWifiScanResultsAvailableListener>
{
    // Inherited documentation.
    @Override
    protected boolean isInvocationValid(Object... params)
    {
        return params.length == 0;
    }

    // Inherited documentation.
    @Override
    protected void onRaise(OnWifiScanResultsAvailableListener listener, Object... params)
        throws Error
    {
        listener.onWifiScanResultsAvailable();
    }
}
