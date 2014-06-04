package ep.pericles.jdbc.debug;

import java.sql.*;

public class SqlFormatter {

  protected String format(java.sql.Blob blob) throws SQLException {
    return "'<Blob length = " + blob.length() + ">'";
  }

  protected String format(java.sql.Clob clob) throws SQLException {
    return "'<Clob length = " + clob.length() + ">'";
  }

  protected String format(java.sql.Array array) throws SQLException {
    return array.getBaseTypeName();
  }

  protected String format(java.sql.Ref ref) throws SQLException {
    return ref.getBaseTypeName();
  }

  protected String format(java.lang.String string) throws SQLException {
    if (string.equals("NULL"))
      return string;
    else
      return "'" + string + "'";
  }

  public String format(Object o) throws SQLException {
    if (o == null)
      return "NULL";
    if (o instanceof Blob)
      return format((Blob) o);
    if (o instanceof Clob)
      return format((Clob) o);
    if (o instanceof Array)
      return format((Array) o);
    if (o instanceof Ref)
      return format((Ref) o);
    if (o instanceof String)
      return format((String) o);
    return o.toString();
  }
}
