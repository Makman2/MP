package tuhh.nme.mp.components;

import java.util.LinkedList;
import java.util.Queue;


/**
 * A Bottleneck allows to stall calls and resume them later.
 *
 * If you extend this class, you should implement your own process() function taking the correctly
 * typed arguments you need. Your own process() function then calls the base class function
 * process() where you pass the arguments as Objects. Don't call onProcess() directly as you would
 * bypass the whole bottleneck system.
 */
public abstract class Bottleneck
{
    /**
     * Instantiates a new Bottleneck.
     *
     * The Bottleneck is active by default (so not in stall mode).
     */
    public Bottleneck()
    {
        m_Stalled = false;
        m_StallQueue = new LinkedList<>();
    }

    /**
     * Called when an incoming call shall be processed.
     *
     * Override this function to implement your processing function.
     *
     * @param object Additional parameters for processing.
     * @throws Error Arbitrary Error during processing.
     */
    protected abstract void onProcess(Object object) throws Error;

    /**
     * Processes an incoming call depending on the stall state.
     *
     * Call this function from your own process() implementation and not onProcess() directly.
     *
     * @param object The general purpose parameter.
     * @throws Error Arbitrary Error during processing.
     */
    protected final void process(Object object) throws Error
    {
        if (m_Stalled)
        {
            m_StallQueue.offer(object);
        }
        else
        {
            onProcess(object);
        }
    }

    /**
     * Sets whether the bottleneck is in stall mode (and caches all incoming calls) or not.
     *
     * If you unstall the bottleneck and calls were cached, they will be flushed in this call.
     *
     * @param stall Whether to stall the bottleneck or not.
     */
    public final void setStall(boolean stall)
    {
        m_Stalled = stall;

        if (!stall)
        {
            // Process complete queue synchronously.
            while(!m_StallQueue.isEmpty())
            {
                Object elem = m_StallQueue.poll();
                onProcess(elem);
            }
        }
    }

    /**
     * The stall mode.
     */
    private boolean m_Stalled;
    /**
     * The stall queue that caches incoming call parameters.
     */
    private Queue<Object> m_StallQueue;
}
