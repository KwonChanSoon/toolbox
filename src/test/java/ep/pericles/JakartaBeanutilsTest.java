package ep.pericles;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.apache.commons.beanutils.*;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import ep.pericles.commons.JakartaBeanutils;

@SuppressWarnings({ "rawtypes" })
public class JakartaBeanutilsTest {

  @Test
  public void m2o_simple() {
    Map m = ImmutableMap.of("apple", "apple");
    A a = new A();
    assertEquals(a.apple, "alma");
    JakartaBeanutils.copyProperties(a, m);
    assertEquals(a.apple, "apple");
  }

  @Test
  public void m2o_nested() throws Exception {
    ConvertUtilsBean cu = new ConvertUtilsBean();
    cu.register(new Converter() {
      @Override
      public Object convert(Class type, Object value) {
        B b = new B();
        JakartaBeanutils.copyProperties(b, value);
        return b;
      }
    }, B.class);
    BeanUtilsBean bu = new BeanUtilsBean(cu);
    Map m = ImmutableMap.of("apple", "apple", "b", ImmutableMap.of("fruit", "barack"));
    A a = new A();
    assertEquals(a.apple, "alma");
    assertEquals(a.b.fruit, "korte");
    bu.copyProperties(a, m);
    assertEquals(a.apple, "apple");
    assertEquals(a.b.fruit, "barack");
  }

  public static class A {
    String apple = "alma";
    B b = new B();

    public String getApple() {
      return apple;
    }

    public void setApple(String apple) {
      this.apple = apple;
    }

    public B getB() {
      return b;
    }

    public void setB(B b) {
      this.b = b;
    }
  }

  public static class B {
    String fruit = "korte";

    public String getFruit() {
      return fruit;
    }

    public void setFruit(String fruit) {
      this.fruit = fruit;
    }
  }

}
