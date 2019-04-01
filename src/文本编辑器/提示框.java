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
import javax.swing.SwingUtilities;
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
    this.文本位置 = 文本位置;
    this.提示源词 = 提示源词;
    弹出菜单.removeAll();
    String[] 提示列表 = 提示词典.取提示(提示源词);
    if (提示列表 == null || 提示列表.length == 0) {
      return;
    }
    弹出菜单.add(列表 = 创建提示列表(提示列表), BorderLayout.CENTER);
    弹出菜单.show(文本区, 显示位置.x, 文本区.getBaseline(0, 0) + 显示位置.y);
  }

  public void 隐藏() {
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
        // TODO Auto-generated method stub

      }

      @Override
      public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

      }
    });
    return 列表;
  }

  public boolean 插入选择文本() {
    if (列表.getSelectedValue() != null) {
      try {
        final String 选中提示 = 列表.getSelectedValue();
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

  public void 上移() {
    int 序号 = Math.min(列表.getSelectedIndex() - 1, 0);
    选择序号(序号);
  }

  public void 下移() {
    int 序号 = Math.min(列表.getSelectedIndex() + 1, 列表.getModel().getSize() - 1);
    选择序号(序号);
  }

  private void 选择序号(int index) {
    列表.setSelectedIndex(index);
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        文本区.setCaretPosition(文本位置);
      };
    });
  }
}
