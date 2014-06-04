package ep.pericles.session;

import javax.servlet.http.HttpServletRequest;

public interface WebSessionUtilDelegate extends ISessionUtilDelegate {
  public void request(HttpServletRequest request);
}
