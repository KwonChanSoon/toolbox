package eu.qualityontime.expression.interpreter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class CompositeVariableContextTest {

  @Test
  public void test() {
    MapVariableContext c1 = new MapVariableContext(ImmutableMap.of("f3", "Otto", "f4", 33));
    BeanVariableContext c2 = new BeanVariableContext(new A());
    CompositeVariableContext c = new CompositeVariableContext(c1, c2);
    assertEquals("Otto", c.get("f1"));
    assertEquals(Integer.valueOf(33), c.get("f2"));
    assertEquals("Otto", c.get("f3"));
    assertEquals(Integer.valueOf(33), c.get("f4"));
  }

  static class A {
    String f1 = "Otto";
    Integer f2 = 33;
  }

}
