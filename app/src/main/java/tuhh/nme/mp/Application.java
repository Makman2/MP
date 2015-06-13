package tuhh.nme.mp;

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
    }
}
