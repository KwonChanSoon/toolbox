package eu.qualityontime;

import static eu.qualityontime.AppObjects.cast;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.*;

import org.springframework.util.ReflectionUtils;

import com.google.common.base.*;

import eu.qualityontime.commons.JakartaPropertyUtils;

public class AppReflection {
  public static Object getField(Object target, String name) {
    Preconditions.checkNotNull(target, "Target object must not be null");
    Field field = ReflectionUtils.findField(target.getClass(), name);
    Preconditions.checkNotNull(field, "Could not find field [%s] on target [%s]", name, target);

    ReflectionUtils.makeAccessible(field);
    return ReflectionUtils.getField(field, target);
  }

  /**
   * Synonime of JakartaPropertyUtils.getProperty
   */
  public static <T> T getProp(Object target, String name) {
    return cast(JakartaPropertyUtils.getProperty(target, name));
  }

  public static <T> T getPropOrField(Object target, String name) {
    if (JakartaPropertyUtils.verifyPropertiesPresented(target, AppCollections.Set(name))) {
      return getProp(target, name);
    }
    else {
      return cast(getField(target, name));
    }
  }

  public static List<String> getPropertyNames(Object target, Predicate<Class<?>> includeClass) {
    PropertyDescriptor[] descs = JakartaPropertyUtils.getPropertyDescriptors(target);

    List<String> names = new ArrayList<String>();
    if (descs != null) {
      for (int i = 0; i < descs.length; i++) {
        String name = descs[i].getName();
        Class<?> cl = descs[i].getReadMethod().getReturnType();
        if ((includeClass == null || includeClass.apply(cl)) && !name.equals("class")) {
          names.add(name);
        }
      }
    }
    return names;
  }

  public static final Predicate<Class<?>> PRIMITIVE_AND_WRAPPERS = new Predicate<Class<?>>() {
    @Override
    public boolean apply(Class<?> input) {
      if (input.isPrimitive()) {
        return true;
      }
      return CLASSES_OF(Number.class, Boolean.class, Character.class).apply(input);
    }
  };

  public static final Predicate<Class<?>> ENUM = new Predicate<Class<?>>() {
    @Override
    public boolean apply(Class<?> input) {
      return input.isEnum();
    }
  };

  public static final Predicate<Class<?>> ARRAY = new Predicate<Class<?>>() {
    @Override
    public boolean apply(Class<?> input) {
      return input.isArray();
    }
  };

  public static Predicate<Class<?>> CLASSES_OF(final Class<?>... cls) {
    return new Predicate<Class<?>>() {
      @Override
      public boolean apply(Class<?> input) {
        for (Class<?> cl : cls) {
          if (cl.isAssignableFrom(input)) {
            return true;
          }
        }
        return false;
      }
    };
  }

}
