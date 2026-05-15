package kino;

import java.time.LocalDate;

public class Clan extends Stranka {
   private int clanskaStevilka;
   private LocalDate datumVclanitve;
   private int popust;

   public Clan() { super(); }

   public Clan(String ime, String priimek, String telefonskaStevilka, String elektronskiNaslov,
               int clanskaStevilka, LocalDate datumVclanitve, int popust) {
      super(ime, priimek, telefonskaStevilka, elektronskiNaslov);
      this.clanskaStevilka = clanskaStevilka;
      this.datumVclanitve = datumVclanitve;
      this.popust = popust;
   }

   public int vrniPopust() {
      return popust;
   }

   public int getClanskaStevilka() { return clanskaStevilka; }
   public LocalDate getDatumVclanitve() { return datumVclanitve; }
   public int getPopust() { return popust; }

   public void setClanskaStevilka(int s) { this.clanskaStevilka = s; }
   public void setDatumVclanitve(LocalDate d) { this.datumVclanitve = d; }
   public void setPopust(int p) { this.popust = p; }

   //@Override
   //public String toString() {return super.toString() + " (član #" + clanskaStevilka + ", -" + popust + "%)";}

   @Override
   public String toString() {
      return getIme() + " " + getPriimek()
              + " | član #" + clanskaStevilka
              + " | popust: " + popust + "%";
   }
}