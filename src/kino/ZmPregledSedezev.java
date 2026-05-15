package kino;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ZmPregledSedezev extends JFrame {

   private final KRezervacija krmilnik;
   private final ZmPregledProjekcij prejsnjaOkno;

   private JButton[][] sedezButtons;
   private JLabel statusLabel;
   private JButton naprejButton;

   private static final int VRSTE = 5;
   private static final int SEDEZI_VRSTA = 8;

   public ZmPregledSedezev(KRezervacija krmilnik, ZmPregledProjekcij prejsnja) {
      this.krmilnik = krmilnik;
      this.prejsnjaOkno = prejsnja;
      initUI();
      prikaziSedeze();
   }

   private void initUI() {
      setTitle("Kino Valentana — Izbira sedežev");
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      setSize(760, 640);
      setLocationRelativeTo(null);

      JPanel main = new JPanel(new BorderLayout(10, 10));
      main.setBorder(new EmptyBorder(15, 15, 15, 15));

      Projekcija proj = krmilnik.getTrenutnaProjekcija();
      int oseb = krmilnik.steviloOseb();

      JLabel header = new JLabel("Projekcija: "
         + (proj != null ? proj.toString() : ""), SwingConstants.CENTER);
      header.setFont(header.getFont().deriveFont(Font.BOLD, 14f));
      header.setBorder(new EmptyBorder(0, 0, 5, 0));

      JLabel oseb_label = new JLabel("Skupina: " + oseb + " oseb — izberite "
         + oseb + " sedežev", SwingConstants.CENTER);
      oseb_label.setForeground(new Color(60, 60, 60));

      JLabel screenLabel = new JLabel("─── PLATNO ───", SwingConstants.CENTER);
      screenLabel.setFont(screenLabel.getFont().deriveFont(Font.BOLD, 13f));
      screenLabel.setForeground(Color.DARK_GRAY);
      screenLabel.setBorder(new EmptyBorder(8, 0, 12, 0));

      JPanel north = new JPanel();
      north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
      header.setAlignmentX(Component.CENTER_ALIGNMENT);
      oseb_label.setAlignmentX(Component.CENTER_ALIGNMENT);
      screenLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
      north.add(header);
      north.add(oseb_label);
      north.add(screenLabel);

      JPanel grid = new JPanel(new GridLayout(VRSTE, SEDEZI_VRSTA, 6, 6));
      sedezButtons = new JButton[VRSTE][SEDEZI_VRSTA];
      for (int v = 0; v < VRSTE; v++) {
         for (int s = 0; s < SEDEZI_VRSTA; s++) {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(60, 50));
            sedezButtons[v][s] = b;
            grid.add(b);
         }
      }

      JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
      legend.add(legendItem("Standardni", new Color(220, 220, 220)));
      legend.add(legendItem("VIP", new Color(255, 215, 0)));
      legend.add(legendItem("Invalidski", new Color(135, 206, 250)));
      legend.add(legendItem("Zaseden", new Color(220, 80, 80)));
      legend.add(legendItem("Izbran", new Color(80, 200, 80)));

      statusLabel = new JLabel(" ", SwingConstants.CENTER);
      statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 14f));
      statusLabel.setBorder(new EmptyBorder(8, 0, 8, 0));

      JButton nazaj = new JButton("Nazaj");
      nazaj.addActionListener(e -> nazaj());
      Stil.sekundarniGumb(nazaj);

      naprejButton = new JButton("Naprej");
      naprejButton.setFont(naprejButton.getFont().deriveFont(Font.BOLD, 14f));
      naprejButton.setEnabled(false);
      naprejButton.addActionListener(e -> naprej());
      Stil.primarniGumb(naprejButton);

      JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
      buttonRow.add(nazaj);
      buttonRow.add(naprejButton);

      JPanel south = new JPanel(new BorderLayout());
      south.add(legend, BorderLayout.NORTH);
      south.add(statusLabel, BorderLayout.CENTER);
      south.add(buttonRow, BorderLayout.SOUTH);

      main.add(north, BorderLayout.NORTH);
      main.add(grid, BorderLayout.CENTER);
      main.add(south, BorderLayout.SOUTH);
      setContentPane(main);

      posodobiStatus();
   }

   private JPanel legendItem(String text, Color c) {
      JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
      JLabel swatch = new JLabel("  ");
      swatch.setOpaque(true);
      swatch.setBackground(c);
      swatch.setBorder(BorderFactory.createLineBorder(Color.GRAY));
      p.add(swatch);
      p.add(new JLabel(text));
      return p;
   }

   public void prikaziSedeze() {
      // If projection has no available seats, inform the user and return
      List<Sedez> prosti = krmilnik.getTrenutnaProjekcija().vrniRazpolozljiveSedeze();
      if (prosti == null || prosti.isEmpty()) {
         JOptionPane.showMessageDialog(this,
            "Projekcija je polno zasedena. Izberite drugo projekcijo.",
            "Polna zasedenost", JOptionPane.INFORMATION_MESSAGE);
         this.dispose();
         prejsnjaOkno.setVisible(true);
         return;
      }

      List<Sedez> sedezi = Podatki.getVsiSedezi();
      for (Sedez s : sedezi) {
         int v = s.getStevilkaVrstice() - 1;
         int idx = s.getStevilkaSedeza() - 1;
         if (v < 0 || v >= VRSTE || idx < 0 || idx >= SEDEZI_VRSTA) continue;
         JButton b = sedezButtons[v][idx];
         b.setText("V" + s.getStevilkaVrstice() + "S" + s.getStevilkaSedeza());
         for (ActionListener al : b.getActionListeners()) b.removeActionListener(al);
         final Sedez sFinal = s;
         b.addActionListener(e -> preklopiSedez(sFinal));
      }
      prikaziZasedanost();
   }

   public void prikaziZasedanost() {
      List<Sedez> sedezi = Podatki.getVsiSedezi();
      for (Sedez s : sedezi) {
         int v = s.getStevilkaVrstice() - 1;
         int idx = s.getStevilkaSedeza() - 1;
         if (v < 0 || v >= VRSTE || idx < 0 || idx >= SEDEZI_VRSTA) continue;
         JButton b = sedezButtons[v][idx];
         b.setOpaque(true);
         b.setBorderPainted(true);

         boolean zaseden = krmilnik.getTrenutnaProjekcija().jeSedezZaseden(s);
      if (zaseden) {
            b.setBackground(new Color(220, 80, 80));
            b.setEnabled(false);
         } else if (krmilnik.jeSedezIzbran(s)) {
            b.setBackground(new Color(80, 200, 80));
            b.setEnabled(true);
         } else {
            String tip = s.getTipSedeza();
            if ("VIP".equalsIgnoreCase(tip)) {
               b.setBackground(new Color(255, 215, 0));
            } else if ("Invalidski".equalsIgnoreCase(tip)) {
               b.setBackground(new Color(135, 206, 250));
            } else {
               b.setBackground(new Color(220, 220, 220));
            }
            b.setEnabled(true);
         }
      }
   }

   public Sedez izberiSedez() {
      List<Sedez> izbrani = krmilnik.getTrenutniSedezi();
      return izbrani.isEmpty() ? null : izbrani.get(0);
   }

   private void preklopiSedez(Sedez s) {
      int oseb = krmilnik.steviloOseb();
      boolean alreadySelected = krmilnik.jeSedezIzbran(s);
      if (!alreadySelected && krmilnik.steviloIzbranihSedezev() >= oseb) {
         JOptionPane.showMessageDialog(this,
            "Že ste izbrali " + oseb + " sedežev (toliko, kot je oseb v skupini).\n"
            + "Najprej odznačite enega od izbranih sedežev.",
            "Omejitev sedežev", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      krmilnik.preklopiSedez(s);
      prikaziZasedanost();
      posodobiStatus();
   }

   private void posodobiStatus() {
      int izbrano = krmilnik.steviloIzbranihSedezev();
      int oseb = krmilnik.steviloOseb();
      statusLabel.setText("Izbrano: " + izbrano + " / " + oseb + " sedežev");
      if (izbrano == oseb && oseb > 0) {
         statusLabel.setForeground(new Color(0, 120, 0));
         naprejButton.setEnabled(true);
      } else {
         statusLabel.setForeground(Color.BLACK);
         naprejButton.setEnabled(false);
      }
   }

   private void nazaj() {
      List<Sedez> izbrani = krmilnik.getTrenutniSedezi();
      for (Sedez s : izbrani) krmilnik.preklopiSedez(s);
      this.dispose();
      prejsnjaOkno.setVisible(true);
   }



   private void naprej() {
      if (krmilnik.steviloIzbranihSedezev() != krmilnik.steviloOseb()) {
         JOptionPane.showMessageDialog(this,
            "Izberite točno toliko sedežev, kot je oseb v skupini.",
            "Napaka", JOptionPane.WARNING_MESSAGE);
         return;
      }

      // 2. ALTERNATIVNI TOK: Preverjanje povezanih sedežev (v isti vrsti)
      if (!krmilnik.soSedeziVistiVrsti()) {
         int izbira = JOptionPane.showConfirmDialog(this,
                 "Izbrani sedeži niso v isti vrsti. Želite vseeno nadaljevati?\n" +
                         "(Blagajnik: Ponudite stranki drugo projekcijo, če želi sedeti skupaj.)",
                 "Opozorilo: Sedeži niso skupaj",
                 JOptionPane.YES_NO_OPTION,
                 JOptionPane.WARNING_MESSAGE);

         if (izbira != JOptionPane.YES_OPTION) {
            return; // Stranka se odloči za drugo izbiro ali prekliče
         }
      }

      try {
         Rezervacija rez = krmilnik.ustvariRezervacijo();
         ZmRezervacija rezOkno = new ZmRezervacija(krmilnik, rez, prejsnjaOkno);
         rezOkno.setVisible(true);
         this.dispose();
      } catch (Exception ex) {
         JOptionPane.showMessageDialog(this,
            "Napaka pri rezervaciji: " + ex.getMessage(),
            "Napaka", JOptionPane.ERROR_MESSAGE);
      }
   }
}