package 文本编辑器;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

public class 文本编辑器 extends JFrame {

  private static final String 默认文件名 = "无名";

  private JTextArea 文本区域 = new JTextArea(20, 120);
  private JTextArea 命令文本区 = new JTextArea(5, 20);
  private JFileChooser 对话框 = new JFileChooser(System.getProperty("user.dir"));
  private String 当前文件 = 默认文件名;
  private String 当前命令 = "";
  private boolean 已改 = false;

  private KeyListener 监听文本输入 = new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
      已改 = true;
    }
  };

  private KeyListener 命令监听 = new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
      String 此行 = "";
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        int 行数 = 命令文本区.getLineCount();
        if (行数 == 1) {
          此行 = 命令文本区.getText();
        } else {
          int 上一行末位置 = 0;
          try {
            上一行末位置 = 命令文本区.getLineEndOffset(行数 - 2);
          } catch (BadLocationException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
          此行 = 命令文本区.getText().substring(上一行末位置);
        }
        当前命令 = 此行;
        执行(当前命令);
      }
    }
  };

  private void 执行(String 当前命令) {
    switch (当前命令) {
      case "新建":
        新建操作();
        break;
      case "打开":
        打开操作();
        break;
      case "保存":
        保存操作();
        break;
      case "另存":
        另存文件为();
        break;
      case "退出":
        退出操作();
        break;
      case "剪切":
        文本区域.cut();
        break;
      case "复制":
        文本区域.copy();
        break;
      case "粘贴":
        文本区域.paste();
        break;
      default:
        // TODO: 提示无此命令或显示帮助
        break;
    }
  }

  private void 新建操作() {
    保存旧文件();
    文本区域.setText("");
    当前文件 = 默认文件名;
    setTitle(当前文件);
    已改 = false;
  }

  private void 打开操作() {
    保存旧文件();
    if (对话框.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      读入文件(对话框.getSelectedFile().getAbsolutePath());
    }
  }

  private void 保存操作() {
    if (!当前文件.equals(默认文件名))
      保存文件(当前文件);
    else
      另存文件为();
  }

  private void 退出操作() {
    保存旧文件();
    System.exit(0);
  }

  public 文本编辑器() {
    文本区域.setFont(new Font("Monospaced", Font.PLAIN, 12));
    JScrollPane 文本区 = new JScrollPane(文本区域, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    add(文本区, BorderLayout.CENTER);

    命令文本区.setFont(new Font("Monospaced", Font.PLAIN, 12));
    JScrollPane 命令区 = new JScrollPane(命令文本区, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    add(命令区, BorderLayout.EAST);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    pack();
    文本区域.addKeyListener(监听文本输入);
    命令文本区.addKeyListener(命令监听);
    setTitle(当前文件);
    setVisible(true);
  }

  private void 另存文件为() {
    if (对话框.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
      保存文件(对话框.getSelectedFile().getAbsolutePath());
  }

  private void 保存旧文件() {
    if (已改) {
      if (JOptionPane.showConfirmDialog(this, "保存 " + 当前文件 + " 吗?", "保存",
          JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        保存文件(当前文件);
    }
  }

  private void 读入文件(String 文件名) {
    try {
      FileReader 读 = new FileReader(文件名);
      文本区域.read(读, null);
      读.close();
      当前文件 = 文件名;
      setTitle(当前文件);
      已改 = false;
    } catch (IOException e) {
      Toolkit.getDefaultToolkit().beep();
      JOptionPane.showMessageDialog(this, "找不到 " + 文件名);
    }
  }

  private void 保存文件(String 文件名) {
    try {
      FileWriter 写 = new FileWriter(文件名);
      文本区域.write(写);
      写.close();
      当前文件 = 文件名;
      setTitle(当前文件);
      已改 = false;
    } catch (IOException e) {
    }
  }

  public static void main(String[] arg) {
    new 文本编辑器();
  }
}
