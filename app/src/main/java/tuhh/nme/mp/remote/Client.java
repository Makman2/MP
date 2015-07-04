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
            m_FlushThrough = false;

            m_ThreadReadyLock = new Object();
            m_ThreadErrorLock = new Object();
            m_FlushThroughLock = new Object();
            m_InitializationLock = new SimpleLock();

            m_InitializationLock.lock();

            m_SocketCommandQueue = new LinkedBlockingQueue<>();
        }

        // Inherited documentation.
        @Override
        public void run()
        {
            // Connect socket to address.
            try
            {
                m_Socket = new Socket(m_Address, m_Port);
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
            while (!m_Socket.isClosed())
            {
                try
                {
                    m_SocketCommandQueue.take().run(m_Socket);
                }
                catch (InterruptedException ignored)
                {
                    // Ignore this exception. When the queue wait is interrupted, try it again.
                }
            }

            setFlushThrough(true);
            setReady(false);

            // Flush pipe.
            flushPipe();
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
         * Sets the connection error that caused the thread to exit.
         *
         * @param value The Exception that caused the thread exit.
         */
        private void setError(IOException value)
        {
            synchronized (m_ThreadErrorLock)
            {
                m_ThreadError = value;
            }
        }

        /**
         * Returns the connection error that caused the thread to exit.
         *
         * @return The Exception that caused the thread to exit. If null, no error occurred.
         */
        public IOException getError()
        {
            synchronized (m_ThreadErrorLock)
            {
                return m_ThreadError;
            }
        }

        /**
         * Sets the flush-through flag.
         *
         * The flush-through flag tells the thread to handle incoming commands immediately since
         * the socket thread is closed.
         *
         * @param value The value for the flush-through flag.
         */
        private void setFlushThrough(boolean value)
        {
            synchronized (m_FlushThroughLock)
            {
                m_FlushThrough = value;
            }
        }

        /**
         * Gets the flush-through flag.
         *
         * The flush-through flag tells the thread to handle incoming commands immediately since
         * the socket thread is closed.
         *
         * @return The flush-through flag.
         */
        private boolean getFlushThrough()
        {
            synchronized (m_FlushThroughLock)
            {
                return m_FlushThrough;
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
         * Puts a command into the processing queue.
         *
         * @param command               The command to process.
         * @throws InterruptedException Thrown when the queue interrupted the put.
         */
        public void command(SocketCommand command) throws InterruptedException
        {
            if (getFlushThrough())
            {
                command.run(null);
            }
            else
            {
                m_SocketCommandQueue.put(command);
            }
        }

        /**
         * Kills the background thread, closes the socket and flushes through all remaining
         * commands.
         */
        public void terminate()
        {
            m_SocketInteractorThread.interrupt();

            try
            {
                m_Socket.close();
            }
            catch (IOException ignored)
            {
                // Something went wrong, but anyway we tried to close.
            }

            // Flush through pipe.
            setFlushThrough(true);
            flushPipe();
        }

        /**
         * Flushes all remaining commands in the pipe.
         */
        private void flushPipe()
        {
            while (!m_SocketCommandQueue.isEmpty())
            {
                try
                {
                    m_SocketCommandQueue.take().run(null);
                }
                catch (InterruptedException ignored)
                {
                    // Ignore this exception. When the queue wait is interrupted, try it again.
                }
            }
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
        private IOException m_ThreadError;
        /**
         * The flush-through flag that determines whether to flush incoming commands immediately
         * and not queue them into the thread.
         */
        private boolean m_FlushThrough;

        /**
         * The lock for accessing the thread-ready state.
         */
        private final Object m_ThreadReadyLock;
        /**
         * The lock for accessing the thread error.
         */
        private final Object m_ThreadErrorLock;
        /**
         * The lock for accessing the flush-through flag.
         */
        private final Object m_FlushThroughLock;

        /**
         * The lock that blocks waitUntilReady().
         */
        private final SimpleLock m_InitializationLock;

        /**
         * The socket instance used in the thread.
         */
        private Socket m_Socket;

        /**
         * The queue that contains the commands to process.
         */
        private final BlockingQueue<SocketCommand> m_SocketCommandQueue;
    }

    /**
     * A SocketCommand that closes the socket.
     */
    private class CloseSocketCommand extends SocketCommand<Void>
    {
        /**
         * Handles the processing of this command.
         *
         * @param socket     The socket to operate on.
         * @return           An Exception that occurred during handling. If no exception occurred,
         *                   returns null.
         * @throws Throwable Any error that occurs during close.
         */
        @Override
        protected Void handle(Socket socket) throws Throwable
        {
            if (socket != null)
            {
                socket.close();
            }
            return null;
        }
    }

    /**
     * Instantiates a new Client.
     *
     * This constructor does not connect to the target. Use connect() to actually connect.
     *
     * @param address The address to connect the client to.
     * @param port    The port to connect to.
     */
    public Client(InetAddress address, int port)
    {
        m_SocketInteractor = new SocketInteractor(address, port);
        m_SocketInteractorThread = new Thread(m_SocketInteractor);
    }

    /**
     * Connects to the desired device.
     *
     * This call blocks until the underlying thread has successfully been started and is ready to
     * process SocketCommand's.
     *
     * @throws IOException Thrown when the client couldn't connect to given address and port.
     */
    public void connect() throws IOException
    {
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
        m_SocketInteractor.command(cmd);
    }

    /**
     * Closes the underlying Client thread.
     *
     * This call blocks until the underlying thread processes remaining SocketCommand's and
     * successfully accepted the close.
     *
     * You can override this function to provide custom closing behaviour.
     *
     * Note that the underlying thread doesn't have to be terminated, this function just signals the
     * thread to shutdown. If you want to terminate the thread, call terminate(), since this
     * immediately kills the thread. To wait for shutdown, use waitForShutdown().
     *
     * @throws SocketCommandHandlingException Thrown when something goes wrong during closing.
     */
    public void close() throws SocketCommandHandlingException
    {
        CloseSocketCommand cmd = new CloseSocketCommand();

        while(true)
        {
            try
            {
                command(cmd);
                break;
            }
            catch (InterruptedException ignored)
            {
                // Repeat until command is eaten.
            }
        }

        // getResult() blocks until result is available. So in this case it blocks until the close
        // gets handled.
        cmd.getResult();

        // Don't join the socket thread, improves user experience. The thread will shutdown
        // sooner or later after the CloseSocketCommand.
        // If the user wants to wait for complete shutdown he can use waitForShutdown().
    }

    /**
     * Waits until the background thread has completely shut down.
     *
     * You should be aware to call close() or terminate() in some place, otherwise this call will
     * block permanently.
     *
     * @throws InterruptedException Thrown when the blocking call gets interrupted.
     */
    public void waitForShutdown() throws InterruptedException
    {
        if (!m_SocketInteractorThread.isInterrupted())
        {
            m_SocketInteractorThread.join();
        }
    }

    /**
     * Tells the background Client thread to immediately terminate and close the connection.
     *
     * This call blocks until the thread is terminated and all remaining SocketCommand's are
     * flushed.
     */
    public void terminate()
    {
        m_SocketInteractor.terminate();
    }

    // Inherited documentation.
    @Override
    protected void finalize() throws Throwable
    {
        terminate();
        super.finalize();
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
