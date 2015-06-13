package tuhh.nme.mp.broadcasts;


/**
 * A listener that is triggered when WiFi is currently enabling. Use WifiBroadcastReceiver to
 * register this listener.
 */
public interface OnWifiEnablingListener
{
    /**
     * Triggered when WiFi is currently enabling.
     */
    public void onWifiEnabling();
}
