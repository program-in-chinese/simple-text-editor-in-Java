package 文本编辑器;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;

// 英文输入至少一个匹配词典的字母后显示提示框, 上下光标选中中文后空格键自动补全
// 弹出提示框后, 如果继续键入, 提示框隐藏后, 根据新键入继续提示
// TODO: 如果退格删除, 应继续按照余下内容提示
// TODO: 如开启了其他中文输入法, 某些情况下会互相干扰

public class 测试 {

  private 提示框 提示;
  private JTextArea 文本区;

  private void 随后显示提示() {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        显示提示();
      }

    });
  }

  private void 显示提示() {
    final int 文本位置 = 文本区.getCaretPosition();
    Point 界面位置;
    try {
      界面位置 = 文本区.modelToView(文本位置).getLocation();
    } catch (BadLocationException e2) {
      e2.printStackTrace();
      return;
    }

    final String 提示源词 = 取提示源词(文本位置);
    if (提示源词 == null) {
      return;
    }

    if (提示 == null) {
      提示 = new 提示框(文本区, 文本位置, 提示源词, 界面位置);
    } else {
      提示.更新(文本位置, 提示源词, 界面位置);
    }
  }

  private String 取提示源词(int 文本位置) {
    String 所有文本 = 文本区.getText();
    int 起始 = 寻找上一处空白位置(所有文本, 文本位置);
    return 起始 < 文本位置 ? 所有文本.substring(起始, 文本位置) : null;
  }

  private int 寻找上一处空白位置(String 所有文本, int 文本位置) {
    int 起始 = Math.max(0, 文本位置 - 1);
    while (起始 > 0) {
      if (!Character.isWhitespace(所有文本.charAt(起始))) {
        起始--;
      } else {
        起始++;
        break;
      }
    }
    return 起始;
  }

  private void 初始化() {
    final JFrame 框 = new JFrame("测试框");
    框.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JPanel 面板 = new JPanel(new BorderLayout());
    文本区 = new JTextArea(24, 80);
    文本区.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {
        随后显示提示();
      }

      @Override
      public void keyReleased(KeyEvent e) {}

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
          随后显示提示();
        }
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
