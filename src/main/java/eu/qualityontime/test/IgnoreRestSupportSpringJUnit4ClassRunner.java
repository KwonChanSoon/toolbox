package eu.qualityontime.test;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class IgnoreRestSupportSpringJUnit4ClassRunner extends SpringJUnit4ClassRunner {
  public IgnoreRestSupportSpringJUnit4ClassRunner(Class<?> clazz) throws InitializationError {
    super(clazz);
  }

  @Override
  protected void runChild(final FrameworkMethod method, RunNotifier notifier) {
    IgnoreRest ignoreRestAnnotation = method.getAnnotation(IgnoreRest.class);
    Description description = describeChild(method);
    if (IgnoreRestHelper.hasIgnoreRestAnnotation(description) && null == ignoreRestAnnotation) {
      notifier.fireTestIgnored(description);
    }
    else {
      super.runChild(method, notifier);
    }
  }

}
