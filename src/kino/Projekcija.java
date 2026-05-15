
package kino;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class Projekcija {
   // --- Screening info ---
   private String naslovFilma;
   private LocalDateTime datumInCas;
   private BigDecimal osnovnaCena;
   private String dvorana;

   // --- Movie metadata ---
   private int trajanje;
   private int letoIzida;
   private String zanr;
   private String reziser;
   private String igralci;
   private String opis;
   private List<Sedez> zasedeniSedezi = new ArrayList<>();

   public Projekcija() {}

   public Projekcija(String naslovFilma, LocalDateTime datumInCas, BigDecimal osnovnaCena) {
      this.naslovFilma = naslovFilma;
      this.datumInCas = datumInCas;
      this.osnovnaCena = osnovnaCena;
   }

   public Projekcija(String naslovFilma, LocalDateTime datumInCas, BigDecimal osnovnaCena,
                     int trajanje, int letoIzida, String zanr, String reziser,
                     String igralci, String opis, String dvorana) {
      this.naslovFilma = naslovFilma;
      this.datumInCas = datumInCas;
      this.osnovnaCena = osnovnaCena;
      this.trajanje = trajanje;
      this.letoIzida = letoIzida;
      this.zanr = zanr;
      this.reziser = reziser;
      this.igralci = igralci;
      this.opis = opis;
      this.dvorana = dvorana;
   }

   //public List<Sedez> vrniRazpolozljiveSedeze() {return Podatki.getProstiSedezi();}

   public List<Sedez> vrniRazpolozljiveSedeze() {
      List<Sedez> prosti = new ArrayList<>();

      for (Sedez s : Podatki.getVsiSedezi()) {
         if (!zasedeniSedezi.contains(s)) {
            prosti.add(s);
         }
      }

      return prosti;
   }

   public Projekcija vrniPodatke() {
      return this;
   }

   public static List<Projekcija> vrniVseProjekcije() {
      return Podatki.getProjekcije();
   }

   public String getNaslovFilma() { return naslovFilma; }
   public LocalDateTime getDatumInCas() { return datumInCas; }
   public BigDecimal getOsnovnaCena() { return osnovnaCena; }
   public String getDvorana() { return dvorana; }
   public int getTrajanje() { return trajanje; }
   public int getLetoIzida() { return letoIzida; }
   public String getZanr() { return zanr; }
   public String getReziser() { return reziser; }
   public String getIgralci() { return igralci; }
   public String getOpis() { return opis; }


   // --- Setters ---
   public void setNaslovFilma(String n) { this.naslovFilma = n; }
   public void setDatumInCas(LocalDateTime d) { this.datumInCas = d; }
   public void setOsnovnaCena(BigDecimal c) { this.osnovnaCena = c; }
   public void setDvorana(String d) { this.dvorana = d; }
   public void setTrajanje(int t) { this.trajanje = t; }
   public void setLetoIzida(int l) { this.letoIzida = l; }
   public void setZanr(String z) { this.zanr = z; }
   public void setReziser(String r) { this.reziser = r; }
   public void setIgralci(String i) { this.igralci = i; }
   public void setOpis(String o) { this.opis = o; }

   public void dodajZasedenSedez(Sedez sedez) {
      if (sedez != null && !zasedeniSedezi.contains(sedez)) {
         zasedeniSedezi.add(sedez);
      }
   }

   public boolean jeSedezZaseden(Sedez sedez) {
      return zasedeniSedezi.contains(sedez);
   }

   public List<Sedez> getZasedeniSedezi() {
      return new ArrayList<>(zasedeniSedezi);
   }

   @Override
   public String toString() {
      DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
      String dvor = (dvorana != null && !dvorana.isEmpty()) ? " — " + dvorana : "";
      return naslovFilma + "   " + datumInCas.format(fmt) + dvor + "   (" + osnovnaCena + " €)";
   }
}