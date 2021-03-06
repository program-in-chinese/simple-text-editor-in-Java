package 文本编辑器;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.text.BadLocationException;

public class 提示框 {

  private JList<String> 列表;
  private JPopupMenu 弹出菜单;
  private String 提示源词;
  private int 文本位置;
  private JTextArea 文本区;

  public 提示框(JTextArea 文本区, int 文本位置, String 提示源词, Point 显示位置) {
    this.文本区 = 文本区;
    弹出菜单 = new JPopupMenu();
    弹出菜单.setOpaque(false);
    弹出菜单.setBorder(null);
    更新(文本位置, 提示源词, 显示位置);
  }

  public void 更新(int 文本位置, String 提示源词, Point 显示位置) {
    String[] 提示列表 = 提示词典.取提示(提示源词);
    if (提示列表 == null || 提示列表.length == 0) {
      return;
    }
    this.文本位置 = 文本位置;
    this.提示源词 = 提示源词;
    弹出菜单.removeAll();
    弹出菜单.add(列表 = 创建提示列表(提示列表), BorderLayout.CENTER);
    弹出菜单.show(文本区, 显示位置.x, 文本区.getBaseline(0, 0) + 显示位置.y);
  }

  private void 隐藏() {
    弹出菜单.setVisible(false);
  }

  private JList<String> 创建提示列表(final String[] 提示列表) {
    JList<String> 列表 = new JList<>(提示列表);
    列表.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
    列表.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    列表.setSelectedIndex(0);
    列表.addKeyListener(new KeyListener() {

      @Override
      public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == KeyEvent.VK_SPACE) {
          if (插入选择文本()) {
            隐藏();
          }
        } else {
          隐藏();
          文本区.requestFocusInWindow();
          文本区.dispatchEvent(e);
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
          下移();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
          上移();
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
          隐藏();
          文本区.requestFocusInWindow();
          文本区.dispatchEvent(e);
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {}
    });
    return 列表;
  }

  private boolean 插入选择文本() {
    final String 选中提示 = 列表.getSelectedValue();
    if (选中提示 != null) {
      try {
        int 当前文本位置 = 文本位置;
        int 提示源词长度 = 提示源词.length();
        当前文本位置 -= 提示源词长度;
        文本区.getDocument().remove(当前文本位置, 提示源词长度);
        文本区.getDocument().insertString(当前文本位置, 选中提示, null);
        return true;
      } catch (BadLocationException e1) {
        e1.printStackTrace();
      }
    }
    return false;
  }

  private void 上移() {
    列表.setSelectedIndex(Math.min(列表.getSelectedIndex() - 1, 0));
  }

  private void 下移() {
    列表.setSelectedIndex(Math.min(列表.getSelectedIndex() + 1, 列表.getModel().getSize() - 1));
  }
}
