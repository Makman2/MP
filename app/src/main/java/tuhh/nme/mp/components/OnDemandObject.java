package tuhh.nme.mp.components;

/**
 * An object that is created when needed.
 *
 * If your object accepts construction parameters, you should follow this pattern:
 * - Create constructors for every (needed) constructor signature in T.
 * - Store the invocation parameters in variables.
 * - If you want to support more than one constructor, use an additional variable that identifies
 *   which constructor you need to invoke.
 * - Instantiate the object in instantiate().
 *
 * @param <T> The instance type of this OnDemandObject that gets instantiated on need.
 */
public abstract class OnDemandObject<T>
{
    /**
     * Creates a new OnDemandObject.
     */
    public OnDemandObject()
    {
        m_Object = null;
    }

    /**
     * Returns the instance of this OnDemandObject.
     *
     * @return The instance.
     */
    public final T get()
    {
        if (m_Object == null)
        {
            m_Object = instantiate();
        }

        return m_Object;
    }

    /**
     * Instantiates an object of type T.
     *
     * Override this method to instantiate your specific type.
     *
     * @return An instance.
     */
    protected abstract T instantiate();

    /**
     * The instance.
     */
    private T m_Object;
}
