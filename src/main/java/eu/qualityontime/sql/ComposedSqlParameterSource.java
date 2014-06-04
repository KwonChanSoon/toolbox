package eu.qualityontime.sql;

import java.util.Collection;

import org.springframework.jdbc.core.namedparam.*;

import com.google.common.collect.ImmutableList;

public class ComposedSqlParameterSource extends AbstractSqlParameterSource {
  private final Collection<SqlParameterSource> sources;

  public ComposedSqlParameterSource(Collection<SqlParameterSource> sources) {
    super();
    this.sources = sources;
  }

  public ComposedSqlParameterSource(SqlParameterSource... sources) {
    this(ImmutableList.copyOf(sources));
  }

  @Override
  public boolean hasValue(String paramName) {
    for (SqlParameterSource s : sources) {
      if (s.hasValue(paramName)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Object getValue(String paramName) throws IllegalArgumentException {
    for (SqlParameterSource s : sources) {
      if (s.hasValue(paramName)) {
        return s.getValue(paramName);
      }
    }
    throw new IllegalArgumentException("No value registered for key '" + paramName + "'");
  }

}
