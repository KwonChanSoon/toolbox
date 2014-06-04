package ep.pericles;

import static com.google.common.collect.Iterables.*;
import static org.apache.commons.lang3.StringUtils.split;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.*;
import com.google.common.collect.*;

public abstract class Humanizer {

  public abstract String apply(String in);

  protected Humanizer() {
  }

  public static Humanizer of() {
    return base;
  }

  public static String humanize(String in) {
    return base.apply(in);
  }

  private static Humanizer base = new BaseHumanizer();

  private static class BaseHumanizer extends Humanizer {

    @Override
    public String apply(String in) {
      if (StringUtils.isBlank(in)) {
        return in;
      }
      Iterable<String> inp = ImmutableList.of(in);
      inp = splitByUnderline(inp);
      inp = splitByCapitalization(inp);

      inp = Iterables.filter(inp, new Predicate<String>() {
        public boolean apply(String input) {
          return !Strings.isNullOrEmpty(input);
        }
      });
      return Joiner.on(" ").skipNulls().join(inp);
    }

    private Iterable<String> splitByUnderline(Iterable<String> inp) {
      return concat(transform(inp, new Function<String, Iterable<String>>() {
        public Iterable<String> apply(String input) {
          return ImmutableList.copyOf(split(input.replaceAll("\\_", " ")));
        }
      }));
    }

    private Iterable<String> splitByCapitalization(Iterable<String> inp) {
      return concat(transform(inp, new Function<String, Iterable<String>>() {
        public Iterable<String> apply(String input) {
          return ImmutableList.copyOf(split(splitCamelCase(input)));
        }
      }));
    }

  }

  static String splitCamelCase(String s) {
    return s.replaceAll(
        String.format("%s|%s|%s",
            "(?<=[A-Z])(?=[A-Z][a-z])",
            "(?<=[^A-Z])(?=[A-Z])",
            "(?<=[A-Za-z])(?=[^A-Za-z])"
            ),
        " "
        );
  }
}
