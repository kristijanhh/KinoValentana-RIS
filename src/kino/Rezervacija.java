package kino;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Rezervacija {
   private LocalDateTime datumInCas;
   private List<Stranka> stranke;
   private Projekcija projekcija;
   private List<Vstopnica> vstopnice;
   private BigDecimal skupnaCena;

   public Rezervacija() {
      this.stranke = new ArrayList<>();
      this.vstopnice = new ArrayList<>();
   }

   public Rezervacija(List<Stranka> stranke, Projekcija projekcija,
                      List<Vstopnica> vstopnice, BigDecimal skupnaCena) {
      this.datumInCas = LocalDateTime.now();
      this.stranke = new ArrayList<>(stranke);
      this.projekcija = projekcija;
      this.vstopnice = new ArrayList<>(vstopnice);
      this.skupnaCena = skupnaCena;
   }

   public Rezervacija ustvariRezervacijo() {
      Podatki.dodajRezervacijo(this);
      for (Vstopnica v : vstopnice) {
         if (v.getSedez() != null) {
            projekcija.dodajZasedenSedez(v.getSedez());
            //v.getSedez().setZaseden(true);
         }
      }
      return this;
   }

   public String generirajIzpisVstopnic() {

      StringBuilder sb = new StringBuilder();

      sb.append("=== VSTOPNICE ===\n");
      sb.append("Film: ")
              .append(projekcija.getNaslovFilma())
              .append("\n");

      sb.append("Datum: ")
              .append(projekcija.getDatumInCas())
              .append("\n\n");

      for (Vstopnica v : vstopnice) {

         sb.append(v.getStranka())
                 .append(" | ");

         sb.append(v.getSedez())
                 .append(" | ");

         sb.append(v.getKoncnaCena())
                 .append(" EUR\n");
      }

      sb.append("\nSkupaj: ")
              .append(skupnaCena)
              .append(" EUR");

      return sb.toString();
   }

   public List<Vstopnica> vrniVstopnice() {
      return new ArrayList<>(vstopnice);
   }

   public LocalDateTime getDatumInCas() { return datumInCas; }
   public List<Stranka> getStranke() { return new ArrayList<>(stranke); }
   public Projekcija getProjekcija() { return projekcija; }
   public BigDecimal getSkupnaCena() { return skupnaCena; }
   public int steviloVstopnic() { return vstopnice.size(); }

   public void setDatumInCas(LocalDateTime d) { this.datumInCas = d; }
   public void setStranke(List<Stranka> s) { this.stranke = new ArrayList<>(s); }
   public void setProjekcija(Projekcija p) { this.projekcija = p; }
   public void setVstopnice(List<Vstopnica> v) { this.vstopnice = new ArrayList<>(v); }
   public void setSkupnaCena(BigDecimal c) { this.skupnaCena = c; }
}