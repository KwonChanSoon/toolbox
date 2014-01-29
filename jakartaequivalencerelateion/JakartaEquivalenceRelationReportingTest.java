package ec.ep.pericles.service.services.impl.comparator;

import static ep.pericles.commons.JakartaPropertyUtils.set;

import org.junit.*;

import com.google.common.base.Joiner;
import com.google.common.collect.*;

@Ignore("for simplification verification is made by human")
public class JakartaEquivalenceRelationReportingTest extends BaseEq {

  private void p() {
    relation.areEquals(a, b);
    String s = Joiner.on("\n").join(relation.getMessages());
    System.out.println(s);
  }

  Object a = new A();
  Object b = new A();

  @Test
  public void baseReporting() throws Exception {
    p();
  }

  @Test
  public void simple_attr_neq() throws Exception {
    set(a, "i", 1);
    set(b, "i", 2);
    p();
  }

  @Test
  public void child_existence_diff() throws Exception {
    set(a, "b", null);
    set(b, "b", new B());
    p();
  }

  @Test
  public void non_empty_child_with_diff_content_should_be_the_same() throws Exception {
    set(a, "b", new B("a"));
    set(b, "b", new B("b"));
    p();
  }

  @Test
  public void diff_length_should_neq() throws Exception {
    a = new Integer[1];
    b = new Integer[2];
    p();
  }

  @Test
  public void content_same_should_eq() throws Exception {
    a = new Integer[] { 1, 2, 3 };
    b = new Integer[] { 1, 2, 3 };
    p();
  }

  @Test
  public void content_diff_should_neq() throws Exception {
    a = new Integer[] { 0, 1, 2, 3 };
    b = new Integer[] { 0, 2, 3, 1 };
    p();
  }

  @Test
  public void list_diff_length_should_neq() throws Exception {
    a = ImmutableList.of(1);
    b = ImmutableList.of(1, 2);
    p();
  }

  @Test
  public void list_content_diff_should_neq() throws Exception {
    a = ImmutableList.of(0, 1, 2, 3);
    b = ImmutableList.of(0, 2, 3, 1);
    p();
  }

  @Test
  public void list_diff_type_should_neq() throws Exception {
    a = ImmutableList.of(1);
    b = ImmutableList.of("1");
    p();
  }

  @Test
  public void map_diff_size_should_eq() throws Exception {
    a = ImmutableMap.of(1, 1, 2, 2, 3, 3);
    b = ImmutableMap.of(1, 1, 2, 2);
    p();
  }

  @Test
  public void map_same_should_eq() throws Exception {
    a = ImmutableMap.of(1, 1, 2, 2, 3, 3);
    b = ImmutableMap.of(1, 1, 2, 2, 3, 3);
    p();
  }

  @Test
  public void map_diff_value_should_neq() throws Exception {
    a = ImmutableMap.of(1, 1, 2, 22, 3, 3);
    b = ImmutableMap.of(1, 1, 2, 2, 3, 3);
    p();
  }

  @Test
  public void map_diff_key_should_neq() throws Exception {
    a = ImmutableMap.of(1, 1, 22, 2, 3, 3);
    b = ImmutableMap.of(1, 1, 2, 2, 3, 3);
    p();
  }

  @Test
  public void map_diff_type_should_neq1() throws Exception {
    a = ImmutableMap.of(1, 1, 2, 2, 3, 3);
    b = ImmutableMap.of("1", 1, "2", 2, "3", 3);
    p();
  }

  public void map_diff_type_should_neq2() throws Exception {
    a = ImmutableMap.of(1, 1, 22, 2, 3, 3);
    b = ImmutableMap.of(1, "1", 2, "2", 3, "3");
    p();

  }
}
