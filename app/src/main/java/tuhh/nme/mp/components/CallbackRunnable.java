package tuhh.nme.mp.components;


/**
 * A runnable that allows to define a manual callback.
 */
public class CallbackRunnable implements Runnable
{
    /**
     * Instantiates a new CallbackRunnable.
     *
     * @param runnable The actual task to perform.
     * @param callback The callback that shall be invoked after the main task finished.
     */
    public CallbackRunnable(Runnable runnable, Runnable callback)
    {
        m_Runnable = runnable;
        m_Callback = callback;
    }

    /**
     * Runs the task and then the callback.
     */
    public void run()
    {
        m_Runnable.run();
        m_Callback.run();
    }

    /**
     * The main task.
     */
    private Runnable m_Runnable;
    /**
     * The callback.
     */
    private Runnable m_Callback;
}
