package kino;

import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class ZmPregledProjekcij extends JFrame {

   private final KRezervacija krmilnik;
   private Stranka najdenaStranka;

   // Search UI
   private JComboBox<String> iskanjeCombo;
   private JLabel primaryLabel;
   private JTextField primaryField;

   // Profile UI
   private JLabel profilImeValue, profilTelefonValue, profilEmailValue, profilStatusValue;
   private JLabel profilClanstvoValue, profilDatumValue, profilPopustValue;
   private JLabel clanstvoRowLabel, datumRowLabel, popustRowLabel;
   private JButton dodajVSkupinoBtn;

   // Group UI
   private JPanel skupinaContainer;
   private TitledBorder skupinaBorder;

   // Movie selection (card 1)
   private JPanel filmiContainer;
   private String izbranFilm;
   private JPanel izbranaFilmVrstica;
   private static final Color FILM_BG = new Color(245, 245, 245);
   private static final Color FILM_BG_HOVER = new Color(232, 240, 254);
   private static final Color FILM_BG_SELECTED = new Color(204, 229, 255);

   // Projection selection (card 2)
   private JLabel izbranFilmHeader;
   private JList<Projekcija> projekcijeList;
   private DefaultListModel<Projekcija> projekcijeModel;
   private Projekcija izbranaProjekcija;

   // Cards
   private CardLayout cardLayout;
   private JPanel cardPanel;
   private JButton nazajButton, naprejButton;
   private JLabel sekcijaLabel;
   private static final String CARD_FILMI = "filmi";
   private static final String CARD_PROJEKCIJE = "projekcije";
   private String trenutnaKartica = CARD_FILMI;

   // Only unique-key search criteria now (no name search)
   private static final String[] ISKANJE_MOZNOSTI = {
      "E-naslov", "Telefonska številka", "Članska številka"
   };

   public ZmPregledProjekcij() {
      this.krmilnik = new KRezervacija();
      initUI();
      prikaziProjekcije();
   }

   private void initUI() {
      setTitle("Kino Valentana — Rezervacija vstopnice");
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setSize(820, 740);
      setLocationRelativeTo(null);

      JPanel main = new JPanel(new BorderLayout(8, 8));
      main.setBorder(new EmptyBorder(12, 12, 12, 12));

      JLabel title = new JLabel("Rezervacija vstopnice", SwingConstants.CENTER);
      title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
      title.setBorder(new EmptyBorder(0, 0, 8, 0));

      JPanel top = new JPanel();
      top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
      top.add(title);
      top.add(buildSearchPanel());
      top.add(Box.createVerticalStrut(4));
      top.add(buildProfilePanel());
      top.add(Box.createVerticalStrut(4));
      top.add(buildSkupinaPanel());

      cardLayout = new CardLayout();
      cardPanel = new JPanel(cardLayout);
      cardPanel.add(buildFilmiCard(), CARD_FILMI);
      cardPanel.add(buildProjekcijeCard(), CARD_PROJEKCIJE);

      sekcijaLabel = new JLabel("3. Izbira filma");
      sekcijaLabel.setFont(sekcijaLabel.getFont().deriveFont(Font.BOLD, 13f));
      sekcijaLabel.setBorder(new EmptyBorder(4, 4, 4, 4));

      JPanel cardWrap = new JPanel(new BorderLayout());
      cardWrap.add(sekcijaLabel, BorderLayout.NORTH);
      cardWrap.add(cardPanel, BorderLayout.CENTER);

      main.add(top, BorderLayout.NORTH);
      main.add(cardWrap, BorderLayout.CENTER);
      main.add(buildSouthPanel(), BorderLayout.SOUTH);
      setContentPane(main);
   }

   // ===== SEARCH =====
   private JPanel buildSearchPanel() {
      JPanel panel = new JPanel(new GridBagLayout());
      panel.setBorder(new TitledBorder("1. Iskanje stranke"));
      GridBagConstraints g = new GridBagConstraints();
      g.insets = new Insets(4, 6, 4, 6);
      g.anchor = GridBagConstraints.WEST;
      g.fill = GridBagConstraints.HORIZONTAL;

      g.gridx = 0; g.gridy = 0; g.weightx = 0;
      panel.add(new JLabel("Iskanje po:"), g);
      iskanjeCombo = new JComboBox<>(ISKANJE_MOZNOSTI);
      iskanjeCombo.addActionListener(e -> posodobiPolja());
      g.gridx = 1; g.weightx = 1;
      panel.add(iskanjeCombo, g);

      primaryLabel = new JLabel();
      g.gridx = 0; g.gridy = 1; g.weightx = 0;
      panel.add(primaryLabel, g);
      primaryField = new JTextField(20);
      g.gridx = 1; g.weightx = 1;
      panel.add(primaryField, g);

      JButton najdiBtn = new JButton("Najdi stranko");
      najdiBtn.addActionListener(e -> najdiStranko());
      g.gridx = 0; g.gridy = 2; g.gridwidth = 2; g.fill = GridBagConstraints.NONE;
      g.anchor = GridBagConstraints.EAST;
      panel.add(najdiBtn, g);

      posodobiPolja();
      return panel;
   }

   private void posodobiPolja() {
      String izbrano = (String) iskanjeCombo.getSelectedItem();
      primaryField.setText("");
      String label;
      if ("E-naslov".equals(izbrano)) label = "E-naslov:";
      else if ("Telefonska številka".equals(izbrano)) label = "Telefon:";
      else label = "Članska št.:";
      primaryLabel.setText(label);
   }

   private void najdiStranko() {
      String izbrano = (String) iskanjeCombo.getSelectedItem();
      najdenaStranka = null;
      String input = primaryField.getText().trim();

      if ("E-naslov".equals(izbrano)) {
         najdenaStranka = Podatki.najdiStrankoPoEmailu(input);
      } else if ("Telefonska številka".equals(izbrano)) {
         najdenaStranka = Podatki.najdiStrankoPoTelefonu(input);
      } else if ("Članska številka".equals(izbrano)) {
         try {
            najdenaStranka = Podatki.najdiClanaPoStevilki(Integer.parseInt(input));
         } catch (NumberFormatException ex) {
            najdenaStranka = null;
            JOptionPane.showMessageDialog(this,
               "Neveljavna članska številka. Popust se ne upošteva.",
               "Neveljavna številka",
               JOptionPane.WARNING_MESSAGE);
         }
      }

      if (najdenaStranka != null) {
         napolniProfil(najdenaStranka);
         dodajVSkupinoBtn.setEnabled(true);
      } else {
         pocistiProfil();
         dodajVSkupinoBtn.setEnabled(false);
         JOptionPane.showMessageDialog(this, "Stranka ni najdena.",
            "Stranka ni najdena", JOptionPane.WARNING_MESSAGE);
      }
   }

   // ===== PROFILE =====
   private JPanel buildProfilePanel() {
      JPanel panel = new JPanel(new GridBagLayout());
      panel.setBorder(new TitledBorder("Profil stranke"));
      GridBagConstraints g = new GridBagConstraints();
      g.insets = new Insets(2, 8, 2, 8);
      g.anchor = GridBagConstraints.WEST;
      g.fill = GridBagConstraints.HORIZONTAL;

      profilImeValue = makeProfileRow(panel, g, 0, "Ime in priimek:", null);
      profilTelefonValue = makeProfileRow(panel, g, 1, "Telefon:", null);
      profilEmailValue = makeProfileRow(panel, g, 2, "E-naslov:", null);
      profilStatusValue = makeProfileRow(panel, g, 3, "Status:", null);
      clanstvoRowLabel = new JLabel("Članska številka:");
      profilClanstvoValue = makeProfileRow(panel, g, 4, null, clanstvoRowLabel);
      datumRowLabel = new JLabel("Datum včlanitve:");
      profilDatumValue = makeProfileRow(panel, g, 5, null, datumRowLabel);
      popustRowLabel = new JLabel("Popust:");
      profilPopustValue = makeProfileRow(panel, g, 6, null, popustRowLabel);

      dodajVSkupinoBtn = new JButton("+ Dodaj v skupino");
      dodajVSkupinoBtn.setEnabled(false);
      dodajVSkupinoBtn.addActionListener(e -> dodajVSkupino());
      g.gridx = 0; g.gridy = 7; g.gridwidth = 2; g.fill = GridBagConstraints.NONE;
      g.anchor = GridBagConstraints.EAST;
      panel.add(dodajVSkupinoBtn, g);

      pocistiProfil();
      return panel;
   }

   private JLabel makeProfileRow(JPanel panel, GridBagConstraints g, int row, String labelText, JLabel customLabel) {
      g.gridx = 0; g.gridy = row; g.gridwidth = 1; g.weightx = 0; g.fill = GridBagConstraints.HORIZONTAL;
      JLabel keyLabel = customLabel != null ? customLabel : new JLabel(labelText);
      keyLabel.setFont(keyLabel.getFont().deriveFont(Font.BOLD));
      panel.add(keyLabel, g);
      JLabel valueLabel = new JLabel("—");
      g.gridx = 1; g.weightx = 1;
      panel.add(valueLabel, g);
      return valueLabel;
   }

   private void napolniProfil(Stranka s) {
      profilImeValue.setText(s.getIme() + " " + s.getPriimek());
      profilTelefonValue.setText(s.getTelefonskaStevilka());
      profilEmailValue.setText(s.getElektronskiNaslov());
      if (s instanceof Clan) {
         Clan c = (Clan) s;
         profilStatusValue.setText("ČLAN");
         profilStatusValue.setForeground(new Color(0, 120, 0));
         profilClanstvoValue.setText(String.valueOf(c.getClanskaStevilka()));
         profilDatumValue.setText(c.getDatumVclanitve().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
         profilPopustValue.setText(c.getPopust() + " %");
         nastaviClanVrsticeVidne(true);
      } else {
         profilStatusValue.setText("Navadna stranka");
         profilStatusValue.setForeground(new Color(80, 80, 80));
         nastaviClanVrsticeVidne(false);
      }
   }

   private void pocistiProfil() {
      profilImeValue.setText("—"); profilTelefonValue.setText("—");
      profilEmailValue.setText("—"); profilStatusValue.setText("—");
      profilStatusValue.setForeground(Color.BLACK);
      profilClanstvoValue.setText("—"); profilDatumValue.setText("—"); profilPopustValue.setText("—");
      nastaviClanVrsticeVidne(false);
   }

   private void nastaviClanVrsticeVidne(boolean vidne) {
      clanstvoRowLabel.setVisible(vidne); profilClanstvoValue.setVisible(vidne);
      datumRowLabel.setVisible(vidne); profilDatumValue.setVisible(vidne);
      popustRowLabel.setVisible(vidne); profilPopustValue.setVisible(vidne);
   }

   // ===== GROUP =====
   private JPanel buildSkupinaPanel() {
      JPanel panel = new JPanel(new BorderLayout());
      skupinaBorder = new TitledBorder("2. Skupina strank (0 oseb)");
      panel.setBorder(skupinaBorder);

      skupinaContainer = new JPanel();
      skupinaContainer.setLayout(new BoxLayout(skupinaContainer, BoxLayout.Y_AXIS));
      skupinaContainer.setBackground(Color.WHITE);

      JScrollPane scroll = new JScrollPane(skupinaContainer);
      scroll.setPreferredSize(new Dimension(780, 80));
      scroll.setBorder(BorderFactory.createEmptyBorder());
      panel.add(scroll, BorderLayout.CENTER);

      osveziPrikazSkupine();
      return panel;
   }

   private void dodajVSkupino() {
      if (najdenaStranka == null) return;
      if (krmilnik.getTrenutneStranke().contains(najdenaStranka)) {
         JOptionPane.showMessageDialog(this,
            "Ta stranka je že v skupini.", "Že dodana", JOptionPane.INFORMATION_MESSAGE);
         return;
      }
      krmilnik.dodajStranko(najdenaStranka);
      osveziPrikazSkupine();
      // Clear search for next customer
      najdenaStranka = null;
      iskanjeCombo.setSelectedIndex(0);
      primaryField.setText("");
      pocistiProfil();
      dodajVSkupinoBtn.setEnabled(false);
      posodobiPolja();
   }

   private void osveziPrikazSkupine() {
      skupinaContainer.removeAll();
      List<Stranka> skupina = krmilnik.getTrenutneStranke();
      skupinaBorder.setTitle("2. Skupina strank (" + skupina.size() + " "
         + (skupina.size() == 1 ? "oseba" : "oseb") + ")");
      if (skupina.isEmpty()) {
         JLabel empty = new JLabel("Še ni dodanih strank. Poiščite stranko zgoraj in jo dodajte v skupino.");
         empty.setForeground(Color.GRAY);
         empty.setBorder(new EmptyBorder(8, 10, 8, 10));
         skupinaContainer.add(empty);
      } else {
         for (int i = 0; i < skupina.size(); i++) {
            skupinaContainer.add(buildSkupinaVrstica(i + 1, skupina.get(i)));
            skupinaContainer.add(Box.createVerticalStrut(2));
         }
      }
      skupinaContainer.revalidate();
      skupinaContainer.repaint();
      skupinaContainer.getParent().getParent().repaint();
   }

   private JPanel buildSkupinaVrstica(int stevilka, final Stranka s) {
      JPanel row = new JPanel(new BorderLayout());
      row.setBackground(new Color(248, 248, 248));
      row.setBorder(BorderFactory.createCompoundBorder(
         new LineBorder(new Color(220, 220, 220), 1),
         new EmptyBorder(6, 10, 6, 6)));
      row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

      String info = stevilka + ".  " + s.getIme() + " " + s.getPriimek();
      if (s instanceof Clan) {
         Clan c = (Clan) s;
         info += "  —  ČLAN, -" + c.getPopust() + " %";
      } else {
         info += "  —  navadna stranka";
      }
      JLabel label = new JLabel(info);

      JButton remove = new JButton("×");
      remove.setForeground(new Color(180, 50, 50));
      remove.setFocusable(false);
      remove.setMargin(new Insets(2, 8, 2, 8));
      remove.addActionListener(e -> {
         krmilnik.odstraniStranko(s);
         osveziPrikazSkupine();
      });

      row.add(label, BorderLayout.CENTER);
      row.add(remove, BorderLayout.EAST);
      return row;
   }

   // ===== FILMS (card 1) =====
   private JScrollPane buildFilmiCard() {
      filmiContainer = new JPanel();
      filmiContainer.setLayout(new BoxLayout(filmiContainer, BoxLayout.Y_AXIS));
      filmiContainer.setBackground(Color.WHITE);
      JScrollPane scroll = new JScrollPane(filmiContainer);
      scroll.setPreferredSize(new Dimension(780, 170));
      scroll.getVerticalScrollBar().setUnitIncrement(16);
      return scroll;
   }

   private JPanel buildFilmRow(final String naslov, final Projekcija primer) {
      final JPanel row = new JPanel(new BorderLayout());
      row.setBackground(FILM_BG);
      row.setBorder(BorderFactory.createCompoundBorder(
         new LineBorder(new Color(220, 220, 220), 1),
         new EmptyBorder(10, 14, 10, 14)));
      row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
      row.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

      JLabel titleLabel = new JLabel(naslov);
      titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 15f));
      JLabel yearLabel = new JLabel("  (" + primer.getLetoIzida() + ")");
      yearLabel.setForeground(Color.GRAY);
      JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
      left.setOpaque(false);
      left.add(titleLabel); left.add(yearLabel);

      JButton detailsBtn = new JButton("Več podrobnosti");
      detailsBtn.setFocusable(false);
      detailsBtn.addActionListener(e -> prikaziPodrobnostiFilma(primer));

      row.add(left, BorderLayout.CENTER);
      row.add(detailsBtn, BorderLayout.EAST);
      row.addMouseListener(new MouseAdapter() {
         @Override public void mouseClicked(MouseEvent e) { izberiFilm(naslov, row); }
         @Override public void mouseEntered(MouseEvent e) {
            if (row != izbranaFilmVrstica) row.setBackground(FILM_BG_HOVER);
         }
         @Override public void mouseExited(MouseEvent e) {
            if (row != izbranaFilmVrstica) row.setBackground(FILM_BG);
         }
      });
      return row;
   }

   private void izberiFilm(String naslov, JPanel row) {
      if (izbranaFilmVrstica != null) izbranaFilmVrstica.setBackground(FILM_BG);
      this.izbranFilm = naslov;
      this.izbranaFilmVrstica = row;
      row.setBackground(FILM_BG_SELECTED);
   }

   private void prikaziPodrobnostiFilma(Projekcija p) {
      JPanel content = new JPanel(new GridBagLayout());
      content.setPreferredSize(new Dimension(540, 380));
      GridBagConstraints g = new GridBagConstraints();
      g.insets = new Insets(4, 8, 4, 8);
      g.anchor = GridBagConstraints.NORTHWEST;
      g.fill = GridBagConstraints.HORIZONTAL;

      JLabel title = new JLabel(p.getNaslovFilma());
      title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
      g.gridx = 0; g.gridy = 0; g.gridwidth = 2; g.weightx = 1;
      content.add(title, g);
      g.gridwidth = 1;
      addDetailRow(content, g, 1, "Leto izida:", String.valueOf(p.getLetoIzida()));
      addDetailRow(content, g, 2, "Trajanje:", p.getTrajanje() + " minut");
      addDetailRow(content, g, 3, "Žanr:", p.getZanr());
      addDetailRow(content, g, 4, "Režiser:", p.getReziser());
      addDetailRow(content, g, 5, "Igralci:", p.getIgralci());

      JTextArea opisArea = new JTextArea(p.getOpis());
      opisArea.setLineWrap(true); opisArea.setWrapStyleWord(true); opisArea.setEditable(false);
      opisArea.setBackground(content.getBackground());
      opisArea.setBorder(BorderFactory.createCompoundBorder(new TitledBorder("Opis"), new EmptyBorder(6, 8, 6, 8)));
      g.gridx = 0; g.gridy = 6; g.gridwidth = 2; g.weighty = 1; g.fill = GridBagConstraints.BOTH;
      content.add(opisArea, g);

      JOptionPane.showMessageDialog(this, content,
         "Podrobnosti filma — " + p.getNaslovFilma(), JOptionPane.INFORMATION_MESSAGE);
   }

   private void addDetailRow(JPanel content, GridBagConstraints g, int row, String label, String value) {
      g.gridx = 0; g.gridy = row; g.weightx = 0;
      JLabel keyLabel = new JLabel(label);
      keyLabel.setFont(keyLabel.getFont().deriveFont(Font.BOLD));
      content.add(keyLabel, g);
      g.gridx = 1; g.weightx = 1;
      content.add(new JLabel("<html>" + value.replace("\n", "<br>") + "</html>"), g);
   }

   // ===== PROJECTIONS (card 2) =====
   private JPanel buildProjekcijeCard() {
      JPanel panel = new JPanel(new BorderLayout(8, 8));
      panel.setBorder(new EmptyBorder(4, 4, 4, 4));
      izbranFilmHeader = new JLabel(" ");
      izbranFilmHeader.setFont(izbranFilmHeader.getFont().deriveFont(Font.BOLD, 14f));
      izbranFilmHeader.setBorder(new EmptyBorder(0, 4, 8, 4));
      projekcijeModel = new DefaultListModel<>();
      projekcijeList = new JList<>(projekcijeModel);
      projekcijeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      projekcijeList.setFont(projekcijeList.getFont().deriveFont(14f));
      projekcijeList.setFixedCellHeight(34);
      JScrollPane scroll = new JScrollPane(projekcijeList);
      scroll.setPreferredSize(new Dimension(780, 150));
      panel.add(izbranFilmHeader, BorderLayout.NORTH);
      panel.add(scroll, BorderLayout.CENTER);
      return panel;
   }

   private void prikaziProjekcijeZaFilm(String film) {
      izbranFilmHeader.setText("Izbran film:  " + film + "  ->  izberite termin in dvorano");
      projekcijeModel.clear();
      for (Projekcija p : krmilnik.pregledajProjekcije()) {
         if (p.getNaslovFilma().equals(film)) projekcijeModel.addElement(p);
      }
      if (projekcijeModel.size() > 0) projekcijeList.setSelectedIndex(0);
   }

   private JPanel buildSouthPanel() {
      nazajButton = new JButton("Nazaj na filme");
      nazajButton.setVisible(false);
      nazajButton.addActionListener(e -> nazaj());
      Stil.sekundarniGumb(nazajButton);
      naprejButton = new JButton("Naprej");
      naprejButton.setFont(naprejButton.getFont().deriveFont(Font.BOLD, 14f));
      naprejButton.addActionListener(e -> naprej());
      Stil.primarniGumb(naprejButton);
      JPanel south = new JPanel(new BorderLayout());
      JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
      left.add(nazajButton);
      JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      right.add(naprejButton);
      south.add(left, BorderLayout.WEST);
      south.add(right, BorderLayout.EAST);
      return south;
   }

   // ===== DESIGN METHODS =====
   public void prikaziProjekcije() {
      filmiContainer.removeAll();
      izbranFilm = null;
      izbranaFilmVrstica = null;
      LinkedHashMap<String, Projekcija> filmi = new LinkedHashMap<>();
      for (Projekcija p : krmilnik.pregledajProjekcije()) {
         filmi.putIfAbsent(p.getNaslovFilma(), p);
      }
      for (Map.Entry<String, Projekcija> entry : filmi.entrySet()) {
         filmiContainer.add(buildFilmRow(entry.getKey(), entry.getValue()));
         filmiContainer.add(Box.createVerticalStrut(4));
      }
      filmiContainer.add(Box.createVerticalGlue());
      filmiContainer.revalidate();
      filmiContainer.repaint();
   }

   public Projekcija izberiProjekcijo() { return izbranaProjekcija; }

   // ===== NAVIGATION =====
   private void naprej() {
      if (krmilnik.steviloOseb() == 0) {
         JOptionPane.showMessageDialog(this,
            "Skupina je prazna. Dodajte vsaj eno stranko.",
            "Napaka", JOptionPane.WARNING_MESSAGE);
         return;
      }
      if (CARD_FILMI.equals(trenutnaKartica)) {
         if (izbranFilm == null) {
            JOptionPane.showMessageDialog(this, "Izberite film s seznama.",
               "Napaka", JOptionPane.WARNING_MESSAGE);
            return;
         }
         prikaziProjekcijeZaFilm(izbranFilm);
         cardLayout.show(cardPanel, CARD_PROJEKCIJE);
         trenutnaKartica = CARD_PROJEKCIJE;
         sekcijaLabel.setText("4. Izbira termina in dvorane");
         nazajButton.setVisible(true);
      } else {
         izbranaProjekcija = projekcijeList.getSelectedValue();
         if (izbranaProjekcija == null) {
            JOptionPane.showMessageDialog(this, "Izberite termin s seznama.",
               "Napaka", JOptionPane.WARNING_MESSAGE);
            return;
         }
         krmilnik.pregledajSedeze(izbranaProjekcija);
         ZmPregledSedezev sedezOkno = new ZmPregledSedezev(krmilnik, this);
         sedezOkno.setVisible(true);
         this.setVisible(false);
      }
   }

   private void nazaj() {
      izbranaProjekcija = null;
      cardLayout.show(cardPanel, CARD_FILMI);
      trenutnaKartica = CARD_FILMI;
      sekcijaLabel.setText("3. Izbira filma");
      nazajButton.setVisible(false);
   }

   public void ponoviPrikaz() {
      najdenaStranka = null;
      iskanjeCombo.setSelectedIndex(0);
      primaryField.setText("");
      pocistiProfil();
      posodobiPolja();
      dodajVSkupinoBtn.setEnabled(false);
      krmilnik.ponastavi();
      osveziPrikazSkupine();
      this.setVisible(true);
      cardLayout.show(cardPanel, CARD_FILMI);
      trenutnaKartica = CARD_FILMI;
      sekcijaLabel.setText("3. Izbira filma");
      nazajButton.setVisible(false);
      izbranaProjekcija = null;
      prikaziProjekcije();
   }
}