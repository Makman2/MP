package tuhh.nme.mp.data;

import java.math.BigInteger;

/**
 * Represents a time difference with nanosecond-precision.
 */
public class HighPrecisionTimeSpan
{
    /**
     * Instantiates a new HighPrecisionTimeSpan from the given count of nanoseconds.
     *
     * @param nanoseconds The nanoseconds to initialize with.
     */
    public HighPrecisionTimeSpan(BigInteger nanoseconds)
    {
        m_Nanoseconds = nanoseconds;
    }

    /**
     * Copy constructor.
     *
     * @param copy Object where to copy from.
     */
    public HighPrecisionTimeSpan(HighPrecisionTimeSpan copy)
    {
        this(copy.getTime());
    }

    /**
     * Instantiates a new HighPrecisionTimeSpan from the given count of nanoseconds.
     *
     * @param nanoseconds The nanoseconds to initialize with.
     */
    public static HighPrecisionTimeSpan fromNanoseconds(long nanoseconds)
    {
        return new HighPrecisionTimeSpan(BigIntegerConverter.toBigInteger(nanoseconds));
    }

    /**
     * Instantiates a new HighPrecisionTimeSpan from the given count of microseconds.
     *
     * @param microseconds The microseconds to initialize with.
     */
    public static HighPrecisionTimeSpan fromMicroseconds(long microseconds)
    {
        BigInteger factor = BigIntegerConverter.toBigInteger(1000);
        BigInteger time = BigIntegerConverter.toBigInteger(microseconds);
        return new HighPrecisionTimeSpan(time.multiply(factor));
    }

    /**
     * Instantiates a new HighPrecisionTimeSpan from the given count of milliseconds.
     *
     * @param milliseconds The milliseconds to initialize with.
     */
    public static HighPrecisionTimeSpan fromMilliseconds(long milliseconds)
    {
        BigInteger factor = BigIntegerConverter.toBigInteger(1000000);
        BigInteger time = BigIntegerConverter.toBigInteger(milliseconds);
        return new HighPrecisionTimeSpan(time.multiply(factor));
    }

    /**
     * Instantiates a new HighPrecisionTimeSpan from the given count of seconds.
     *
     * @param seconds The seconds to initialize with.
     */
    public static HighPrecisionTimeSpan fromSeconds(long seconds)
    {
        BigInteger factor = BigIntegerConverter.toBigInteger(1000000000);
        BigInteger time = BigIntegerConverter.toBigInteger(seconds);
        return new HighPrecisionTimeSpan(time.multiply(factor));
    }

    /**
     * Instantiates a new HighPrecisionTimeSpan from the given count of minutes.
     *
     * @param minutes The minutes to initialize with.
     */
    public static HighPrecisionTimeSpan fromMinutes(long minutes)
    {
        BigInteger factor = BigIntegerConverter.toBigInteger(60000000000L);
        BigInteger time = BigIntegerConverter.toBigInteger(minutes);
        return new HighPrecisionTimeSpan(time.multiply(factor));
    }

    /**
     * Instantiates a new HighPrecisionTimeSpan from the given count of hours.
     *
     * @param hours The hours to initialize with.
     */
    public static HighPrecisionTimeSpan fromHours(long hours)
    {
        BigInteger factor = BigIntegerConverter.toBigInteger(3600000000000L);
        BigInteger time = BigIntegerConverter.toBigInteger(hours);
        return new HighPrecisionTimeSpan(time.multiply(factor));
    }

    /**
     * Returns the amount of time stored (in nanoseconds) in this HighPrecisionTimeSpan.
     *
     * @return The number of nanoseconds.
     */
    public BigInteger getTime()
    {
        return m_Nanoseconds;
    }

    /**
     * Adds an HighPrecisionTimeSpan to this HighPrecisionTimeSpan.
     *
     * @param sum The HighPrecisionTimeSpan to add.
     * @return    A HighPrecisionTimeSpan that represents the sum.
     */
    public HighPrecisionTimeSpan add(HighPrecisionTimeSpan sum)
    {
        return new HighPrecisionTimeSpan(m_Nanoseconds.add(sum.getTime()));
    }

    /**
     * Subtracts an HighPrecisionTimeSpan from this HighPrecisionTimeSpan.
     *
     * @param difference The HighPrecisionTimeSpan to subtract.
     * @return           A HighPrecisionTimeSpan that represents the difference.
     */
    public HighPrecisionTimeSpan subtract(HighPrecisionTimeSpan difference)
    {
        return new HighPrecisionTimeSpan(m_Nanoseconds.subtract(difference.getTime()));
    }

    /**
     * Multiplies a BigInteger with this HighPrecisionTimeSpan.
     *
     * @param factor The factor to multiply with.
     * @return       A HighPrecisionTimeSpan that represents the product.
     */
    public HighPrecisionTimeSpan multiply(BigInteger factor)
    {
        return new HighPrecisionTimeSpan(m_Nanoseconds.multiply(factor));
    }

    /**
     * Checks whether the stored time spans of this and the other object do equal.
     *
     * @param o The other object to check.
     * @return  true if equal, false if not.
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof HighPrecisionTimeSpan)) return false;

        HighPrecisionTimeSpan oHighPrecisionTimeSpan = (HighPrecisionTimeSpan)o;
        return oHighPrecisionTimeSpan.getTime().equals(m_Nanoseconds);
    }

    /**
     * A zero time span.
     */
    public static final HighPrecisionTimeSpan zero = HighPrecisionTimeSpan.fromNanoseconds(0);

    /**
     * The number of nanoseconds stored.
     */
    private BigInteger m_Nanoseconds;
}
