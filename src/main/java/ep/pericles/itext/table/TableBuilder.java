package ep.pericles.itext.table;

import java.lang.reflect.InvocationTargetException;
import java.util.Stack;

import org.apache.commons.beanutils.BeanUtils;

public class TableBuilder {
  private Stack<ITable> stack = new Stack<ITable>();

  public TableBuilder getSelf() {
    return this;
  }

  public TableBuilder() {
    stack.push(new TTable());
  }

  public TableBuilder attr(String property, Object val) {
    ITable subject = stack.peek();
    setProperty(subject, property, val);
    return getSelf();
  }

  private void setProperty(ITable subject, String property, Object val) {
    try {
      BeanUtils.setProperty(subject, property, val);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  public TTable getTable() {
    return (TTable) stack.get(0);
  }

  public TableBuilder newRow() {
    return newRow(new TRow());
  }

  public TableBuilder newRow(TRow r) {
    if (stack.peek() instanceof TRow) {
      endRow();
    }
    setProperty(stack.peek(), "row", r);
    stack.push(r);
    return getSelf();
  }

  public TableBuilder newCell() {
    return newCell(new TCell());
  }

  public TableBuilder newCell(TCell c) {
    if (stack.peek() instanceof TCell) {
      endCell();
    }
    setProperty(stack.peek(), "cell", c);
    stack.push(c);
    return getSelf();
  }

  public TableBuilder endCell() {
    while (stack.peek() instanceof TCell) {
      stack.pop();
    }
    return getSelf();
  }

  public TableBuilder text(String text) {
    setProperty(stack.peek(), "phrase", new TPhrase(text));
    return getSelf();
  }

  public TableBuilder text(String text, String font) {
    setProperty(stack.peek(), "phrase", new TPhrase(text, font));
    return getSelf();
  }

  public TableBuilder newHeaderCell() {
    if (stack.peek() instanceof TCell) {
      endCell();
    }
    THeaderCell r = new THeaderCell();
    setProperty(stack.peek(), "cell", r);
    stack.push(r);
    return getSelf();
  }

  @Override
  public String toString() {
    String s = "[\n";
    for (ITable e : stack) {
      s += e + "\n";
    }
    s += "]";
    return s;
  }

  public TableBuilder endRow() {
    endCell();
    while (stack.peek() instanceof TRow) {
      stack.pop();
    }
    return getSelf();
  }

  public TableBuilder addTable(TTable table) {
    attr("table", table);
    return getSelf();
  }

  public TableBuilder addTable(TableBuilder table) {
    return this.addTable(table.getTable());
  }

  public TableBuilder disableBorderSide(Integer... disableds) {
    attr("disabledBordersSide", disableds);
    return getSelf();
  }

  public TableBuilder setWidths(int[] widths) {
    attr("widths", widths);
    return getSelf();
  }

  public TableBuilder comment(String comment) {
    attr("comment", comment);
    return getSelf();
  }

}
