package eu.qualityontime.pagination;

import java.util.*;

import com.google.common.base.Function;

public class PaginationCriterialBuilder {
  protected final static Map<String, Function<String, String>> ORDER_FUNCT = new HashMap<String, Function<String, String>>();
  static {
    ORDER_FUNCT.put("create_user", new Function<String, String>() {
      public String apply(String fieldname) {
        return " UPPER(" + fieldname + ") ";
      }
    });
  }
  private final static Function<String, String> IDENTICAL = new Function<String, String>() {
    public String apply(String fieldname) {
      return fieldname;
    }
  };

  private PagingCriteria crit;

  public PaginationCriterialBuilder(PagingCriteria crit) {
    this.crit = crit;
  }

  public String order_by() {
    if (null == crit || crit.isEmptyOrderCriteria()) {
      return "";
    }
    return " ORDER BY " + order_by_close();
  }

  public String order_by_close() {
    return order_by_column() + " " + crit.getOrder().toString();
  }

  public String order_by_column() {
    return order_funct(crit.sortableColumn()).apply(crit.sortableColumn());
  }

  private Function<String, String> order_funct(String sortCol) {
    Function<String, String> ret = getOrderFunctions().get(sortCol);
    if (null == ret) {
      return IDENTICAL;
    }
    return ret;
  }

  protected Map<String, Function<String, String>> getOrderFunctions() {
    return ORDER_FUNCT;
  }

  public String paging_where() {
    if (null == crit || crit.isEmptyPagingCriteria()) {
      return "";
    }
    return " WHERE " + where_close();
  }

  public String where_close() {
    return " rn BETWEEN " + lowerBound() + " AND " + upperBound() + " ";
  }

  private int lowerBound() {
    return (crit.getPage() - 1) * crit.getItemPerPage() + 1;
  }

  private int upperBound() {
    return lowerBound() + crit.getItemPerPage() - 1;
  }

  public PagingCriteria getCrit() {
    return crit;
  }
}
