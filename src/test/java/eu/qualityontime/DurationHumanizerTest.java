package eu.qualityontime;

import static org.junit.Assert.*;

import org.junit.Test;

public class DurationHumanizerTest {

  @Test
  public void isValid() {
    assertTrue(DurationHumanizer.isValid("12h32m"));
    assertTrue(DurationHumanizer.isValid("12h"));
    assertTrue(DurationHumanizer.isValid("32m"));
    assertFalse(DurationHumanizer.isValid("32minutes"));
    assertTrue(DurationHumanizer.isValid("  12   h    32   m   "));

    assertTrue(DurationHumanizer.isValid("12:32"));
    assertTrue(DurationHumanizer.isValid("02:75"));
    assertFalse(DurationHumanizer.isValid("2:11"));
    assertTrue(DurationHumanizer.isValid("   02  :   75   "));
    assertFalse(DurationHumanizer.isValid("02:75he"));

    assertTrue(DurationHumanizer.isValid("02"));
    assertTrue(DurationHumanizer.isValid("2"));
    assertTrue(DurationHumanizer.isValid("  1   2     "));
    assertFalse(DurationHumanizer.isValid("02a"));

    assertFalse(DurationHumanizer.isValid(""));

  }

  @Test
  public void parseInMillis() throws Exception {
    assertEquals(1000 * 60, DurationHumanizer.parseInMillis("1m"));
    assertEquals(1000 * 60 * 60, DurationHumanizer.parseInMillis("1h"));
    assertEquals(1000 * 60 * 61, DurationHumanizer.parseInMillis("1h1m"));

    assertEquals(1000 * 60, DurationHumanizer.parseInMillis("00:01"));
    assertEquals(1000 * 60 * 60, DurationHumanizer.parseInMillis("01:00"));
    assertEquals(1000 * 60 * 61, DurationHumanizer.parseInMillis("01:01"));

    assertEquals(1000 * 60, DurationHumanizer.parseInMillis("1"));
    assertEquals(1000 * 60 * 60, DurationHumanizer.parseInMillis("60"));
    assertEquals(1000 * 60 * 61, DurationHumanizer.parseInMillis("61"));
  }

  @Test
  public void format() throws Exception {
    assertEquals("1m", DurationHumanizer.formatMillis(1000 * 60));
    assertEquals("1h", DurationHumanizer.formatMillis(1000 * 60 * 60));
    assertEquals("1h1m", DurationHumanizer.formatMillis(1000 * 60 * 61));
  }
}
