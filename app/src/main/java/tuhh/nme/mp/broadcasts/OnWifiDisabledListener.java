package tuhh.nme.mp.broadcasts;


/**
 * A listener that is triggered when WiFi was disabled. Use WifiBroadcastReceiver to register this
 * listener.
 */
public interface OnWifiDisabledListener
{
    /**
     * Triggered when WiFi was disabled.
     */
    public void onWifiDisabled();
}
