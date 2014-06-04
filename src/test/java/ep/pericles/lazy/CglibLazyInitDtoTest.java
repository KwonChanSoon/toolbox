package ep.pericles.lazy;

import static org.junit.Assert.assertNotNull;

import java.util.*;

import net.sf.cglib.proxy.*;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import ep.pericles.AppObjects;

/**
 * Learning test to find proper solution of lazy init DTO attributes;
 * 
 * @author otakacs
 * 
 */
public class CglibLazyInitDtoTest {
  SomeoneToCall toCall = new SomeoneToCall();

  @Test
  public void test() {
    List<String> result = AppObjects.cast(Enhancer.create(List.class, new LazyLoader() {
      public Object loadObject() throws Exception {
        return toCall.someCall();
      }
    }));
    assertNotNull(result);
    //    System.out.println(result.getClass());
    //    System.out.println(result.toString());
  }

  //  class MyDto {
  //    public String getLazyProperty() {
  //      return "hmmm";
  //    }
  //  }

  class SomeoneToCall {
    public Collection<String> someCall() {
      return ImmutableList.of("Sample");
    }
  }
}
