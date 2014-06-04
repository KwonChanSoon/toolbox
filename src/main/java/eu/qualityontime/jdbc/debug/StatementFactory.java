package eu.qualityontime.jdbc.debug;

import java.sql.*;

import org.slf4j.*;

public class StatementFactory {
  private final static Logger log = LoggerFactory.getLogger("eu.qualityontime.jdbc.debug");
  /* Default debug level */
  //  private static DebugLevel defaultDebug = DebugLevel.OFF;

  /* Default sql formatter */
  private static SqlFormatter defaultFormatter = new OracleSqlFormatter();

  public static PreparedStatement getStatement(Connection con, String stmt,
      SqlFormatter formatter, Logger debug) throws SQLException {
    if (con == null)
      throw new SQLException("Connection passed to StatementFactory is null");
    if (debug.isDebugEnabled()) {
      return new DebuggableStatement(con, stmt, formatter, debug);
    }
    else {
      return con.prepareStatement(stmt);
    }
  }

  public static PreparedStatement getStatement(Connection con, String stmt, SqlFormatter formatter) throws SQLException {
    return StatementFactory.getStatement(con, stmt, formatter, log);

  }

  public static PreparedStatement getStatement(Connection con, String stmt, Logger log) throws SQLException {

    return StatementFactory.getStatement(con, stmt, defaultFormatter, log);

  }

  /**
   * this is the typical way to retrieve a statement. This method uses the static
   * formatter and debug level.
   * 
   * @param con
   *          Connection to jdbc data source.
   * @param stmt
   *          sql statement that will be executed.
   * @return PreparedStatement returns a DebuggableStatement if debug = ON or VERBOSE. Returns a standard
   *         PreparedStatement if debug = OFF.
   * @exception SQLException
   *              thrown if problem with connection.
   */
  public static PreparedStatement getStatement(Connection con, String stmt) throws SQLException {

    return StatementFactory.getStatement(con, stmt, defaultFormatter, log);
  }

}
