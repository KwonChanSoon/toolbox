package ep.pericles.web.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

public class CacheControlFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
      ServletException {
    HttpServletResponse resp = (HttpServletResponse) response;
    resp.setHeader("Expires", "Tue, 03 Jul 2001 06:00:00 GMT");
    //resp.setHeader("Last-Modified", new Date().toString());
    resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0, post-check=0, pre-check=0");
    resp.setHeader("Pragma", "no-cache");
    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig fConfig) throws ServletException {
  }

  @Override
  public void destroy() {
  }

}