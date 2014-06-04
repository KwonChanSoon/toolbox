package ep.pericles.spring.database;

import static ep.pericles.functionals.FIterable.FIterable;
import static org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils.extractCaseInsensitiveParameterNames;

import java.util.*;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.KeyHolder;

import com.google.common.collect.ImmutableMap;

import ep.pericles.*;
import ep.pericles.sql.ComposedSqlParameterSource;

/**
 * Automatically fill
 * CREATE_DATE DATE
 * CREATE_USER VARCHAR2(30)
 * UPDATE_DATE DATE
 * UPDATE_USER VARCHAR2(30)
 */
public class AuditableJdbcInsert extends SimpleJdbcInsert {
  private UsernameSupplier currentUserSupplier;

  public AuditableJdbcInsert(DataSource dataSource, UsernameSupplier currentUserSupplier) {
    super(dataSource);
    this.currentUserSupplier = currentUserSupplier;
  }

  public AuditableJdbcInsert(JdbcTemplate jdbcTemplate, UsernameSupplier currentUserSupplier) {
    super(jdbcTemplate);
    this.currentUserSupplier = currentUserSupplier;
  }

  @Override
  public int execute(Map<String, Object> args) {
    return super.execute(extend(args));
  }

  @Override
  public int execute(SqlParameterSource parameterSource) {
    return super.execute(extend(parameterSource));
  }

  @Override
  public Number executeAndReturnKey(Map<String, Object> args) {
    return super.executeAndReturnKey(extend(args));
  }

  @Override
  public Number executeAndReturnKey(SqlParameterSource parameterSource) {
    return super.executeAndReturnKey(extend(parameterSource));
  }

  @Override
  public KeyHolder executeAndReturnKeyHolder(Map<String, Object> args) {
    return super.executeAndReturnKeyHolder(extend(args));
  }

  @Override
  public KeyHolder executeAndReturnKeyHolder(SqlParameterSource parameterSource) {
    return super.executeAndReturnKeyHolder(extend(parameterSource));
  }

  @Override
  public int[] executeBatch(Map<String, Object>[] batch) {
    return super.executeBatch(extend(batch));
  }

  @Override
  public int[] executeBatch(SqlParameterSource[] batch) {
    return super.executeBatch(extend(batch));
  }

  private Map<String, Object> extend(Map<String, Object> args) {
    Set<String> keys = FIterable(args.keySet())
        .map(AppFunction.toUpper())
        .asSet();
    ImmutableMap.Builder<String, Object> b = new ImmutableMap.Builder<String, Object>().putAll(args);
    if (!keys.contains("CREATE_DATE")) {
      b.put("CREATE_DATE", AppCalendar.now());
    }
    if (!keys.contains("UPDATE_DATE")) {
      b.put("UPDATE_DATE", AppCalendar.now());
    }
    if (!keys.contains("CREATE_USER")) {
      b.put("CREATE_USER", currentUserSupplier.username());
    }
    if (!keys.contains("UPDATE_USER")) {
      b.put("UPDATE_USER", currentUserSupplier.username());
    }
    return b.build();
  }

  private SqlParameterSource extend(SqlParameterSource parameterSource) {
    Set<String> keys = FIterable(((Map<String, String>) extractCaseInsensitiveParameterNames(parameterSource)).keySet())
        .map(AppFunction.toUpper()).asSet();
    MapSqlParameterSource extention = new MapSqlParameterSource();
    if (!keys.contains("CREATE_DATE")) {
      extention.addValue("CREATE_DATE", AppCalendar.now());
    }
    if (!keys.contains("UPDATE_DATE")) {
      extention.addValue("UPDATE_DATE", AppCalendar.now());
    }
    if (!keys.contains("CREATE_USER")) {
      extention.addValue("CREATE_USER", currentUserSupplier.username());
    }
    if (!keys.contains("UPDATE_USER")) {
      extention.addValue("UPDATE_USER", currentUserSupplier.username());
    }
    return new ComposedSqlParameterSource(parameterSource, extention);
  }

  private Map<String, Object>[] extend(Map<String, Object>[] batch) {
    @SuppressWarnings("unchecked")
    Map<String, Object>[] ret = new Map[batch.length];
    int counter = 0;
    for (Map<String, Object> i : batch) {
      ret[counter++] = extend(i);
    }
    return ret;
  }

  private SqlParameterSource[] extend(SqlParameterSource[] batch) {
    SqlParameterSource[] ret = new SqlParameterSource[batch.length];
    int counter = 0;
    for (SqlParameterSource s : batch) {
      ret[counter++] = extend(s);
    }
    return ret;
  }

}
