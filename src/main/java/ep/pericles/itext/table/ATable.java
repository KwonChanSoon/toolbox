package ep.pericles.itext.table;

import ep.pericles.visitor.Visitor;

public abstract class ATable implements ITable {
  private String comment;

  @Override
  public void accept(Visitor visitor) {
    visitor.visit(this);
  }

  public abstract void done();

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }
}
