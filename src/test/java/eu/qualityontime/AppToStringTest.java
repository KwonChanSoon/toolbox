package eu.qualityontime;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.junit.Test;

public class AppToStringTest {

  @Test
  public void test() {
    A a = new A();
    String res = ToStringBuilder.reflectionToString(a, AppToString.IGNORE_NULL_SHORT_PREFIX_STYLE);
    //System.out.println(res);
    assertEquals("AppToStringTest.A[fruit=orange]", res);
  }

  @SuppressWarnings("unused")
  private class A {
    String fruit = "orange";
    String animal;

    public String getFruit() {
      return fruit;
    }

    public void setFruit(String fruit) {
      this.fruit = fruit;
    }

    public String getAnimal() {
      return animal;
    }

    public void setAnimal(String animal) {
      this.animal = animal;
    }
  }
}
