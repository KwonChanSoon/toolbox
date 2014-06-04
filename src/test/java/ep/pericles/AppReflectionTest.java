package ep.pericles;

import static com.google.common.base.Predicates.or;
import static ep.pericles.AppCollections.*;
import static ep.pericles.AppReflection.PRIMITIVE_AND_WRAPPERS;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Predicate;

public class AppReflectionTest {

  @SuppressWarnings("unused")
  private static class A {
    int x;

    public int getX() {
      return x;
    }

    public void setX(int x) {
      this.x = x;
    }
  }

  @SuppressWarnings("unused")
  private static class B {
    int y;
    String s;
    A a;

    public int getY() {
      return y;
    }

    public void setY(int y) {
      this.y = y;
    }

    public A getA() {
      return a;
    }

    public void setA(A a) {
      this.a = a;
    }

    public String getS() {
      return s;
    }

    public void setS(String s) {
      this.s = s;
    }

  }

  @Test
  public void getPropertyNames() {
    assertEquals(List("x"), sort(AppReflection.getPropertyNames(new A(), null)));
    assertEquals(List("a", "s", "y"), sort(AppReflection.getPropertyNames(new B(), null)));
    Predicate<Class<?>> p = PRIMITIVE_AND_WRAPPERS;
    assertEquals(List("y"), sort(AppReflection.getPropertyNames(new B(), p)));
    p = or(PRIMITIVE_AND_WRAPPERS, AppReflection.CLASSES_OF(String.class));
    assertEquals(List("s", "y"), sort(AppReflection.getPropertyNames(new B(), p)));
  }

}
