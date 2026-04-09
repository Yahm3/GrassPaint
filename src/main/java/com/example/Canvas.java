package com.example;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Canvas extends JPanel {
  private int X1, Y1, X2, Y2;
  private Graphics2D g;
  private Image img, undoTemp, redoTemp;
  private final Custom_Stack<Image> undoStack = new Custom_Stack<>(10);
  private final Custom_Stack<Image> redoStack = new Custom_Stack<>(10);
  private MouseListener listener;
  private MouseMotionAdapter motion;
  private boolean isDarkTheme = false;
  public final static Color LIGHT = new Color(255, 255, 255);
  public final static Color DARK_BG = new Color(30, 30, 30);
  private Color canvasBackground = Color.WHITE;

  private Color currentColor = Color.BLACK;

  public Canvas() {
    defaultListener();
    this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    this.setPreferredSize(new Dimension(900, 600));
    System.out.println("[INFO] Canvas working...");
  }

  public void updateTheme(boolean dark) {
    this.isDarkTheme = dark;
    Color oldBg = canvasBackground;
    Color newBg = dark ? new Color(45, 45, 45) : Color.WHITE;
    canvasBackground = newBg;

    if (g != null && img != null) {
      BufferedImage buf = (BufferedImage) img;
      int oldRGB = oldBg.getRGB();
      int newRGB = newBg.getRGB();
      for (int x = 0; x < buf.getWidth(); x++) {
        for (int y = 0; y < buf.getHeight(); y++) {
          if (buf.getRGB(x, y) == oldRGB) {
            buf.setRGB(x, y, newRGB);
          }
        }
      }
      g.setPaint(currentColor);
    }
    repaint();
  }

  public void invertCanvas() {
    for (int x = 0; x < img.getWidth(null); x++) {
      for (int y = 0; y < img.getHeight(null); y++) {
        int rgba = ((BufferedImage) img).getRGB(x, y);
        ((BufferedImage) img).setRGB(x, y, rgba ^ 0xFFFFFF);
      }
    }
    repaint();
  }

  public void saveFile(File file) {
    try {
      ImageIO.write((RenderedImage) img, "PNG", file);
    } catch (IOException ex) {
      System.err.println(
          "[ERROR] Could not save file\n Please retry again later or close the application and retry saving");
    }
  }

  private BufferedImage copyImage(Image img) {
    if (img == null || getWidth() == 0 || getHeight() == 0) {
      return null;
    }
    BufferedImage copyOfImage = new BufferedImage(getSize().width,
        getSize().height, BufferedImage.TYPE_INT_RGB);
    Graphics g = copyOfImage.createGraphics();
    g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
    g.dispose();// :NOTE: Always dispose temporary Graphics objects
    return copyOfImage;
  }

  private void saveToStack(Image img) {
    undoStack.push(copyImage(img));
  }

  public void defaultListener() {
    setDoubleBuffered(false);
    listener = new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        saveToStack(img);
        X2 = e.getX();
        Y2 = e.getY();
      }
    };
    motion = new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        X1 = e.getX();
        Y1 = e.getY();

        if (g != null) {
          g.drawLine(X2, Y2, X1, Y1);
          repaint();
          X2 = X1;
          Y2 = Y1;
        }
      }
    };
    addMouseListener(listener);
    addMouseMotionListener(motion);
  }

  public void openFile(File file) {
    try {
      BufferedImage loadedImage = ImageIO.read(file);
      if (img == null) {
        if (getWidth() == 0 || getHeight() == 0) {
          setSize(getPreferredSize());
        }
        initializeCanvasBuffer();
      }
      int x = (getWidth() - loadedImage.getWidth()) / 2;
      int y = (getHeight() - loadedImage.getHeight()) / 2;
      if (g != null) {
        saveToStack(img);
        g.setPaint(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.drawImage(loadedImage, x, y, null);
        g.setPaint(Color.BLACK);
      }

      this.revalidate();
      this.repaint();
    } catch (IOException ex) {
      System.err.println("[ERROR] Could not open file: " + ex.getMessage());
      ex.printStackTrace();
    }
  }

  private void setImage(Image img) {
    this.img = img;
    g = (Graphics2D) img.getGraphics();
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g.setPaint(currentColor);
    repaint();
  }

  private void initializeCanvasBuffer() {
    if (getWidth() <= 0 || getHeight() <= 0) {
      return;
    }
    img = createImage(getWidth(), getHeight());
    g = (Graphics2D) img.getGraphics();
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g.setPaint(canvasBackground);
    g.fillRect(0, 0, getWidth(), getHeight());
    g.setPaint(Color.black);// :BUG: Could likely cause a bug here
    new Rectangle(0, 0, getSize().width - 1, getSize().height - 1);
  }

  public void paintComponent(Graphics g1) {
    super.paintComponent(g1);

    if (img == null) {
      img = createImage(getWidth(), getHeight());
      g = (Graphics2D) img.getGraphics();
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g.setPaint(canvasBackground);
      g.fillRect(0, 0, getWidth(), getHeight());
      g.setPaint(currentColor);
    } else if (img.getWidth(null) < getWidth() || img.getHeight(null) < getHeight()) {
      Image newImg = createImage(getWidth(), getHeight());
      Graphics2D newG = (Graphics2D) newImg.getGraphics();
      newG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      newG.setPaint(canvasBackground);
      newG.fillRect(0, 0, getWidth(), getHeight());

      newG.drawImage(img, 0, 0, null);

      this.img = newImg;
      this.g = newG;
      this.g.setPaint(currentColor);
    }

    g1.drawImage(img, 0, 0, null);
    g1.setColor(isDarkTheme ? Color.DARK_GRAY : Color.RED);
    g1.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
  }

  public void undo() {
    if (undoStack.size() > 0) {
      undoTemp = undoStack.pop();
      if (img != null)
        redoStack.push(copyImage(img));
      setImage(undoTemp);
    }
  }

  public void redo() {
    if (redoStack.size() > 0) {
      redoTemp = redoStack.pop();
      undoStack.push(img);
      setImage(redoTemp);
    }
  }

  public void eraserColor() {
    g.setPaint(canvasBackground);
  }

  public void pencil() {
    removeMouseListener(listener);
    removeMouseMotionListener(motion);
    defaultListener();
  }

  public void setBackground(Image img) {
    copyImage(img);
    setImage(copyImage(img));
  }

  public void setThickness(int thick) {
    g.setStroke(new BasicStroke(thick));
  }

  public void chooser(Color color) {
    currentColor = color;
    g.setPaint(color);
  }

  public void red() {
    currentColor = Color.RED;
    g.setPaint(currentColor);
  }

  public void pink() {
    currentColor = Color.PINK;
    g.setPaint(currentColor);
  }

  public void blue() {
    currentColor = Color.BLUE;
    g.setPaint(currentColor);
  }

  public void gray() {
    currentColor = Color.GRAY;
    g.setPaint(currentColor);
  }

  public void green() {
    currentColor = Color.GREEN;
    g.setPaint(currentColor);
  }

  public void white() {
    currentColor = Color.WHITE;
    g.setPaint(currentColor);
  }

  public void yellow() {
    currentColor = Color.YELLOW;
    g.setPaint(currentColor);
  }

  public void black() {
    currentColor = Color.BLACK;
    g.setPaint(currentColor);
  }
}
