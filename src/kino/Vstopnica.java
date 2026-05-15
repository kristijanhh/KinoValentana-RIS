package kino;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Vstopnica {
   private BigDecimal osnovnaCena;
   private Stranka stranka;
   private Sedez sedez;
   private BigDecimal koncnaCena;

   public Vstopnica() {}

   public Vstopnica(BigDecimal osnovnaCena) {
      this.osnovnaCena = osnovnaCena;
   }

   public Vstopnica(BigDecimal osnovnaCena, Stranka stranka, Sedez sedez, BigDecimal koncnaCena) {
      this.osnovnaCena = osnovnaCena;
      this.stranka = stranka;
      this.sedez = sedez;
      this.koncnaCena = koncnaCena;
   }

   public BigDecimal izracunajCeno() {
      return osnovnaCena;
   }

   public BigDecimal izracunajCenoSPopustom(int popustOdstotek) {
      BigDecimal popust = BigDecimal.valueOf(popustOdstotek);
      BigDecimal sto = BigDecimal.valueOf(100);
      BigDecimal mnozilec = sto.subtract(popust).divide(sto, 4, RoundingMode.HALF_UP);
      return osnovnaCena.multiply(mnozilec).setScale(2, RoundingMode.HALF_UP);
   }

   public BigDecimal getOsnovnaCena() { return osnovnaCena; }
   public Stranka getStranka() { return stranka; }
   public Sedez getSedez() { return sedez; }
   public BigDecimal getKoncnaCena() { return koncnaCena; }

   public void setOsnovnaCena(BigDecimal c) { this.osnovnaCena = c; }
   public void setStranka(Stranka s) { this.stranka = s; }
   public void setSedez(Sedez s) { this.sedez = s; }
   public void setKoncnaCena(BigDecimal c) { this.koncnaCena = c; }
}