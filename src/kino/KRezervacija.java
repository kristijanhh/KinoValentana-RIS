package kino;

import java.util.*;

public class KRezervacija {

   public java.util.Collection<Sedez> sedez;
   public java.util.Collection<Vstopnica> vstopnica;
   public java.util.Collection<Stranka> stranka;
   public java.util.Collection<Projekcija> projekcija;
   public java.util.Collection<Rezervacija> rezervacija;

   private final java.util.List<Stranka> trenutneStranke = new java.util.ArrayList<>();
   private final java.util.List<Sedez> trenutniSedezi = new java.util.ArrayList<>();
   private Projekcija trenutnaProjekcija;
   private Rezervacija zadnjaRezervacija;
   public List<Projekcija> pregledajProjekcije() {
      return Projekcija.vrniVseProjekcije();
   }

   public List<Sedez> pregledajSedeze(Projekcija projekcija) {
      this.trenutnaProjekcija = projekcija;
      return projekcija.vrniRazpolozljiveSedeze();
   }

   /*public void izbereSedez(Sedez sedez) {
      if (sedez != null && !trenutniSedezi.contains(sedez)) {
         trenutniSedezi.add(sedez);
      }
   }*/

   public void izbereSedez(Sedez sedez) {

      if (sedez == null) {
         throw new IllegalArgumentException("Sedež ne obstaja.");
      }

      if (trenutnaProjekcija.jeSedezZaseden(sedez)) {
         throw new IllegalStateException("Sedež je že zaseden.");
      }

      if (trenutniSedezi.contains(sedez)) {
         throw new IllegalStateException("Sedež je že izbran.");
      }

      trenutniSedezi.add(sedez);
   }
   public Rezervacija ustvariRezervacijo() {
      if (trenutnaProjekcija == null) {
         throw new IllegalStateException("Projekcija ni izbrana.");
      }

      if (trenutneStranke.isEmpty()) {
         throw new IllegalStateException("Ni dodanih strank.");
      }

      if (trenutniSedezi.isEmpty()) {
         throw new IllegalStateException("Ni izbranih sedežev.");
      }

      if (trenutneStranke.size() != trenutniSedezi.size()) {
         throw new IllegalStateException(
                 "Število strank mora biti enako številu sedežev."
         );
      }

      if (trenutnaProjekcija == null || trenutniSedezi.isEmpty() || trenutneStranke.isEmpty()) {
         throw new IllegalStateException("Manjkajo podatki za rezervacijo.");
      }
      int n = Math.min(trenutneStranke.size(), trenutniSedezi.size());
      List<Vstopnica> vstopnice = new ArrayList<>();
      java.math.BigDecimal skupaj = java.math.BigDecimal.ZERO;

      for (int i = 0; i < n; i++) {
         Stranka stranka = trenutneStranke.get(i);
         Sedez sedez = trenutniSedezi.get(i);
         java.math.BigDecimal osnova = trenutnaProjekcija.getOsnovnaCena();
         Vstopnica tempV = new Vstopnica(osnova);
         java.math.BigDecimal koncna = preveriClanstvo(stranka)
            ? tempV.izracunajCenoSPopustom(((Clan) stranka).getPopust())
            : tempV.izracunajCeno();
         vstopnice.add(new Vstopnica(osnova, stranka, sedez, koncna));
         skupaj = skupaj.add(koncna);
      }

      Rezervacija r = new Rezervacija(trenutneStranke, trenutnaProjekcija, vstopnice, skupaj);
      this.zadnjaRezervacija = r;
      return r;
   }

   public Vstopnica ustvariVstopnico() {
      if (trenutnaProjekcija == null) {
         throw new IllegalStateException("Projekcija ni izbrana.");
      }
      return new Vstopnica(trenutnaProjekcija.getOsnovnaCena());
   }

   public boolean preveriClanstvo(Stranka stranka) {
      return stranka instanceof Clan;
   }

   public void potrdiRezervacijo() {
      if (zadnjaRezervacija == null) {
         throw new IllegalStateException("Ni rezervacije za potrditev.");
      }
      zadnjaRezervacija.ustvariRezervacijo();
   }

   public void potrdiRezervacijo(String nacinPlacila) throws Exception {

      if (zadnjaRezervacija == null) {
         throw new Exception("Ni pripravljene rezervacije.");
      }

      if (nacinPlacila == null) {
         throw new Exception("Način plačila ni izbran.");
      }

      boolean placiloUspesno =
              BancniSistem_SIM.procesirajPlacilo(
                      zadnjaRezervacija.getSkupnaCena(),
                      nacinPlacila
              );

      if (!placiloUspesno) {
         throw new Exception(
                 "Plačilo ni bilo uspešno. Poskusite ponovno."
         );
      }

      zadnjaRezervacija.ustvariRezervacijo();
   }

   public void prekliciRezervacijo() {

      trenutniSedezi.clear();

      zadnjaRezervacija = null;

      trenutnaProjekcija = null;
   }

   public void ponastavi() {
      this.trenutnaProjekcija = null;
      this.trenutniSedezi.clear();
      this.trenutneStranke.clear();
      this.zadnjaRezervacija = null;

      if (this.stranka != null) this.stranka.clear();
      if (this.sedez != null) this.sedez.clear();
      if (this.vstopnica != null) this.vstopnica.clear();
      removeAllRezervacija();
   }

   // To dodaj v KRezervacija.java pod metodo ponastavi()
   public boolean soSedeziVistiVrsti() {
      if (this.trenutniSedezi.size() <= 1) return true;

      // Vzamemo vrstico prvega izbranega sedeža
      int prvaVrsta = this.trenutniSedezi.get(0).getStevilkaVrstice();

      // Preverimo, če so vsi ostali v isti vrsti
      for (Sedez s : this.trenutniSedezi) {
         if (s.getStevilkaVrstice() != prvaVrsta) {
            return false; // Sedeži so v različnih vrstah
         }
      }
      return true;
   }

   public void dodajStranko(Stranka s) {
      if (s != null && !trenutneStranke.contains(s)) trenutneStranke.add(s);
   }
   public void odstraniStranko(Stranka s) { trenutneStranke.remove(s); }
   public int steviloOseb() { return trenutneStranke.size(); }
   public List<Stranka> getTrenutneStranke() { return new ArrayList<>(trenutneStranke); }

   public void preklopiSedez(Sedez s) {
      if (s == null) {
         throw new IllegalArgumentException("Sedež ne obstaja.");
      }
      if (trenutniSedezi.contains(s)) {
         trenutniSedezi.remove(s);
         return;
      }
      if (trenutnaProjekcija != null && trenutnaProjekcija.jeSedezZaseden(s)) {
         throw new IllegalStateException("Sedež je že zaseden in ga ni mogoče izbrati.");
      }
      trenutniSedezi.add(s);
   }
   public boolean jeSedezIzbran(Sedez s) { return trenutniSedezi.contains(s); }
   public int steviloIzbranihSedezev() { return trenutniSedezi.size(); }
   public List<Sedez> getTrenutniSedezi() { return new ArrayList<>(trenutniSedezi); }

   public Projekcija getTrenutnaProjekcija() { return trenutnaProjekcija; }
   public Rezervacija getZadnjaRezervacija() { return zadnjaRezervacija; }


   public java.util.Collection<Sedez> getSedez() {
      if (sedez == null)
         sedez = new java.util.HashSet<>();
      return sedez;
   }
   public java.util.Iterator<Sedez> getIteratorSedez() {
      if (sedez == null)
         sedez = new java.util.HashSet<>();
      return sedez.iterator();
   }

   public void setSedez(java.util.Collection<Sedez> newSedez) {
      removeAllSedez();
      for (java.util.Iterator<Sedez> iter = newSedez.iterator(); iter.hasNext();)
         addSedez(iter.next());
   }

   public void addSedez(Sedez newSedez) {
      if (newSedez == null)
         return;
      if (this.sedez == null)
         this.sedez = new java.util.HashSet<>();
      if (!this.sedez.contains(newSedez))
         this.sedez.add(newSedez);
   }

   public void removeSedez(Sedez oldSedez) {
      if (oldSedez == null)
         return;
      if (this.sedez != null)
         if (this.sedez.contains(oldSedez))
            this.sedez.remove(oldSedez);
   }

   public void removeAllSedez() {
      if (sedez != null)
         sedez.clear();
   }

   public java.util.Collection<Vstopnica> getVstopnica() {
      if (vstopnica == null)
         vstopnica = new java.util.HashSet<>();
      return vstopnica;
   }

   public java.util.Iterator<Vstopnica> getIteratorVstopnica() {
      if (vstopnica == null)
         vstopnica = new java.util.HashSet<>();
      return vstopnica.iterator();
   }

   public void setVstopnica(java.util.Collection<Vstopnica> newVstopnica) {
      removeAllVstopnica();
      for (java.util.Iterator<Vstopnica> iter = newVstopnica.iterator(); iter.hasNext();)
         addVstopnica(iter.next());
   }
   public void addVstopnica(Vstopnica newVstopnica) {
      if (newVstopnica == null)
         return;
      if (this.vstopnica == null)
         this.vstopnica = new java.util.HashSet<>();
      if (!this.vstopnica.contains(newVstopnica))
         this.vstopnica.add(newVstopnica);
   }

   public void removeVstopnica(Vstopnica oldVstopnica) {
      if (oldVstopnica == null)
         return;
      if (this.vstopnica != null)
         if (this.vstopnica.contains(oldVstopnica))
            this.vstopnica.remove(oldVstopnica);
   }

   public void removeAllVstopnica() {
      if (vstopnica != null)
         vstopnica.clear();
   }

   public java.util.Collection<Stranka> getStranka() {
      if (stranka == null)
         stranka = new java.util.HashSet<>();
      return stranka;
   }

   public java.util.Iterator<Stranka> getIteratorStranka() {
      if (stranka == null)
         stranka = new java.util.HashSet<>();
      return stranka.iterator();
   }

   public void setStranka(java.util.Collection<Stranka> newStranka) {
      removeAllStranka();
      for (java.util.Iterator<Stranka> iter = newStranka.iterator(); iter.hasNext();)
         addStranka(iter.next());
   }

   public void addStranka(Stranka newStranka) {
      if (newStranka == null)
         return;
      if (this.stranka == null)
         this.stranka = new java.util.HashSet<>();
      if (!this.stranka.contains(newStranka))
         this.stranka.add(newStranka);
   }

   public void removeStranka(Stranka oldStranka) {
      if (oldStranka == null)
         return;
      if (this.stranka != null)
         if (this.stranka.contains(oldStranka))
            this.stranka.remove(oldStranka);
   }

   public void removeAllStranka() {
      if (stranka != null)
         stranka.clear();
   }

   public java.util.Collection<Projekcija> getProjekcija() {
      if (projekcija == null)
         projekcija = new java.util.HashSet<>();
      return projekcija;
   }

   public java.util.Iterator<Projekcija> getIteratorProjekcija() {
      if (projekcija == null)
         projekcija = new java.util.HashSet<>();
      return projekcija.iterator();
   }

   public void setProjekcija(java.util.Collection<Projekcija> newProjekcija) {
      removeAllProjekcija();
      for (java.util.Iterator<Projekcija> iter = newProjekcija.iterator(); iter.hasNext();)
         addProjekcija(iter.next());
   }

   public void addProjekcija(Projekcija newProjekcija) {
      if (newProjekcija == null)
         return;
      if (this.projekcija == null)
         this.projekcija = new java.util.HashSet<>();
      if (!this.projekcija.contains(newProjekcija))
         this.projekcija.add(newProjekcija);
   }

   public void removeProjekcija(Projekcija oldProjekcija) {
      if (oldProjekcija == null)
         return;
      if (this.projekcija != null)
         if (this.projekcija.contains(oldProjekcija))
            this.projekcija.remove(oldProjekcija);
   }

   public void removeAllProjekcija() {
      if (projekcija != null)
         projekcija.clear();
   }

   public java.util.Collection<Rezervacija> getRezervacija() {
      if (rezervacija == null)
         rezervacija = new java.util.HashSet<>();
      return rezervacija;
   }

   public java.util.Iterator<Rezervacija> getIteratorRezervacija() {
      if (rezervacija == null)
         rezervacija = new java.util.HashSet<>();
      return rezervacija.iterator();
   }

   public void setRezervacija(java.util.Collection<Rezervacija> newRezervacija) {
      removeAllRezervacija();
      for (java.util.Iterator<Rezervacija> iter = newRezervacija.iterator(); iter.hasNext();)
         addRezervacija(iter.next());
   }

   public void addRezervacija(Rezervacija newRezervacija) {
      if (newRezervacija == null)
         return;
      if (this.rezervacija == null)
         this.rezervacija = new java.util.HashSet<>();
      if (!this.rezervacija.contains(newRezervacija))
         this.rezervacija.add(newRezervacija);
   }

   public void removeRezervacija(Rezervacija oldRezervacija) {
      if (oldRezervacija == null)
         return;
      if (this.rezervacija != null)
         if (this.rezervacija.contains(oldRezervacija))
            this.rezervacija.remove(oldRezervacija);
   }
   public void removeAllRezervacija() {
      if (rezervacija != null)
         rezervacija.clear();
   }

}