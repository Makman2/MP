package tuhh.nme.mp;

import android.util.Log;

import tuhh.nme.mp.components.SharedObjectMemory;
import tuhh.nme.mp.data.storage.DataFrameFileManager;
import tuhh.nme.mp.remote.WifiConnector;


/**
 * The static application instance of this project.
 */
public class Application extends android.app.Application
{
    /**
     * Instantiates a new application.
     */
    public Application()
    {
        super();
    }

    /**
     * Returns the static Application instance.
     *
     * @return The Application instance.
     */
    public static Application getInstance()
    {
        return m_Instance;
    }

    /**
     * The Application instance.
     */
    private static Application m_Instance = null;

    @Override
    public void onCreate()
    {
        m_Instance = this;
        super.onCreate();

        // Set the DataFrameFileManager context.
        DataFrameFileManager.setContext(this);
        WifiConnector.setContext(this);

        // Initialize shared object memory.
        m_Memory = new SharedObjectMemory();

        // Print the summary program state.
        printLog();
    }

    /**
     * Returns the application shared memory.
     *
     * @return The shared memory object.
     */
    public SharedObjectMemory getSharedMemory()
    {
        return m_Memory;
    }

    /**
     * Print the initial program log.
     *
     * Can be also used to be called later in program to get a state summary.
     */
    public void printLog()
    {
        // Print WiFi state.
        switch (WifiConnector.getWifiState())
        {
            case ENABLED:
                Log.d(Application.class.getName(), "WiFi is currently enabled.");
                break;

            case ENABLING:
                Log.d(Application.class.getName(), "WiFi is currently enabling.");
                break;

            case DISABLED:
                Log.d(Application.class.getName(), "WiFi is currently disabled.");
                break;

            case DISABLING:
                Log.d(Application.class.getName(), "WiFi is currently disabling.");
                break;

            case UNKNOWN:
                Log.w(Application.class.getName(), "Warning: Unknown WiFi state!");
                break;
        }
    }

    /**
     * The shared memory for this application.
     */
    private SharedObjectMemory m_Memory;
}
