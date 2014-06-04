package eu.qualityontime.pagination;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class PaginationWrapperSql {
  String origSql;
  Boolean returnCount = Boolean.FALSE;
  PaginationCriterialBuilder pb;
  PagingCriteria pagingCrit;
  String defaultSortPhrase;

  public PaginationWrapperSql(String sql) {
    this.origSql = sql;
  }

  public PaginationWrapperSql(String sql, Boolean returnCount) {
    this(sql);
    this.returnCount = returnCount;
  }

  public PaginationWrapperSql(String sql, Boolean returnCount, PagingCriteria pagingCrit, String defaultSortPhrase) {
    this(sql);
    this.returnCount = returnCount;
    this.pagingCrit = pagingCrit;
    pb = new PaginationCriterialBuilder(pagingCrit);
    this.defaultSortPhrase = defaultSortPhrase;
  }

  public PaginationWrapperSql(String sql, Boolean returnCount, PaginationCriterialBuilder pcb, String defaultSortPhrase) {
    this(sql);
    this.returnCount = returnCount;
    this.pagingCrit = pcb.getCrit();
    pb = pcb;
    this.defaultSortPhrase = defaultSortPhrase;
  }

  public String wrappedSql() {
    String res = null;
    String orderedSql = origSql + buildOrderBy();
    if (returnCount) {
      res = "SELECT * from (SELECT rownum as rn, count(1) over (partition by 1) " + getCountField() + ", otto_1.* from ("
          + orderedSql + ") otto_1)";
    }
    else {
      res = "SELECT * from (SELECT rownum as rn, otto_1.* from (" + orderedSql + ") otto_1)";
    }
    res += pb.paging_where();
    return res;
  }

  private String buildOrderBy() {
    if (null != pagingCrit && pagingCrit.isUnordered()) {
      return "";
    }
    if (null != pagingCrit && isBlank(pagingCrit.getSortCol())) {
      return (defaultSortPhrase);
    }
    else {
      return pb.order_by();
    }
  }

  public String getCountField() {
    return "num_of_recs";
  }
}
