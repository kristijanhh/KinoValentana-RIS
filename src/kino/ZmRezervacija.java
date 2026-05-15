package kino;

import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ZmRezervacija extends JFrame {

   private final KRezervacija krmilnik;
   private final Rezervacija rezervacija;
   private final ZmPregledProjekcij osnovnoOkno;

   private JLabel cenaLabel;

   public ZmRezervacija(KRezervacija krmilnik, Rezervacija rez, ZmPregledProjekcij osnovno) {
      this.krmilnik = krmilnik;
      this.rezervacija = rez;
      this.osnovnoOkno = osnovno;
      initUI();
      prikaziPovzetekRezervacije();
      prikaziCeno();
   }

   private void initUI() {
      setTitle("Kino Valentana — Povzetek rezervacije");
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      setSize(740, 560);
      setLocationRelativeTo(null);

      JPanel main = new JPanel(new BorderLayout(10, 10));
      main.setBorder(new EmptyBorder(20, 20, 20, 20));

      JLabel title = new JLabel("Povzetek rezervacije", SwingConstants.CENTER);
      title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
      title.setBorder(new EmptyBorder(0, 0, 15, 0));

      JPanel content = new JPanel(new BorderLayout(8, 8));
      content.add(buildProjekcijaPanel(), BorderLayout.NORTH);
      content.add(buildVstopniceTable(), BorderLayout.CENTER);

      cenaLabel = new JLabel(" ", SwingConstants.CENTER);
      cenaLabel.setFont(cenaLabel.getFont().deriveFont(Font.BOLD, 20f));
      cenaLabel.setForeground(new Color(0, 100, 0));
      cenaLabel.setBorder(new EmptyBorder(15, 0, 15, 0));

      JButton potrdiButton = new JButton("Potrdi rezervacijo");
      potrdiButton.setFont(potrdiButton.getFont().deriveFont(Font.BOLD, 14f));
      potrdiButton.addActionListener(e -> potrdiRezervacijo());
      Stil.primarniGumb(potrdiButton);

      JButton preklicButton = new JButton("Prekliči");
      preklicButton.addActionListener(e -> preklici());
      Stil.sekundarniGumb(preklicButton);

      JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
      buttons.add(preklicButton);
      buttons.add(potrdiButton);

      JPanel south = new JPanel(new BorderLayout());
      south.add(cenaLabel, BorderLayout.NORTH);
      south.add(buttons, BorderLayout.SOUTH);

      main.add(title, BorderLayout.NORTH);
      main.add(content, BorderLayout.CENTER);
      main.add(south, BorderLayout.SOUTH);
      setContentPane(main);
   }

   private JPanel buildProjekcijaPanel() {
      JPanel p = new JPanel(new GridBagLayout());
      p.setBorder(new TitledBorder("Projekcija"));
      GridBagConstraints g = new GridBagConstraints();
      g.insets = new Insets(3, 8, 3, 8);
      g.anchor = GridBagConstraints.WEST;
      g.fill = GridBagConstraints.HORIZONTAL;

      Projekcija proj = rezervacija.getProjekcija();
      addRow(p, g, 0, "Film:", proj.getNaslovFilma() + " (" + proj.getLetoIzida() + ")");
      addRow(p, g, 1, "Datum in čas:",
         proj.getDatumInCas().format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy 'ob' HH:mm")));
      addRow(p, g, 2, "Dvorana:", proj.getDvorana());
      addRow(p, g, 3, "Trajanje:", proj.getTrajanje() + " minut");
      return p;
   }

   private void addRow(JPanel p, GridBagConstraints g, int row, String label, String value) {
      g.gridx = 0; g.gridy = row; g.weightx = 0;
      JLabel keyLabel = new JLabel(label);
      keyLabel.setFont(keyLabel.getFont().deriveFont(Font.BOLD));
      p.add(keyLabel, g);
      g.gridx = 1; g.weightx = 1;
      p.add(new JLabel(value), g);
   }

   private JScrollPane buildVstopniceTable() {
      String[] columns = {"#", "Stranka", "Status", "Sedež", "Cena"};
      List<Vstopnica> vstopnice = rezervacija.vrniVstopnice();
      Object[][] data = new Object[vstopnice.size()][5];
      for (int i = 0; i < vstopnice.size(); i++) {
         Vstopnica v = vstopnice.get(i);
         Stranka s = v.getStranka();
         data[i][0] = i + 1;
         data[i][1] = s.getIme() + " " + s.getPriimek();
         if (s instanceof Clan) {
            data[i][2] = "ČLAN (-" + ((Clan) s).getPopust() + "%)";
         } else {
            data[i][2] = "Navadna";
         }
         data[i][3] = v.getSedez().toString();
         data[i][4] = v.getKoncnaCena() + " €";
      }
      DefaultTableModel model = new DefaultTableModel(data, columns) {
         @Override public boolean isCellEditable(int r, int c) { return false; }
      };
      JTable table = new JTable(model);
      table.setRowHeight(28);
      table.setFont(table.getFont().deriveFont(13f));
      JTableHeader header = table.getTableHeader();
      header.setFont(header.getFont().deriveFont(Font.BOLD));
      JScrollPane scroll = new JScrollPane(table);
      scroll.setBorder(new TitledBorder("Vstopnice (" + vstopnice.size() + ")"));
      scroll.setPreferredSize(new Dimension(680, 220));
      return scroll;
   }

   public void prikaziPovzetekRezervacije() {
      // Populated in initUI via buildProjekcijaPanel + buildVstopniceTable
   }

   public void prikaziCeno() {
      BigDecimal skupaj = rezervacija.getSkupnaCena();
      int n = rezervacija.steviloVstopnic();
      cenaLabel.setText("SKUPAJ ZA " + n + " "
         + (n == 1 ? "vstopnico:  " : "vstopnice:  ") + skupaj + " €");
   }

   /*public void potrdiRezervacijo() {
      try {
         krmilnik.potrdiRezervacijo();
         prikaziSporocilo("Rezervacija je uspešno potrjena!\n\n"
            + rezervacija.steviloVstopnic() + " vstopnic za "
            + rezervacija.getProjekcija().getNaslovFilma() + ".");
         krmilnik.ponastavi();
         this.dispose();
         osnovnoOkno.ponoviPrikaz();
      } catch (Exception ex) {
         prikaziSporocilo("Napaka: " + ex.getMessage());
      }
   }*/

   public void potrdiRezervacijo() {
      // 1. Izbira načina plačila
      NacinPlacila[] opcije = NacinPlacila.values();
      int izbira = JOptionPane.showOptionDialog(this,
              "Izberite način plačila za znesek " + rezervacija.getSkupnaCena() + " €",
              "Plačilo",
              JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
              null, opcije, opcije[0]);

      if (izbira == -1) return; // Uporabnik je zaprl okno (Alternativni tok 3 - Preklic pred plačilom)

      NacinPlacila nacin = opcije[izbira];

      try {
         krmilnik.potrdiRezervacijo(nacin);
         
         // Ticket dialog s "Save as .txt" gumbom
         JTextArea ticketArea = new JTextArea(rezervacija.generirajIzpisVstopnic());
         ticketArea.setEditable(false);
         ticketArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
         ticketArea.setCaretPosition(0);
         JScrollPane ticketScroll = new JScrollPane(ticketArea);
         ticketScroll.setPreferredSize(new Dimension(520, 300));

         // Ustvari dialog s Save gumbom
         JPanel ticketPanel = new JPanel(new BorderLayout(10, 10));
         ticketPanel.add(ticketScroll, BorderLayout.CENTER);
         
         JButton saveButton = new JButton("Shrani kot .txt");
         saveButton.addActionListener(e -> shraniVstopnicoKotTxt(ticketArea.getText()));
         Stil.primarniGumb(saveButton);
         
         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
         buttonPanel.add(saveButton);
         ticketPanel.add(buttonPanel, BorderLayout.SOUTH);

         JOptionPane.showMessageDialog(this, ticketPanel,
                 "Natisnjena vstopnica", JOptionPane.INFORMATION_MESSAGE);

         prikaziSporocilo("Rezervacija in plačilo uspešna!\n\n"
                 + rezervacija.steviloVstopnic() + " vstopnic za "
                 + rezervacija.getProjekcija().getNaslovFilma() + ".");
         krmilnik.ponastavi();
         this.dispose();
         osnovnoOkno.ponoviPrikaz();
      } catch (Exception ex) {
         prikaziSporocilo("Plačilo ni bilo uspešno: " + ex.getMessage());
      }
   }

   private void shraniVstopnicoKotTxt(String vsebina) {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileFilter(new FileNameExtensionFilter("Text files (*.txt)", "txt"));
      fileChooser.setSelectedFile(new java.io.File("vstopnica.txt"));
      
      int rezultat = fileChooser.showSaveDialog(this);
      if (rezultat == JFileChooser.APPROVE_OPTION) {
         try {
            java.io.File datoteka = fileChooser.getSelectedFile();
            PrintWriter writer = new PrintWriter(new FileWriter(datoteka));
            writer.print(vsebina);
            writer.close();
            JOptionPane.showMessageDialog(this, 
               "Vstopnica je uspešno shranjena v:\n" + datoteka.getAbsolutePath(),
               "Uspeh", JOptionPane.INFORMATION_MESSAGE);
         } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
               "Napaka pri shranjevanju: " + ex.getMessage(),
               "Napaka", JOptionPane.ERROR_MESSAGE);
         }
      }
   }

   public void prikaziSporocilo(String sporocilo) {
      JOptionPane.showMessageDialog(this, sporocilo, "Sporočilo",
         JOptionPane.INFORMATION_MESSAGE);
   }

   private void preklici() {
      int odgovor = JOptionPane.showConfirmDialog(this,
         "Ali res želite preklicati rezervacijo?", "Potrditev",
         JOptionPane.YES_NO_OPTION);
      if (odgovor == JOptionPane.YES_OPTION) {
         krmilnik.ponastavi();
         this.dispose();
         osnovnoOkno.ponoviPrikaz();
      }
   }
}