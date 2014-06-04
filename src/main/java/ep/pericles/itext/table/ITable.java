package ep.pericles.itext.table;

import ep.pericles.visitor.Visitor;

public interface ITable {

  public void accept(Visitor visitor);
}
