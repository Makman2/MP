package tuhh.nme.mp.remote;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import tuhh.nme.mp.components.SimpleLock;


/**
 * The Client class exposes a general purpose interface to handle socket communication in a
 * background thread.
 */
public class Client
{
    /**
     * The internal Runnable used in a thread that communicates via socket.
     */
    private class SocketInteractor implements Runnable
    {
        /**
         * Instantiates a new SocketInteractor.
         *
         * @param address The address to connect to.
         * @param port    The port to connect to at address.
         */
        public SocketInteractor(final InetAddress address, final int port)
        {
            m_Address = address;
            m_Port = port;

            m_ThreadReady = false;
            m_ThreadError = null;

            m_ThreadReadyLock = new Object();
            m_ThreadErrorLock = new Object();
            m_InitializationLock = new SimpleLock();

            m_InitializationLock.lock();

            m_SocketCommandQueue = new LinkedBlockingQueue<>();
        }

        // Inherited documentation.
        @Override
        public void run()
        {
            Socket socket;

            // Connect socket to address.
            try
            {
                socket = new Socket(m_Address, m_Port);
            }
            catch (IOException ex)
            {
                setError(ex);
                m_InitializationLock.unlock();
                return;
            }

            setReady(true);
            m_InitializationLock.unlock();

            // Wait for incoming tasks.
            while (!socket.isClosed())
            {
                try
                {
                    SocketCommand cmd = m_SocketCommandQueue.take();
                    cmd.run(socket);
                }
                catch (InterruptedException ignored)
                {
                    // Ignore this exception. When the queue wait is interrupted, try it again.
                }
            }

            // Close socket.
            try
            {
                socket.close();
            }
            catch (IOException ex)
            {
                setError(ex);
            }

            setReady(false);
        }

        /**
         * Sets the ready state.
         *
         * @param value The ready state.
         */
        private void setReady(boolean value)
        {
            synchronized (m_ThreadReadyLock)
            {
                m_ThreadReady = value;
            }
        }

        /**
         * Returns whether the thread successfully initialized and is ready to receive commands.
         *
         * @return true if ready, false if not.
         */
        public boolean isReady()
        {
            synchronized (m_ThreadReadyLock)
            {
                return m_ThreadReady;
            }
        }

        /**
         * Sets the error that caused the thread to exit.
         *
         * @param value The Exception that caused the thread exit.
         */
        private void setError(Throwable value)
        {
            synchronized (m_ThreadErrorLock)
            {
                m_ThreadError = value;
            }
        }

        /**
         * Returns the error that caused the thread to crash.
         *
         * @return The Exception that caused the thread to crash. If null, no error occurred.
         */
        public Throwable getError()
        {
            synchronized (m_ThreadErrorLock)
            {
                return m_ThreadError;
            }
        }

        /**
         * Blocks until the thread is ready to receive commands.
         */
        public void waitUntilReady()
        {
            // Try to acquire lock. Blocks until no other function locks.
            m_InitializationLock.lock();
            m_InitializationLock.unlock();
        }

        /**
         * Puts an command into the processing queue.
         *
         * @param command               The command to process.
         * @throws InterruptedException Thrown when the queue interrupted the put.
         */
        public void putCommand(SocketCommand command) throws InterruptedException
        {
            m_SocketCommandQueue.put(command);
        }

        /**
         * Returns the address this class is connected to.
         *
         * @return The address.
         */
        public InetAddress getAddress()
        {
            return m_Address;
        }

        /**
         * Returns the port this class is connected to.
         *
         * @return The port.
         */
        public int getPort()
        {
            return m_Port;
        }

        /**
         * The socket address.
         */
        private final InetAddress m_Address;
        /**
         * The socket port.
         */
        private final int m_Port;

        /**
         * Whether the thread is ready to receive commands.
         */
        private boolean m_ThreadReady;
        /**
         * The error that caused the thread to exit.
         */
        private Throwable m_ThreadError;

        /**
         * The lock for accessing the thread-ready state.
         */
        private final Object m_ThreadReadyLock;
        /**
         * The lock for accessing the thread error.
         */
        private final Object m_ThreadErrorLock;
        /**
         * The lock that blocks waitUntilReady().
         */
        private final SimpleLock m_InitializationLock;

        /**
         * The queue that contains the commands to process.
         */
        private final BlockingQueue<SocketCommand> m_SocketCommandQueue;
    }

    private class CloseSocketCommand extends SocketCommand<Exception>
    {
        /**
         * Handles the processing of this command.
         *
         * @param socket The socket to operate on.
         * @return       An Exception that occurred during handling. If no exception occurred,
         *               returns null.
         */
        @Override
        protected Exception handle(Socket socket)
        {
            try
            {
                socket.close();
            }
            catch (IOException ex)
            {
                return ex;
            }

            return null;
        }
    }

    /**
     * Instantiates a new Client.
     *
     * @param address    The address to connect the client to.
     * @param port       The port to connect to.
     * @throws Throwable Any error that occurred during thread initialization.
     */
    public Client(InetAddress address, int port) throws Throwable
    {
        m_SocketInteractor = new SocketInteractor(address, port);
        m_SocketInteractorThread = new Thread(m_SocketInteractor);
        m_SocketInteractorThread.start();

        m_SocketInteractor.waitUntilReady();

        // Check for errors.
        if (!m_SocketInteractor.isReady())
        {
            throw m_SocketInteractor.getError();
        }
    }

    /**
     * Sends a command to the underlying socket thread.
     *
     * @param cmd                   The command to send to the thread.
     * @throws InterruptedException Thrown when command sending was interrupted.
     */
    public void command(SocketCommand cmd) throws InterruptedException
    {
        m_SocketInteractor.putCommand(cmd);
    }

    /**
     * Closes the underlying Client thread.
     *
     * This call blocks until the underlying thread successfully accepted the close.
     *
     * You can override this function to provide custom closing behaviour.
     *
     * @throws Exception Thrown when something goes wrong during closing.
     */
    public void close() throws Exception
    {
        CloseSocketCommand cmd = new CloseSocketCommand();
        boolean errored = true;

        while(errored)
        {
            try
            {
                command(cmd);
                errored = false;
            }
            catch (InterruptedException ignored)
            {
                // Repeat until command is eaten.
            }
        }

        // getResult() blocks until result is available.
        Exception result = cmd.getResult();
        if (result != null)
        {
            throw result;
        }

        // Don't join the socket thread, improves user experience. The thread will shutdown
        // sooner or later after the CloseSocketCommand.
    }

    /**
     * Waits until the background thread is completely shutdown.
     *
     * You should be aware to call close() in some place, otherwise this call will block
     * permanently.
     *
     * @throws InterruptedException Thrown when the blocking call gets interrupted.
     */
    public void waitForShutdown() throws InterruptedException
    {
        m_SocketInteractorThread.join();
    }

    /**
     * Returns the address the Client is connected to.
     *
     * @return The address.
     */
    public final InetAddress getAddress()
    {
        return m_SocketInteractor.getAddress();
    }

    /**
     * Returns the port the Client is connected to.
     *
     * @return The port.
     */
    public final int getPort()
    {
        return m_SocketInteractor.getPort();
    }

    /**
     * Whether the Client is operational and started its background processing thread.
     *
     * @return true if ready, false if not.
     */
    public boolean isReady()
    {
        return m_SocketInteractor.isReady();
    }

    /**
     * The started SocketInteractor that runs as a thread.
     */
    private SocketInteractor m_SocketInteractor;
    /**
     * The background thread that runs the socket interactor.
     */
    private Thread m_SocketInteractorThread;
}
