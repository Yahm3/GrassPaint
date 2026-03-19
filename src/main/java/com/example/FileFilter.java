package com.example;

import javax.swing.filechooser.FileNameExtensionFilter;

public class FileFilter {
  public static final FileNameExtensionFilter png = new FileNameExtensionFilter("PNG files", "png");
  public static final FileNameExtensionFilter jpg = new FileNameExtensionFilter("JPG files", "jpg");
  public static final FileNameExtensionFilter jpeg = new FileNameExtensionFilter("JPEG files", "jpeg");
  public static final FileNameExtensionFilter ppm = new FileNameExtensionFilter("PPM files", "ppm");
}
