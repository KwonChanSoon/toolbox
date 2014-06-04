package ep.pericles.session;

import java.util.Locale;

public interface ISessionUtilDelegate {

  String currentLangIsoCode();

  void currentLangIsoCode(String lang_iso_code);

  Locale currentLocale();

  void currentLocale(Locale l);

  String urlContext();

  String baseUrl();

  String attr(String operationsSearch);

  void attr(String operationsSearch, String val);

  <T> T fetch(Class<T> klass);

  void push(Object o);

  public boolean isLoggedIn();

  public String currentUsername();
}
