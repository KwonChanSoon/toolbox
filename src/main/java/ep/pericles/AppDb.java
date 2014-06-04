package ep.pericles;

import java.sql.*;
import java.util.List;

import org.slf4j.*;
import org.springframework.jdbc.support.nativejdbc.*;

import com.google.common.base.*;
import com.google.common.collect.*;

public class AppDb {
  public static final Integer MAX_IN_LIST_SIZE = 999;
  private static final Logger log = LoggerFactory.getLogger(AppDb.class);
  public static final Function<String, String> NOT_WRAP = new Function<String, String>() {
    public String apply(String input) {
      return "NOT(" + input + ")";
    }
  };

  public static void closeSilently(ResultSet rs) {
    try {
      close(rs);
    } catch (SQLException e) {
      //ignore. Most of the time nothing to do.
    }
  }

  public static void close(ResultSet rs) throws SQLException {
    if (null != rs) {
      rs.close();
    }
  }

  public static void close(Statement rs) throws SQLException {
    if (null != rs) {
      rs.close();
    }
  }

  public static void closeSilently(Statement rs) {
    try {
      close(rs);
    } catch (SQLException e) {
      //ignore as not relevant
    }
  }

  /**
   * O just simply should be injected by Spring the proper extractor.
   */
  private static final List<? extends NativeJdbcExtractor> extractors = ImmutableList.of(new CommonsDbcpNativeJdbcExtractor(),
      /*new JBossNativeJdbcExtractor(),*/new SimpleNativeJdbcExtractor());
  private static NativeJdbcExtractor currentJdbcExtractor = null;

  public static Connection getInnermostDelegate(Connection con) {
    Connection ret = null;
    if (null == currentJdbcExtractor) {
      NativeJdbcExtractor workingOne = null;
      for (NativeJdbcExtractor ne : extractors) {
        ret = getConnectionByExtractor(con, ne);
        if (null != ret) {
          workingOne = ne;
          break;
        }
      }
      synchronized (extractors) {
        if (null == currentJdbcExtractor) {
          currentJdbcExtractor = workingOne;
        }
      }
    }
    else {
      try {
        ret = currentJdbcExtractor.getNativeConnection(con);
      } catch (SQLException e) {
        throw Throwables.propagate(e);
      }
    }
    if (null == ret) {
      log.error("Unhandled connection pool type: {}", con.getClass());
      ret = con;
    }
    AppPreconditions.checkNotNull("Must have a valid connection", ret);
    return ret;
  }

  private static Connection getConnectionByExtractor(Connection con, NativeJdbcExtractor ne) {
    try {
      return ne.getNativeConnection(con);
    } catch (SQLException e) {
      return null;
    }
  }

  /**
   * Into the `placeholder` it puts variables like `param_prefix_x` where x is a sequence from 0 until `size`-1
   */
  public static List<String> genSeqParams(String template, String placeholder, String param_prefix, int size) {
    List<String> res = AppCollections.List();
    for (int i = 0; i < size; i++) {
      res.add(AppStrings.replace(template, ImmutableMap.of(placeholder, param_prefix + i)));
    }
    Preconditions.checkArgument(res.size() == size);
    return res;
  }

}
