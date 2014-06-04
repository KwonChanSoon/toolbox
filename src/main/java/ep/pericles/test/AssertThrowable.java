package ep.pericles.test;

import org.junit.Assert;
import org.slf4j.*;

import com.google.common.base.Throwables;

import ep.pericles.type.Procedure;

public class AssertThrowable {
  private final static Logger log = LoggerFactory.getLogger(AssertThrowable.class);

  public static void assertThrowable(Class<?> throwable, String expectedMessage, Procedure closure) {
    boolean wasException = false;
    try {
      closure.execute();
    } catch (AssertionError ae) {
      wasException = handleException(throwable, expectedMessage, ae);
    } catch (Exception e) {
      wasException = handleException(throwable, expectedMessage, e);
    }
    if (!wasException) {
      throw new AssertionError("Code block MUST throw exception!");
    }
  }

  private static boolean handleException(Class<?> throwable, String expectedMessage, Throwable e) throws AssertionError {
    boolean wasException;
    wasException = true;
    if (!throwable.isAssignableFrom(e.getClass())) {
      log.error("{}", e);
      throw new AssertionError("Unexpected exception type " + e.getClass());
    }
    else {
      if (null != expectedMessage) {
        if (!e.getMessage().contains(expectedMessage)) {
          log.error("{}", e);
          Assert.fail("Error messages are different. expected:<" + expectedMessage + "> but was:<" + e.getMessage() + ">");
        }
      }
    }
    return wasException;
  }

  public static void assertThrowable(Class<?> throwable, Procedure closure) {
    assertThrowable(throwable, null, closure);
  }

  public static void assertThrowable(String expectedMessage, Procedure closure) {
    assertThrowable(Throwable.class, expectedMessage, closure);
  }

  public static void logAndThrow(Procedure closure) {
    try {
      closure.execute();
    } catch (Exception e) {
      log.error("{}", e);
      throw Throwables.propagate(e);
    }
  }
}
