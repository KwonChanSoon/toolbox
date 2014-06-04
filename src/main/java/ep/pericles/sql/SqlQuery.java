package ep.pericles.sql;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import ep.pericles.tuple.ImmutablePair;

public class SqlQuery extends ImmutablePair<String, SqlParameterSource> {

  public SqlQuery(String left, SqlParameterSource right) {
    super(left, right);
  }

  public String getSql() {
    return _1();
  }

  public SqlParameterSource getParams() {
    return _2();
  }
}