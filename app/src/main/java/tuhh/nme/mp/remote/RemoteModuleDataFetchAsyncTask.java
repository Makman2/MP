package tuhh.nme.mp.remote;

import android.os.AsyncTask;
import android.util.Log;

import tuhh.nme.mp.data.HighPrecisionDatedDataFrame;


/**
 * An AsyncTask that fetches permanently data in background until stop.
 *
 * Since this class actually capsules an AsyncTask you should create this class inside the UI main
 * thread and start it from there.
 *
 * Note: If a SocketCommandHandlingException is raised from the read attempt, this class
 * automatically closes the socket because the server seems to malfunction.
 */
public abstract class RemoteModuleDataFetchAsyncTask
{
    /**
     * The task that fetches data in background.
     */
    private class FetchDataAsyncTask
        extends AsyncTask<Void, Void, HighPrecisionDatedDataFrame<Short>>
    {
        // Inherited documentation.
        @Override
        protected HighPrecisionDatedDataFrame<Short> doInBackground(Void... params)
        {
            try
            {
                // This call blocks until finished.
                return m_Client.read(m_FetchRate);
            }
            catch (InterruptedException ex)
            {
                return null;
            }
            catch (SocketCommandHandlingException ex)
            {
                // Something serious went wrong during remote data read attempt.
                stop();

                m_Client.close();

                Log.e(FetchDataAsyncTask.class.getName(),
                      "Serious error during remote data read attempt. Disconnected client.",
                      ex);

                return null;
            }
        }

        // Inherited documentation.
        @Override
        protected void onPostExecute(HighPrecisionDatedDataFrame<Short> dataPoints)
        {
            onIncomingData(dataPoints);

            if (m_FetchStarted)
            {
                new FetchDataAsyncTask().execute();
            }
        }
    }

    /**
     * Instantiates a new RemoteModuleDataFetchAsyncTask.
     *
     * @param client     The client from where to fetch data.
     * @param fetch_rate The number of data points fetched per request.
     */
    public RemoteModuleDataFetchAsyncTask(RemoteModuleClient client, int fetch_rate)
    {
        m_Client = client;
        m_FetchRate = fetch_rate;
        m_FetchStarted = false;
    }

    /**
     * Starts the data fetch.
     */
    public void start()
    {
        if (!m_FetchStarted)
        {
            m_FetchStarted = true;
            new FetchDataAsyncTask().execute();
        }
    }

    /**
     * Stop the data fetch if active.
     *
     * This function does nothing if the data fetch already stopped.
     */
    public void stop()
    {
        m_FetchStarted = false;
    }

    /**
     * Raised when data comes in.
     *
     * This function is called inside the UI main thread.
     *
     * @param data The fetched data. If an error occurred, this value is null.
     */
    protected abstract void onIncomingData(HighPrecisionDatedDataFrame<Short> data);

    /**
     * The client to operate on.
     */
    private RemoteModuleClient m_Client;
    /**
     * The number of data points to fetch per request.
     */
    private int m_FetchRate;
    /**
     * Whether the fetch thread started or not.
     */
    private boolean m_FetchStarted;
}
