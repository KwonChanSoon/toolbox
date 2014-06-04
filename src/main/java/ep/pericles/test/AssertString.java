package ep.pericles.test;

import static org.junit.Assert.fail;

public class AssertString {
  /**
   * Can a given `expectedPattern` found in the `actual` string? uing `String.contains`
   */
  public static void assertLike(String message, String expectedPattern, String actual) {
    if (!actual.contains(expectedPattern)) {
      fail(message);
    }
  }

  /**
   * Can a given `expectedPattern` found in the `actual` string? Using `String.contains`
   */
  public static void assertLike(String expectedPattern, String actual) {
    assertLike("Pattern <" + expectedPattern + "> cannot be found", expectedPattern, actual);
  }

  public static void assertNotLike(String message, String expectedPattern, String actual) {
    if (actual.contains(expectedPattern)) {
      fail(message);
    }
  }

  public static void assertNotLike(String expectedPattern, String actual) {
    assertNotLike("Pattern match found", expectedPattern, actual);
  }

}
