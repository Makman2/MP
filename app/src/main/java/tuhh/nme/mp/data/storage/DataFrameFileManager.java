package tuhh.nme.mp.data.storage;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;

import tuhh.nme.mp.data.HighPrecisionDate;


/**
 * The file manager for all MPDF (MP-DataFrame format) files.
 *
 * This class exposes various functions to store and load them together quickly.
 *
 * To use this class you need to set its context first. Use setContext() for that purpose.
 */
public class DataFrameFileManager
{
    /**
     * Static class stub constructor.
     */
    private DataFrameFileManager()
    {}

    /**
     * Creates a new MPDF file and opens a writing stream.
     *
     * @param timestamp    The timestamp of the file. It is used to create a unique file name.
     * @return             The opened stream to the newly created file.
     * @throws IOException Thrown when the file already exists (so there already exists an entry
     *                     with this timestamp).
     */
    public static FileOutputStream create(HighPrecisionDate timestamp) throws IOException
    {
        File file = getFile(timestamp);

        if (!file.createNewFile())
        {
            throw new IOException("File already exists.");
        }

        Log.d(DataFrameFileManager.class.getName(),
              "Created MPDF file with timestamp " + timestamp.toString() + ".");

        return new FileOutputStream(file);
    }

    /**
     * Deletes an MPDF file.
     *
     * @param timestamp    The timestamp that identifies the MPDF file.
     * @throws IOException Thrown when the file couldn't be deleted.
     */
    public static void delete(HighPrecisionDate timestamp) throws IOException
    {
        File file = getFile(timestamp);
        if (!file.delete())
        {
            throw new IOException("File with the given timestamp could not be deleted.");
        }

        Log.d(DataFrameFileManager.class.getName(),
              "Deleted MPDF file with timestamp " + timestamp.toString() + ".");
    }

    /**
     * Deletes all MPDF files available.
     *
     * @throws IOException Thrown when an IO fault occurred.
     */
    public static void wipe() throws IOException
    {
        for (HighPrecisionDate stamps : getStoredFiles())
        {
            delete(stamps);
        }

        Log.d(DataFrameFileManager.class.getName(), "Wiped all saved MPDF files.");
    }

    /**
     * Opens an MPDF file for write access.
     *
     * @param timestamp    The timestamp that identifies the MPDF file.
     * @return             A FileOutputStream that allows writing to file.
     * @throws IOException Thrown when the file specified by the timestamp does not exist.
     */
    public static FileOutputStream openForWrite(HighPrecisionDate timestamp)
        throws IOException
    {
        File file = getFile(timestamp);

        if (!file.exists())
        {
            throw new IOException("File does not exist.");
        }

        return new FileOutputStream(file);
    }

    /**
     * Opens an MPDF file for read access.
     *
     * @param timestamp    The timestamp that identifies the MPDF file.
     * @return             A FileInputStream that allows reading from file.
     * @throws IOException Thrown when the file specified by the timestamp does not exist.
     */
    public static FileInputStream openForRead(HighPrecisionDate timestamp) throws IOException
    {
        File file = getFile(timestamp);

        if (!file.exists())
        {
            throw new IOException("File does not exist.");
        }

        return new FileInputStream(file);
    }

    /**
     * Checks whether the MPDF file with the given timestamp exists.
     *
     * @param timestamp The timestamp that identifies the MPDF file.
     * @return          true if exists, false if not.
     */
    public static boolean exists(HighPrecisionDate timestamp)
    {
        return getFile(timestamp).exists();
    }

    /**
     * Gets the size of the file stored with given timestamp.
     *
     * @param timestamp    The timestamp that identifies the MPDF file.
     * @return             The size of the file in bytes.
     * @throws IOException Thrown when the file identified by given timestamp does not exist.
     */
    public static long size(HighPrecisionDate timestamp) throws IOException
    {
        File file = getFile(timestamp);

        if (!file.exists())
        {
            throw new IOException("File does not exist.");
        }

        return getFile(timestamp).length();
    }

    /**
     * Returns all available timestamps for that MPDF files are saved.
     *
     * @return The collection of the timestamps that identify each MPDF file.
     */
    public static Collection<HighPrecisionDate> getStoredFiles()
    {
        LinkedList<HighPrecisionDate> stored_timestamps = new LinkedList<>();

        for (File elem : getWorkingDirectory().listFiles())
        {
            String filename = elem.getName();

            if (org.apache.commons.io.FilenameUtils.getExtension(filename).equals(FILE_EXTENSION))
            {
                try
                {
                    stored_timestamps.add(timestampFromFilename(filename));
                }
                catch (NumberFormatException ignored)
                {}
            }
        }

        return Collections.unmodifiableList(stored_timestamps);
    }

    /**
     * Sets the context this module shall operate on.
     *
     * @param context The context.
     */
    public static void setContext(Context context)
    {
        m_Context = context;
    }

    /**
     * Returns the working directory of the MPDF file manager.
     *
     * @return The File object that describes the working directory.
     */
    private static File getWorkingDirectory()
    {
        return m_Context.getDir("data-frames", Context.MODE_PRIVATE);
    }

    /**
     * Returns the File object from the given timestamp.
     *
     * @param timestamp The timestamp that identifies the MPDF file.
     * @return          The File descriptor object.
     */
    private static File getFile(HighPrecisionDate timestamp)
    {
        return new File(getWorkingDirectory(),
                        filenameFromTimestamp(timestamp));
    }

    /**
     * Constructs the path string from a timestamp.
     *
     * @param timestamp The timestamp that identifies the MPDF file.
     * @return          The path string.
     */
    private static String filenameFromTimestamp(HighPrecisionDate timestamp)
    {
        return Long.toString(timestamp.toDate().getTime()) + "." + FILE_EXTENSION;
    }

    /**
     * Constructs the HighPrecisionDate out of the stored MPDF file name.
     *
     * @param filename               The file name of the MPDF file.
     * @return                       The HighPrecisionDate that is the key of this file.
     * @throws NumberFormatException The given file path is not a managed MPDF file and so does not
     *                               has a valid key.
     */
    private static HighPrecisionDate timestampFromFilename(String filename)
        throws NumberFormatException
    {
        String timestamp_name = org.apache.commons.io.FilenameUtils.removeExtension(filename);

        return new HighPrecisionDate(new Date(Long.parseLong(timestamp_name)));
    }

    /**
     * The MPDF file ending.
     */
    private final static String FILE_EXTENSION = "mpdf";

    /**
     * The context the DataFrameFileManager shall operate on.
     */
    private static Context m_Context = null;
}
