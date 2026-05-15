package kino;

import java.awt.Font;
//import java.util.Locale;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class Main {

   public static void main(String[] args) {
      try {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      } catch (Exception e) {
      }

      Font baseFont = new Font("Segoe UI", Font.PLAIN, 13);
      FontUIResource fontResource = new FontUIResource(baseFont);
      for (Map.Entry<Object, Object> entry : UIManager.getDefaults().entrySet()) {
         Object key = entry.getKey();
         if (key.toString().endsWith(".font")) {
            UIManager.put(key, fontResource);
         }
      }
      Podatki.inicializiraj();
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            ZmPregledProjekcij okno = new ZmPregledProjekcij();
            okno.setVisible(true);
         }
      });
   }
}