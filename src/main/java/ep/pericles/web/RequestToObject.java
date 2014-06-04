package ep.pericles.web;

import java.util.*;

import org.apache.commons.beanutils.*;

import com.google.common.base.*;
import com.google.common.collect.*;

import ep.pericles.*;
import ep.pericles.commons.JakartaBeanutilsBean;
import ep.pericles.tuple.Pair;

/**
 * Parsing complex collection fo objects from http request parameters.
 * E.g expected object structure
 * name{sessio_uid, assignments[{booth_uid, pers_uid}]}
 * input requests parameter names:
 * name[session_uid]
 * name[assignments][0][booth_uid]
 * name[assignments][0][pers_uid]
 * name[assignments][1][booth_uid]
 * name[assignments][1][pers_uid]
 * 
 * TODO: work in progress. Probably wont needed at all.
 */
public class RequestToObject {
  public static Map<String, Object> getObject(String name, Map<String, String> req) {
    return new RequestToObject(req).object(name);
  }

  private Map<String, String> req;
  private Collection<Pair<Converter, Class<?>>> converters;

  public RequestToObject(Map<String, String> req) {
    this.req = req;
  }

  public <T> T object(String name, Class<T> clazz) {
    Map<String, Object> r = object(name);
    T ret = object(r, clazz, converters);
    return ret;
  }

  public static <T> T object(Map<String, Object> r, Class<T> clazz, Collection<Pair<Converter, Class<?>>> converters) {
    T ret = AppObjects.invokeConstructor(clazz);

    ConvertUtilsBean cu = new ConvertUtilsBean();
    final JakartaBeanutilsBean bu = new JakartaBeanutilsBean(new BeanUtilsBean(cu));
    for (Pair<Converter, Class<?>> cp : converters) {
      registerDelegate(cp._1(), bu);
      cu.register(cp._1(), cp._2());
    }
    bu.copyProperties(ret, r);

    return ret;
  }

  private static void registerDelegate(Converter _1, JakartaBeanutilsBean bu) {
    if (_1 instanceof ABeanutilsBeanDelegateConverter) {
      ((ABeanutilsBeanDelegateConverter) _1).setDelegeConverter(bu);
    }
  }

  public Map<String, Object> object(final String name) {
    Iterable<String> relevantFields = Iterables.filter(req.keySet(), new Predicate<String>() {
      @Override
      public boolean apply(String input) {
        return input.startsWith(name + "[");
      }
    });
    boolean hasName = relevantFields.iterator().hasNext();
    if (!hasName) {
      return null;
    }
    Map<String, Object> ret = new HashMap<String, Object>();
    for (String p : relevantFields) {
      List<String> params = splitParams(name, p);
      String val = req.get(p);
      assign(ret, params, val);
    }

    return ret;
  }

  private void assign(Map<String, Object> ret, List<String> params, String val) {
    if (params.isEmpty()) {
      return;
    }
    String currentParam = params.get(0);
    if (1 == params.size()) {
      ret.put(currentParam, val);
      return;
    }
    Map<String, Object> subobject = getsubobject(ret, currentParam);
    List<String> remainingParam = ImmutableList.copyOf(Iterables.skip(params, 1));
    assign(subobject, remainingParam, val);
  }

  private Map<String, Object> getsubobject(Map<String, Object> ret, String currentParam) {
    Map<String, Object> o = (Map<String, Object>) ret.get(currentParam);
    if (null == o) {
      o = AppCollections.Map();
      ret.put(currentParam, o);
    }
    return o;
  }

  private final static CharMatcher paramMatcher = CharMatcher.anyOf("][");

  public static List<String> splitParams(String name, String p) {
    String lp = p.replace(name, "");
    Splitter s = Splitter.on(paramMatcher).trimResults().omitEmptyStrings();
    List<String> res = ImmutableList.copyOf(s.split(lp));
    return res;
  }
}
