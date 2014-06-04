package ep.pericles.jdbc.debug;

import java.io.*;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.sql.Array;
import java.sql.Date;
import java.util.*;

import org.slf4j.Logger;

import com.google.common.base.Function;
import com.google.common.collect.*;

public class DebuggableStatement implements PreparedStatement {
  private class DebugObject {
    private Object debugObject;
    private boolean valueAssigned;

    public DebugObject(Object debugObject) {
      this.debugObject = debugObject;
      valueAssigned = true;
    }

    public Object getDebugObject() {
      return debugObject;
    }

    public boolean isValueAssigned() {
      return valueAssigned;
    }
  }

  private PreparedStatement ps; //preparedStatement being proxied for.
  //  private String sql; //original statement going to database.
  private String filteredSql; //statement filtered for rogue '?' that are not bind variables.
  private DebugObject[] variables; //array of bind variables
  private SqlFormatter formatter; //format for dates
  private Logger log; //level of debug

  protected DebuggableStatement(Connection con, String sqlStatement, SqlFormatter formatter, Logger debugLevel) throws SQLException {
    //set values for member variables
    if (con == null)
      throw new SQLException("Connection object is null");
    this.ps = con.prepareStatement(sqlStatement);
    //    this.sql = sqlStatement;
    this.log = debugLevel;
    this.formatter = formatter;

    //see if there are any '?' in the statement that are not bind variables
    //and filter them out.
    boolean isString = false;
    char[] sqlString = sqlStatement.toCharArray();
    for (int i = 0; i < sqlString.length; i++) {
      if (sqlString[i] == '\'')
        isString = !isString;
      //substitute the ? with an unprintable character if the ? is in a
      //string.
      if (sqlString[i] == '?' && isString)
        sqlString[i] = '\u0007';
    }
    filteredSql = new String(sqlString);

    //find out how many variables are present in statement.
    int count = 0;
    int index = -1;
    while ((index = filteredSql.indexOf("?", index + 1)) != -1) {
      count++;
    }

    //show how many bind variables found
    if (debugLevel.isDebugEnabled())
      debugLevel.debug("count= " + count);

    //create array for bind variables
    variables = new DebugObject[count];

  }

  @Override
  public String toString() {
    return toSql();
  }

  public String toSql() {
    StringTokenizer st = new StringTokenizer(filteredSql, "?");
    int count = 1;
    StringBuffer statement = new StringBuffer();
    while (st.hasMoreTokens()) {
      statement.append(st.nextToken());
      if (count <= variables.length) {
        if (variables[count - 1] != null && variables[count - 1].isValueAssigned()) {
          try {
            statement.append(formatter.format(variables[count - 1].getDebugObject()));
          } catch (SQLException e) {
            statement.append("SQLException");
          }
        }
        else {
          statement.append("? " + "(missing variable # " + count + " ) ");
        }
      }
      count++;
    }
    //unfilter the string in case there where rogue '?' in query string.
    char[] unfilterSql = statement.toString().toCharArray();
    for (int i = 0; i < unfilterSql.length; i++) {
      if (unfilterSql[i] == '\u0007')
        unfilterSql[i] = '?';
    }
    return new String(unfilterSql);
  }

  private static Function<Object, Class<?>> classOfObject = new Function<Object, Class<?>>() {
    public Class<?> apply(Object in) {
      if (null == in) {
        return Object.class;
      }
      return in.getClass();
    }
  };

  private Object executeVerboseQuery(String methodName, Object... parameters) throws SQLException, NoSuchMethodException,
      InvocationTargetException, IllegalAccessException {
    Class<?>[] parameterTypes = null;
    if (null != parameters && 0 < parameters.length) {
      parameterTypes = (Class[]) Lists.transform(ImmutableList.of(parameters), classOfObject).toArray();
    }
    //determine which method we have
    Method m = ps.getClass().getDeclaredMethod(methodName, parameterTypes);

    //calculate execution time for verbose debugging
    Object returnObject = m.invoke(ps, parameters);

    //return the executions return type
    return returnObject;
  }

  private void saveObject(int parameterIndex, Object o) {
    if (parameterIndex > variables.length)
      throw new IllegalArgumentException("Parameter index of " + parameterIndex + " exceeds actual parameter count of "
          + variables.length);

    variables[parameterIndex - 1] = new DebugObject(o);
  }

  protected PreparedStatement getDelegate() {
    return ps;
  }

  public ResultSet executeQuery(String sql) throws SQLException {
    log.debug("{}", sql);
    ResultSet results = null;
    try {
      results = (ResultSet) executeVerboseQuery("executeQuery", sql);
    } catch (Exception e) {
      throw new SQLException("Could not execute sql command - Original message: " + e.getMessage(), e);
    }
    return results;
  }

  public <T> T unwrap(Class<T> iface) throws SQLException {
    return ps.unwrap(iface);
  }

  public ResultSet executeQuery() throws SQLException {
    log.debug("{}", this);
    ResultSet results = null;
    try {
      results = (ResultSet) executeVerboseQuery("executeQuery");
    } catch (Exception e) {
      throw new SQLException("Could not execute sql command - Original message: " + e.getMessage(), e);
    }
    return results;
  }

  public int executeUpdate(String sql) throws SQLException {
    log.debug("{}", sql);
    Integer results = null;
    try {
      results = (Integer) executeVerboseQuery("executeUpdate", sql);
    } catch (Exception e) {
      throw new SQLException("Could not execute sql command - Original message: " + e.getMessage());
    }
    return results.intValue();
  }

  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return ps.isWrapperFor(iface);
  }

  public int executeUpdate() throws SQLException {
    log.debug("{}", this);
    Integer results = null;
    try {
      results = (Integer) executeVerboseQuery("executeUpdate");
    } catch (Exception e) {
      throw new SQLException("Could not execute sql command - Original message: " + e.getMessage());
    }
    return results.intValue();
  }

  public void close() throws SQLException {
    ps.close();
  }

  public void setNull(int parameterIndex, int sqlType) throws SQLException {
    saveObject(parameterIndex, "NULL");
    ps.setNull(parameterIndex, sqlType);
  }

  public int getMaxFieldSize() throws SQLException {
    return ps.getMaxFieldSize();
  }

  public void setBoolean(int parameterIndex, boolean x) throws SQLException {
    saveObject(parameterIndex, new Boolean(x));
    ps.setBoolean(parameterIndex, x);
  }

  public void setMaxFieldSize(int max) throws SQLException {
    ps.setMaxFieldSize(max);
  }

  public void setByte(int parameterIndex, byte x) throws SQLException {
    saveObject(parameterIndex, new Byte(x));
    ps.setByte(parameterIndex, x);
  }

  public void setShort(int parameterIndex, short x) throws SQLException {
    saveObject(parameterIndex, new Short(x));
    ps.setShort(parameterIndex, x);
  }

  public int getMaxRows() throws SQLException {
    return ps.getMaxRows();
  }

  public void setInt(int parameterIndex, int x) throws SQLException {
    saveObject(parameterIndex, new Integer(x));
    ps.setInt(parameterIndex, x);
  }

  public void setMaxRows(int max) throws SQLException {
    ps.setMaxRows(max);
  }

  public void setLong(int parameterIndex, long x) throws SQLException {
    saveObject(parameterIndex, new Long(x));
    ps.setLong(parameterIndex, x);
  }

  public void setEscapeProcessing(boolean enable) throws SQLException {
    ps.setEscapeProcessing(enable);
  }

  public void setFloat(int parameterIndex, float x) throws SQLException {
    saveObject(parameterIndex, new Float(x));
    ps.setFloat(parameterIndex, x);
  }

  public int getQueryTimeout() throws SQLException {
    return ps.getQueryTimeout();
  }

  public void setDouble(int parameterIndex, double x) throws SQLException {
    saveObject(parameterIndex, new Double(x));
    ps.setDouble(parameterIndex, x);
  }

  public void setQueryTimeout(int seconds) throws SQLException {
    ps.setQueryTimeout(seconds);
  }

  public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
    saveObject(parameterIndex, x);
    ps.setBigDecimal(parameterIndex, x);
  }

  public void cancel() throws SQLException {
    ps.cancel();
  }

  public void setString(int parameterIndex, String x) throws SQLException {
    saveObject(parameterIndex, x);
    ps.setString(parameterIndex, x);
  }

  public SQLWarning getWarnings() throws SQLException {
    return ps.getWarnings();
  }

  public void setBytes(int parameterIndex, byte[] x) throws SQLException {
    saveObject(parameterIndex, (x == null ? "NULL" : "byte[] length=" + x.length));
    ps.setBytes(parameterIndex, x);
  }

  public void clearWarnings() throws SQLException {
    ps.clearWarnings();
  }

  public void setDate(int parameterIndex, Date x) throws SQLException {
    saveObject(parameterIndex, x);
    ps.setDate(parameterIndex, x);
  }

  public void setCursorName(String name) throws SQLException {
    ps.setCursorName(name);
  }

  public void setTime(int parameterIndex, Time x) throws SQLException {
    saveObject(parameterIndex, x);
    ps.setTime(parameterIndex, x);
  }

  public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
    saveObject(parameterIndex, x);
    ps.setTimestamp(parameterIndex, x);
  }

  public boolean execute(String sql) throws SQLException {
    log.debug("{}", this);
    Boolean results = null;
    try {
      results = (Boolean) executeVerboseQuery("execute", sql);
    } catch (Exception e) {
      throw new SQLException("Could not execute sql command - Original message: " + e.getMessage(), e);
    }
    return results.booleanValue();
  }

  public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
    saveObject(parameterIndex, (x == null ? "NULL" : "<stream length= " + length + ">"));
    ps.setAsciiStream(parameterIndex, x, length);
  }

  public ResultSet getResultSet() throws SQLException {
    return ps.getResultSet();
  }

  public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
    throw new IllegalAccessError("As it is deplracated do not use in Pericles");
  }

  public int getUpdateCount() throws SQLException {
    return ps.getUpdateCount();
  }

  public boolean getMoreResults() throws SQLException {
    return ps.getMoreResults();
  }

  public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
    saveObject(parameterIndex, (x == null ? "NULL" : "<stream length= " + length + ">"));
    ps.setBinaryStream(parameterIndex, x, length);
  }

  public void setFetchDirection(int direction) throws SQLException {
    ps.setFetchDirection(direction);
  }

  public void clearParameters() throws SQLException {
    ps.clearParameters();
  }

  public int getFetchDirection() throws SQLException {
    return ps.getFetchDirection();
  }

  public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
    saveObject(parameterIndex, (x == null ? "NULL" : x.getClass().getName()));
    ps.setObject(parameterIndex, x, targetSqlType);
  }

  public void setFetchSize(int rows) throws SQLException {
    ps.setFetchSize(rows);
  }

  public int getFetchSize() throws SQLException {
    return ps.getFetchSize();
  }

  public void setObject(int parameterIndex, Object x) throws SQLException {
    saveObject(parameterIndex, (x == null ? "NULL" : x.getClass().getName()));
    ps.setObject(parameterIndex, x);
  }

  public int getResultSetConcurrency() throws SQLException {
    return ps.getResultSetConcurrency();
  }

  public int getResultSetType() throws SQLException {
    return ps.getResultSetType();
  }

  public void addBatch(String sql) throws SQLException {
    ps.addBatch(sql);
  }

  public void clearBatch() throws SQLException {
    ps.clearBatch();
  }

  public boolean execute() throws SQLException {
    log.debug("{}", this);
    Boolean results = null;
    try {
      results = (Boolean) executeVerboseQuery("execute");
    } catch (Exception e) {
      throw new SQLException("Could not execute sql command - Original message: " + e.getMessage(), e);
    }
    return results.booleanValue();
  }

  public int[] executeBatch() throws SQLException {
    log.debug("{}", this);
    int[] results = null;
    try {
      results = (int[]) executeVerboseQuery("executeBatch");
    } catch (Exception e) {
      throw new SQLException("Could not execute sql command - Original message: " + e.getMessage(), e);
    }
    return results;
  }

  public void addBatch() throws SQLException {
    ps.addBatch();
  }

  public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
    saveObject(parameterIndex, (reader == null ? "NULL" : "<stream length= " + length + ">"));
    ps.setCharacterStream(parameterIndex, reader, length);
  }

  public void setRef(int i, Ref x) throws SQLException {
    saveObject(i, x);
    ps.setRef(i, x);
  }

  public Connection getConnection() throws SQLException {
    return ps.getConnection();
  }

  public void setBlob(int parameterIndex, Blob x) throws SQLException {
    saveObject(parameterIndex, x);
    ps.setBlob(parameterIndex, x);
  }

  public void setClob(int i, Clob x) throws SQLException {
    saveObject(i, x);
    ps.setClob(i, x);
  }

  public boolean getMoreResults(int current) throws SQLException {
    return ps.getMoreResults(current);
  }

  public void setArray(int i, Array x) throws SQLException {
    saveObject(i, x);
    ps.setArray(i, x);
  }

  public ResultSetMetaData getMetaData() throws SQLException {
    return ps.getMetaData();
  }

  public ResultSet getGeneratedKeys() throws SQLException {
    return ps.getGeneratedKeys();
  }

  public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
    cal.setTime(new java.util.Date(x.getTime()));
    saveObject(parameterIndex, cal);
    ps.setDate(parameterIndex, x, cal);
  }

  public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
    log.debug("{}", sql);
    return ps.executeUpdate(sql, autoGeneratedKeys);
  }

  public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
    cal.setTime(new java.util.Date(x.getTime()));
    saveObject(parameterIndex, cal);
    ps.setTime(parameterIndex, x, cal);
  }

  public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
    log.debug("{}", sql);
    return ps.executeUpdate(sql, columnIndexes);
  }

  public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
    cal.setTime(new java.util.Date(x.getTime()));
    saveObject(parameterIndex, cal);
    ps.setTimestamp(parameterIndex, x, cal);
  }

  public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
    saveObject(parameterIndex, "NULL");
    ps.setNull(parameterIndex, sqlType, typeName);
  }

  public int executeUpdate(String sql, String[] columnNames) throws SQLException {
    log.debug("{}", sql);
    return ps.executeUpdate(sql, columnNames);
  }

  public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
    log.debug("{}", this);
    return ps.execute(sql, autoGeneratedKeys);
  }

  public void setURL(int parameterIndex, URL x) throws SQLException {
    ps.setURL(parameterIndex, x);
  }

  public ParameterMetaData getParameterMetaData() throws SQLException {
    return ps.getParameterMetaData();
  }

  public void setRowId(int parameterIndex, RowId x) throws SQLException {
    ps.setRowId(parameterIndex, x);
  }

  public boolean execute(String sql, int[] columnIndexes) throws SQLException {
    log.debug("{}", this);
    return ps.execute(sql, columnIndexes);
  }

  public void setNString(int parameterIndex, String value) throws SQLException {
    ps.setNString(parameterIndex, value);
  }

  public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
    ps.setNCharacterStream(parameterIndex, value, length);
  }

  public boolean execute(String sql, String[] columnNames) throws SQLException {
    log.debug("{}", this);
    return ps.execute(sql, columnNames);
  }

  public void setNClob(int parameterIndex, NClob value) throws SQLException {
    ps.setNClob(parameterIndex, value);
  }

  public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
    ps.setClob(parameterIndex, reader, length);
  }

  public int getResultSetHoldability() throws SQLException {
    return ps.getResultSetHoldability();
  }

  public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
    ps.setBlob(parameterIndex, inputStream, length);
  }

  public boolean isClosed() throws SQLException {
    return ps.isClosed();
  }

  public void setPoolable(boolean poolable) throws SQLException {
    ps.setPoolable(poolable);
  }

  public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
    ps.setNClob(parameterIndex, reader, length);
  }

  public boolean isPoolable() throws SQLException {
    return ps.isPoolable();
  }

  public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
    ps.setSQLXML(parameterIndex, xmlObject);
  }

  public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
    saveObject(parameterIndex, (x == null ? "NULL" : x.getClass().getName()));
    ps.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
  }

  public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
    ps.setAsciiStream(parameterIndex, x, length);
  }

  public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
    ps.setBinaryStream(parameterIndex, x, length);
  }

  public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
    ps.setCharacterStream(parameterIndex, reader, length);
  }

  public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
    ps.setAsciiStream(parameterIndex, x);
  }

  public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
    ps.setBinaryStream(parameterIndex, x);
  }

  public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
    ps.setCharacterStream(parameterIndex, reader);
  }

  public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
    ps.setNCharacterStream(parameterIndex, value);
  }

  public void setClob(int parameterIndex, Reader reader) throws SQLException {
    ps.setClob(parameterIndex, reader);
  }

  public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
    ps.setBlob(parameterIndex, inputStream);
  }

  public void setNClob(int parameterIndex, Reader reader) throws SQLException {
    ps.setNClob(parameterIndex, reader);
  }

}
