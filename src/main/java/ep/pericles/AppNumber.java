package ep.pericles;

import java.math.*;

/**
 * Check AppNumberTest to understand why these utility methods are needed.
 */
public class AppNumber {
  public static int compare(final Number x, final Number y) {
    return toBigDecimal(x).compareTo(toBigDecimal(y));
  }

  private static BigDecimal toBigDecimal(final Number number) {
    if (null == number) {
      return null;
    }
    if (number instanceof BigDecimal) {
      return (BigDecimal) number;
    }
    if (number instanceof BigInteger) {
      return new BigDecimal((BigInteger) number);
    }
    if (number instanceof Byte || number instanceof Short
        || number instanceof Integer || number instanceof Long) {
      return new BigDecimal(number.longValue());
    }
    if (number instanceof Float || number instanceof Double) {
      return new BigDecimal(number.doubleValue());
    }

    try {
      return new BigDecimal(number.toString());
    } catch (final NumberFormatException e) {
      throw new RuntimeException("The given number (\"" + number
          + "\" of class " + number.getClass().getName()
          + ") does not have a parsable string representation", e);
    }
  }

  public static boolean equal(final Number x, final Number y) {
    return eq(x, y);
  }

  public static boolean eq(final Number x, final Number y) {
    return 0 == compare(x, y);
  }

  public static boolean neq(final Number x, final Number y) {
    return !eq(x, y);
  }

  public static boolean gt(final Number x, final Number y) {
    return 0 < compare(x, y);
  }

  public static boolean lt(final Number x, final Number y) {
    return 0 > compare(x, y);
  }
}
