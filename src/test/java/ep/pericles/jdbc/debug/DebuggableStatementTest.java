package ep.pericles.jdbc.debug;

import static ep.pericles.AppDate.parse;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.*;

import org.junit.*;
import org.slf4j.Logger;

public class DebuggableStatementTest {
  Logger log;
  Connection con;
  String sql;

  @Before
  public void setup() throws SQLException {
    log = mock(Logger.class);
    when(log.isDebugEnabled()).thenReturn(true);
    con = mock(Connection.class);
    sql = "sample sql ?,?,?,?,?,?";
    when(con.prepareStatement(sql)).thenReturn(mock(PreparedStatement.class));

  }

  @Test
  public void no_parameter() throws SQLException {
    DebuggableStatement s = (DebuggableStatement) StatementFactory.getStatement(con, sql, log);
    String expected = "sample sql ? (missing variable # 1 ) ,? (missing variable # 2 ) ,? (missing variable # 3 ) ,? (missing variable # 4 ) ,? (missing variable # 5 ) ,? (missing variable # 6 ) ";
    assertEquals(expected, s.toSql());
  }

  @Test
  public void parameterized() throws SQLException {
    DebuggableStatement s = (DebuggableStatement) StatementFactory.getStatement(con, sql, log);
    s.setInt(1, 1);
    s.setLong(2, 2L);
    s.setDate(3, new Date(parse("2013-09-02 12:13:14").getTime()));
    s.setTimestamp(4, new Timestamp(parse("2013-09-02 12:13:14").getTime()));
    s.setString(5, "sample");
    s.setBigDecimal(6, new BigDecimal(6));
    String expected = "sample sql 1,2,TO_DATE('2013-09-02 12:13:14.0','YYYY-MM-DD HH24:MI:SS.#'),TO_DATE('2013-09-02 12:13:14.0','YYYY-MM-DD HH24:MI:SS.#'),'sample',6";
    assertEquals(expected, s.toSql());
  }

  @Test
  public void not_logging_out() throws Exception {
    when(log.isDebugEnabled()).thenReturn(false);
    sql = "?";
    PreparedStatement s = StatementFactory.getStatement(con, sql);
    assertFalse("As nonon debuggin environment we must got back the original", s instanceof DebuggableStatement);
  }
}
