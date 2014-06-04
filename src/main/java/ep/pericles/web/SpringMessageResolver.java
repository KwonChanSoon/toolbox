package ep.pericles.web;

import static ep.pericles.AppCollections.transformList;
import static ep.pericles.AppObjects.cast;

import java.util.*;

import org.springframework.context.*;
import org.springframework.validation.Errors;

import com.google.common.base.Function;

import ep.pericles.AppPreconditions;
import ep.pericles.session.SessionUtil;

/**
 * Utility which can be used when you cannot rely in Spring MVC error message internationalization.
 * Example: when using redirect and you want to display error messages you could use RedirectAtribute to store.
 * But in certain cases you could not use `<form:errors/>` <code><pre>
 * 
 * @Autowired
 *            private SpringMessageResolver messageResolver;
 *            public String delete(...RedirectAttributes redirect) {
 *            if (result.hasErrors()) {
 *            redirect.addFlashAttribute("errors", messageResolver.errorMessages(result));
 *            return "redirect:/statusreport_profile/list.do";
 *            }
 *            </pre></code> In the JSP you could list errors: <code><pre>
  <c:if test="${not empty errors }">
    <div class="alert alert-error">
    <c:forEach items="${errors}" var="error">
      <c:out value="${error}" />
    </c:forEach>
    </div>
  </c:if>
 </pre><code>
 * 
 */
public class SpringMessageResolver {
  private MessageSource messages;

  public void setMessages(MessageSource messages) {
    this.messages = messages;
  }

  public List<String> messages(List<? extends MessageSourceResolvable> input) {
    AppPreconditions.checkNotNull(input);
    List<MessageSourceResolvable> l = cast(input);
    return transformList(l, new Function<MessageSourceResolvable, String>() {
      public String apply(MessageSourceResolvable input) {
        return messages.getMessage(input, SessionUtil.currentLocale());
      }
    });
  }

  public List<String> errorMessages(Errors result) {
    return messages(result.getAllErrors());
  }

  public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
    return messages.getMessage(code, args, defaultMessage, locale);
  }

  public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
    return messages.getMessage(code, args, locale);
  }

  public String getMessage(String code, Object[] args) throws NoSuchMessageException {
    return messages.getMessage(code, args, SessionUtil.currentLocale());
  }

  public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
    return messages.getMessage(resolvable, locale);
  }
}
