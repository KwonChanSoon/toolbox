package ep.pericles.session;

import java.util.Locale;

public class SessionUtil {
  static ISessionUtilDelegate delegate;

  public void setDelegate(ISessionUtilDelegate d) {
    delegate = d;
  }

  public static void delegate(ISessionUtilDelegate d) {
    delegate = d;
  }

  public static ISessionUtilDelegate delegate() {
    return delegate;
  }

  public static String currentLang() {
    return delegate.currentLangIsoCode();
  }

  public static void currentLang(String lang_iso_code) {
    delegate.currentLangIsoCode(lang_iso_code);
  }

  public static Locale currentLocale() {
    return delegate.currentLocale();
  }

  public static void currentLocale(Locale locale) {
    delegate.currentLocale(locale);
  }

  public static String urlContext() {
    return delegate.urlContext();
  }

  public static String urlContext(String urlAction) {
    return delegate.urlContext() + urlAction;
  }

  public static String baseUrl() {
    return delegate.baseUrl();
  }

  public static String attr(String operationsSearch) {
    return delegate.attr(operationsSearch);
  }

  public static void attr(String operationsSearch, String val) {
    delegate.attr(operationsSearch, val);
  }

  public static <T> T fetch(Class<T> klass) {
    return delegate.fetch(klass);
  }

  public static void push(Object o) {
    delegate.push(o);
  }

  public static String currentUsername() {
    return delegate.currentUsername();
  }

  public static boolean isLoggedIn() {
    return delegate.isLoggedIn();
  }
}
