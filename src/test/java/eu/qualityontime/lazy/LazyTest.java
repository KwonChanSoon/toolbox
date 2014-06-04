package eu.qualityontime.lazy;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.qualityontime.test.AssertThrowable;
import eu.qualityontime.type.Procedure;

public class LazyTest {

  @Test
  public void not_initialized() {
    final Lazy<Long> ll = Lazy.of();
    assertFalse(ll.isInitialized());
    AssertThrowable.assertThrowable(IllegalStateException.class,
        "Attribute is not initialized. Configure fetching parameters.", new Procedure() {
          public void execute() throws Exception {
            ll.get();
          }
        });
  }

  @Test
  public void initialized() {
    final Lazy<Long> ll = Lazy.of(1L);
    assertTrue(ll.isInitialized());
    assertEquals(Long.valueOf(1L), ll.get());
  }

  @Test
  public void changeInitialized() {
    final Lazy<Long> ll = Lazy.of();
    assertFalse(ll.isInitialized());
    ll.set(1L);
    assertTrue(ll.isInitialized());
    assertEquals(Long.valueOf(1L), ll.get());
  }

  @Test
  public void switchedOffLazyCheck() throws Exception {
    final Lazy<Long> ll = Lazy.of();
    Lazy.withoutCheck(new Procedure() {
      public void execute() throws Exception {
        assertNull(ll.get());
      }
    });
  }
}
