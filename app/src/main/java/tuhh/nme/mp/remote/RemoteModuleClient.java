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
 * Also you should use this class in the UI main thread since this object uses AsyncTask's.
 */
public class RemoteModuleClient
{
    /**
     * The command that fetches a bunch of data.
     */
    private class GetDataFrameCommand extends SocketCommand<HighPrecisionDatedDataFrame<Short>>
    {
        /**
         * Instantiates a new GetDataFrameCommand.
         *
         * @param count The number of values to fetch.
         */
        public GetDataFrameCommand(int count)
        {
            m_Count = count;
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
            data.ensureCapacity(m_Count);

            OutputStream out_stream = socket.getOutputStream();
            InputStream in_stream = socket.getInputStream();

            out_stream.write(CMD_START_TRANSFER);
            out_stream.flush();

            try
            {
                byte[] buffer = new byte[2];

                ByteBuffer short_buffer = ByteBuffer.allocate(Short.SIZE / Byte.SIZE);

                for (int i = 0; i < m_Count; i++)
                {
                    if (short_buffer.capacity() != in_stream.read(buffer))
                    {
                        // Server seems to work wrong since he sent the wrong number of bytes.
                        throw new InvalidReactionException(
                            "Server sent wrong number of bytes. Expected " +
                            Integer.toString(short_buffer.capacity()) + " bytes.");
                    }

                    short_buffer.position(0);
                    short_buffer.put(buffer);
                    data.add(new DataPoint<>(HighPrecisionDate.now(), short_buffer.getShort(0)));
                }
            }
            catch(IOException ex)
            {
                // Re-raise the exception after closing, so we stop the data transfer.
                out_stream.write(CMD_STOP_TRANSFER);
                out_stream.flush();
                throw ex;
            }

            // Stop data transfer.
            out_stream.write(CMD_STOP_TRANSFER);
            out_stream.flush();

            // It's possible that this command was too slow to stop the data-transfer exactly
            // at that time when we finished reading, so we have data left in the socket buffer.
            //in_stream.skip(Long.MAX_VALUE);

            return new HighPrecisionDatedDataFrame<>(data);
        }

        private static final int CMD_START_TRANSFER = 0x00;
        private static final int CMD_STOP_TRANSFER = 0x01;

        /**
         * The number of values to fetch in this command.
         */
        private final int m_Count;
    }

    /**
     * Instantiates a RemoteModuleClient.
     *
     * @param address    The address to connect to.
     * @param port       The port to connect to.
     * @throws Throwable Any error that occurs during initialization.
     */
    public RemoteModuleClient(InetAddress address, int port) throws Throwable
    {
        m_Client = new Client(address, port);
    }

    /**
     * Retrieves data from the remote module.
     *
     * This routine blocks the calling thread until the data is completely received.
     *
     * @param count                           The number of data points to fetch.
     * @return                                The received data packed into a DataFrame.
     * @throws InterruptedException           Thrown when the read command could not be placed into
     *                                        processing queue.
     * @throws SocketCommandHandlingException Thrown when an exception occurred during network read
     *                                        attempt.
     */
    public HighPrecisionDatedDataFrame<Short> read(int count)
        throws InterruptedException, SocketCommandHandlingException
    {
        GetDataFrameCommand cmd = new GetDataFrameCommand(count);
        m_Client.command(cmd);
        return cmd.getResult();
    }

    /**
     * Closes the Client connection.
     *
     * @throws SocketCommandHandlingException Thrown when something goes wrong during close.
     */
    public void close() throws SocketCommandHandlingException
    {
        m_Client.close();
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
     * The Client that fires up the socket thread the client does communicate over.
     */
    private Client m_Client;
}
