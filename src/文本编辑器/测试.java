package 文本编辑器;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;

// 英文输入至少一个匹配词典的字母后显示提示框, 上下光标选中中文后空格键自动补全
// 弹出提示框后, 如果继续键入, 提示框隐藏后, 根据新键入继续提示
public class 测试 {

  private static final HashMap<String, String[]> 提示词典 = new HashMap<>();
  static {
    提示词典.put("xj", new String[] {"新建"});
    提示词典.put("dk", new String[] {"打开"});
    提示词典.put("bc", new String[] {"保存"});
    提示词典.put("jq", new String[] {"剪切"});
    提示词典.put("fz", new String[] {"复制"});
    提示词典.put("nt", new String[] {"粘贴"});
    提示词典.put("zt", new String[] {"粘贴"});
    提示词典.put("tc", new String[] {"退出"});
    提示词典.put("j", new String[] {"新建", "剪切"});
    提示词典.put("t", new String[] {"退出", "粘贴"});
  }

  public class 提示框 {
    private JList<String> 列表;
    private JPopupMenu 弹出菜单;
    private String 提示源词;
    private final int 文本位置;

    public 提示框(JTextArea 文本区, int 文本位置, String 提示源词, Point 显示位置) {
      this.文本位置 = 文本位置;
      this.提示源词 = 提示源词;
      弹出菜单 = new JPopupMenu();
      弹出菜单.removeAll();
      弹出菜单.setOpaque(false);
      弹出菜单.setBorder(null);
      String[] 提示列表 = 提示词典.get(提示源词);
      if (提示列表 == null || 提示列表.length == 0) {
        return;
      }
      弹出菜单.add(列表 = 创建提示列表(提示列表), BorderLayout.CENTER);
      弹出菜单.show(文本区, 显示位置.x, 文本区.getBaseline(0, 0) + 显示位置.y);
    }

    public void 隐藏() {
      弹出菜单.setVisible(false);
      if (提示 == this) {
        提示 = null;
      }
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
            if (提示 != null) {
              if (提示.插入选择文本()) {
                隐藏提示();
              }
            }
          } else {
            隐藏提示();
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
      final int 文本位置 = 文本区.getCaretPosition();
      列表.setSelectedIndex(index);
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          文本区.setCaretPosition(文本位置);
        };
      });
    }
  }

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
    提示 = new 提示框(文本区, 文本位置, 提示源词, 界面位置);
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
