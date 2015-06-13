package tuhh.nme.mp.broadcasts;

import android.net.NetworkInfo;


/**
 * Listener that listens for network connectivity changes regarding WiFi.
 */
public interface OnWifiConnectivityChangedListener
{
    /**
     * Triggered when the WiFi network connectivity changed.
     */
    public void onWifiConnectivityChanged(NetworkInfo info);
}
