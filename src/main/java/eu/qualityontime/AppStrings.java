package eu.qualityontime;

import java.text.MessageFormat;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.helpers.MessageFormatter;


public class AppStrings {


  /**
   * commons lang StrSubstitutor
   */
  @SuppressWarnings("unchecked")
  public static <K, V> String replace(String src, Map<K, V> m) {
    return StrSubstitutor.replace(src, (Map<String, V>) m);
  }


  public static String chop(String src, int to, String append) {
    return StringUtils.substring(src, 0, to) + (src.length() > to ? append : "");
  }

  /**
   * String placeholder is just indexed. 'asdasd{}a asdas{}'
   */
  public static String replace_simple(String src, Object... objects) {
    return MessageFormatter.arrayFormat(src, objects);
  }

  public static String rs(String src, Object... objects) {
    return replace_simple(src, objects);
  }

  public static String messageFormatFormatter(String message, Object... params) {
    return MessageFormat.format(message, params);
  }

  public static String randomText(int length) {
    String ret = "";
    for (int i = 0; i < length; i++) {
      ret += "a";
    }
    return ret;
  }

}
