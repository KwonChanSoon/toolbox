package eu.qualityontime.swing;

import java.awt.event.*;

import javax.swing.*;

public class AppSwing {
  public static void addDisposeOnEscapeListener(final JDialog dialog) {
    ActionListener escListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dialog.dispose();
      }
    };
    dialog.getRootPane().registerKeyboardAction(escListener,
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_IN_FOCUSED_WINDOW);

  }

  public static void addHideOnEscapeListener(final JDialog dialog) {
    ActionListener escListener = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dialog.setVisible(false);
      }
    };
    dialog.getRootPane().registerKeyboardAction(escListener,
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

}
