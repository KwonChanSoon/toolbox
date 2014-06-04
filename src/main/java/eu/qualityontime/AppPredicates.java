package eu.qualityontime;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.*;

public class AppPredicates {

  public static <T> Predicate<T> fieldEq(String field, Object value) {
    return new FieldEqualPredicate<T>(field, value);
  }

  public static <T> Predicate<T> fieldEqIgnoreCase(String field, String value) {
    return new FieldEqualIgnoreCasePredicate<T>(field, value);
  }

  public static <T> Predicate<T> propEq(String field, Object value) {
    return new PropEqualPredicate<T>(field, value);
  }

  public static <T> Predicate<T> propEqIgnoreCase(String field, String value) {
    return new PropEqualPredicateIgnoreCase<T>(field, value);
  }

  public static <T> Predicate<T> expressionEq(String fieldExp, String value) {
    return new FieldEqualIgnoreCasePredicate<T>(fieldExp, value);
  }

  public static class FieldEqualPredicate<T> implements Predicate<T> {
    protected final String field;
    protected final Object expectedValue;

    public FieldEqualPredicate(String field, Object expectedValue) {
      Preconditions.checkNotNull(field);
      Preconditions.checkNotNull(expectedValue);
      this.field = field;
      this.expectedValue = expectedValue;
    }

    @Override
    public boolean apply(T input) {
      return expectedValue.equals(AppReflection.getField(input, field));
    }
  }

  public static class FieldEqualIgnoreCasePredicate<T> extends FieldEqualPredicate<T> {
    public FieldEqualIgnoreCasePredicate(String field, String expectedValue) {
      super(field, expectedValue);
    }

    @Override
    public boolean apply(T input) {
      if (input == null) {
        return false;
      }
      return StringUtils.equalsIgnoreCase((String) expectedValue, (String) AppReflection.getField(input, field));
    }
  }

  public static class PropEqualPredicate<T> implements Predicate<T> {
    protected final String field;
    protected final Object expectedValue;

    public PropEqualPredicate(String field, Object expectedValue) {
      Preconditions.checkNotNull(field);
      Preconditions.checkNotNull(expectedValue);
      this.field = field;
      this.expectedValue = expectedValue;
    }

    @Override
    public boolean apply(T input) {
      return expectedValue.equals(AppReflection.getProp(input, field));
    }
  }

  public static class PropEqualPredicateIgnoreCase<T> extends PropEqualPredicate<T> {
    public PropEqualPredicateIgnoreCase(String field, Object expectedValue) {
      super(field, expectedValue);
    }

    @Override
    public boolean apply(T input) {
      return StringUtils.equalsIgnoreCase((String) expectedValue, (String) AppReflection.getProp(input, field));
    }
  }

  public static class FieldExpressionEqualPredicate<T> implements Predicate<T> {
    private final String field;
    private final Object expectedValue;

    public FieldExpressionEqualPredicate(String field, Object expectedValue) {
      Preconditions.checkNotNull(field);
      Preconditions.checkNotNull(expectedValue);
      this.field = field;
      this.expectedValue = expectedValue;
    }

    @Override
    public boolean apply(T input) {
      return expectedValue.equals(AppBean.get(input, field));
    }
  }

  private static final Predicate<String> is_blank_predicate = new Predicate<String>() {
    public boolean apply(String input) {
      return StringUtils.isBlank(input);
    }
  };

  /**
   * See org.apache.commons.lang3.StringUtils.isBlank(String str)
   */
  public static Predicate<String> isBlankPred() {
    return is_blank_predicate;
  }
}
