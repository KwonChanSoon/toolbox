package eu.qualityontime.pagination;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PaginationWrapperSqlTest {

  String sql = "select sysdate from dual";

  @Test
  public void basic() {
    String expected = "SELECT * from (SELECT rownum as rn, count(1) over (partition by 1) num_of_recs, otto_1.* from (select sysdate from dual) otto_1)";
    PagingCriteria pagingCrit = new PagingCriteria();
    String res = sql;
    PaginationWrapperSql paginationWrapperSql = new PaginationWrapperSql(res, true, pagingCrit, "");
    res = paginationWrapperSql.wrappedSql();
    //System.out.println(res);
    assertEquals(expected, res);
  }

  @Test
  public void basic_default_order() {
    String expected = "SELECT * from (SELECT rownum as rn, count(1) over (partition by 1) num_of_recs, otto_1.* from (select sysdate from dual order by sess_uid desc ) otto_1)";
    PagingCriteria pagingCrit = new PagingCriteria();
    String res = sql;
    PaginationWrapperSql paginationWrapperSql = new PaginationWrapperSql(res, true, pagingCrit, " order by sess_uid desc ");
    res = paginationWrapperSql.wrappedSql();
    //System.out.println(res);
    assertEquals(expected, res);
  }

  @Test
  public void basic_custom_order() {
    String expected = "SELECT * from (SELECT rownum as rn, count(1) over (partition by 1) num_of_recs, otto_1.* from (select sysdate from dual ORDER BY dummy ASC) otto_1)";
    PagingCriteria pagingCrit = new PagingCriteria();
    pagingCrit.setSortCol("dummy");
    String res = sql;
    PaginationWrapperSql paginationWrapperSql = new PaginationWrapperSql(res, true, pagingCrit, " order by sess_uid desc ");
    res = paginationWrapperSql.wrappedSql();
    //System.out.println(res);
    assertEquals(expected, res);
  }

  @Test
  public void with_paging() {
    String expected = "SELECT * from (SELECT rownum as rn, count(1) over (partition by 1) num_of_recs, otto_1.* from (select sysdate from dual ORDER BY dummy ASC) otto_1) WHERE  rn BETWEEN 1 AND 5 ";
    PagingCriteria pagingCrit = new PagingCriteria();
    pagingCrit.setSortCol("dummy");
    pagingCrit.setPage(1);
    pagingCrit.setItemPerPage(5);
    String res = sql;
    PaginationWrapperSql paginationWrapperSql = new PaginationWrapperSql(res, true, pagingCrit, " order by sess_uid desc ");
    res = paginationWrapperSql.wrappedSql();
    //System.out.println(res);
    assertEquals(expected, res);
  }

  @Test
  public void infinite_unordered_result_from_paging_criteria() {
    String expected = "SELECT * from (SELECT rownum as rn, count(1) over (partition by 1) num_of_recs, otto_1.* from (select sysdate from dual) otto_1)";
    PagingCriteria pagingCrit = new PagingCriteria();
    pagingCrit.setUnordered(true);
    String res = sql;
    PaginationWrapperSql paginationWrapperSql = new PaginationWrapperSql(res, true, pagingCrit, " order by sess_uid desc ");
    res = paginationWrapperSql.wrappedSql();
    //System.out.println(res);
    assertEquals(expected, res);
  }

}
