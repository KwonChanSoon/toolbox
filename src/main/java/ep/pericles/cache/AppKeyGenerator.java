package ep.pericles.cache;

import java.lang.reflect.Method;
import java.util.*;

import org.slf4j.*;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.cache.interceptor.KeyGenerator;

import com.google.common.collect.*;
import com.google.common.collect.ImmutableList.Builder;

/**
 * Use `27.3.1.2 Custom Key Generation Declaration`
 * in http://static.springsource.org/spring/docs/3.1.0.M1/spring-framework-reference/html/cache.html
 * But Spring SPELL performance is not so funy.
 */
public class AppKeyGenerator implements KeyGenerator {
  private static Logger log = LoggerFactory.getLogger(AppKeyGenerator.class);

  @Override
  public Object generate(Object target, Method method, Object... params) {
    Class<?> objectclass = AopProxyUtils.ultimateTargetClass(target);
    List<Object> cacheKey = new LinkedList<Object>();
    cacheKey.add(objectclass.getName().intern());
    //cacheKey.add(System.identityHashCode(target));
    cacheKey.add(method.toString().intern());
    ImmutableList.Builder<Object> paramBuilder = new ImmutableList.Builder<Object>();
    buildFlattenParamList(paramBuilder, params);
    List<Object> flattenParamList = paramBuilder.build();
    cacheKey.addAll(flattenParamList);
    log.trace("{}", cacheKey);
    return cacheKey;
  }

  private void buildFlattenParamList(Builder<Object> paramBuilder, Object[] params) {
    for (Object p : params) {
      if (p instanceof Object[]) {
        buildFlattenParamList(paramBuilder, (Object[]) p);
      }
      else {
        paramBuilder.add(p);
      }
    }
  }
}
