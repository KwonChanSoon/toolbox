package ep.pericles.web;

import java.io.StringWriter;

import org.junit.Test;

import com.googlecode.jatl.Html;

/**
 * Learning test
 */
@SuppressWarnings("unused")
public class HtmlBuilderTest {

  @Test
  public void test2() throws Exception {
    final StringWriter sw = new StringWriter();
    Html nested = new Html(sw) {
      {
        h1().text("header");
        endAll();
        //        done();
      }
    };

    //    Html main = new Html(nested, true) {
    Html main = new Html(sw) {
      {
        p().text("hello");
        endAll();
        new Html(this) {
          {
            h2().text("subjeader").endAll().done();
          }
        };
        done();
      }
    };

    //System.out.println(sw);
  }

  @Test
  public void test() throws Exception {
    final StringWriter sw = new StringWriter();
    Html nested = new Html(sw) {
      {
        h1().text("header");
        endAll();
        //        done();
      }
    };

    //    Html main = new Html(nested, true) {
    Html main = new Html(sw) {
      {
        p().text("hello");
        endAll();
        new Html(sw) {
          {
            h2().text("subjeader").endAll().done();
          }
        };
        done();
      }
    };

    //System.out.println(sw);
  }

}
