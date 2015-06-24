#!/usr/bin/python3

import argparse

from Server import Server


def create_argparser():
    """
    Creates the application argparser object.
    """
    def parse_port(arg):
        value = int(arg)
        if value < 0 or value > 65535:
            raise argparse.ArgumentTypeError(
                "Port must range from 0 until 65535.")
        return value

    def parse_speed(arg):
        value = float(arg)
        if value < 0:
            raise argparse.ArgumentTypeError(
                "Speed must be a positive integer.")
        return value

    parser = argparse.ArgumentParser(
        description="Remote module simulation for MP.")

    parser.add_argument("--port",
                        "-p",
                        type=parse_port,
                        nargs="?",
                        default="1337",
                        help="The port where the server shall listen.")

    parser.add_argument("--speed",
                        "-s",
                        type=parse_speed,
                        nargs="?",
                        default="0.05",
                        help="The speed of data-transfers when data-transfer "
                             "is activated (in seconds).")

    parser.add_argument("--verbose",
                        "-v",
                        help="When specified, prints extra verbose messages.",
                        action="store_true")

    return parser


if __name__ == "__main__":
    args = create_argparser().parse_args()

    print("Data-transfer speed: {}s".format(args.speed))

    if args.verbose:
        print("Verbose mode on.")

    print("Starting server on port {}... ".format(args.port), end="")
    server = Server(args.port, args.speed, args.verbose)
    print("Done.")

    print("To cancel the program, press CTRL+C.")

    try:
        server.serve_forever()
    except KeyboardInterrupt:
        pass

