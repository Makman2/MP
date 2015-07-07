package tuhh.nme.mp;


/**
 * Contains IDs for Intent values passed as parameters, in bundles etc.
 */
public final class IntentExtra
{
    /**
     * Empty static class constructor.
     */
    private IntentExtra()
    {}

    /**
     * The parameter that is passed to HistoryPreviewActivity that identifies the timestamp to load.
     */
    public static final String history_preview_activity_timestamp =
        "history_preview_activity_timestamp";
    /**
     * Tells the presenting activity to use the previous WLAN again since the user has chosen a
     * custom one.
     */
    public static final String present_data_activity_app_wifi_connected =
        "present_data_activity_app_wifi_connected";
}
