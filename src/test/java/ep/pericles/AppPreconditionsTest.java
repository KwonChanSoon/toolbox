package ep.pericles;

import org.junit.Test;

public class AppPreconditionsTest {

  @Test
  public void test_checkValueSet() {
    AppPreconditions.checkValueSet("A", AppCollections.List("A", "B"));
    AppPreconditions.checkValueSet("B", AppCollections.List("A", "B"));
  }

  @Test(expected = RuntimeException.class)
  public void test_checkValueSet_err() {
    AppPreconditions.checkValueSet("C", AppCollections.List("A", "B"));
  }

}
