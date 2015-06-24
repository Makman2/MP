import math


class DataGenerator():
    """
    A data generator class that holds a state and returns arbitrary data.
    """

    def __init__(self):
        """
        Instantiates a new data generator class.
        """
        self.t = 0.0

    def generate(self):
        """
        Simulates a background measuring process of the remote module. This
        function returns custom defined data.
        """
        val = math.sin(self.t) * 50 + 50
        self.t += 0.4
        return int(val)

