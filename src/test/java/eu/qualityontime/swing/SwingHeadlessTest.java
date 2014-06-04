package eu.qualityontime.swing;

import static org.mockito.Mockito.mock;

import javax.swing.*;

import org.junit.*;

/**
 * Testing headless option as it is not needed to be set for hudson build.
 * http://www.oracle.com/technetwork/articles/javase/headless-136834.html
 */
public class SwingHeadlessTest {
  //  static{
  //    System.setProperty("java.awt.headless", "true");
  //  }
  @Test
  @Ignore("neither setProperty neither command line headless option is working")
  public void java_awt_headless() {
    JDialog d = new JDialog();
    d.add(new JButton());
  }

  @Test
  public void mocked_out_gui_comp() throws Exception {
    //this solution seems to be working on non-gui server side
    JDialog d = mock(JDialog.class);
    JButton b = mock(JButton.class);
    d.add(b);
  }
}
