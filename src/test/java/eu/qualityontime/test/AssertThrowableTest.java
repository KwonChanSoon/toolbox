package eu.qualityontime.test;

import java.io.FileNotFoundException;

import org.junit.*;

import eu.qualityontime.type.Procedure;

public class AssertThrowableTest {

  @Test
  public void test() {
    AssertThrowable.assertThrowable(Exception.class, "aaa", new Procedure() {
      public void execute() throws Exception {
        throw new FileNotFoundException("aaa");
      }
    });
  }

  @Test(expected = AssertionError.class)
  public void not_proper_exception() {
    AssertThrowable.assertThrowable(RuntimeException.class, "aaa", new Procedure() {
      public void execute() throws Exception {
        throw new FileNotFoundException("aaa");
      }
    });
  }

  @Test(expected = AssertionError.class)
  public void not_proper_message() {
    AssertThrowable.assertThrowable(Exception.class, "bbb", new Procedure() {
      public void execute() throws Exception {
        throw new FileNotFoundException("aaa");
      }
    });
  }

  @Test(expected = AssertionError.class)
  public void exception_is_must() {
    AssertThrowable.assertThrowable(Exception.class, new Procedure() {
      public void execute() throws Exception {
      }
    });
    Assert.fail("Exception is a must in these blocks");
  }

  @Test
  public void verifyingAssertionError() {
    AssertThrowable.assertThrowable(AssertionError.class, new Procedure() {
      public void execute() throws Exception {
        Assert.fail();
      }
    });
  }

}
