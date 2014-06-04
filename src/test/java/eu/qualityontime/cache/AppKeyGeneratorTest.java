package eu.qualityontime.cache;

import org.junit.*;
import org.springframework.beans.factory.annotation.Autowired;

import eu.qualityontime.ASpringToolboxTest;

public class AppKeyGeneratorTest extends ASpringToolboxTest {
  @Autowired
  private CacheTestHelper testClass;

  @Test
  public void testOneParam() {
    Assert.assertEquals("P1", testClass.cachedMethodOneParam("P1"));
    Assert.assertEquals("P2", testClass.cachedMethodOneParam("P2"));
  }

  @Test
  public void testTwoParam() {
    Assert.assertEquals("P1P2", testClass.cachedMethodTwoParams("P1", "P2"));
    Assert.assertEquals("P3P4", testClass.cachedMethodTwoParams("P3", "P4"));
  }

  @Test
  public void testTwoParamOneKey() {
    Assert.assertEquals("P1", testClass.cachedMethodTwoParamsOneKey("P1", "P2"));
    Assert.assertEquals("P1", testClass.cachedMethodTwoParamsOneKey("P1", "P3"));
    Assert.assertEquals("P3", testClass.cachedMethodTwoParamsOneKey("P3", "P4"));
  }

  @Test
  public void testNoParam() {
    Assert.assertEquals("cachedMethodNoParam", testClass.cachedMethodNoParam());
    Assert.assertEquals("cachedMethodNoParam2", testClass.cachedMethodNoParam2());
  }
}
