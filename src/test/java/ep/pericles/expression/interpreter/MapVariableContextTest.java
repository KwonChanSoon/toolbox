package ep.pericles.expression.interpreter;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class MapVariableContextTest {

  @Test
  public void test() {
    MapVariableContext c = new MapVariableContext(ImmutableMap.of("f1", "Otto", "f2", 33));
    assertEquals("Otto", c.get("f1"));
    assertEquals(Integer.valueOf(33), c.get("f2"));
  }
}
