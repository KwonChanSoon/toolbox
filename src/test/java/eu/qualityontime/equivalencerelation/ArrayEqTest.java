package eu.qualityontime.equivalencerelation;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArrayEqTest extends BaseEq {
  Object[] a1, a2;

  private void eq() {
    assertTrue(relation.areEquals(a1, a2));
  }

  private void neq() {
    assertFalse(relation.areEquals(a1, a2));
  }

  @Test
  public void nulleq() throws Exception {
    eq();
  }

  @Test
  public void zero_length_should_be_eq() throws Exception {
    a1 = new Integer[0];
    a2 = new Integer[0];
    eq();
  }

  @Test
  public void diff_length_should_neq() throws Exception {
    a1 = new Integer[1];
    a2 = new Integer[2];
    neq();
  }

  @Test
  public void same_length_empty_should_be_eq() throws Exception {
    a1 = new Integer[1];
    a2 = new Integer[1];
    eq();
  }

  @Test
  public void content_same_should_eq() throws Exception {
    a1 = new Integer[] { 1, 2, 3 };
    a2 = new Integer[] { 1, 2, 3 };
    eq();
  }
  @Test
  public void content_diff_should_neq() throws Exception {
    a1 = new Integer[] { 1, 2, 3 };
    a2 = new Integer[] { 2, 3, 1 };
    neq();
  }
}
