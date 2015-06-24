import socket
import socketserver
import struct

from DataGenerator import DataGenerator


class Server(socketserver.TCPServer):
    """
    The TCPServer that connects to the remote module.
    """

    def __init__(self, port, data_rate, verbose=False):
        """
        Instantiates a new TCPServer listening for the app.

        :param port:      The port on which to listen.
        :param data_rate: The speed of data-sends when data transfer is
                          activated (in seconds).
        :param verbose:   Whether to print verbose messages.
        """
        socketserver.TCPServer.__init__(self, ("", port), ServerHandler)

        self.data_rate = data_rate
        self.verbose = verbose

    def printv(self, *args, **kwargs):
        """
        Prints a message if in verbose mode.

        :param args:   The messages to print.
        :param kwargs: Additional named arguments to pass to the print function.
        """
        if self.verbose:
            print(*args, **kwargs)


class ServerHandler(socketserver.BaseRequestHandler):
    """
    The TCPServer handler that shall simulate the remote module.
    """

    # Server commands.
    commands = {"CMD_START_TRANSFER": 0x00,
                "CMD_STOP_TRANSFER": 0x01}

    # Inverse order of commands to allow command string lookup by value.
    inverse_commands = {v: k for k, v in commands.items()}

    @staticmethod
    def _convert_short_to_string(value):
        """
        Pack the given short into a 2-byte string.

        :param value: The short value to pack.
        """
        return struct.pack("!h", value)

    def _receive_command(self):
        """
        Waits for an incoming 1-byte command.
        """
        x = self.request.recv(1)
        if len(x) != 0:
            x = int.from_bytes(x, "big")
            try:
                self.server.printv("Received command: {} ({})".format(
                    hex(x), self.inverse_commands[x]))
            except KeyError:
                self.server.printv("Received unknown command: {}".format(
                    hex(x)))

            return x

    def handle(self):
        """
        Handles an incoming connection.
        """
        self.server.printv("Connection opened.")

        data_generator = DataGenerator()

        while True:
            cmd = self._receive_command()

            if cmd == self.commands["CMD_START_TRANSFER"]:
                self.server.printv("Starting transfer...")

                to = self.request.gettimeout()
                self.request.settimeout(self.server.data_rate)

                running = True

                while running:
                    try:
                        cmd = self._receive_command()

                        if cmd == self.commands["CMD_STOP_TRANSFER"]:
                            self.request.settimeout(to)
                            running = False

                    except socket.timeout:
                        # No command received, continue sending data.
                        value_to_send = data_generator.generate()
                        self.request.sendall(
                            self._convert_short_to_string(value_to_send))
                        self.server.printv("Sent {}.".format(value_to_send))

                self.server.printv("Transfer complete.")

            elif cmd == None:
                self.server.printv("Connection closed.")
                break

