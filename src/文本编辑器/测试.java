package 文本编辑器;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;

// 英文输入至少一个匹配词典的字母后显示提示框, 上下光标选中中文后空格键自动补全
// 弹出提示框后, 如果继续键入, 提示框隐藏后, 根据新键入继续提示
public class 测试 {

  private 提示框 提示;
  private JTextArea 文本区;

  protected void 随后显示提示() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        显示提示();
      }

    });
  }

  protected void 显示提示() {
    隐藏提示();
    final int 文本位置 = 文本区.getCaretPosition();
    Point 界面位置;
    try {
      界面位置 = 文本区.modelToView(文本位置).getLocation();
    } catch (BadLocationException e2) {
      e2.printStackTrace();
      return;
    }
    String 所有文本 = 文本区.getText();
    int 起始 = Math.max(0, 文本位置 - 1);
    while (起始 > 0) {
      if (!Character.isWhitespace(所有文本.charAt(起始))) {
        起始--;
      } else {
        起始++;
        break;
      }
    }
    if (起始 > 文本位置) {
      return;
    }
    final String 提示源词 = 所有文本.substring(起始, 文本位置);

    if (提示 == null) {
      提示 = new 提示框(文本区, 文本位置, 提示源词, 界面位置);
    } else {
      提示.更新(文本位置, 提示源词, 界面位置);
    }
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        文本区.requestFocusInWindow();
      }
    });
  }

  private void 隐藏提示() {
    if (提示 != null) {
      提示.隐藏();
    }
  }

  protected void 初始化() {
    final JFrame 框 = new JFrame();
    框.setTitle("测试框");
    框.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel 面板 = new JPanel(new BorderLayout());
    文本区 = new JTextArea(24, 80);
    文本区.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    文本区.addKeyListener(new KeyListener() {

      @Override
      public void keyReleased(KeyEvent e) {
        char 键入字符 = e.getKeyChar();
        if (e.getKeyCode() == KeyEvent.VK_DOWN && 提示 != null) {
          提示.下移();
        } else if (e.getKeyCode() == KeyEvent.VK_UP && 提示 != null) {
          提示.上移();
        } else if (Character.isLetterOrDigit(键入字符)) {
          随后显示提示();
        } else if (Character.isWhitespace(键入字符)) {
          隐藏提示();
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {

      }

      @Override
      public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

      }
    });
    面板.add(文本区, BorderLayout.CENTER);
    框.add(面板);
    框.pack();
    框.setVisible(true);
  }

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
    SwingUtilities.invokeLater(new Runnable() {

      @Override
      public void run() {
        new 测试().初始化();
      }
    });
  }

}
