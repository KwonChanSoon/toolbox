package ep.pericles.visitor;

import java.lang.reflect.*;
import java.util.HashMap;

import com.google.common.base.Throwables;

public abstract class ReflectiveFunctionVisitor<T> implements FunctionVisitor<T> {
  public T defaultVisit(Object o) {
    return null;
  }

  public T visitNull(Object o) {
    throw new RuntimeException("No appropriate method declaration for " + (null == o ? "null" : o.getClass()));
  }

  private HashMap<Class<?>, Method> _cache_method = new HashMap<Class<?>, Method>();

  @Override
  public T visit(Object o) {
    Class<?> klass = null == o ? null : o.getClass();
    Method m = _cache_method.get(klass);
    if (null == m) {
      m = VisitorUtil.getMethod(this, o);
      if (null == m) {
        return visitNull(o);
      }
      if (null != klass) {
        _cache_method.put(klass, m);
      }
    }
    try {
      m.setAccessible(true);
      return (T) m.invoke(this, new Object[] { o });
    } catch (IllegalArgumentException e) {
      throw Throwables.propagate(e);
    } catch (IllegalAccessException e) {
      throw Throwables.propagate(e);
    } catch (InvocationTargetException e) {
      throw Throwables.propagate(e.getTargetException());
    }
  }
}
