package tuhh.nme.mp.components;


/**
 * A simple exclusive lock.
 *
 * This Lock does not distinct locks from same threads. If someone locks, the lock is hold until
 * unlock.
 */
public class SimpleLock
{
    /**
     * Instantiates a new non-locked SimpleLock.
     */
    public SimpleLock()
    {
        m_IsLocked = false;
    }

    /**
     * Locks this instance.
     *
     * This call blocks until this instance gets unlocked.
     */
    public synchronized void lock()
    {
        while(m_IsLocked)
        {
            try
            {
                wait();
            }
            catch (InterruptedException ignored)
            {
                // Ignore, just continue waiting.
            }
        }

        m_IsLocked = true;
    }

    /**
     * Unlocks this instance.
     */
    public synchronized void unlock()
    {
        m_IsLocked = false;
        notify();
    }

    /**
     * Whether this instance is locked or not.
     */
    private boolean m_IsLocked = false;
}
