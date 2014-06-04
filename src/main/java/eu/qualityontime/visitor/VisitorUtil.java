package eu.qualityontime.visitor;

import java.lang.reflect.Method;

public class VisitorUtil {
  public static Method getMethod(Object visitor, Object o) {
    Class<?> visitorClass = visitor.getClass();
    if (null == o) {
      return null;
    }
    Class<?> newc = o.getClass();

    Method m = null;
    // Try the superclasses
    while (m == null && newc != Object.class) {
      String method = newc.getSimpleName();
      method = "visit" + method;
      try {
        m = visitorClass.getMethod(method, new Class[] { newc });
      } catch (NoSuchMethodException e) {
        newc = newc.getSuperclass();
      }
    }
    // Try the interfaces. If necessary, you
    // can sort them first to define 'visitable' interface wins
    // in case an object implements more than one.
    if (newc == Object.class) {
      Class<?>[] interfaces = o.getClass().getInterfaces();
      for (int i = 0; i < interfaces.length; i++) {
        String method = interfaces[i].getName();
        method = "visit" + method.substring(method.lastIndexOf('.') + 1);
        try {
          m = visitorClass.getMethod(method, new Class[] { interfaces[i] });
        } catch (NoSuchMethodException e) {
        }
      }
    }
    if (m == null) {
      try {
        m = visitorClass.getMethod("visitObject", new Class[] { Object.class });
      } catch (Exception e) {
        // Can't happen
      }
    }
    return m;
  }
}
