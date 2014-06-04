package eu.qualityontime.clipboard;

import static eu.qualityontime.clipboard.Clipboard.EItemCategory.INTERPRETER;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import com.google.common.collect.ImmutableList;

public class ClipboardTest {
  Clipboard c;

  @Before
  public void setUp() {
    c = new Clipboard();
  }

  @Test
  public void test() {
    c.addItem(INTERPRETER, LongWrap.of(1L));
    assertNotNull(c.getItems(INTERPRETER));
    assertEquals(LongWrap.of(1L), ImmutableList.copyOf(c.get(INTERPRETER)).get(0));
  }

  @Test
  public void remove() {
    c.add(INTERPRETER, LongWrap.of(1));
    c.remove(INTERPRETER, LongWrap.of(1));
    assertEquals(0, c.get(INTERPRETER).size());
  }

  @Test
  public void removeItem() {
    c.add(INTERPRETER, LongWrap.of(1));
    Long itemId = ImmutableList.copyOf(c.getItems(INTERPRETER)).get(0)._1();
    c.removeItem(itemId);
    assertEquals(0, c.get(INTERPRETER).size());
  }

  @Test
  public void genericList() {
    c.addItem(INTERPRETER, LongWrap.of(1L));
    List<LongWrap> res = c.get(INTERPRETER, LongWrap.class);
    assertEquals(LongWrap.of(1L).value, res.get(0).value);
  }

  private static class LongWrap implements Clipboardable {
    Long value;

    public static LongWrap of(long l) {
      LongWrap lw = new LongWrap();
      lw.value = l;
      return lw;
    }

    @Override
    public boolean equals(Object obj) {
      LongWrap llw = (LongWrap) obj;
      return value.equals(llw.value);
    }

    @Override
    public String toString() {
      return "LW(" + value + ")";
    }
  }

  @Test
  public void refreshed() throws Exception {
    long now = System.currentTimeMillis();
    Thread.sleep(100);
    c.add(INTERPRETER, LongWrap.of(1));
    Collection<?> res = c.getItems(INTERPRETER, now);
    assertEquals(1, res.size());
    now = System.currentTimeMillis();
    Thread.sleep(100);
    res = c.getItems(INTERPRETER, now);
    assertEquals(0, res.size());
    now = System.currentTimeMillis();
    Thread.sleep(100);
    c.add(INTERPRETER, LongWrap.of(2));
    res = c.getItems(INTERPRETER, now);
    assertEquals(2, res.size());
  }
}
