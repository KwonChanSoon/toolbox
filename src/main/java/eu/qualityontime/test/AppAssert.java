package eu.qualityontime.test;

import java.util.Collection;

public class AppAssert {
  public static void assertSize(int length, Collection<?> col) {
    assertSize("not " + length, length, col);
  }

  public static void assertSize(String message, int length, Collection<?> col) {
    if (length != col.size()) {
      fail(message);
    }
  }

  private static void fail(String message) {
    if (message == null) {
      throw new AssertionError();
    }
    throw new AssertionError(message);
  }

  public static void assertLength(int length, Collection<?> col) {
    assertSize(length, col);
  }

  public static void assertLength(String message, int length, Collection<?> col) {
    assertSize(message, length, col);
  }

}
