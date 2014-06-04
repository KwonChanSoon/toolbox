package ep.pericles.tuple;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PairsTest {

  @Test
  public void test_pariExtractor() {
    Pair<Integer, String> t = Pair.of(1, "egy");
    assertEquals(Integer.valueOf(1), new Pairs.LeftExtractor<Integer>().apply(t));
    assertEquals("egy", new Pairs.RightExtractor<String>().apply(t));
  }

}
