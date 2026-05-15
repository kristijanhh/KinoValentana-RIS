# Kino Valentana — Rezervacija vstopnice

Seminarska naloga RIS 2025/2026 (2. del)

## Avtorja
- Ana Vlashki, 63210466
- Kristijan Handanovic, 63210101

## Opis
Namizna Java Swing aplikacija, ki realizira primer uporabe
**»Rezerviraj vstopnico«** za fiktivno kino podjetje Valentana d.d.

Aplikacija omogoča:
- Iskanje strank po e-naslovu, telefonski ali članski številki
- Sestavo skupine (1–N oseb) z mešanico navadnih strank in članov
- Izbiro filma in projekcije (datum, čas, dvorana)
- Izbiro več sedežev (točno toliko, kot je oseb v skupini)
- Avtomatski izračun cene s členskimi popusti (10–20 %)
- Pregled in potrditev rezervacije

## Zahteve
- Java 21 ali novejša (razvito in testirano na JDK 21.0.9 LTS)

## Arhitektura

Implementirano v skladu z VOPC diagramom iz 1. dela seminarske naloge.

### Mejni razredi (Boundary)
- `ZmPregledProjekcij` — glavno okno (iskanje stranke, skupina, izbira filma in projekcije)
- `ZmPregledSedezev` — okno za izbiro sedežev
- `ZmRezervacija` — okno za pregled in potrditev rezervacije


### Krmilni razred (Control)
- `KRezervacija` — koordinira potek rezervacije, hrani trenutno stanje

### Entitetni razredi (Entity)
- `Stranka` — osnovni razred stranke
- `Clan` — član kina (deduje od `Stranka`, dodaten popust)
- `Projekcija` — projekcija filma (datum/čas, dvorana, cena, metapodatki o filmu)
- `Sedez` — sedež v dvorani 
- `Rezervacija` — rezervacija skupine, vsebuje seznam vstopnic
- `Vstopnica` — posamezna vstopnica (stranka + sedež + končna cena)
- `TipSedeza` — pomozni enum razred za tip sedeža (tip: Standardni / VIP / Invalidski)

### Pomožna razreda
- `Podatki` — simulacija trajnosti (hardcoded testni podatki ob zagonu)
- `Main` — vstopna točka programa
- `BancniSistem_SIM` — simulacija bančnega sistema
- `NacinPlacila` — enum razred za tip plačila (tip: kartica/gotovina)

## Testni podatki

### Stranke za testiranje

| Tip | Ime | Telefon | E-naslov | Članska št. | Popust |
|-----|-----|---------|----------|-------------|--------|
| Stranka | Janez Novak | 041-123-456 | janez.novak@example.com | — | — |
| Stranka | Marija Kovač | 031-987-654 | marija.kovac@example.com | — | — |
| Stranka | Ana Krajnc | 040-222-333 | ana.krajnc@example.com | — | — |
| Član | Petra Horvat | 070-111-222 | petra.horvat@example.com | 1001 | 15 % |
| Član | Marko Zupan | 051-555-333 | marko.zupan@example.com | 1002 | 20 % |
| Član | Luka Bizjak | 064-789-456 | luka.bizjak@example.com | 1003 | 10 % |

### Filmi in projekcije
5 filmov, 11 projekcij v dvoranah 1, 2 in 3 v obdobju 15.–18. maj 2026
(Oppenheimer, Barbie, Dune: Part Two, Inside Out 2, Bratovščina prstana).
Cene 6.50 € – 9.00 €.

### Dvorana
5 vrst × 8 sedežev = 40 sedežev na projekcijo.
Vrsta 1 = invalidski (modri), vrsta 5 = VIP (rumeni), ostalo = standardni (sivi).

## Scenarij testiranja

1. Vnesite e-naslov `petra.horvat@example.com` → najde člana s popustom 15 %
2. Kliknite **+ Dodaj v skupino**
3. Vnesite člansko številko `1002` → Marko Zupan, popust 20 % → dodajte v skupino
4. Skupina sedaj šteje 2 osebi
5. Izberite film (npr. Oppenheimer) → **Naprej**
6. Izberite projekcijo → **Naprej**
7. Izberite 2 sedeža (npr. en VIP, en navadni)
8. Pregled rezervacije prikaže končno ceno z upoštevanimi popusti
9. **Potrdi rezervacijo** — sedeža sta rezervirana
10. Izberite način plačila (kartica/gotovina)
11. Izpis natisnjene vstopnice (možnost shranitve kot .txt)
12. Potrditev rezervacije in plačila

## Skladnost z načrtom

- Vsi razredi so bili generirani neposredno iz PowerDesigner modela 1. dela
- Strukture razredov (atributi, asociacije, signature operacij) niso bile spremenjene
- Vsa PowerDesigner boilerplate koda (`@pdOid`, `@pdRoleInfo`, `@pdGenerated`) je ohranjena
- Dodane so bile samo notranje metode za delovanje, skladno z navodili točke 5 naloge
- Imena razredov in metod ustrezajo VOPC diagramu (drobne prilagoditve za Java naming:
  `KRezervacija` namesto `K_Rezervacija`, `ZmPregledProjekcij` namesto `ZM_pregledProjekcij`)
- Trajnost je simulirana z razredom `Podatki` (hardcoded testni podatki),
  skladno z navodili točke 4 naloge


