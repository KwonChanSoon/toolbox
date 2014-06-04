package eu.qualityontime.pagination;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class JQGridPaginationHelper {

  public static final String PAGE_PARAM = "page";
  public static final String SORT_COL_PARAM = "sidx";
  public static final String SORT_ORDER_PARAM = "sord";
  public static final String LIMIT_PARAM = "rows";

  public static PagingCriteria PagingCriteria(HttpServletRequest req) {

    PagingCriteria criteria = new PagingCriteria();

    criteria.setPage(parseInt(defaultIfBlank(req.getParameter(PAGE_PARAM), "1")));
    criteria.setSortCol(req.getParameter(SORT_COL_PARAM));
    criteria.setOrder(getOrder(req.getParameter(SORT_ORDER_PARAM)));
    criteria.setItemPerPage(parseInt(defaultIfBlank(req.getParameter(LIMIT_PARAM), "10")));

    return criteria;
  }

  protected static ESortOrder getOrder(String orderValue) {
    if (StringUtils.isBlank(orderValue)) {
      return ESortOrder.ASC;
    }
    if (!orderValue.equalsIgnoreCase("ASC")) {
      return ESortOrder.DESC;
    }
    else {
      return ESortOrder.ASC;
    }

  }

}
