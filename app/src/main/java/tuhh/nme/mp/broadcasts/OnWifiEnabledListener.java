package tuhh.nme.mp.broadcasts;


/**
 * A listener that is triggered when WiFi was enabled. Use WifiBroadcastReceiver to register this
 * listener.
 */
public interface OnWifiEnabledListener
{
    /**
     * Triggered when WiFi was enabled.
     */
    public void onWifiEnabled();
}
