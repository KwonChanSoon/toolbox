package eu.qualityontime.lazy;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;

/**
 * Learning test to find proper solution of lazy init DTO attributes;
 * 
 * @author otakacs
 * 
 */
public class LazyInitDtoTest {
  @Test
  public void test() {
    assertEquals("Sample", new MyDto().getLazyProperty());
  }

  class MyDto {
    public String getLazyProperty() {
      return new LazyInitializationProperty<String>(new SomeoneToCall(), "someCall").getProperty();
    }
  }

  class SomeoneToCall {
    public String someCall() {
      return "Sample";
    }
  }

  public interface InterceptableProperty<T> {
    public T getProperty();

    public void setProperty(T property);
  }

  @SuppressWarnings("rawtypes")
  public class LazyInitializationProperty<T> implements InterceptableProperty<T> {
    private volatile T property = null;
    private volatile boolean initialized = false;
    private final Object lock = new Object();
    private final Object caller;
    private final Method method;
    private final Object[] args;

    public LazyInitializationProperty(Object caller, String method) {
      this(caller, method, new Class[] {}, new Object[] {});
    }

    public LazyInitializationProperty(Object caller, String method, Class[] argsClass, Object... args) {
      this.caller = caller;
      this.args = args;
      try {
        this.method = caller.getClass().getDeclaredMethod(method, argsClass);
      } catch (Exception e) {
        StringBuilder argsClassString = new StringBuilder("(");
        for (Class argsClazz : argsClass) {
          argsClassString.append(argsClazz.getName());
          argsClassString.append(",");
        }
        argsClassString.insert(argsClassString.length() - 1, ")");
        throw new IllegalStateException("The method with name: " + method + " with arguments: " + argsClassString
            + " does not exist for class: "
            + caller.getClass().getName(), e);
      }
    }

    public T getProperty() {
      if (property == null) {
        synchronized (lock) {
          if (property == null && initialized == false) {
            try {
              property = (T) method.invoke(caller, args);
              initialized = true;
            } catch (Exception e) {
              throw new IllegalStateException("The property could not be set: " + e.getMessage());
            }
          }
        }
      }
      return property;
    }

    public void setProperty(T property) {
      synchronized (lock) {
        this.property = property;
        initialized = true;
      }
    }
  }
}
