import socket
import socketserver
import struct
import time

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
    commands = {"CMD_TRANSFER_DENSITY0": 0x30,
                "CMD_TRANSFER_DENSITY1": 0x31,
                "CMD_TRANSFER_DENSITY2": 0x32,
                "CMD_TRANSFER_DENSITY3": 0x33,
                "CMD_TRANSFER_DENSITY4": 0x34,
                "CMD_TRANSFER_DENSITY5": 0x35,
                "CMD_TRANSFER_DENSITY6": 0x36,
                "CMD_TRANSFER_DENSITY7": 0x37,
                "CMD_TRANSFER_DENSITY8": 0x38,
                "CMD_TRANSFER_DENSITY9": 0x39}

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

    def _send_transfer_package(self, size, data_generator):
        """
        Sends a data-package with specified size.

        :param size:           The number of values to send.
        :param data_generator: The data generator that generates the values to
                               send.
        """
        def send(value):
            # The send emulates also the zero byte from the remote module.
            self.request.sendall(
                bytes([0]) + self._convert_short_to_string(value))
            self.server.printv("Sent {}.".format(value))

        self.server.printv("Transfer package (size = {})...".format(size))

        for i in range(size - 1):
            send(data_generator.generate())
            time.sleep(self.server.data_rate)

        send(data_generator.generate())
        self.server.printv("Transfer complete.")

    def handle(self):
        """
        Handles an incoming connection.
        """
        self.server.printv("Connection opened.")

        data_generator = DataGenerator()

        while True:
            cmd = self._receive_command()

            if cmd == None:
                self.server.printv("Connection closed.")
                break

            elif cmd == self.commands["CMD_TRANSFER_DENSITY0"]:
                self._send_transfer_package(2, data_generator)

            elif cmd == self.commands["CMD_TRANSFER_DENSITY1"]:
                self._send_transfer_package(4, data_generator)

            elif cmd == self.commands["CMD_TRANSFER_DENSITY2"]:
                self._send_transfer_package(6, data_generator)

            elif cmd == self.commands["CMD_TRANSFER_DENSITY3"]:
                self._send_transfer_package(8, data_generator)

            elif cmd == self.commands["CMD_TRANSFER_DENSITY4"]:
                self._send_transfer_package(10, data_generator)

            elif cmd == self.commands["CMD_TRANSFER_DENSITY5"]:
                self._send_transfer_package(12, data_generator)

            elif cmd == self.commands["CMD_TRANSFER_DENSITY6"]:
                self._send_transfer_package(14, data_generator)

            elif cmd == self.commands["CMD_TRANSFER_DENSITY7"]:
                self._send_transfer_package(16, data_generator)

            elif cmd == self.commands["CMD_TRANSFER_DENSITY8"]:
                self._send_transfer_package(18, data_generator)

            elif cmd == self.commands["CMD_TRANSFER_DENSITY9"]:
                self._send_transfer_package(20, data_generator)

