package eu.qualityontime.commons;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.converters.*;

import com.google.common.base.Throwables;

public class JakartaBeanutils {
  private static BeanUtilsBean delege = BeanUtilsBean.getInstance();
  static {
    delege.getConvertUtils().register(new BigDecimalConverter(null), BigDecimal.class);
    delege.getConvertUtils().register(new DateConverter(null), Date.class);
  }

  public static String getProperty(Object bean, String name) {
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

  public static void setProperty(Object bean, String name, Object value) {
    try {
      delege.setProperty(bean, name, value);
    } catch (IllegalAccessException e) {
      throw Throwables.propagate(e);
    } catch (InvocationTargetException e) {
      throw Throwables.propagate(e);
    }
  }

  public static void copyProperties(Object dest, Object orig) {
    try {
      delege.copyProperties(dest, orig);
    } catch (IllegalAccessException e) {
      throw Throwables.propagate(e);
    } catch (InvocationTargetException e) {
      throw Throwables.propagate(e);
    }
  }

}
