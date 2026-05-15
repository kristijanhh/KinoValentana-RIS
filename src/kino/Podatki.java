package kino;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Podatki {

   private static final List<Stranka> stranke = new ArrayList<>();
   private static final List<Projekcija> projekcije = new ArrayList<>();
   private static final List<Sedez> sedezi = new ArrayList<>();
   private static final List<Rezervacija> rezervacije = new ArrayList<>();

   private static boolean inicializirano = false;

   public static void inicializiraj() {
      if (inicializirano) return;
      inicializirano = true;
      napolniStranke();
      napolniSedeze();
      napolniProjekcije();
      projekcije.get(0).dodajZasedenSedez(sedezi.get(0));
      projekcije.get(0).dodajZasedenSedez(sedezi.get(1));
      projekcije.get(1).dodajZasedenSedez(sedezi.get(5));
   }

   private static void napolniStranke() {
      stranke.add(new Stranka("Janez", "Novak", "041-123-456", "janez.novak@example.com"));
      stranke.add(new Stranka("Marija", "Kovač", "031-987-654", "marija.kovac@example.com"));
      stranke.add(new Stranka("Ana", "Krajnc", "040-222-333", "ana.krajnc@example.com"));

      //VIP stranke
      stranke.add(new Clan("Petra", "Horvat", "070-111-222", "petra.horvat@example.com",
                           1001, LocalDate.of(2023, 5, 14), 15));
      stranke.add(new Clan("Marko", "Zupan", "051-555-333", "marko.zupan@example.com",
                           1002, LocalDate.of(2024, 1, 8), 20));
      stranke.add(new Clan("Luka", "Bizjak", "064-789-456", "luka.bizjak@example.com",
                           1003, LocalDate.of(2024, 11, 20), 10));
   }

   private static void napolniSedeze() {
      for (int vrsta = 1; vrsta <= 5; vrsta++) {
         for (int sedez = 1; sedez <= 8; sedez++) {
            String tip;
            if (vrsta == 1) tip = "Invalidski";
            else if (vrsta == 5) tip = "VIP";
            else tip = "Standardni";
            sedezi.add(new Sedez(vrsta, sedez, tip));
         }
      }
   }

   private static void napolniProjekcije() {

      // --- Oppenheimer ---
      String oppOpis = "Zgodba o ameriškem znanstveniku J. Robertu Oppenheimerju in njegovi vlogi pri razvoju atomske bombe.";
      String oppZanr = "Biografija, Drama, Zgodovinski";
      String oppRez  = "Christopher Nolan";
      String oppIgr  = "Cillian Murphy, Emily Blunt, Matt Damon, Robert Downey Jr.";
      projekcije.add(new Projekcija("Oppenheimer",
         LocalDateTime.of(2026, 5, 15, 18, 0), new BigDecimal("8.50"),
         180, 2023, oppZanr, oppRez, oppIgr, oppOpis, "Dvorana 1"));
      projekcije.add(new Projekcija("Oppenheimer",
         LocalDateTime.of(2026, 5, 15, 21, 0), new BigDecimal("8.50"),
         180, 2023, oppZanr, oppRez, oppIgr, oppOpis, "Dvorana 2"));
      projekcije.add(new Projekcija("Oppenheimer",
         LocalDateTime.of(2026, 5, 16, 19, 30), new BigDecimal("8.50"),
         180, 2023, oppZanr, oppRez, oppIgr, oppOpis, "Dvorana 1"));

      // --- Barbie ---
      String barbOpis = "Barbie in Ken doživita pustolovščino, ko se podata iz Barbie sveta v človeški svet.";
      String barbZanr = "Komedija, Fantazija, Avantura";
      String barbRez  = "Greta Gerwig";
      String barbIgr  = "Margot Robbie, Ryan Gosling, America Ferrera, Kate McKinnon";
      projekcije.add(new Projekcija("Barbie",
         LocalDateTime.of(2026, 5, 15, 20, 30), new BigDecimal("7.50"),
         114, 2023, barbZanr, barbRez, barbIgr, barbOpis, "Dvorana 3"));
      projekcije.add(new Projekcija("Barbie",
         LocalDateTime.of(2026, 5, 16, 17, 0), new BigDecimal("7.50"),
         114, 2023, barbZanr, barbRez, barbIgr, barbOpis, "Dvorana 2"));

      // --- Dune: Part Two ---
      String duneOpis = "Paul Atreides nadaljuje svojo pot maščevanja proti zaroti, ki je uničila njegovo družino.";
      String duneZanr = "Znanstvena fantastika, Avantura, Drama";
      String duneRez  = "Denis Villeneuve";
      String duneIgr  = "Timothée Chalamet, Zendaya, Rebecca Ferguson, Javier Bardem";
      projekcije.add(new Projekcija("Dune: Part Two",
         LocalDateTime.of(2026, 5, 16, 21, 0), new BigDecimal("9.00"),
         166, 2024, duneZanr, duneRez, duneIgr, duneOpis, "Dvorana 1"));
      projekcije.add(new Projekcija("Dune: Part Two",
         LocalDateTime.of(2026, 5, 17, 18, 30), new BigDecimal("9.00"),
         166, 2024, duneZanr, duneRez, duneIgr, duneOpis, "Dvorana 3"));

      // --- Inside Out 2 ---
      String ioOpis = "Riley se sooča z novimi čustvi, ko vstopa v puberteto.";
      String ioZanr = "Animacija, Družinski, Komedija";
      String ioRez  = "Kelsey Mann";
      String ioIgr  = "Amy Poehler, Maya Hawke, Lewis Black, Phyllis Smith";
      projekcije.add(new Projekcija("Inside Out 2",
         LocalDateTime.of(2026, 5, 16, 16, 30), new BigDecimal("6.50"),
         96, 2024, ioZanr, ioRez, ioIgr, ioOpis, "Dvorana 2"));
      projekcije.add(new Projekcija("Inside Out 2",
         LocalDateTime.of(2026, 5, 17, 16, 0), new BigDecimal("6.50"),
         96, 2024, ioZanr, ioRez, ioIgr, ioOpis, "Dvorana 3"));

      // --- The Lord of the Rings: The Fellowship of the Ring (retro screening) ---
      String lotrOpis = "Mladi hobit Frodo Baggins se z družbo prijateljev odpravi na pustolovščino, da bi uničil mogočni Edinstveni prstan.";
      String lotrZanr = "Fantazija, Avantura, Drama";
      String lotrRez  = "Peter Jackson";
      String lotrIgr  = "Elijah Wood, Ian McKellen, Viggo Mortensen, Orlando Bloom, Sean Astin";
      projekcije.add(new Projekcija("Bratovščina prstana",
         LocalDateTime.of(2026, 5, 17, 19, 0), new BigDecimal("8.00"),
         178, 2001, lotrZanr, lotrRez, lotrIgr, lotrOpis, "Dvorana 1"));
      projekcije.add(new Projekcija("Bratovščina prstana",
         LocalDateTime.of(2026, 5, 18, 20, 0), new BigDecimal("8.00"),
         178, 2001, lotrZanr, lotrRez, lotrIgr, lotrOpis, "Dvorana 2"));
   }

   // === Public access ===

   public static List<Stranka> getStranke() {
      return new ArrayList<>(stranke);
   }

   public static List<Projekcija> getProjekcije() {
      return new ArrayList<>(projekcije);
   }

   public static List<Sedez> getVsiSedezi() {
      return new ArrayList<>(sedezi);
   }

   public static List<Rezervacija> getRezervacije() {
      return new ArrayList<>(rezervacije);
   }

   /** Find a single customer by exact first + last name (returns first match). */
   public static Stranka najdiStranko(String ime, String priimek) {
      for (Stranka s : stranke) {
         if (s.getIme().equalsIgnoreCase(ime) && s.getPriimek().equalsIgnoreCase(priimek)) {
            return s;
         }
      }
      return null;
   }

   //Find ALL customers matching first + last name
   public static List<Stranka> najdiStrankePoImenu(String ime, String priimek) {
      List<Stranka> rezultati = new ArrayList<>();
      if (ime == null || priimek == null) return rezultati;
      for (Stranka s : stranke) {
         if (s.getIme().equalsIgnoreCase(ime) && s.getPriimek().equalsIgnoreCase(priimek)) {
            rezultati.add(s);
         }
      }
      return rezultati;
   }

   public static Stranka najdiClanaPoStevilki(int clanskaStevilka) {
      for (Stranka s : stranke) {
         if (s instanceof Clan && ((Clan) s).getClanskaStevilka() == clanskaStevilka) {
            return s;
         }
      }
      return null;
   }

   public static Stranka najdiStrankoPoEmailu(String email) {
      if (email == null || email.trim().isEmpty()) return null;
      String iskan = email.trim();
      for (Stranka s : stranke) {
         if (s.getElektronskiNaslov() != null
             && s.getElektronskiNaslov().equalsIgnoreCase(iskan)) {
            return s;
         }
      }
      return null;
   }

   public static Stranka najdiStrankoPoTelefonu(String telefon) {
      if (telefon == null || telefon.trim().isEmpty()) return null;
      String iskan = telefon.replaceAll("[\\s\\-]", "");
      for (Stranka s : stranke) {
         if (s.getTelefonskaStevilka() != null) {
            String norm = s.getTelefonskaStevilka().replaceAll("[\\s\\-]", "");
            if (norm.equalsIgnoreCase(iskan)) return s;
         }
      }
      return null;
   }

   public static void dodajRezervacijo(Rezervacija r) {
      rezervacije.add(r);
   }

   public static List<Sedez> getProstiSedezi() {
      List<Sedez> prosti = new ArrayList<>();
      for (Sedez s : sedezi) {
         if (!s.isZaseden()) prosti.add(s);
      }
      return prosti;
   }
}