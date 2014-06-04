package ep.pericles.sql;

import static ep.pericles.AppCollections.List;

import java.util.List;

import com.google.common.base.*;
import com.google.common.collect.Lists;

public abstract class SqlCondition {
  public static SqlCondition condition(String cond) {
    return condition(new SimpleCondition(cond));
  }

  public static SqlCondition condition(SqlCondition cond) {
    return cond;
  }

  @Override
  public String toString() {
    return getStatement();
  }

  public static SqlCondition OR(String cond1, String cond2) {
    return new OrCondition(new SimpleCondition(cond1), new SimpleCondition(cond2));
  }

  public static SqlCondition AND(String cond1, String cond2) {
    return new AndCondition(new SimpleCondition(cond1), new SimpleCondition(cond2));
  }

  public abstract String getStatement();

  public abstract SqlCondition AND(String cond);

  public abstract SqlCondition AND(SqlCondition cond);

  public abstract SqlCondition OR(String cond);

  public abstract SqlCondition OR(SqlCondition cond);

  public static class SimpleCondition extends SqlCondition {
    private final String condition;

    public SimpleCondition(String condition) {
      this.condition = condition;
    }

    @Override
    public SqlCondition AND(String cond) {
      return AND(new SimpleCondition(cond));
    }

    @Override
    public SqlCondition AND(SqlCondition cond) {
      return new AndCondition(this, cond);
    }

    @Override
    public SqlCondition OR(String cond) {
      return OR(new SimpleCondition(cond));
    }

    @Override
    public SqlCondition OR(SqlCondition cond) {
      return new OrCondition(this, cond);
    }

    @Override
    public String getStatement() {
      return condition;
    }
  }

  abstract static class AggregateCondition extends SqlCondition {
    final List<SqlCondition> conditions = List();

    public abstract String getOp();

    @Override
    public String getStatement() {
      return Joiner.on(" " + getOp() + " ").join(Lists.transform(this.conditions, GetStatementFunction));
    }

  }

  public static class AndCondition extends AggregateCondition {

    public AndCondition(SqlCondition sqlCondition, SqlCondition simpleCondition) {
      this.conditions.add(sqlCondition);
      this.conditions.add(simpleCondition);
    }

    public AndCondition(SqlCondition cond) {
      conditions.add(cond);
    }

    public AndCondition() {
    }

    @Override
    public String getOp() {
      return "AND";
    }

    @Override
    public SqlCondition AND(String cond) {
      return AND(new SimpleCondition(cond));
    }

    @Override
    public SqlCondition AND(SqlCondition cond) {
      conditions.add(cond);
      return this;
    }

    @Override
    public SqlCondition OR(String cond) {
      throw new UnsupportedOperationException(
          "Simple condition cannot be added to AND statement without qualifying precedence: " + cond);
    }

    @Override
    public SqlCondition OR(SqlCondition cond) {
      return new OrCondition(new GroupCondition(this), new GroupCondition(cond));
    }
  }

  public static class OrCondition extends AggregateCondition {
    public OrCondition(SqlCondition sqlCondition, SqlCondition simpleCondition) {
      this.conditions.add(sqlCondition);
      this.conditions.add(simpleCondition);
    }

    @Override
    public String getOp() {
      return "OR";
    }

    @Override
    public SqlCondition AND(String cond) {
      return AND(new SimpleCondition(cond));
    }

    @Override
    public SqlCondition AND(SqlCondition cond) {
      return new AndCondition(new GroupCondition(this), cond);
    }

    @Override
    public SqlCondition OR(String cond) {
      return OR(new SimpleCondition(cond));
    }

    @Override
    public SqlCondition OR(SqlCondition cond) {
      conditions.add(cond);
      return this;
    }
  }

  public static class GroupCondition extends SqlCondition {
    private final SqlCondition condition;

    public GroupCondition(SqlCondition condition) {
      this.condition = condition;
    }

    @Override
    public SqlCondition AND(String cond) {
      return AND(new SimpleCondition(cond));
    }

    @Override
    public SqlCondition AND(SqlCondition cond) {
      return new AndCondition(this, cond);
    }

    @Override
    public SqlCondition OR(String cond) {
      return OR(new SimpleCondition(cond));
    }

    @Override
    public SqlCondition OR(SqlCondition cond) {
      return new OrCondition(this, cond);
    }

    @Override
    public String getStatement() {
      return "(" + condition.getStatement() + ")";
    }
  }

  private static Function<SqlCondition, String> GetStatementFunction = new Function<SqlCondition, String>() {
    public String apply(SqlCondition input) {
      return input.getStatement();
    }
  };

  public static SqlCondition empty() {
    return EMPTY;
  }

  private static SqlCondition EMPTY = new EmptyCondition();

  private static class EmptyCondition extends SqlCondition {

    @Override
    public String getStatement() {
      return "NA";
    }

    @Override
    public SqlCondition AND(String cond) {
      return AND(new SimpleCondition(cond));
    }

    @Override
    public SqlCondition AND(SqlCondition cond) {
      return SqlCondition.condition(cond);
    }

    @Override
    public SqlCondition OR(String cond) {
      return OR(new SimpleCondition(cond));
    }

    @Override
    public SqlCondition OR(SqlCondition cond) {
      return SqlCondition.condition(cond);
    }

  }

  public static String statement(String prefix, SqlCondition cond) {
    return prefix + " " + cond.getStatement();
  }
}
