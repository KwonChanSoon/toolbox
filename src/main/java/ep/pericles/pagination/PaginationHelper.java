package ep.pericles.pagination;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

public class PaginationHelper {
  public static PagingCriteria PagingCriteria(HttpServletRequest req, String tableId) {
    ParamEncoder pe = new ParamEncoder(tableId);
    String parameterPage = (pe.encodeParameterName(TableTagParameters.PARAMETER_PAGE));
    String parameterOrder = (pe.encodeParameterName(TableTagParameters.PARAMETER_ORDER));
    String parameterSort = (pe.encodeParameterName(TableTagParameters.PARAMETER_SORT));

    PagingCriteria c = new PagingCriteria(req.getParameter(parameterPage), req.getParameter(parameterOrder),
        req.getParameter(parameterSort));
    return c;
  }

  public static PagingCriteria PagingCriteria(PagingCriteria c, HttpServletRequest req, String tableId) {
    ParamEncoder pe = new ParamEncoder(tableId);
    String parameterPage = (pe.encodeParameterName(TableTagParameters.PARAMETER_PAGE));
    String parameterOrder = (pe.encodeParameterName(TableTagParameters.PARAMETER_ORDER));
    String parameterSort = (pe.encodeParameterName(TableTagParameters.PARAMETER_SORT));

    c.setPage(parseInt(defaultIfBlank(req.getParameter(parameterPage), "1")));
    PagingCriteria.decodeOrder(c, req.getParameter(parameterOrder));
    c.setSortCol(req.getParameter(parameterSort));
    return c;
  }
}
