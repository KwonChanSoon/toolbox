package ep.pericles;

import org.apache.commons.lang3.StringUtils;

public class AppSql {

  public static String like(String in) {
    if (!StringUtils.startsWith(in, "%")) {
      in = "%" + in;
    }
    if (!StringUtils.endsWith(in, "%")) {
      in = in + "%";
    }
    return in;
  }
}
