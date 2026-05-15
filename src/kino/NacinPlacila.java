package kino;

public enum NacinPlacila {
   KARTICA("Kartica"),
   GOTOVINA("Gotovina");

   private final String label;

   NacinPlacila(String label) {
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
