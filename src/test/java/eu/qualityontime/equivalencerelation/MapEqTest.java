package eu.qualityontime.equivalencerelation;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class MapEqTest extends BaseEq {
  Map<?, ?> a, b;

  private void eq() {
    assertTrue(relation.areEquals(a, b));
  }

  private void neq() {
    assertFalse(relation.areEquals(a, b));
  }

  @Test
  public void null_should_eq() throws Exception {
    eq();
  }

  @Test
  public void empty_should_eq() throws Exception {
    a = ImmutableMap.of();
    b = ImmutableMap.of();
    eq();
  }

  @Test
  public void diff_size_should_eq() throws Exception {
    a = ImmutableMap.of(1, 1, 2, 2, 3, 3);
    b = ImmutableMap.of(1, 1, 2, 2);
    neq();
  }

  @Test
  public void same_should_eq() throws Exception {
    a = ImmutableMap.of(1, 1, 2, 2, 3, 3);
    b = ImmutableMap.of(1, 1, 2, 2, 3, 3);
    eq();
  }

  @Test
  public void diff_value_should_neq() throws Exception {
    a = ImmutableMap.of(1, 1, 2, 22, 3, 3);
    b = ImmutableMap.of(1, 1, 2, 2, 3, 3);
    neq();
  }

  @Test
  public void diff_key_should_neq() throws Exception {
    a = ImmutableMap.of(1, 1, 22, 2, 3, 3);
    b = ImmutableMap.of(1, 1, 2, 2, 3, 3);
    neq();
  }

  @Test
  public void diff_type_should_neq() throws Exception {
    a = ImmutableMap.of(1, 1, 2, 2, 3, 3);
    b = ImmutableMap.of("1", 1, "2", 2, "3", 3);
    neq();
    a = ImmutableMap.of(1, 1, 22, 2, 3, 3);
    b = ImmutableMap.of(1, "1", 2, "2", 3, "3");
    neq();

  }

}
