package eu.qualityontime.equivalencerelation;

import java.util.*;

import com.google.common.base.Joiner;

public abstract class ReportingEquivalenceRelation implements EquivalenceRelation {
  final List<String> messages = new ArrayList<String>();
  int indent = -1;
  static String END_MARKER = "<!--diff -->";
  static String INDENT = "   ";

  public ReportingEquivalenceRelation() {
  }

  public ReportingEquivalenceRelation(String title) {
    addMessage(title);
  }

  public boolean areEquals(Object o1, Object o2) {
    indent();
    boolean res = _areEquals(o1, o2);
    unindent();
    if (!res && !alreadyEndMarked()) {
      addMessage(END_MARKER);
    }
    return res;
  }

  private boolean alreadyEndMarked() {
    return messages.size() > 0 && messages.get(messages.size() - 1).endsWith(END_MARKER);
  }

  public abstract boolean _areEquals(Object o1, Object o2);

  public List<String> getMessages() {
    return messages;
  }

  public String getMessage() {
    return Joiner.on("\n").join(messages);
  }

  public void addMessage(String s) {
    messages.add(indentation() + s);
  }

  private String indentation() {
    String res = "";
    for (int i = 0; i < indent; i++) {
      res += INDENT;
    }
    return res;
  }

  public void indent() {
    indent++;
  }

  public void unindent() {
    indent--;
  }
}
