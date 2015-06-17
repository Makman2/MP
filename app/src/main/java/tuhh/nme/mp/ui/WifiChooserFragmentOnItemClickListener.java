package tuhh.nme.mp.ui;


/**
 * The listener that is triggered when the user clicked on an item of the ListView in
 * WifiChooserFragment.
 */
public interface WifiChooserFragmentOnItemClickListener
{
    /**
     * Triggered when user clicked an item from ListView.
     *
     * @param element The adapter element the user clicked on.
     */
    void onItemClick(Object element);
}
