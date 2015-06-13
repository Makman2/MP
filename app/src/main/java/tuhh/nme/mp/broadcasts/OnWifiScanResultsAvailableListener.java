package tuhh.nme.mp.broadcasts;


/**
 * A listener that listens for WiFi network scans.
 */
public interface OnWifiScanResultsAvailableListener
{
    /**
     * Triggered when the device scanned for available WiFi connections.
     */
    public void onWifiScanResultsAvailable();
}
