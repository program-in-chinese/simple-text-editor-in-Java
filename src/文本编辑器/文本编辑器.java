package 文本编辑器;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.text.DefaultEditorKit;

public class 文本编辑器 extends JFrame {

  private JTextArea 区域 = new JTextArea(20, 120);
  private JFileChooser 对话框 = new JFileChooser(System.getProperty("user.dir"));
  private String 当前文件 = "Untitled";
  private boolean 已改 = false;

  private KeyListener 监听按键 = new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
      已改 = true;
      保存.setEnabled(true);
      另存为.setEnabled(true);
    }
  };
  Action New = new AbstractAction("New", new ImageIcon("Path/image.gif")) {
    @Override
    public void actionPerformed(ActionEvent e) {
      保存旧文件();
      区域.setText("");
      当前文件 = "Untitled";
      setTitle(当前文件);
      已改 = false;
      保存.setEnabled(false);
      另存为.setEnabled(false);
    }
  };
  Action 打开 = new AbstractAction("Open", new ImageIcon("open.gif")) {
    @Override
    public void actionPerformed(ActionEvent e) {
      保存旧文件();
      if (对话框.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        读入文件(对话框.getSelectedFile().getAbsolutePath());
      }
      另存为.setEnabled(true);
    }
  };
  Action 保存 = new AbstractAction("Save", new ImageIcon("save.gif")) {
    @Override
    public void actionPerformed(ActionEvent e) {
      if (!当前文件.equals("Untitled"))
        保存文件(当前文件);
      else
        另存文件为();
    }
  };
  Action 另存为 = new AbstractAction("Save as...") {
    @Override
    public void actionPerformed(ActionEvent e) {
      另存文件为();
    }
  };
  Action 退出 = new AbstractAction("Quit") {
    @Override
    public void actionPerformed(ActionEvent e) {
      保存旧文件();
      System.exit(0);
    }
  };
  ActionMap 操作表 = 区域.getActionMap();
  Action 剪切 = 操作表.get(DefaultEditorKit.cutAction);
  Action 拷贝 = 操作表.get(DefaultEditorKit.copyAction);
  Action 粘贴 = 操作表.get(DefaultEditorKit.pasteAction);

  public 文本编辑器() {
    区域.setFont(new Font("Monospaced", Font.PLAIN, 12));
    JScrollPane 滚动条 = new JScrollPane(区域, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    add(滚动条, BorderLayout.CENTER);

    JMenuBar 菜单栏 = new JMenuBar();
    setJMenuBar(菜单栏);
    JMenu 文件菜单 = new JMenu("File");
    JMenu 编辑菜单 = new JMenu("Edit");
    菜单栏.add(文件菜单);
    菜单栏.add(编辑菜单);

    文件菜单.add(New);
    文件菜单.add(打开);
    文件菜单.add(保存);
    文件菜单.add(退出);
    文件菜单.add(另存为);
    文件菜单.addSeparator();

    for (int i = 0; i < 4; i++)
      文件菜单.getItem(i).setIcon(null);

    编辑菜单.add(剪切);
    编辑菜单.add(拷贝);
    编辑菜单.add(粘贴);

    编辑菜单.getItem(0).setText("Cut out");
    编辑菜单.getItem(1).setText("Copy");
    编辑菜单.getItem(2).setText("Paste");

    JToolBar 工具栏 = new JToolBar();
    add(工具栏, BorderLayout.NORTH);
    工具栏.add(New);
    工具栏.add(打开);
    工具栏.add(保存);
    工具栏.addSeparator();

    JButton 剪切按钮 = 工具栏.add(剪切), 拷贝按钮 = 工具栏.add(拷贝), 粘贴按钮 = 工具栏.add(粘贴);

    剪切按钮.setText(null);
    剪切按钮.setIcon(new ImageIcon("cut.gif"));
    拷贝按钮.setText(null);
    拷贝按钮.setIcon(new ImageIcon("copy.gif"));
    粘贴按钮.setText(null);
    粘贴按钮.setIcon(new ImageIcon("paste.gif"));

    保存.setEnabled(false);
    另存为.setEnabled(false);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    pack();
    区域.addKeyListener(监听按键);
    setTitle(当前文件);
    setVisible(true);
  }

  private void 另存文件为() {
    if (对话框.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
      保存文件(对话框.getSelectedFile().getAbsolutePath());
  }

  private void 保存旧文件() {
    if (已改) {
      if (JOptionPane.showConfirmDialog(this, "Would you like to save " + 当前文件 + " ?",
          "Save", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
        保存文件(当前文件);
    }
  }

  private void 读入文件(String 文件名) {
    try {
      FileReader 读 = new FileReader(文件名);
      区域.read(读, null);
      读.close();
      当前文件 = 文件名;
      setTitle(当前文件);
      已改 = false;
    } catch (IOException e) {
      Toolkit.getDefaultToolkit().beep();
      JOptionPane.showMessageDialog(this, "Editor can't find the file called " + 文件名);
    }
  }

  private void 保存文件(String 文件名) {
    try {
      FileWriter 写 = new FileWriter(文件名);
      区域.write(写);
      写.close();
      当前文件 = 文件名;
      setTitle(当前文件);
      已改 = false;
      保存.setEnabled(false);
    } catch (IOException e) {
    }
  }

  public static void main(String[] arg) {
    new 文本编辑器();
  }
}
