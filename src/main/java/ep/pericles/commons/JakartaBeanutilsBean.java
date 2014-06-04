package ep.pericles.commons;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtilsBean;

import com.google.common.base.Throwables;

public class JakartaBeanutilsBean {
  private BeanUtilsBean delege;

  public JakartaBeanutilsBean(BeanUtilsBean delege) {
    this.delege = delege;
  }

  public void copyProperties(Object dest, Object orig) {
    try {
      delege.copyProperties(dest, orig);
    } catch (IllegalAccessException e) {
      throw Throwables.propagate(e);
    } catch (InvocationTargetException e) {
      throw Throwables.propagate(e);
    }
  }

  public String getProperty(Object bean, String name) {
    try {
      return delege.getProperty(bean, name);
    } catch (IllegalAccessException e) {
      throw Throwables.propagate(e);
    } catch (InvocationTargetException e) {
      throw Throwables.propagate(e);
    } catch (NoSuchMethodException e) {
      throw Throwables.propagate(e);
    }
  }

  public void setProperty(Object bean, String name, Object value) {
    try {
      delege.setProperty(bean, name, value);
    } catch (IllegalAccessException e) {
      throw Throwables.propagate(e);
    } catch (InvocationTargetException e) {
      throw Throwables.propagate(e);
    }
  }
}
