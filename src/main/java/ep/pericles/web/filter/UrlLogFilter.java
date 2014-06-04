package ep.pericles.web.filter;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.*;

import com.google.common.collect.ImmutableList;

public class UrlLogFilter implements Filter {
  private static final Logger log = LoggerFactory.getLogger(UrlLogFilter.class);
  private Collection<String> except = ImmutableList.of();
  private Collection<String> ignoreExtension = ImmutableList.of("js", "css", "png", "jpg", "gif", "woff", "eot");

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
      ServletException {
    if (!log.isDebugEnabled()) {
      chain.doFilter(request, response);
      return;
    }
    StopWatch watch = null;
    HttpServletRequest req = ((HttpServletRequest) request);
    String uri = req.getRequestURI();
    if (ignoreExtension.contains(extension(uri))) {
      chain.doFilter(request, response);
      return;
    }
    if (except.contains(uri)) {
      chain.doFilter(request, response);
      return;
    }
    if (log.isDebugEnabled()) {
      watch = new StopWatch();
      watch.start();
      String queryString = req.getQueryString();
      if (queryString != null) {
        uri += "?" + queryString;
      }
      log.debug("Before request [{}]", uri);
    }
    chain.doFilter(request, response);
    if (log.isDebugEnabled()) {
      watch.stop();
      log.debug("After  request [{}],{}", uri, watch.toString());
    }
  }

  private String extension(String uri) {
    if (uri.contains(".")) {
      String ext = uri.substring(uri.lastIndexOf(".") + 1);
      if (ext.contains("?")) {
        ext = ext.substring(0, ext.indexOf('?'));
      }
      return ext;
    }
    return uri;
  }

  @Override
  public void destroy() {
  }

  @Override
  public void init(FilterConfig fConfig) throws ServletException {
  }

}
