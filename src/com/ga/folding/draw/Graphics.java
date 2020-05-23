package com.ga.folding.draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Graphics {

  int width = 1920;
  int height = 1080;
  Graphics2D g2;
  BufferedImage image;
  String folded_acid2 = "1r-0u-1u-0l-0d-1l-1u-0l-1u-0l-0d-1d-0r-1d-1l-0d-0r-1r-0u-1";

  int cellSize;

  public Graphics() {
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    g2 = image.createGraphics();



    draw();
  }

  public void draw() {
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    g2.setColor(Color.DARK_GRAY);
    g2.fillRect(0,0,width,height);

    cellSize = 80;
    int lineLength = 80;

    String[] tmp = folded_acid2.split("-");

    // starting position
    int x_pos = width/2 - 40;
    int y_pos = height/2 - 40;


    // draw the first acid
    if(tmp[0].charAt(0) == '1') {
      g2.setColor(Color.BLACK);
    } else if(tmp[0].charAt(0) == '0'){
      g2.setColor(Color.WHITE);
    }


    g2.fillRect(x_pos, y_pos, cellSize, cellSize);

    g2.setColor(Color.GREEN);
    String label = 0 + "";
    Font font = new Font("Serif", Font.PLAIN, 40);
    g2.setFont(font);
    FontMetrics metric = g2.getFontMetrics();
    int ascent = metric.getAscent();
    int labelWidth = metric.stringWidth(label);

    g2.drawString(label, x_pos+ cellSize/2 - labelWidth/2, y_pos + cellSize/2 + ascent/2);



    for(int i = 0; i < tmp.length-1; i++) {
      if(tmp[i].length() == 2) {
        if (tmp[i].charAt(1) == 'r') {
          g2.setColor(Color.BLACK);
          g2.drawLine(x_pos + cellSize, y_pos + cellSize/2, x_pos + lineLength + cellSize, y_pos + cellSize/2);

          x_pos = x_pos + lineLength + cellSize;
          y_pos = y_pos;


        } else if(tmp[i].charAt(1) == 'l') {
          g2.setColor(Color.BLACK);
          g2.drawLine(x_pos, y_pos + cellSize/2, x_pos - lineLength - cellSize, y_pos + cellSize/2);

          x_pos = x_pos - lineLength - cellSize;
          y_pos = y_pos;


        } else if(tmp[i].charAt(1) == 'u') {
          g2.setColor(Color.BLACK);
          g2.drawLine(x_pos + cellSize/2, y_pos, x_pos + cellSize/2, y_pos - lineLength - cellSize);

          x_pos = x_pos;
          y_pos = y_pos - lineLength - cellSize;


        } else if(tmp[i].charAt(1) == 'd') {
          g2.setColor(Color.BLACK);
          g2.drawLine(x_pos + cellSize/2, y_pos + cellSize, x_pos + cellSize/2, y_pos + lineLength + cellSize);

          x_pos = x_pos;
          y_pos = y_pos + lineLength + cellSize;


        }

        // i + 1 because we want to draw the next acid
        if(tmp[i+1].charAt(0) == '1') {
          g2.setColor(Color.BLACK);
        } else if(tmp[i+1].charAt(0) == '0'){
          g2.setColor(Color.WHITE);
        }

        g2.fillRect(x_pos, y_pos, cellSize, cellSize);

      }

      g2.setColor(Color.GREEN);
      System.out.println(i+1);
      label = i + 1 + "";
      font = new Font("Serif", Font.PLAIN, 40);
      g2.setFont(font);
      metric = g2.getFontMetrics();
      ascent = metric.getAscent();
      labelWidth = metric.stringWidth(label);

      g2.drawString(label, x_pos+ cellSize/2 - labelWidth/2, y_pos + cellSize/2 + ascent/2);



    }


    String folder = "/tmp/paul/ga";
    String filename = "image.png";
    if(new File(folder).exists() == false) new File(folder).mkdirs();

    try {
      ImageIO.write(image, "png", new File(folder + File.separator + filename));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(0);
    }
  }
}
