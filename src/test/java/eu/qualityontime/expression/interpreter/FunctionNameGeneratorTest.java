package eu.qualityontime.expression.interpreter;

import static org.junit.Assert.assertEquals;

import org.junit.*;

public class FunctionNameGeneratorTest {
  FunctionNameGenerator g;

  @Before
  public void setUp() {
    g = new FunctionNameGenerator("abc_", "myfn", 3);
  }

  @Test
  public void test() {
    assertEquals("abc_myfn3_0", g.get());
    assertEquals("abc_myfn3_1", g.get());
    assertEquals("abc_myfn3_2", g.get());
  }

}
