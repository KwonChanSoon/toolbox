package ep.pericles;

import java.math.BigDecimal;

import com.google.common.base.Function;

import ep.pericles.commons.JakartaPropertyUtils;

public class AppFunction {
  /**
   * Function to access bean field.
   */
  public static <T> BeanFieldValueExtractor<T> byField(String fieldName) {
    return new BeanFieldValueExtractor<T>(fieldName);
  }

  /**
   * Function to access bean field.
   */
  public static <T> Function<? extends Object, T> byField(String fieldName, Class<T> claz) {
    return new BeanFieldValueExtractor<T>(fieldName);
  }

  /**
   * Function to access bean property.
   */
  public static <I, T> Function<I, T> byProp(String prop) {
    return AppObjects.cast(new BeanPropertyExtractor<I, T>(prop));
  }

  /**
   * Function to access bean property.
   */
  public static <I, T> Function<I, T> byProp(String prop, Class<T> claz) {
    return new BeanPropertyExtractor<I, T>(prop);
  }

  public static class BeanPropertyExtractor<I, T> implements Function<I, T> {
    private final String prop;

    public BeanPropertyExtractor(String prop) {
      super();
      this.prop = prop;
    }

    @Override
    public T apply(Object input) {
      return AppObjects.cast(JakartaPropertyUtils.getProperty(input, prop));
    }
  }

  public static Function<BigDecimal, Long> bd2Long() {
    return BD2LONG;
  }

  private static Bd2LongFun BD2LONG = new Bd2LongFun();

  private static class Bd2LongFun implements Function<BigDecimal, Long> {
    @Override
    public Long apply(BigDecimal input) {
      return input.longValue();
    }
  }

  public static Function<String, String> wrap(final String around) {
    return wrap(around, around);
  }

  public static Function<String, String> wrap(final String before, final String after) {
    return new Function<String, String>() {
      public String apply(String input) {
        return before + input + after;
      }

    };
  }

  private static final Function<String, String> to_upper_function = new Function<String, String>() {
    public String apply(String input) {
      return input.toUpperCase();
    }

  };

  public static Function<String, String> toUpper() {
    return to_upper_function;
  }
}
