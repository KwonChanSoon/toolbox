package eu.qualityontime.spring.database;

import static eu.qualityontime.AppCollections.IMapExtend;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.*;

import javax.sql.DataSource;

import org.junit.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.KeyHolder;

import eu.qualityontime.*;

public class AuditableJdbcInsertTest {
  private static class SpyInsert extends AuditableJdbcInsert {

    public SpyInsert(DataSource dataSource, UsernameSupplier currentUserSupplier) {
      super(dataSource, currentUserSupplier);
    }

    public SpyInsert(JdbcTemplate jdbcTemplate, UsernameSupplier currentUserSupplier) {
      super(jdbcTemplate, currentUserSupplier);
    }

    Map<String, Object> map_args;
    SqlParameterSource sqlparamSource_arg;

    @Override
    protected int doExecute(Map<String, Object> args) {
      map_args = args;
      return -1;
    }

    @Override
    protected int doExecute(SqlParameterSource parameterSource) {
      sqlparamSource_arg = parameterSource;
      return -1;
    }

    @Override
    protected Number doExecuteAndReturnKey(Map<String, Object> args) {
      map_args = args;
      return -1L;
    }

    @Override
    protected Number doExecuteAndReturnKey(SqlParameterSource parameterSource) {
      sqlparamSource_arg = parameterSource;
      return -1L;
    }

    @Override
    protected KeyHolder doExecuteAndReturnKeyHolder(Map<String, Object> args) {
      map_args = args;
      return null;
    }

    @Override
    protected KeyHolder doExecuteAndReturnKeyHolder(SqlParameterSource parameterSource) {
      sqlparamSource_arg = parameterSource;
      return null;
    }

    Map<String, Object>[] batch_args;

    @Override
    protected int[] doExecuteBatch(Map<String, Object>[] batch) {
      batch_args = batch;
      return null;
    }

    SqlParameterSource[] paramource_batch_args;

    @Override
    protected int[] doExecuteBatch(SqlParameterSource[] batch) {
      paramource_batch_args = batch;
      return null;
    }
  };

  static Date currentDate = AppDate.parse("2014-04-15 15:29");

  @BeforeClass
  public static void beforeClass() {
    AppCalendar.implementation(new AppCalendarBean() {
      @Override
      public Date now() {
        return currentDate;
      }
    });
  }

  Map<String, Object> param = AppCollections.<String, Object> IMap("id", 1L);
  String currentUsername = "test_user";
  UsernameSupplier usernamesupplier = new UsernameSupplier() {
    @Override
    public String username() {
      return currentUsername;
    }
  };
  DataSource ds = mock(DataSource.class);
  SpyInsert insert = new SpyInsert(ds, usernamesupplier);
  MapSqlParameterSource paramSource = new MapSqlParameterSource(param);

  @Test
  public void execute_map() throws Exception {
    insert.execute(param);
    assertMapArgs();
  }

  @Test
  public void execute_map_not_overriding_if_audit_field_given() throws Exception {
    param = IMapExtend(param, "CREATE_USER", "takacsot");
    insert.execute(param);
    assertEquals("takacsot", insert.map_args.get("CREATE_USER"));
  }

  @Test
  public void executeandReturnKey_map() throws Exception {
    insert.executeAndReturnKey(param);
    assertMapArgs();
  }

  @Test
  public void executeandReturnKeyholder_map() throws Exception {
    insert.executeAndReturnKeyHolder(param);
    assertMapArgs();
  }

  private void assertMapArgs() {
    assertMapArgs(insert.map_args);
  }

  private void assertMapArgs(Map<String, Object> args) {
    assertEquals(Long.valueOf(1L), args.get("id"));
    assertEquals("test_user", args.get("CREATE_USER"));
    assertEquals("test_user", args.get("UPDATE_USER"));
    assertNotNull(args.get("CREATE_DATE"));
    assertNotNull(args.get("UPDATE_DATE"));
    assertEquals(currentDate, args.get("CREATE_DATE"));
    assertEquals(currentDate, args.get("UPDATE_DATE"));
  }

  @Test
  public void execute_sql_param_source() throws Exception {
    insert.execute(paramSource);
    assertSqlParamSourceArgs();
  }

  @Test
  public void execute_sqlparamsource_not_extend_when_not_needed() throws Exception {
    paramSource.addValue("CREATE_USER", "takacsot");
    insert.execute(paramSource);
    assertEquals("takacsot", insert.sqlparamSource_arg.getValue("CREATE_USER"));
  }

  @Test
  public void executeAndReturnKey_sql_param_source() throws Exception {
    insert.executeAndReturnKey(paramSource);
    assertSqlParamSourceArgs();
  }

  @Test
  public void executeAndReturnKeyHolder_sql_param_source() throws Exception {
    insert.executeAndReturnKeyHolder(paramSource);
    assertSqlParamSourceArgs();
  }

  private void assertSqlParamSourceArgs() {
    assertSqlParamSourceArgs(insert.sqlparamSource_arg);
  }

  private void assertSqlParamSourceArgs(SqlParameterSource paramSoure) {
    assertEquals(Long.valueOf(1L), paramSoure.getValue("id"));
    assertEquals("test_user", paramSoure.getValue("CREATE_USER"));
    assertEquals("test_user", paramSoure.getValue("UPDATE_USER"));
    assertNotNull(paramSoure.getValue("CREATE_DATE"));
    assertNotNull(paramSoure.getValue("UPDATE_DATE"));
    assertEquals(currentDate, paramSoure.getValue("CREATE_DATE"));
    assertEquals(currentDate, paramSoure.getValue("UPDATE_DATE"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void executeBatch_map() throws Exception {
    insert.executeBatch(new Map[] { param, param, param });
    for (Map<String, Object> m : insert.batch_args) {
      assertMapArgs(m);
    }
  }

  public void executeBatch_paramsource() throws Exception {
    insert.executeBatch(new SqlParameterSource[] { paramSource, paramSource, paramSource });
    for (SqlParameterSource m : insert.paramource_batch_args) {
      assertSqlParamSourceArgs(m);
    }
  }
}
