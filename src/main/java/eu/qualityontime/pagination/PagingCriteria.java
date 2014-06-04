package eu.qualityontime.pagination;

import static eu.qualityontime.AppObjects.defaultIfNull;
import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.*;

public class PagingCriteria {
  protected int itemPerPage = 10;
  protected int page;
  protected ESortOrder order = ESortOrder.ASC;
  protected String sortCol;
  private boolean unordered = false;

  public PagingCriteria(String page, String order, String sortCol) {
    this.page = parseInt(defaultIfBlank(page, "1"));
    decodeOrder(this, order);
    this.sortCol = sortCol;
  }

  public PagingCriteria() {
  }

  public static void decodeOrder(PagingCriteria c, String parameter) {
    if (StringUtils.isBlank(parameter)) {
      c.order = c.defaultSortOrder();
      return;
    }
    int orderid = parseInt(parameter);
    if (2 == orderid) {
      c.order = ESortOrder.ASC;
    }
    else {
      c.order = ESortOrder.DESC;
    }
  }

  public String sortableColumn() {
    return defaultIfBlank(sortCol, defaultSortColumn());
  }

  /**
   * Candidate to overwrite
   */
  protected String defaultSortColumn() {
    return null;
  }

  /**
   * Candidate to overwrite
   */
  protected ESortOrder defaultSortOrder() {
    return ESortOrder.ASC;
  }

  public boolean isDesc() {
    return ESortOrder.DESC.equals(defaultIfNull(getOrder(), defaultSortOrder()));
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public ESortOrder getOrder() {
    return order;
  }

  public void setOrder(ESortOrder order) {
    this.order = order;
  }

  public void setSortCol(String sortCol) {
    this.sortCol = sortCol;
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

  public int getItemPerPage() {
    return itemPerPage;
  }

  public void setItemPerPage(int itemPerPage) {
    this.itemPerPage = itemPerPage;
  }

  public boolean isEmptyOrderCriteria() {
    return isBlank(sortableColumn()) || null == order;
  }

  public boolean isEmptyPagingCriteria() {
    return 0 == page;
  }

  public String getSortCol() {
    return null == sortCol ? this.defaultSortColumn() : sortCol;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }

  public boolean isUnordered() {
    return unordered;
  }

  public PagingCriteria setUnordered(boolean unordered) {
    this.unordered = unordered;
    return this;
  }
}
