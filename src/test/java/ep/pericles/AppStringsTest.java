package ep.pericles;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AppStringsTest {

  @Test
  public void test_messageFormatFormatter() {
    assertEquals("I am Otto Takacs", AppStrings.messageFormatFormatter("I am {1} {0}", "Takacs", "Otto"));
  }

}
