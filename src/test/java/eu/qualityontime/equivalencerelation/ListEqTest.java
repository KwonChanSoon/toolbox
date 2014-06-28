package eu.qualityontime.equivalencerelation;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class ListEqTest extends BaseEq {
  List<?> a1, a2;

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
    a1 = new ArrayList<Object>();
    a2 = new ArrayList<Object>();
    eq();
  }

  @Test
  public void diff_length_should_neq() throws Exception {
    a1 = ImmutableList.of(1);
    a2 = ImmutableList.of(1, 2);
    neq();
  }

  @Test
  public void same_length_empty_should_be_eq() throws Exception {
    a1 = ImmutableList.of();
    a2 = ImmutableList.of();
    eq();
  }

  @Test
  public void content_same_should_eq() throws Exception {
    a1 = ImmutableList.of(1, 2, 3);
    a2 = ImmutableList.of(1, 2, 3);
    eq();
  }

  @Test
  public void content_diff_should_neq() throws Exception {
    a1 = ImmutableList.of(1, 2, 3);
    a2 = ImmutableList.of(2, 3, 1);
    neq();
  }
  
  @Test
  public void diff_type_should_neq() throws Exception {
    a1 = ImmutableList.of(1);
    a2 = ImmutableList.of("1");
    neq();
  }
}
