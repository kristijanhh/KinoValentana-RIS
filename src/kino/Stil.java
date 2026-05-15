package kino;

import java.awt.*;
import javax.swing.*;

public class Stil {

   // === Cinema palette ===
   public static final Color OZADJE          = new Color(245, 242, 238);  // warm off-white
   public static final Color KARTICA         = Color.WHITE;
   public static final Color PRIMARNA        = new Color(139, 26, 26);    // deep cinema red
   public static final Color PRIMARNA_TEMNA  = new Color(102, 18, 18);    // hover/border
   public static final Color ZLATA           = new Color(201, 169, 97);   // warm gold accent
   public static final Color TEKST           = new Color(44, 44, 44);
   public static final Color TEKST_TIH       = new Color(107, 107, 107);
   public static final Color OBROBA          = new Color(220, 215, 205);
   public static final Color USPEH           = new Color(46, 125, 50);
   public static final Color NAPAKA          = new Color(180, 50, 50);

   // === Fonts ===
   public static final Font NASLOV    = new Font("Segoe UI", Font.PLAIN, 26);
   public static final Font PODNASLOV = new Font("Segoe UI", Font.BOLD, 15);
   public static final Font GUMB      = new Font("Segoe UI", Font.BOLD, 13);
   public static final Font BODY      = new Font("Segoe UI", Font.PLAIN, 13);
   public static final Font CENA      = new Font("Segoe UI", Font.BOLD, 22);

   /** Style a button as a primary action (red bg, white text). */
   public static void primarniGumb(JButton b) {
      b.setBackground(PRIMARNA);
      b.setForeground(Color.WHITE);
      b.setFont(GUMB);
      b.setFocusPainted(false);
      b.setBorderPainted(false);
      b.setOpaque(true);
      b.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
      b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
   }

   //a button as secondary (light bg, dark text).
   public static void sekundarniGumb(JButton b) {
      b.setBackground(new Color(235, 232, 226));
      b.setForeground(TEKST);
      b.setFont(GUMB);
      b.setFocusPainted(false);
      b.setBorderPainted(false);
      b.setOpaque(true);
      b.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
      b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
   }

   // Style a button as a small accent (gold).
   public static void accentGumb(JButton b) {
      b.setBackground(ZLATA);
      b.setForeground(new Color(50, 30, 0));
      b.setFont(GUMB);
      b.setFocusPainted(false);
      b.setBorderPainted(false);
      b.setOpaque(true);
      b.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
      b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
   }
}
