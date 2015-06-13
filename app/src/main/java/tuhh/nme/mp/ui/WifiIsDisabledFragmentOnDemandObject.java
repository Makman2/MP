package tuhh.nme.mp.ui;

import tuhh.nme.mp.components.OnDemandObject;


/**
 * Creates a WifiIsDisabledFragment on demand.
 */
public class WifiIsDisabledFragmentOnDemandObject extends OnDemandObject<WifiIsDisabledFragment>
{
    /**
     * Instantiates a new WifiIsDisabledFragmentOnDemandObject.
     */
    public WifiIsDisabledFragmentOnDemandObject()
    {
        super();
    }

    /**
     * Instantiates the WifiIsDisabledFragment.
     *
     * @return The WifiIsDisabledFragment.
     */
    @Override
    protected WifiIsDisabledFragment instantiate()
    {
        return new WifiIsDisabledFragment();
    }
}
