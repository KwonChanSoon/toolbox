package eu.qualityontime.test;

import org.junit.Assume;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * When a methof is annotated with `@IgnoreRest` all the other method is skipped.
 * Like the opposite of `@Ignore`.
 * Issue: not marking skipped test as ignored. (JUnit is not giving the infrastructure for this functionality.
 */
public class IgnoreRestRule extends TestWatcher {

  @Override
  public Statement apply(Statement base, Description description) {
    if (IgnoreRestHelper.isIgnoreable(description)) {
      return new Statement() {
        @Override
        public void evaluate() throws Throwable {
          Assume.assumeFalse("IgnoreRest", true);
        }
      };
    }
    return super.apply(base, description);
  }

}
