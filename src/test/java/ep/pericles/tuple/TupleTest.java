package ep.pericles.tuple;

import static org.junit.Assert.assertEquals;

import org.junit.*;

public class TupleTest {

  @Test
  public void get() {
    Tuple t = Tuple4.of(1, 2, 3, "Otto");
    assertEquals(4, t.size());
    assertEquals(2, t.get(1));
    Assert.assertEquals("Otto", t.get(3));
  }

}
