package tuhh.nme.mp.remote;

import java.net.Socket;

import tuhh.nme.mp.components.SimpleLock;


/**
 * A base class for each socket command.
 *
 * @param <T> The type of the result this command produces.
 */
public abstract class SocketCommand<T>
{
    /**
     * Instantiates a new SocketCommand.
     */
    public SocketCommand()
    {
        m_ResultLock = new SimpleLock();
        m_ResultLock.lock();
    }

    /**
     * Runs the socket command.
     *
     * @param socket The socket on which to operate.
     */
    public void run(final Socket socket)
    {
        m_Result = handle(socket);
        m_ResultLock.unlock();
    }

    /**
     * The result of the command.
     *
     * This function blocks until the command has been run.
     *
     * @return The result of the command.
     */
    public T getResult()
    {
        m_ResultLock.lock();
        T result = m_Result;
        m_ResultLock.unlock();
        return result;
    }

    /**
     * Raised when the command gets handled from the processing network thread.
     *
     * @param socket The socket to operate on.
     * @return       The result of the handling. It can be retrieved via getResult() when this
     *               function finished.
     */
    protected abstract T handle(final Socket socket);

    /**
     * Locks the result until processing is done.
     */
    private final SimpleLock m_ResultLock;
    /**
     * Holds the result.
     */
    private T m_Result;
}
