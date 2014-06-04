package eu.qualityontime;

import java.util.Collection;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Iterables;

import eu.qualityontime.web.GsonHelper;

public class AppToString {
  /**
   * Same as ToStringStyle.SHORT_PREFIX_STYLE but ignoring null values.
   */
  public static final ToStringStyle IGNORE_NULL_SHORT_PREFIX_STYLE = new IgnorNullShortPrefixToStringStyle();
  public static final ToStringStyle IGNORE_NULL_MULTILINE_STYLE = new IgnorNullMultilineToStringStyle();

  private static class AIgnoreNullToStringStyle extends ToStringStyle {
    @Override
    public void append(StringBuffer buffer, String fieldName, Object value, Boolean fullDetail) {
      if (null != value) {
        super.append(buffer, fieldName, value, fullDetail);
      }
    }
  }

  private static final class IgnorNullShortPrefixToStringStyle extends AIgnoreNullToStringStyle {
    IgnorNullShortPrefixToStringStyle() {
      super();
      this.setUseShortClassName(true);
      this.setUseIdentityHashCode(false);
    }

    /**
     * <p>
     * Ensure <code>Singleton</ode> after serialization.
     * </p>
     * 
     * @return the singleton
     */
    private Object readResolve() {
      return AppToString.IGNORE_NULL_SHORT_PREFIX_STYLE;
    }
  }

  private static final class IgnorNullMultilineToStringStyle extends AIgnoreNullToStringStyle {
    IgnorNullMultilineToStringStyle() {
      super();
      this.setUseShortClassName(true);
      this.setContentStart("[");
      this.setFieldSeparator(SystemUtils.LINE_SEPARATOR + "  ");
      this.setFieldSeparatorAtStart(true);
      this.setContentEnd(SystemUtils.LINE_SEPARATOR + "]");
    }

    /**
     * <p>
     * Ensure <code>Singleton</ode> after serialization.
     * </p>
     * 
     * @return the singleton
     */
    private Object readResolve() {
      return AppToString.IGNORE_NULL_MULTILINE_STYLE;
    }
  }

  public static class BeanFieldValueOfCollectionFormatter {
    private final Collection<?> col;
    private final String attribute;

    public BeanFieldValueOfCollectionFormatter(Collection<?> col, String attribute) {
      this.col = col;
      this.attribute = attribute;
    }

    @Override
    public String toString() {
      return Iterables.transform(col, BeanFieldValueExtractor.byField(attribute)).toString();
    }

  }

  public static Object collectionAttr(Collection<?> col, String attribute) {
    return new BeanFieldValueOfCollectionFormatter(col, attribute);
  }

  public static String jsonToString(Object o, Class<?>... classesExcluding) {
    return o.getClass().getSimpleName() + GsonHelper.prettyPrint(o, classesExcluding);
  }
}
