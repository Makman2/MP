package tuhh.nme.mp.remote;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the WiFi interface used to connect to the remote module.
 */
public class WifiConnector
{
    /**
     * The internal Runnable that connects asynchronously.
     */
    private static class Connector implements Runnable
    {
        /**
         * Creates a new Connector Runnable.
         *
         * @param wifi The Wifi to connecto to asynchronously.
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
        WifiManager manager = getWifiManager();
        manager.disconnect();
        manager.removeNetwork(m_WifiConfigurationID);
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

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        WifiManager manager = getWifiManager();
        m_WifiConfigurationID = manager.addNetwork(conf);

        manager.disconnect();
        manager.enableNetwork(m_WifiConfigurationID, true);
        manager.reconnect();
    }

    /**
     * Checks whether the ScanResult is a valid remote module connectable to.
     *
     * @param wifi The ScanResult that describes the WiFi.
     * @return     true if the ScanResult indicates a valid remote device, false if not.
     */
    private static boolean isRemoteModule(ScanResult wifi)
    {
        // TODO Implement remote module characteristics condition.
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
}
