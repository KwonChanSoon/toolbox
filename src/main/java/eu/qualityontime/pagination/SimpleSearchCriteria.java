package eu.qualityontime.pagination;

public class SimpleSearchCriteria extends APagingSearchCriteria {
  private String criteria;

  public String getCriteria() {
    return criteria;
  }

  public void setCriteria(String criteria) {
    this.criteria = criteria;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((criteria == null) ? 0 : criteria.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SimpleSearchCriteria other = (SimpleSearchCriteria) obj;
    if (criteria == null) {
      if (other.criteria != null)
        return false;
    }
    else if (!criteria.equals(other.criteria))
      return false;
    return true;
  }

}
