package eu.qualityontime.diff;

import static org.junit.Assert.*;

import org.junit.Test;

public class PathMatcherTest {
  private PathMatcher matcher = new PathMatcher();

  @Test
  public void testName() throws Exception {
    assertTrue(matcher.match("a", "a"));
    assertFalse(matcher.match("a", "b"));
    assertTrue(matcher.match("a.b", "a.b"));
    assertTrue(matcher.match("a.b", "a[1].b"));
    assertTrue(matcher.match("a.b", "a<ADDED>.b"));
    assertTrue(matcher.match("a.b", "a<REMOVED>.b"));
    assertTrue(matcher.match("a.b", "a<UNTOUCHED>.b"));
    assertTrue(matcher.match("a.b.c.d", "a[1].b<ADDED>.c<REMOVED>.d"));
    assertTrue(matcher.match("a.b.c.d", "a{i=1}.b[1].c{b=asda}.d"));
  }
}
