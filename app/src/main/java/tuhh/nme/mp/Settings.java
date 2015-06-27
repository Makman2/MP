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
}
