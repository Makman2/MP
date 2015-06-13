package tuhh.nme.mp.remote;

import android.net.wifi.WifiManager;


/**
 * Represents the state of the WiFi module.
 */
public enum WifiState
{
    /**
     * The WiFi capabilities are enabled.
     */
    ENABLED,
    /**
     * The WiFi capabilities are currently enabling.
     */
    ENABLING,
    /**
     * The WiFi capabilities are disabled.
     */
    DISABLED,
    /**
     * The WiFi capabilities are currently disabling.
     */
    DISABLING,
    /**
     * WiFi state is unknown.
     */
    UNKNOWN;

    /**
     * Converts a native WiFi state (from the WifiManager class) into a WifiState.
     *
     * @param state                            The state to convert.
     * @return                                 The corresponding WifiState value.
     * @throws EnumConstantNotPresentException Thrown when the given native state is invalid.
     */
    public static WifiState fromState(int state) throws EnumConstantNotPresentException
    {
        switch (state)
        {
            case WifiManager.WIFI_STATE_ENABLED:
                return WifiState.ENABLED;

            case WifiManager.WIFI_STATE_ENABLING:
                return WifiState.ENABLING;

            case WifiManager.WIFI_STATE_DISABLED:
                return WifiState.DISABLED;

            case WifiManager.WIFI_STATE_DISABLING:
                return WifiState.DISABLING;

            case WifiManager.WIFI_STATE_UNKNOWN:
                return WifiState.UNKNOWN;

            default:
                throw new EnumConstantNotPresentException(WifiState.class, "Invalid state.");
        }
    }
}
