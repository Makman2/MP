package tuhh.nme.mp.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;


/**
 * Receives all broadcasts related to WiFi and network state.
 */
public class WifiBroadcastReceiver extends BroadcastReceiver
{
    /**
     * Initializes the static receiver system.
     */
    static
    {
        m_OnWifiEnabledEvent = new OnWifiEnabledEvent();
        m_OnWifiEnablingEvent = new OnWifiEnablingEvent();
        m_OnWifiDisabledEvent = new OnWifiDisabledEvent();
        m_OnWifiDisablingEvent = new OnWifiDisablingEvent();
        m_OnUnknownWifiBroadcastEvent = new OnUnknownWifiBroadcastEvent();
        m_OnWifiScanResultsAvailableEvent = new OnWifiScanResultsAvailableEvent();
        m_OnWifiConnectivityChangedEvent = new OnWifiConnectivityChangedEvent();
    }

    /**
     * Called when a broadcast is received.
     *
     * @param context The BroadcastReceiver context.
     * @param intent  The Intent that was sent.
     */
    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION))
        {
            int extra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                                           WifiManager.WIFI_STATE_UNKNOWN);

            switch (extra)
            {
                case WifiManager.WIFI_STATE_ENABLED:
                    Log.d(WifiBroadcastReceiver.class.getName(), "WiFi was enabled.");
                    m_OnWifiEnabledEvent.raise();
                    break;

                case WifiManager.WIFI_STATE_ENABLING:
                    Log.d(WifiBroadcastReceiver.class.getName(), "WiFi is enabling...");
                    m_OnWifiEnablingEvent.raise();
                    break;

                case WifiManager.WIFI_STATE_DISABLED:
                    Log.d(WifiBroadcastReceiver.class.getName(), "WiFi was disabled.");
                    m_OnWifiDisabledEvent.raise();
                    break;

                case WifiManager.WIFI_STATE_DISABLING:
                    Log.d(WifiBroadcastReceiver.class.getName(), "WiFi is disabling...");
                    m_OnWifiDisablingEvent.raise();
                    break;

                case WifiManager.WIFI_STATE_UNKNOWN:
                    Log.d(WifiBroadcastReceiver.class.getName(),
                          "Received unknown WiFi-related intent.");
                    m_OnUnknownWifiBroadcastEvent.raise(intent);
                    break;
            }
        }
        else if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
        {
            Log.d(WifiBroadcastReceiver.class.getName(), "Received WiFi rescan notification.");
            m_OnWifiScanResultsAvailableEvent.raise();
        }
        else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION))
        {
            Log.d(WifiBroadcastReceiver.class.getName(), "WiFi network connectivity changed.");
            m_OnWifiConnectivityChangedEvent.raise(
                (NetworkInfo)intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO));
        }
        else
        {
            Log.e(WifiBroadcastReceiver.class.getName(), "Received illegal intent action.");
        }
    }

    /**
     * Attaches an OnWifiEnabledListener to listen for WiFi enabling.
     *
     * @param listener       The listener to attach.
     * @throws InternalError Thrown when the listener is already registered. This exception shall
     *                       prevent listeners from being called twice (or more times).
     */
    public static void attach(OnWifiEnabledListener listener) throws InternalError
    {
        m_OnWifiEnabledEvent.attach(listener);
    }

    /**
     * Attaches an OnWifiEnablingListener to listen for current WiFi enabling.
     *
     * @param listener       The listener to attach.
     * @throws InternalError Thrown when the listener is already registered. This exception shall
     *                       prevent listeners from being called twice (or more times).
     */
    public static void attach(OnWifiEnablingListener listener) throws InternalError
    {
        m_OnWifiEnablingEvent.attach(listener);
    }

    /**
     * Attaches an OnWifiDisabledListener to listen for WiFi disabling.
     *
     * @param listener       The listener to attach.
     * @throws InternalError Thrown when the listener is already registered. This exception shall
     *                       prevent listeners from being called twice (or more times).
     */
    public static void attach(OnWifiDisabledListener listener) throws InternalError
    {
        m_OnWifiDisabledEvent.attach(listener);
    }

    /**
     * Attaches an OnWifiDisablingListener to listen for current WiFi disabling.
     *
     * @param listener       The listener to attach.
     * @throws InternalError Thrown when the listener is already registered. This exception shall
     *                       prevent listeners from being called twice (or more times).
     */
    public static void attach(OnWifiDisablingListener listener) throws InternalError
    {
        m_OnWifiDisablingEvent.attach(listener);
    }

    /**
     * Attaches an OnUnknownWifiBroadcastListener to listen for unknown WiFi-related intents.
     *
     * @param listener       The listener to attach.
     * @throws InternalError Thrown when the listener is already registered. This exception shall
     *                       prevent listeners from being called twice (or more times).
     */
    public static void attach(OnUnknownWifiBroadcastListener listener) throws InternalError
    {
        m_OnUnknownWifiBroadcastEvent.attach(listener);
    }

    /**
     * Attaches an OnWifiScanResultsAvailableListener to listen for notifications that signalize
     * that the device rescanned for wireless networks.
     *
     * @param listener       The listener to attach.
     * @throws InternalError Thrown when the listener is already registered. This exception shall
     *                       prevent listeners from being called twice (or more times).
     */
    public static void attach(OnWifiScanResultsAvailableListener listener) throws InternalError
    {
        m_OnWifiScanResultsAvailableEvent.attach(listener);
    }

    /**
     * Attaches an OnWifiConnectivityChangedListener to listen for WiFi network state changes.
     *
     * @param listener       The listener to attach.
     * @throws InternalError Thrown when the listener is already registered. This exception shall
     *                       prevent listeners from being called twice (or more times).
     */
    public static void attach(OnWifiConnectivityChangedListener listener) throws InternalError
    {
        m_OnWifiConnectivityChangedEvent.attach(listener);
    }

    /**
     * Detaches an OnWifiEnabledListener from the registered listeners so its events are not
     * triggered any more.
     *
     * @param listener       The listener to detach.
     * @throws InternalError Thrown when the listener is not registered.
     */
    public static void detach(OnWifiEnabledListener listener)
    {
        m_OnWifiEnabledEvent.detach(listener);
    }

    /**
     * Detaches an OnWifiEnablingListener from the registered listeners so its events are not
     * triggered any more.
     *
     * @param listener       The listener to detach.
     * @throws InternalError Thrown when the listener is not registered.
     */
    public static void detach(OnWifiEnablingListener listener)
    {
        m_OnWifiEnablingEvent.detach(listener);
    }

    /**
     * Detaches an OnWifiDisabledListener from the registered listeners so its events are not
     * triggered any more.
     *
     * @param listener       The listener to detach.
     * @throws InternalError Thrown when the listener is not registered.
     */
    public static void detach(OnWifiDisabledListener listener)
    {
        m_OnWifiDisabledEvent.detach(listener);
    }

    /**
     * Detaches an OnWifiDisablingListener from the registered listeners so its events are not
     * triggered any more.
     *
     * @param listener       The listener to detach.
     * @throws InternalError Thrown when the listener is not registered.
     */
    public static void detach(OnWifiDisablingListener listener)
    {
        m_OnWifiDisablingEvent.detach(listener);
    }

    /**
     * Detaches an OnUnknownWifiBroadcastListener from the registered listeners so its events are
     * not triggered any more.
     *
     * @param listener       The listener to detach.
     * @throws InternalError Thrown when the listener is not registered.
     */
    public static void detach(OnUnknownWifiBroadcastListener listener)
    {
        m_OnUnknownWifiBroadcastEvent.detach(listener);
    }

    /**
     * Detaches an OnWifiScanResultsAvailableListener from the registered listeners so its events
     * are not triggered any more.
     *
     * @param listener       The listener to detach.
     * @throws InternalError Thrown when the listener is not registered.
     */
    public static void detach(OnWifiScanResultsAvailableListener listener)
    {
        m_OnWifiScanResultsAvailableEvent.detach(listener);
    }

    /**
     * Detaches an OnWifiConnectivityChangedListener from the registered listeners so its events
     * are not triggered any more.
     *
     * @param listener       The listener to detach.
     * @throws InternalError Thrown when the listener is not registered.
     */
    public static void detach(OnWifiConnectivityChangedListener listener)
    {
        m_OnWifiConnectivityChangedEvent.detach(listener);
    }

    /**
     * The event that manages OnWifiEnabledListener's.
     */
    private static OnWifiEnabledEvent m_OnWifiEnabledEvent;
    /**
     * The event that manages OnWifiEnablingListener's.
     */
    private static OnWifiEnablingEvent m_OnWifiEnablingEvent;
    /**
     * The event that manages OnWifiDisabledListener's.
     */
    private static OnWifiDisabledEvent m_OnWifiDisabledEvent;
    /**
     * The event that manages OnWifiDisablingListener's.
     */
    private static OnWifiDisablingEvent m_OnWifiDisablingEvent;
    /**
     * The event that manages OnUnknownWifiBroadcastListener's.
     */
    private static OnUnknownWifiBroadcastEvent m_OnUnknownWifiBroadcastEvent;
    /**
     * The event that manages OnWifiScanResultsAvailableListener's.
     */
    private static OnWifiScanResultsAvailableEvent m_OnWifiScanResultsAvailableEvent;
    /**
     * The event that manages OnWifiConnectivityChangedListener's.
     */
    private static OnWifiConnectivityChangedEvent m_OnWifiConnectivityChangedEvent;
}
