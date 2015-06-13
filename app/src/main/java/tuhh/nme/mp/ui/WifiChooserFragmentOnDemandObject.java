package tuhh.nme.mp.ui;

import tuhh.nme.mp.components.OnDemandObject;


/**
 * Creates a WifiChooserFragment on demand.
 */
public class WifiChooserFragmentOnDemandObject extends OnDemandObject<WifiChooserFragment>
{
    /**
     * Instantiates a new WifiChooserFragmentOnDemandObject.
     */
    public WifiChooserFragmentOnDemandObject()
    {
        super();
    }

    /**
     * Instantiates the WifiChooserFragment.
     *
     * @return The WifiChooserFragment.
     */
    @Override
    protected WifiChooserFragment instantiate()
    {
        return new WifiChooserFragment();
    }
}
