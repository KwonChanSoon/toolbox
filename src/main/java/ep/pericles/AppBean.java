package ep.pericles;

import java.util.concurrent.ExecutionException;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.*;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.common.base.Throwables;
import com.google.common.cache.*;

/**
 * See AppBeanTest for usage scennarios
 */
public class AppBean {
  private static ExpressionParser parser = new AppBeanSpelExpressionParser();

  private static class AppBeanSpelExpressionParser extends SpelExpressionParser {
    LoadingCache<String, SpelExpression> graphs = CacheBuilder.newBuilder()
        .maximumSize(1000)
        .build(
            new CacheLoader<String, SpelExpression>() {
              @Override
              public SpelExpression load(String key) throws ParseException {
                return AppBeanSpelExpressionParser.super.doParseExpression(key, null);
              }
            });

    @Override
    protected SpelExpression doParseExpression(String expressionString, ParserContext context) throws ParseException {
      try {
        return graphs.get(expressionString);
      } catch (ExecutionException e) {
        throw Throwables.propagate(e);
      }
    }
  }

  /**
   * supporting nested property access and safe reference operator.
   */
  public static <T> T get(Object o, String property) {
    Expression exp = parser.parseExpression(property);
    return (T) exp.getValue(o);
  }

  /**
   * supporting nested property access and safe reference operator.
   */
  public static <T> T get(Object o, String property, Class<T> desiredReturnType) {
    Expression exp = parser.parseExpression(property);
    return exp.getValue(o, desiredReturnType);
  }

  @SuppressWarnings({ "unchecked" })
  public static <T> T getSpringTargetObject(Object proxy, Class<T> targetClass) throws Exception {
    if (AopUtils.isJdkDynamicProxy(proxy)) {
      return (T) ((Advised) proxy).getTargetSource().getTarget();
    }
    else {
      return (T) proxy; // expected to be cglib proxy then, which is simply a specialized class
    }
  }

  public static <T> void set(Object o, String property, Object val) {
    StandardEvaluationContext simpleContext = new StandardEvaluationContext(o);
    Expression exp = parser.parseExpression(property);
    exp.setValue(simpleContext, val);
  }

  /*
     public static <T> T map(Object sourceObject, Class<T> targetClass) {
   
      return AppObjects.cast( new DozerBeanMapper().map(sourceObject, targetClass));
    }
   */

}