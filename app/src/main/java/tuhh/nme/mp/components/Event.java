package tuhh.nme.mp.components;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * A list of handlers. This class allows multiple registration of more than one listener and
 * easy calling.
 *
 * To use an event, just override the onRaise() function and put in the specific listener raise.
 *
 * To register/unregister an event, call the attach/detach functions. You can also get all
 * registered listeners by iterating over this class or using getListeners().
 *
 * @param <ListenerType> The type of the listeners that will be registered.
 */
public abstract class Event<ListenerType> implements Iterable<ListenerType>
{
    /**
     * Instantiates a new Event.
     */
    public Event()
    {
        m_Listeners = new HashSet<>();
    }

    /**
     * Attaches a listener to this event.
     *
     * @param listener       The listener to attach.
     * @throws InternalError Thrown when the passed listener was already registered.
     */
    public void attach(ListenerType listener) throws InternalError
    {
        if (!m_Listeners.add(listener))
        {
            throw new InternalError("The given listener is already registered.");
        }
    }

    /**
     * Detaches a listener from this event.
     *
     * @param listener       The listener to detach.
     * @throws InternalError Thrown when the passed listener was not registered.
     */
    public void detach(ListenerType listener) throws InternalError
    {
        if (!m_Listeners.remove(listener))
        {
            throw new InternalError("The passed listener was not attached to this event.");
        }
    }

    /**
     * Detaches all registered listeners.
     */
    public void detachAll()
    {
        m_Listeners.clear();
    }

    /**
     * Invokes each registered listener.
     *
     * @param params Additional params to invoke the listener with.
     */
    public final void raise(Object... params) throws Error
    {
        if (!isInvocationValid(params))
        {
            throw new InvalidInvocationException("Invalid call invocation.");
        }

        for (ListenerType elem : m_Listeners)
        {
            onRaise(elem, params);
        }
    }

    /**
     * Returns a Set of all registered listeners.
     *
     * @return The set that contains all registered listeners.
     */
    public Set<ListenerType> getListeners()
    {
        return Collections.unmodifiableSet(m_Listeners);
    }

    /**
     * Returns an iterator to the registered listeners.
     *
     * @return The iterator.
     */
    @Override
    public Iterator<ListenerType> iterator()
    {
        return m_Listeners.iterator();
    }

    /**
     * Checks whether the invoked params are suited for listener invocation.
     *
     * You should override this function and check for argument number to defeat invocation errors.
     * But you don't need to override this function, in this case every invocation is valid.
     *
     * Parameter types should be not checked, you need to cast them anyway in onRaise() where the
     * casts would raise automatically a ClassCastException.
     *
     * @param params The parameters passed to the raise.
     * @return       true if invocation with these arguments is valid, otherwise false.
     */
    protected boolean isInvocationValid(Object ... params)
    {
        return true;
    }

    /**
     * Raised when raise() is invoked.
     *
     * Override this function to invoke the specific listening function in your listener.
     *
     * @param listener The listener that shall be invoked.
     * @param params   Additional parameters to invoke the listener with.
     * @throws Error   Any Error that can occur during invocation.
     */
    protected abstract void onRaise(ListenerType listener, Object... params) throws Error;

    /**
     * The Set that stores all attached listeners.
     */
    private Set<ListenerType> m_Listeners;
}
