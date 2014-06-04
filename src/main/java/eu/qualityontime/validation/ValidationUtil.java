package eu.qualityontime.validation;

import static eu.qualityontime.commons.JakartaPropertyUtils.getProperty;
import static org.apache.commons.lang3.StringUtils.*;

import java.util.List;

public class ValidationUtil {
  public static void checkMaxlength(List<String> errors, Object request, String field, int maxlength) {
    if (maxlength < defaultIfBlank(getProperty(request, field, String.class), "").length()) {
      errors.add(field + ".maxlength");
    }
  }

  public static void checkEmpty(List<String> errors, Object request, String field) {
    if (isBlank(getProperty(request, field, String.class))) {
      errors.add(field + ".empty");
    }
  }

}
