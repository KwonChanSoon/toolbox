package eu.qualityontime;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HumanizerTest {

  @Test
  public void test() {
    assertEquals("", Humanizer.of().apply(""));
    assertEquals("apple", Humanizer.of().apply("apple"));
    assertEquals("Template Example", Humanizer.of().apply("TemplateExample"));
    assertEquals("Template HUD Example", Humanizer.of().apply("TemplateHUDExample"));
    assertEquals("a b", Humanizer.of().apply("a_b"));
    assertEquals("alma and retek", Humanizer.of().apply("alma_and_retek"));
  }

}
