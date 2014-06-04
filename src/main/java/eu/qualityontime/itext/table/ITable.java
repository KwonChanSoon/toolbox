package eu.qualityontime.itext.table;

import eu.qualityontime.visitor.Visitor;

public interface ITable {

  public void accept(Visitor visitor);
}
