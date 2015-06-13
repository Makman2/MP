package tuhh.nme.mp.broadcasts;


/**
 * A listener that is triggered when WiFi is currently disabling. Use WifiBroadcastReceiver to
 * register this listener.
 */
public interface OnWifiDisablingListener
{
    /**
     * Triggered when WiFi is currently disabling.
     */
    public void onWifiDisabling();
}
