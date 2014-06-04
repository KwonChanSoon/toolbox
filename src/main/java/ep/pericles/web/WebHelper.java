package ep.pericles.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

public class WebHelper {
  public static Map<String, String> flatParameterMap(HttpServletRequest request) {
    return flatParameterMap(request.getParameterMap());
  }

  public static Map<String, String> flatParameterMap(Map<java.lang.String, java.lang.String[]> in) {
    return Maps.transformValues(in, new Function<String[], String>() {
      @Override
      public String apply(String[] input) {
        return input[0];
      }
    });
  }
  
  public static String hostUrl(HttpServletRequest request){
    String url  = request.getRequestURL().toString();
    return url.substring(0,url.indexOf(request.getContextPath()));
  }
}
