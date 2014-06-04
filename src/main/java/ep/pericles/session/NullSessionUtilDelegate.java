package ep.pericles.session;

import java.util.Locale;

/**
 * Defailtl implementation. Role as an adapter for extension.
 */
public class NullSessionUtilDelegate implements ISessionUtilDelegate {

  @Override
  public String currentLangIsoCode() {
    return null;
  }

  @Override
  public void currentLangIsoCode(String lang_iso_code) {
  }

  @Override
  public Locale currentLocale() {
    return null;
  }

  @Override
  public void currentLocale(Locale locale) {
  }

  @Override
  public String urlContext() {
    return null;
  }

  @Override
  public String baseUrl() {
    return null;
  }

  @Override
  public String attr(String operationsSearch) {
    return null;
  }

  @Override
  public void attr(String operationsSearch, String val) {

  }

  @Override
  public <T> T fetch(Class<T> klass) {
    return null;
  }

  @Override
  public void push(Object o) {

  }

  @Override
  public boolean isLoggedIn() {
    return false;
  }

  @Override
  public String currentUsername() {
    return null;
  }
}
