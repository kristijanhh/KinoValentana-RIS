package kino;

public class Sedez {
   private int stevilkaVrstice;
   private int stevilkaSedeza;
   private TipSedeza tipSedeza;
   private boolean zaseden;

   public Sedez() {}

   public Sedez(int stevilkaVrstice, int stevilkaSedeza, TipSedeza tipSedeza) {
      this.stevilkaVrstice = stevilkaVrstice;
      this.stevilkaSedeza = stevilkaSedeza;
      this.tipSedeza = tipSedeza;
      this.zaseden = false;
   }

   public String vrniTipSedeza() {
      return tipSedeza.toString();
   }

   public int getStevilkaVrstice() { return stevilkaVrstice; }
   public int getStevilkaSedeza() { return stevilkaSedeza; }
   public TipSedeza getTipSedeza() { return tipSedeza; }
   public boolean isZaseden() { return zaseden; }
   public void setZaseden(boolean zaseden) { this.zaseden = zaseden; }

   @Override
   public String toString() {
      return "Vrsta " + stevilkaVrstice + ", sedež " + stevilkaSedeza + " (" + tipSedeza + ")";
   }

   @Override
   public boolean equals(Object obj) {

      if (this == obj) return true;

      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }

      Sedez other = (Sedez) obj;

      return stevilkaVrstice == other.stevilkaVrstice
              && stevilkaSedeza == other.stevilkaSedeza;
   }

   @Override
   public int hashCode() {
      return java.util.Objects.hash(
              stevilkaVrstice,
              stevilkaSedeza
      );
   }
}