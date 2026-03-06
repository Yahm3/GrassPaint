package com.example;

import com.environment.WindowTools;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.InputEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JFileChooser;

public class Draw extends JFrame implements ActionListener {
  private int saveCounter;
  private Canvas canvas;
  private JLabel filenameBar, thicknessStat;
  private JFileChooser fileChooser;
  private File file;
  private Color color = Color.WHITE;
  private JButton pencil, eraser, color_picker, gray, red, pink, black, green, white, yellow, blue;
  private JSlider slider;
  private JMenuItem open;

  public Draw() {
    super("GrassPaint");
    // :NOTE: Global Variables initialization
    canvas = new Canvas();
    filenameBar = new JLabel("No file");
    thicknessStat = new JLabel();
    thicknessStat = new JLabel("Size: 0px");

    // :NOTE: Frame stuff
    this.setIconImage(new ImageIcon(getClass().getResource("/icons/logo.png")).getImage());
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setSize(900, 600);
    setEnvironmentalDecoration();
    this.setJMenuBar(menuBar());
    this.setLayout(new BorderLayout());
    this.add(equipmentPanel(), BorderLayout.NORTH);
    this.add(canvas, BorderLayout.CENTER);
    statusBarPanel().setVisible(true);
    this.add(statusBarPanel(), BorderLayout.SOUTH);
    this.add(sizePanel(), BorderLayout.WEST);
    this.setVisible(true);
    System.out.println("[INFO] Draw working...");
  }

  private List<JButton> addBtn_toEquipment() {
    List<JButton> buttons = new ArrayList<>();

    Icon pencilIcon = new ImageIcon(getClass().getResource("/icons/pencil.png"));
    pencil = new JButton(pencilIcon);
    pencil.setMnemonic('P');
    pencil.setToolTipText("ALT + P");
    pencil.setPreferredSize(new Dimension(48, 48));
    pencil.addActionListener(this);
    buttons.add(pencil);

    Icon eraserIcon = new ImageIcon(getClass().getResource("/icons/eraser.png"));
    eraser = new JButton(eraserIcon);
    eraser.setMnemonic('E');
    eraser.setPreferredSize(new Dimension(48, 48));
    eraser.addActionListener(this);
    buttons.add(eraser);

    red = new JButton();
    red.setToolTipText("red");
    red.setPreferredSize(new Dimension(48, 48));
    red.setBackground(Color.RED);
    red.addActionListener(this);
    buttons.add(red);

    pink = new JButton();
    pink.setToolTipText("pink");
    pink.setPreferredSize(new Dimension(48, 48));
    pink.setBackground(Color.PINK);
    pink.addActionListener(this);
    buttons.add(pink);

    black = new JButton();
    black.setToolTipText("black");
    black.setPreferredSize(new Dimension(48, 48));
    black.setBackground(Color.BLACK);
    black.addActionListener(this);
    buttons.add(black);

    green = new JButton();
    green.setToolTipText("green");
    green.setPreferredSize(new Dimension(48, 48));
    green.setBackground(Color.GREEN);
    green.addActionListener(this);
    buttons.add(green);

    white = new JButton();
    white.setToolTipText("white");
    white.setPreferredSize(new Dimension(48, 48));
    white.setBackground(Color.WHITE);
    white.addActionListener(this);
    buttons.add(white);

    yellow = new JButton();
    yellow.setToolTipText("yellow");
    yellow.setPreferredSize(new Dimension(48, 48));
    yellow.setBackground(Color.YELLOW);
    yellow.addActionListener(this);
    buttons.add(yellow);

    blue = new JButton();
    blue.setToolTipText("blue");
    blue.setPreferredSize(new Dimension(48, 48));
    blue.setBackground(Color.BLUE);
    blue.addActionListener(this);
    buttons.add(blue);

    gray = new JButton();
    gray.setToolTipText("gray");
    gray.setPreferredSize(new Dimension(48, 48));
    gray.setBackground(Color.GRAY);
    gray.addActionListener(this);
    buttons.add(gray);

    Icon color_ChooserIcon = new ImageIcon(getClass().getResource("/icons/color-chooser.png"));
    color_picker = new JButton(color_ChooserIcon);
    color_picker.setMnemonic('C');
    color_picker.setPreferredSize(new Dimension(48, 48));
    color_picker.addActionListener(this);
    buttons.add(color_picker);

    System.out.println("[INFO] addBtn_toEquipment working...");
    return buttons;
  }

  ChangeListener thick = new ChangeListener() {
    public void stateChanged(ChangeEvent e) {
      canvas.setThickness(slider.getValue());
      if (slider.getValueIsAdjusting()) {
        thicknessStat.setText(String.format("[size: %s]", slider.getValue()));
        thicknessStat.setVisible(true);
      } else {
        thicknessStat.setVisible(false);
      }
    }
  };

  private JPanel sizePanel() {
    JPanel sizePnl = new JPanel();
    sizePnl.add(new JSeparator(JSeparator.VERTICAL));
    sizePnl.setPreferredSize(new Dimension(25, WindowTools.GetScreenHeight()));
    slider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
    slider.addChangeListener(thick);
    slider
        .setPreferredSize(new Dimension(WindowTools.GetScreenHeight(), (int) (WindowTools.GetScreenHeight() * 0.70)));
    slider.setVisible(true);
    thicknessStat.setText(String.format("[size: %s]", slider.getValue()));
    sizePnl.add(slider);
    System.out.println("[INFO] sizePanel working...");
    return sizePnl;
  }

  public void updateFrameLayout() {
    this.revalidate();
    this.repaint();
  }

  private JPanel statusBarPanel() {
    JPanel statusPnl = new JPanel(new FlowLayout());
    statusPnl.add(filenameBar);
    statusPnl.add(new JSeparator(JSeparator.VERTICAL));
    statusPnl.add(thicknessStat);
    return statusPnl;
  }

  private JPanel equipmentPanel() {
    JPanel equipment = new JPanel();
    for (JButton btns : addBtn_toEquipment()) {
      equipment.add(btns);
    }
    System.out.println("[INFO] EquipmentPanel working...");
    return equipment;
  }

  private void setEnvironmentalDecoration() {
    if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {
      System.setProperty("flatlaf.menuBarEmbedded", "true");
      System.out.println("[SYSTEM] Linux");
    } else {
      System.out.println("[SYSTEM] Windows");
      System.setProperty("flatlaf.menuBarEmbedded", "true");
    }
    System.out.println("[INFO] SetEnvironmentalDecoration working...");
  }

  private JMenuBar menuBar() {
    JMenuBar menuBar = new JMenuBar();
    menuBar.add(settingMenu());
    menuBar.add(fileMenu());
    menuBar.add(editMenu());
    menuBar.add(helpMenu());
    System.out.println("[INFO] MenuBar working...");
    return menuBar;
  }

  private JMenu helpMenu() {
    JMenu helpMenu = new JMenu("Help");

    Icon licenseIcon = new ImageIcon(getClass().getResource("/icons/license.png"));
    JMenuItem license = new JMenuItem("license", licenseIcon);
    license.setAccelerator(KeyStroke.getKeyStroke('L', InputEvent.CTRL_DOWN_MASK));
    license.setActionCommand("license");
    license.addActionListener(this);
    helpMenu.add(license);
    System.out.println("[INFO] HelpMenu working...");
    return helpMenu;
  }

  private JMenu editMenu() {
    JMenu editMenu = new JMenu("Edit");
    Icon undoIcon = new ImageIcon(getClass().getResource("/icons/undo.png"));
    Icon redoIcon = new ImageIcon(getClass().getResource("/icons/redo.png"));

    JMenuItem undo = new JMenuItem("undo", undoIcon);
    undo.setAccelerator(KeyStroke.getKeyStroke('U', InputEvent.CTRL_DOWN_MASK));
    undo.setActionCommand("undo");
    undo.addActionListener(this);
    editMenu.add(undo);

    JMenuItem redo = new JMenuItem("redo", redoIcon);
    redo.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));
    redo.setActionCommand("redo");
    redo.addActionListener(this);
    editMenu.add(redo);
    System.out.println("[INFO] EditMenu working...");
    return editMenu;
  }

  private JMenu settingMenu() {
    JMenu setting = new JMenu("Setting");
    String[] themes = { "Light", "Dark", "Gradient Blue", "Gradient Green", "darkPurple" };

    Icon exitIcon = new ImageIcon(getClass().getResource("/icons/exit.png"));
    JMenuItem exit = new JMenuItem("Exit", exitIcon);
    JMenu theme = new JMenu("Theme");

    for (String theme_name : themes) {
      JMenuItem item = new JMenuItem(theme_name);
      item.addActionListener((e) -> {
        if (theme_name.equalsIgnoreCase("light")) {
          FlatLightLaf.setup();
        } else if (theme_name.equalsIgnoreCase("dark")) {
          FlatDarkLaf.setup();
        } else if (theme_name.equalsIgnoreCase("Gradient Green")) {
          com.formdev.flatlaf.intellijthemes.FlatGradiantoNatureGreenIJTheme.setup();
        } else if (theme_name.equalsIgnoreCase("Gradient Blue")) {
          com.formdev.flatlaf.intellijthemes.FlatGradiantoMidnightBlueIJTheme.setup();
        } else if (theme_name.equalsIgnoreCase("darkPurple")) {
          com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme.setup();
        } else {
          JOptionPane.showMessageDialog(null, "Could not set " + theme_name, "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        SwingUtilities.updateComponentTreeUI(this);
        System.out.println("[INFO] Current theme: " + theme_name);
      });
      theme.add(item);
    }

    exit.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
    exit.setActionCommand("Exit");
    exit.addActionListener(this);
    setting.add(theme);
    setting.add(exit);
    return setting;
  }

  private JMenu fileMenu() {
    Icon saveIcon = new ImageIcon(getClass().getResource("/icons/save.png"));
    Icon openIcon = new ImageIcon(getClass().getResource("/icons/new-window.png"));

    JMenu fileMenu = new JMenu("File");

    open = new JMenuItem("Open File", openIcon);
    open.setAccelerator(KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK));
    open.setActionCommand("Open File");

    open.addActionListener(this);
    fileMenu.add(open);

    JMenuItem saveFile = new JMenuItem("Save File", saveIcon);

    saveFile.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
    saveFile.setActionCommand("Save File");
    saveFile.addActionListener(this);
    fileMenu.add(saveFile);
    System.out.println("[INFO] FileMenu working...");
    return fileMenu;
  }

  private void showLicenseDialog() {
    String mitLicense = """
                MIT License

        Copyright (c) 2025 Yahm3

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in all
        copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
        SOFTWARE.

                """;

    JTextArea textArea = new JTextArea(mitLicense);
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.setCaretPosition(0);

    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(500, 300));

    JOptionPane.showMessageDialog(this, scrollPane, "MIT License", JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("Exit")) {
      System.exit(0);
    } else if (e.getActionCommand().equals("Save File")) {
      System.out.println("Save pressed...");
      if (saveCounter == 0) {
        fileChooser = new JFileChooser();
        int returnVal = fileChooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          file = fileChooser.getSelectedFile();
          saveCounter = 1;
          filenameBar.setText(file.toString());
          canvas.saveFile(file);
        }
      } else {
        filenameBar.setText(file.toString());
        canvas.saveFile(file);
      }
    } else if (e.getSource() == open) {
      if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {
        fileChooser = new JFileChooser("~/");
      } else {
        fileChooser = new JFileChooser("C:\\");
      }
      fileChooser.setFileFilter(FileFilter.png);
      fileChooser = new JFileChooser();
      if (fileChooser.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
        file = fileChooser.getSelectedFile();
        canvas.openFile(file);
        filenameBar.setText(file.toString());
        updateFrameLayout();
      } else {
        System.err.println("[INFO] User cancelled opening file");
      }
    } else if (e.getActionCommand().equals("undo")) {
      canvas.undo();
    } else if (e.getActionCommand().equals("redo")) {
      canvas.redo();
    } else if (e.getActionCommand().equals("license")) {
      showLicenseDialog();
    } else if (e.getSource() == pencil) {
      canvas.pencil();
    } else if (e.getSource() == eraser) {
      canvas.eraserColor();
    } else if (e.getSource() == color_picker) {
      color = JColorChooser.showDialog(canvas, "Pick your color!", color);
      if (color == null) {
        color = (Color.BLACK);
      }
      canvas.chooser(color);
    }

    // :NOTE: Color buttons
    else if (e.getSource() == red) {
      canvas.red();
    } else if (e.getSource() == black) {
      canvas.black();
    } else if (e.getSource() == blue) {
      canvas.blue();
    } else if (e.getSource() == pink) {
      canvas.pink();
    } else if (e.getSource() == green) {
      canvas.green();
    } else if (e.getSource() == yellow) {
      canvas.yellow();
    } else if (e.getSource() == white) {
      canvas.green();
    } else if (e.getSource() == gray) {
      canvas.gray();
    }
  }
}
