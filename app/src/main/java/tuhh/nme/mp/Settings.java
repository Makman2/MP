package tuhh.nme.mp;


/**
 * Contains settings names.
 */
public final class Settings
{
    /**
     * Contains default values for each setting.
     */
    public final class Default
    {
        /**
         * Empty static class constructor.
         */
        private Default()
        {}

        public static final String device_address = "192.168.1.1";
        public static final String device_port = "1337";
        public static final boolean auto_save = true;
        public static final String data_fetch_rate = "4";
        public static final String chart_x_scale = "0.0000001";
        public static final String chart_livescroll_x_range_time = "5";
    }

    /**
     * Empty static class constructor.
     */
    private Settings()
    {}

    /**
     * The name / address of the device to connect to.
     */
    public static final String device_address = "device_address";
    /**
     * The device port to connect to.
     */
    public static final String device_port = "device_port";
    /**
     * Whether the live data preview auto-saves the data.
     */
    public static final String auto_save = "auto_save";
    /**
     * The number of data points collected per request.
     */
    public static final String data_fetch_rate = "data_fetch_rate";
    /**
     * The x-axis scaling factor for the charts used for display. The smaller the scaling the
     * finer-graded the display, but the worse the performance.
     */
    public static final String chart_x_scale = "chart_x_scale";
    /**
     * The scrolling x-axis area range while live data-preview. This values holds the time window to
     * represent instead of the actual x-value range. This is a decimal value in seconds.
     */
    public static final String chart_livescroll_x_range_time = "chart_livescroll_x_range_time";
}
