package ep.pericles.visitor;

import java.lang.reflect.*;
import java.util.HashMap;

public abstract class ReflectiveVisitor implements Visitor {
  public void defaultVisit(Object o) {
    throw new RuntimeException("" + o);
  }

  private HashMap<Class<?>, Method> _cache_method = new HashMap<Class<?>, Method>();

  @Override
  public void visit(Object o) {
    Class<?> klass = null == o ? null : o.getClass();
    Method m = _cache_method.get(klass);
    if (null == m) {
      m = VisitorUtil.getMethod(this, o);
      if (null == m) {
        throw new RuntimeException("No appropriate method declaration for " + (null == o ? "null" : o.getClass()));
      }
      if (null != klass) {
        _cache_method.put(klass, m);
      }
    }
    try {
      m.setAccessible(true);
      m.invoke(this, new Object[] { o });
      //m.setAccessible(false);
    } catch (IllegalArgumentException e) {
      defaultVisit(o);
    } catch (IllegalAccessException e) {
      defaultVisit(o);
    } catch (InvocationTargetException e) {
      defaultVisit(o);
    }

    if (o instanceof Visitable) {
      callAccept((Visitable) o);
    }
  }

  public void callAccept(Visitable visitable) {
    // visitable.accept(this);
  }
}
