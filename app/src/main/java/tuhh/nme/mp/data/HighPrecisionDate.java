package tuhh.nme.mp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigInteger;
import java.util.Date;


/**
 * A date with nanosecond precision. Starts counting from epoch (1/1/1970 00:00:00:000:000).
 */
public class HighPrecisionDate implements Parcelable
{
    /**
     * Instantiates a new HighPrecisionDate from the given count of nanoseconds passed since epoch.
     *
     * @param nanoseconds The nanoseconds to initialize with.
     */
    public HighPrecisionDate(BigInteger nanoseconds)
    {
        m_Nanoseconds = nanoseconds;
    }

    /**
     * Instantiates a new HighPrecisionDate from the given Date.
     *
     * @param date The Date to initialize with.
     */
    public HighPrecisionDate(Date date)
    {
        this(BigInteger.valueOf(date.getTime()).multiply(BigInteger.valueOf(1000000L)));
    }

    /**
     * Copy constructor.
     *
     * @param copy Object where to copy from.
     */
    public HighPrecisionDate(HighPrecisionDate copy)
    {
        this(copy.getTime());
    }

    /**
     * Instantiates a new HighPrecisionDate from the given count of nanoseconds passed since epoch.
     *
     * @param nanoseconds The nanoseconds to initialize with.
     */
    public static HighPrecisionDate fromNanoseconds(long nanoseconds)
    {
        return new HighPrecisionDate(BigInteger.valueOf(nanoseconds));
    }

    /**
     * Instantiates a new HighPrecisionDate from the given count of microseconds passed since epoch.
     *
     * @param microseconds The microseconds to initialize with.
     */
    public static HighPrecisionDate fromMicroseconds(long microseconds)
    {
        BigInteger factor = BigInteger.valueOf(1000L);
        BigInteger time = BigInteger.valueOf(microseconds);
        return new HighPrecisionDate(time.multiply(factor));
    }

    /**
     * Instantiates a new HighPrecisionDate from the given count of milliseconds passed since epoch.
     *
     * @param milliseconds The milliseconds to initialize with.
     */
    public static HighPrecisionDate fromMilliseconds(long milliseconds)
    {
        BigInteger factor = BigInteger.valueOf(1000000L);
        BigInteger time = BigInteger.valueOf(milliseconds);
        return new HighPrecisionDate(time.multiply(factor));
    }

    /**
     * Instantiates a new HighPrecisionDate from the given count of seconds passed since epoch.
     *
     * @param seconds The seconds to initialize with.
     */
    public static HighPrecisionDate fromSeconds(long seconds)
    {
        BigInteger factor = BigInteger.valueOf(1000000000L);
        BigInteger time = BigInteger.valueOf(seconds);
        return new HighPrecisionDate(time.multiply(factor));
    }

    /**
     * Instantiates a new HighPrecisionDate from the given count of minutes passed since epoch.
     *
     * @param minutes The minutes to initialize with.
     */
    public static HighPrecisionDate fromMinutes(long minutes)
    {
        BigInteger factor = BigInteger.valueOf(60000000000L);
        BigInteger time = BigInteger.valueOf(minutes);
        return new HighPrecisionDate(time.multiply(factor));
    }

    /**
     * Instantiates a new HighPrecisionDate from the given count of hours passed since epoch.
     *
     * @param hours The hours to initialize with.
     */
    public static HighPrecisionDate fromHours(long hours)
    {
        BigInteger factor = BigInteger.valueOf(3600000000000L);
        BigInteger time = BigInteger.valueOf(hours);
        return new HighPrecisionDate(time.multiply(factor));
    }

    /**
     * Returns the current date at invocation.
     *
     * Note that the time has millisecond precision, since the system time is not finer graded.
     *
     * @return The current date.
     */
    public static HighPrecisionDate now()
    {
        return new HighPrecisionDate(new Date());
    }

    /**
     * Returns the amount of time stored (in nanoseconds) in this HighPrecisionDate.
     *
     * @return The number of nanoseconds.
     */
    public BigInteger getTime()
    {
        return m_Nanoseconds;
    }

    /**
     * Converts the HighPrecisionDate into a normal Date while rounding to milliseconds.
     *
     * @return                       The Date.
     * @throws IllegalStateException Thrown when the current HighPrecisionDate exceeds the capacity
     *                               of a normal Date.
     */
    public Date toDate() throws IllegalStateException
    {
        BigInteger ms = m_Nanoseconds.divide(BigInteger.valueOf(1000000L));
        if (ms.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) == 1)
        {
            throw new IllegalStateException("The current HighPrecisionDate is not convertible " +
                                            "into a Date since it exceeds its capacity limit.");
        }
        return new Date(ms.longValue());
    }

    /**
     * Adds an HighPrecisionTimeSpan to this HighPrecisionDate.
     *
     * @param sum The HighPrecisionTimeSpan to add.
     * @return    A HighPrecisionDate that represents the sum.
     */
    public HighPrecisionDate add(HighPrecisionTimeSpan sum)
    {
        return new HighPrecisionDate(m_Nanoseconds.add(sum.getTime()));
    }

    /**
     * Subtracts an HighPrecisionTimeSpan from this HighPrecisionDate.
     *
     * @param difference The HighPrecisionTimeSpan to subtract.
     * @return           A HighPrecisionDate that represents the difference.
     */
    public HighPrecisionDate subtract(HighPrecisionTimeSpan difference)
    {
        return new HighPrecisionDate(m_Nanoseconds.subtract(difference.getTime()));
    }

    /**
     * Subtracts an HighPrecisionDate from this HighPrecisionDate.
     *
     * @param date The HighPrecisionDate to subtract.
     * @return     The relative HighPrecisionTimeSpan difference.
     */
    public HighPrecisionTimeSpan subtract(HighPrecisionDate date)
    {
        return new HighPrecisionTimeSpan(m_Nanoseconds.subtract(date.getTime()));
    }

    /**
     * Checks whether the stored dates of this and the other object do equal.
     *
     * @param o The other object to check.
     * @return  true if equal, false if not.
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof HighPrecisionDate)) return false;

        HighPrecisionDate oHighPrecisionDate = (HighPrecisionDate)o;
        return oHighPrecisionDate.getTime().equals(m_Nanoseconds);
    }

    /**
     * Determines whether this instance is less than the given HighPrecisionDate.
     *
     * @param dt The HighPrecisionDate to compare with.
     * @return   If this instance is less, returns true, otherwise false.
     */
    public boolean less(HighPrecisionDate dt)
    {
        int compare = m_Nanoseconds.compareTo(dt.m_Nanoseconds);
        return compare == -1;
    }

    /**
     * Determines whether this instance is greater than the given HighPrecisionDate.
     *
     * @param dt The HighPrecisionDate to compare with.
     * @return   If this instance is greater, returns true, otherwise false.
     */
    public boolean greater(HighPrecisionDate dt)
    {
        int compare = m_Nanoseconds.compareTo(dt.m_Nanoseconds);
        return compare == 1;
    }

    /**
     * Returns a string representation of this object.
     *
     * @return The string representing this object.
     */
    @Override
    public String toString()
    {
        return m_Nanoseconds.toString();
    }

    // Inherited documentation.
    @Override
    public int describeContents()
    {
        return 0;
    }

    // Inherited documentation.
    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(toString());
    }

    /**
     * Creates a Parcelable creator.
     */
    public static final Parcelable.Creator<HighPrecisionDate> CREATOR =
        new Parcelable.Creator<HighPrecisionDate>()
        {
            public HighPrecisionDate createFromParcel(Parcel in)
            {
                return new HighPrecisionDate(new BigInteger(in.readString()));
            }

            public HighPrecisionDate[] newArray(int size)
            {
                return new HighPrecisionDate[size];
            }
        };

    /**
     * The epoch date.
     */
    public static final HighPrecisionDate epoch = HighPrecisionDate.fromNanoseconds(0);

    /**
     * The number of nanoseconds stored.
     */
    private BigInteger m_Nanoseconds;
}
