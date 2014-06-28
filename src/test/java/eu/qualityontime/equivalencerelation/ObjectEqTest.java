package eu.qualityontime.equivalencerelation;

import static org.junit.Assert.*;

import org.junit.*;

public class ObjectEqTest extends BaseEq {

  private void eq() {
    assertTrue(relation.areEquals(a1, a2));
  }

  private void neq() {
    assertFalse(relation.areEquals(a1, a2));
  }

  A a1 = new A();
  A a2 = new A();

  @Test
  public void emptyBean() throws Exception {
    eq();
  }

  @Test
  public void simple_attr_eq() throws Exception {
    a1.i = 1;
    a2.i = 1;
    eq();
  }

  @Test
  public void simple_attr_neq() throws Exception {
    a1.i = 1;
    a2.i = 2;
    neq();
  }

  @Test
  public void child_existence_diff() throws Exception {
    a1.b = null;
    a2.b = new B();
    neq();
  }

  @Test
  public void empty_childsould_be_same() throws Exception {
    a1.b = new B();
    a2.b = new B();
    eq();
  }

  @Test
  public void non_empty_child_with_same_content_should_be_the_same() throws Exception {
    a1.b = new B("a");
    a2.b = new B("a");
    eq();
  }

  @Test
  public void non_empty_child_with_diff_content_should_be_the_same() throws Exception {
    a1.b = new B("a");
    a2.b = new B("b");
    neq();
  }

  @Test
  public void infinite_data_structures() throws Exception {
    //a -> b -> ... -> a
    a1.b = new B();
    a2.b = new B();
    a1.b.a = a1;
    a2.b.a = a2;
    eq();
  }

}
