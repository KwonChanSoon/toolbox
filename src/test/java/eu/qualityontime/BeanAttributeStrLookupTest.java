package eu.qualityontime;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.junit.Test;

public class BeanAttributeStrLookupTest {

  @Test
  public void test() {
    BeanAttributeSample sample = new BeanAttributeSample();
    sample.a = "Otto";
    sample.b = "Takacs";
    StrSubstitutor subs = new StrSubstitutor(new BeanAttributeStrLookup(sample));
    String actual = subs.replace("Sample text ${a} and ${b}");
    assertEquals("Sample text Otto and Takacs", actual);
  }

  public static class BeanAttributeSample {
    String a;
    String b;

    public String getA() {
      return a;
    }

    public void setA(String a) {
      this.a = a;
    }

    public String getB() {
      return b;
    }

    public void setB(String b) {
      this.b = b;
    }
  }
}
