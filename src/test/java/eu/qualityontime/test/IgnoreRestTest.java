package eu.qualityontime.test;

import static org.junit.Assert.assertTrue;

import org.junit.*;

public class IgnoreRestTest {

  @Rule
  public IgnoreRestRule rule = new IgnoreRestRule();

  @Test
  public void test1() throws Exception {
    assertTrue(false);
  }

  @Test
  public void test2() throws Exception {
    assertTrue(false);
  }

  @Test
  public void test3() throws Exception {
    assertTrue(false);
  }

  @Test
  @IgnoreRest
  public void test4() throws Exception {
    assertTrue(true);
  }

  @Test
  @IgnoreRest
  public void test5() throws Exception {
    assertTrue(true);
  }

  @Test
  public void test6() throws Exception {
    assertTrue(false);
  }

}
