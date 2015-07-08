package tuhh.nme.mp.remote;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import tuhh.nme.mp.components.CallbackRunnable;


/**
 * Handles the WiFi interface used to connect to the remote module.
 */
public final class WifiConnector
{
    /**
     * Non-instantiatable static class constructor.
     */
    private WifiConnector()
    {}

    /**
     * The internal Runnable that connects asynchronously.
     */
    private static class Connector implements Runnable
    {
        /**
         * Creates a new Connector Runnable.
         *
         * @param wifi The Wifi to connect to to asynchronously.
         */
        public Connector(ScanResult wifi)
        {
            m_Wifi = wifi;
        }

        /**
         * Connects to the given WiFi.
         */
        public void run()
        {
            connectTo(m_Wifi);
        }

        /**
         * The WiFi to connect to.
         */
        private ScanResult m_Wifi;
    }

    /**
     * The internal Runnable that disconnects asynchronously.
     */
    private static class Disconnector implements Runnable
    {
        /**
         * Creates a new Disconnector Runnable.
         */
        public Disconnector()
        {}

        /**
         * Disconnects from the current WiFi.
         */
        public void run()
        {
            disconnect();
        }
    }

    /**
     * Lists all available remote devices found in the WiFi-list.
     *
     * @return The list of ScanResult's that represents the available devices.
     */
    public static List<ScanResult> getAvailableDevices()
    {
        WifiManager wifi = getWifiManager();
        ArrayList<ScanResult> wifiResults = new ArrayList<>();

        for (ScanResult scan : wifi.getScanResults())
        {
            if (isRemoteModule(scan))
            {
                wifiResults.add(scan);
            }
        }

        return wifiResults;
    }

    /**
     * Returns the current WiFi state.
     *
     * @return The WiFi state.
     */
    public static WifiState getWifiState()
    {
        return WifiState.fromState(getWifiManager().getWifiState());
    }

    /**
     * Triggers a new WiFi scan request. This function returns immediately.
     *
     * @return true if the rescan was triggered successfully, false if not. Use this value to check
     *         if you need to wait for a broadcast that signals a finished scan.
     */
    public static boolean triggerRescan()
    {
        return getWifiManager().startScan();
    }

    /**
     * Connects to a remote module WiFi.
     *
     * @param wifi                       The WiFi of the remote module to connect to.
     * @throws InvalidParameterException Thrown when the given WiFi is not a valid remote module
     *                                   WiFi.
     */
    public static void connect(ScanResult wifi) throws InvalidParameterException
    {
        checkWifi(wifi);
        connectTo(wifi);
    }

    /**
     * Connects to a remote module WiFi asynchronously.
     *
     * @param wifi                       The WiFi of the remote module to connect to.
     * @param callback                   The callback that should be called if the connection
     *                                   is established. Passing null as a value doesn't invoke any
     *                                   callback.
     * @throws InvalidParameterException Thrown when the given WiFi is not a valid remote module
     *                                   WiFi
     */
    public static void connectAsync(ScanResult wifi, Runnable callback)
        throws InvalidParameterException
    {
        checkWifi(wifi);

        Runnable running;

        if (callback == null)
        {
            running = new Connector(wifi);
        }
        else
        {
            running = new CallbackRunnable(new Connector(wifi), callback);
        }

        Thread td = new Thread(running);
        td.start();
    }

    /**
     * Disconnects from the remote WiFi.
     */
    public static void disconnect()
    {
        Log.d(WifiConnector.class.getName(), "Disconnecting from current WiFi network...");

        WifiManager manager = getWifiManager();
        manager.disconnect();
        manager.disableNetwork(m_WifiConfigurationID);
        if (!m_WifiConfigurationAlreadyExisted)
        {
            manager.removeNetwork(m_WifiConfigurationID);
        }
        manager.reconnect();
    }

    /**
     * Disconnects from the remote WiFi asynchronously.
     *
     * @param callback The callback that should be called if the connection is established. Passing
     *                 null as a value doesn't invoke any callback.
     */
    public static void disconnectAsync(Runnable callback)
    {
        Runnable running;

        if (callback == null)
        {
            running = new Disconnector();
        }
        else
        {
            running = new CallbackRunnable(new Disconnector(), callback);
        }

        Thread td = new Thread(running);
        td.start();
    }

    /**
     * Sets the context this module shall operate on.
     *
     * @param context The context.
     */
    public static void setContext(Context context)
    {
        m_Context = context;
    }

    /**
     * Connects to the specified network.
     *
     * @param wifi The WiFi network to connect to.
     */
    private static void connectTo(ScanResult wifi)
    {
        String networkSSID = wifi.SSID;

        Log.d(WifiConnector.class.getName(), "Connecting to WiFi Network '" + networkSSID + "'.");

        WifiManager manager = getWifiManager();

        WifiConfiguration conf = null;

        m_WifiConfigurationAlreadyExisted = false;
        for (WifiConfiguration config : manager.getConfiguredNetworks())
        {
            if (config.SSID == wifi.SSID)
            {
                // Found already existing configuration, use that.
                m_WifiConfigurationAlreadyExisted = true;
                conf = config;
                m_WifiConfigurationID = conf.networkId;
                break;
            }
        }

        if (!m_WifiConfigurationAlreadyExisted)
        {
            conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            m_WifiConfigurationID = manager.addNetwork(conf);
        }

        manager.disconnect();
        manager.enableNetwork(m_WifiConfigurationID, true);
        manager.reconnect();
    }

    /**
     * Enables WiFi capabilities.
     */
    public static void enableWiFi()
    {
        getWifiManager().setWifiEnabled(true);
        Log.d(WifiConnector.class.getName(), "Attempt to enable WiFi...");
    }

    /*
     * Disables WiFi capabilities.
     */
    public static void disableWiFi()
    {
        getWifiManager().setWifiEnabled(false);
        Log.d(WifiConnector.class.getName(), "Attempt to disable WiFi...");
    }

    /**
     * Checks whether the ScanResult is a valid remote module connectable to.
     *
     * @param wifi The ScanResult that describes the WiFi.
     * @return     true if the ScanResult indicates a valid remote device, false if not.
     */
    private static boolean isRemoteModule(ScanResult wifi)
    {
        String[] security_modes = {"WEP", "PSK", "EAP"};
        for (String elem : security_modes)
        {
            if (wifi.capabilities.contains(elem))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Throws an InvalidParameterException if the given WiFi network represents no valid remote
     * device.
     *
     * @param wifi                       The WiFi network to check.
     * @throws InvalidParameterException Thrown when wifi is no valid remote device.
     */
    private static void checkWifi(ScanResult wifi) throws InvalidParameterException
    {
        if (!isRemoteModule(wifi))
        {
            throw new InvalidParameterException("The passed ScanResult that describes the WiFi " +
                                                "is no valid remote module WiFi.");
        }
    }

    /**
     * Returns the running WifiManager from registered context.
     *
     * @return The WifiManager.
     */
    private static WifiManager getWifiManager()
    {
        return (WifiManager)m_Context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * The context the WifiConnector shall operate on.
     */
    private static Context m_Context = null;

    /**
     * The WiFi configuration ID that identifies the created remote module connection between all
     * already registered WiFi configurations.
     */
    private static int m_WifiConfigurationID;

    /**
     * Checks whether the WiFi configuration for given ScanResult already existed or not. If it
     * already existed, it shall not be deleted from system.
     */
    private static boolean m_WifiConfigurationAlreadyExisted;
}
