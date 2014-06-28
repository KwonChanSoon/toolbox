package eu.qualityontime.test;

import java.lang.reflect.Method;

import org.junit.Assume;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * When a method is annotated with `@IgnoreRest` all the other method is skipped.
 * Like the opposite of `@Ignore`.
 * Issue: not marking skipped test as ignored. (JUnit is not giving the infrastructure for this functionality.)
 */
public class IgnoreRestRule extends TestWatcher {

  @Override
  public Statement apply(Statement base, Description description) {
    if (isIgnorable(description)) {
      return new Statement() {
        @Override
        public void evaluate() throws Throwable {
          Assume.assumeFalse("IgnoreRest", true);
        }
      };
    }
    return super.apply(base, description);
  }

  private boolean isIgnorable(Description description) {
    return hasIgnoreRestAnnotation(description) && null == description.getAnnotation(IgnoreRest.class);
  }

  private boolean hasIgnoreRestAnnotation(Description description) {
    Class<?> c = description.getTestClass();
    for (Method m : c.getDeclaredMethods()) {
      if (null != m.getAnnotation(IgnoreRest.class)) {
        return true;
      }
    }
    return false;
  }
}
