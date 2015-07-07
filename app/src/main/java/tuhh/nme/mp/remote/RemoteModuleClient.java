package tuhh.nme.mp.remote;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import tuhh.nme.mp.data.DataPoint;
import tuhh.nme.mp.data.HighPrecisionDate;
import tuhh.nme.mp.data.HighPrecisionDatedDataFrame;


/**
 * This client is the interface for data transactions between this app and the remote module.
 *
 * Note that you need to close this class manually.
 */
public class RemoteModuleClient
{
    /**
     * The transfer package sizes/densities available in the remote module device.
     */
    public enum TransferDensity
    {
        /**
         * 2 values per package.
         */
        DENSITY0,
        /**
         * 4 values per package.
         */
        DENSITY1,
        /**
         * 6 values per package.
         */
        DENSITY2,
        /**
         * 8 values per package.
         */
        DENSITY3,
        /**
         * 10 values per package.
         */
        DENSITY4,
        /**
         * 12 values per package.
         */
        DENSITY5,
        /**
         * 14 values per package.
         */
        DENSITY6,
        /**
         * 16 values per package.
         */
        DENSITY7,
        /**
         * 18 values per package.
         */
        DENSITY8,
        /**
         * 20 values per package.
         */
        DENSITY9;

        /**
         * Returns the value count corresponding to the given density.
         *
         * @param density The density to determine the package size of.
         * @return        The package size. -1 if the given density is invalid.
         */
        public static int count(TransferDensity density)
        {
            switch (density)
            {
                case DENSITY0:
                    return 2;

                case DENSITY1:
                    return 4;

                case DENSITY2:
                    return 6;

                case DENSITY3:
                    return 8;

                case DENSITY4:
                    return 10;

                case DENSITY5:
                    return 12;

                case DENSITY6:
                    return 14;

                case DENSITY7:
                    return 16;

                case DENSITY8:
                    return 18;

                case DENSITY9:
                    return 20;

                default:
                    return -1;
            }
        }

        /**
         * Returns the network protocol command byte.
         *
         * @param density                   The density to determine protocol command of.
         * @return                          The protocol command of the given density.
         * @throws IllegalArgumentException Thrown when the given density is not supported.
         */
        static char protocolCommand(TransferDensity density) throws IllegalArgumentException
        {
            switch (density)
            {
                case DENSITY0:
                    return 0x30;

                case DENSITY1:
                    return 0x31;

                case DENSITY2:
                    return 0x32;

                case DENSITY3:
                    return 0x33;

                case DENSITY4:
                    return 0x34;

                case DENSITY5:
                    return 0x35;

                case DENSITY6:
                    return 0x36;

                case DENSITY7:
                    return 0x37;

                case DENSITY8:
                    return 0x38;

                case DENSITY9:
                    return 0x39;

                default:
                    throw new IllegalArgumentException("Given density not supported!");
            }
        }
    }

    /**
     * The command that fetches a bunch of data.
     */
    private class GetDataFrameCommand extends SocketCommand<HighPrecisionDatedDataFrame<Short>>
    {
        /**
         * Instantiates a new GetDataFrameCommand.
         *
         * @param density The number of values to fetch.
         */
        public GetDataFrameCommand(TransferDensity density)
        {
            m_Density = density;
        }

        /**
         * Handles the command processing.
         *
         * @param socket     The socket to operate on.
         * @return           The fetched values from the remote module packed into a DataFrame.
         * @throws Throwable The exception that was raised during handling.
         */
        @Override
        protected HighPrecisionDatedDataFrame<Short> handle(Socket socket) throws Throwable
        {
            if (socket == null)
            {
                return null;
            }

            ArrayList<DataPoint<HighPrecisionDate, Short>> data = new ArrayList<>();
            int pkg_size = TransferDensity.count(m_Density);
            data.ensureCapacity(pkg_size);

            OutputStream out_stream = socket.getOutputStream();
            InputStream in_stream = socket.getInputStream();

            out_stream.write(TransferDensity.protocolCommand(m_Density));
            out_stream.flush();

            // Since the remote module has problems to send two bytes (it sets the low-byte to zero
            // every time, regardless of data), we receive three and ignore the zero byte.
            byte[] buffer = new byte[3];

            ByteBuffer short_buffer = ByteBuffer.allocate(Short.SIZE / Byte.SIZE);

            // Don't catch socket errors, pass them to the socket thread.
            for (int i = 0; i < pkg_size; i++)
            {
                if (buffer.length != in_stream.read(buffer))
                {
                    // Server seems to work wrong since it has sent the wrong number of bytes.
                    throw new InvalidReactionException(
                        "Server sent wrong number of bytes. Expected " +
                        Integer.toString(buffer.length) + " bytes.");
                }

                short_buffer.position(0);
                short_buffer.put(buffer, 1, buffer.length - 1);
                data.add(new DataPoint<>(HighPrecisionDate.now(), short_buffer.getShort(0)));
            }

            return new HighPrecisionDatedDataFrame<>(data);
        }

        /**
         * The number of values to fetch in this command.
         */
        private final TransferDensity m_Density;
    }

    /**
     * Instantiates a RemoteModuleClient.
     *
     * This constructor does not actually connect to the target. To do so, use connect().
     *
     * @param address The address to connect to.
     * @param port    The port to connect to.
     */
    public RemoteModuleClient(InetAddress address, int port)
    {
        m_Client = new Client(address, port);
    }

    /**
     * Connects to the desired device.
     *
     * @throws IOException Thrown when the client couldn't connect to given address and port.
     */
    public void connect() throws IOException
    {
        m_Client.connect();
    }

    /**
     * Retrieves data from the remote module.
     *
     * This routine blocks the calling thread until the data is completely received.
     *
     * @param density                         The density that determines the number of data points
     *                                        to fetch.
     * @return                                The received data packed into a DataFrame.
     * @throws InterruptedException           Thrown when the read command could not be placed into
     *                                        processing queue.
     * @throws SocketCommandHandlingException Thrown when an exception occurred during network read
     *                                        attempt.
     */
    public HighPrecisionDatedDataFrame<Short> read(TransferDensity density)
        throws InterruptedException, SocketCommandHandlingException
    {
        GetDataFrameCommand cmd = new GetDataFrameCommand(density);
        m_Client.command(cmd);
        return cmd.getResult();
    }

    /**
     * Closes the Client connection.
     *
     * This call blocks until all remaining read attempts are processed and the close is
     * acknowledged by the background thread.
     *
     * @throws SocketCommandHandlingException Thrown when something goes wrong during close.
     */
    public void close() throws SocketCommandHandlingException
    {
        m_Client.close();
    }

    /**
     * Tells the background Client thread to immediately terminate and close the connection.
     *
     * This call blocks until all remaining commands are flushed.
     */
    public void terminate()
    {
        m_Client.terminate();
    }

    /**
     * Returns the address the Client is connected to.
     *
     * @return The address.
     */
    public final InetAddress getAddress()
    {
        return m_Client.getAddress();
    }

    /**
     * Returns the port the Client is connected to.
     *
     * @return The port.
     */
    public final int getPort()
    {
        return m_Client.getPort();
    }

    /**
     * The Client that fires up the socket thread.
     */
    private Client m_Client;
}
