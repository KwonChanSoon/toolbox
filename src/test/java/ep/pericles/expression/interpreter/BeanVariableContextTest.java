package ep.pericles.expression.interpreter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BeanVariableContextTest {

  @Test
  public void test() {
    BeanVariableContext c = new BeanVariableContext(new A());
    assertEquals("Otto", c.get("f1"));
    assertEquals(Integer.valueOf(33), c.get("f2"));
  }

  static class A {
    String f1 = "Otto";
    Integer f2 = 33;
  }
}
