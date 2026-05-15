package kino;

public class Stranka {
   private String ime;
   private String priimek;
   private String telefonskaStevilka;
   private String elektronskiNaslov;

   public Stranka() {}

   public Stranka(String ime, String priimek, String telefonskaStevilka, String elektronskiNaslov) {
      this.ime = ime;
      this.priimek = priimek;
      this.telefonskaStevilka = telefonskaStevilka;
      this.elektronskiNaslov = elektronskiNaslov;
   }

   public Stranka vrniPodatke() {
      return this;
   }

   public String getIme() { return ime; }
   public String getPriimek() { return priimek; }
   public String getTelefonskaStevilka() { return telefonskaStevilka; }
   public String getElektronskiNaslov() { return elektronskiNaslov; }

   public void setIme(String ime) { this.ime = ime; }
   public void setPriimek(String priimek) { this.priimek = priimek; }
   public void setTelefonskaStevilka(String t) { this.telefonskaStevilka = t; }
   public void setElektronskiNaslov(String e) { this.elektronskiNaslov = e; }

   @Override
   public String toString() {
      return ime + " " + priimek;
   }
}