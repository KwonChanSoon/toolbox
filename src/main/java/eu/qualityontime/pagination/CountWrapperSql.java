package eu.qualityontime.pagination;

public class CountWrapperSql {
  final String sql;

  public CountWrapperSql(String sql) {
    this.sql = sql;
  }

  public String wrappedSql() {
    return "select count(1) from(" + sql + ")";
  }
}
