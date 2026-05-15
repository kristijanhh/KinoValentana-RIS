package kino;

public enum TipSedeza {
   STANDARDNI("Standardni"),
   VIP("VIP"),
   INVALIDSKI("Invalidski");

   private final String label;

   TipSedeza(String label) {
      this.label = label;
   }

   @Override
   public String toString() {
      return label;
   }

   public String getLabel() {
      return label;
   }
}
