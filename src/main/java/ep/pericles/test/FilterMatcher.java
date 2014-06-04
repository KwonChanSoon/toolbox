package ep.pericles.test;

import org.hamcrest.*;

import com.googlecode.genericdao.search.*;

public class FilterMatcher extends BaseMatcher<Search> {

  public static Matcher<Search> hasFilterOf(String filterText) {
    return new FilterMatcher(filterText);
  }

  private final String expectedFilter;

  FilterMatcher(String filterText) {
    expectedFilter = filterText;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("Search criteria is not found. Expected: ").appendText(expectedFilter);
  }

  @Override
  public boolean matches(Object o) {
    Search search = (Search) o;
    boolean matched = false;
    for (Filter f : search.getFilters()) {
      if (expectedFilter.equals(f.toString())) {
        matched = true;
        break;
      }
    }
    return matched;
  }

  @Override
  public void describeMismatch(Object item, Description mismatchDescription) {
    mismatchDescription.appendText(item.toString());
  }

}
