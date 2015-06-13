package tuhh.nme.mp.broadcasts;

import android.content.Intent;


/**
 * A listener that is triggered when an unknown Intent was broadcast that is WiFi-related.
 */
public interface OnUnknownWifiBroadcastListener
{
    /**
     * Triggered when an unknown WiFi-related Intent was sent.
     *
     * @param unknown_intent The unknown Intent sent.
     */
    public void onUnknownWifiBroadcast(Intent unknown_intent);
}
