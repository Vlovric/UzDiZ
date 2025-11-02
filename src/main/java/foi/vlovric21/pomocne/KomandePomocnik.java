package foi.vlovric21.pomocne;

import foi.vlovric21.objekti.Aranzman;
import foi.vlovric21.objekti.Rezervacija;
import foi.vlovric21.objekti.RezervacijaStatus;
import foi.vlovric21.singleton.RepozitorijPodataka;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KomandePomocnik {
    private static String porukaPogreske = "Neispravani ili nedostajući parametri za komandu.";
    private static RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
    private static RezervacijaPomocnik rezervacijaPomocnik = new RezervacijaPomocnik();

    private Matcher provjeriRegex(Pattern regex, String unos){
        Matcher matcher = regex.matcher(unos.trim());
        if(!matcher.matches()){
            System.out.println(porukaPogreske);
            return null;
        }
        return matcher;
    }

    private void ispisiAranzmaneTablicaITAK(List<Aranzman> aranzmani){
        System.out.printf("%-8s %-30s %-18s %-18s %-18s %-18s %-10s %-18s %-18s%n",
                "Oznaka", "Naziv", "Početni datum", "Završni datum", "Vrijeme kretanja",
                "Vrijeme povratka", "Cijena", "Min broj putnika", "Maks broj putnika");
        System.out.println("-".repeat(160));

        for(Aranzman a : aranzmani){
            System.out.printf("%-8d %-30s %-18s %-18s %-18s %-18s %-10d %-18d %-18d%n",
                    a.getOznaka(),
                    skratiTekst(a.getNaziv(), 30),
                    a.getPocetniDatum(),
                    a.getZavrsniDatum(),
                    a.getVrijemeKretanja() != null ? a.getVrijemeKretanja() : "",
                    a.getVrijemePovratka() != null ? a.getVrijemePovratka() : "",
                    a.getCijena(),
                    a.getMinBrojPutnika(),
                    a.getMaxBrojPutnika());
        }
        System.out.println();
    }

    private void ispisiAranzmaneTablicaITAP(Aranzman aranzman){
        System.out.printf("%-25s: %d%n", "Oznaka", aranzman.getOznaka());
        System.out.println("-".repeat(60));

        System.out.printf("%-25s: %s%n", "Naziv", aranzman.getNaziv());
        System.out.printf("%-25s: %s%n", "Program", aranzman.getProgram());
        System.out.println("-".repeat(60));

        System.out.printf("%-25s: %s%n", "Početni datum", aranzman.getPocetniDatum());
        System.out.printf("%-25s: %s%n", "Završni datum", aranzman.getZavrsniDatum());
        System.out.printf("%-25s: %s%n", "Vrijeme kretanja", aranzman.getVrijemeKretanja() != null ? aranzman.getVrijemeKretanja() : "");
        System.out.printf("%-25s: %s%n", "Vrijeme povratka", aranzman.getVrijemePovratka() != null ? aranzman.getVrijemePovratka() : "");
        System.out.println("-".repeat(60));

        System.out.printf("%-25s: %d%n", "Cijena", aranzman.getCijena());
        System.out.printf("%-25s: %s%n", "Doplata za 1/1 sobu", aranzman.getDoplataZaJednokrevetnuSobu() != null ? aranzman.getDoplataZaJednokrevetnuSobu().toString() : "");
        System.out.println("-".repeat(60));

        System.out.printf("%-25s: %d%n", "Min broj putnika", aranzman.getMinBrojPutnika());
        System.out.printf("%-25s: %d%n", "Maks broj putnika", aranzman.getMaxBrojPutnika());
        System.out.printf("%-25s: %d%n", "Broj noćenja", aranzman.getBrojNocenja());
        System.out.println("-".repeat(60));

        System.out.printf("%-25s: %s%n", "Prijevoz", aranzman.getPrijevoz() != null ? aranzman.getPrijevoz() : "");
        System.out.println("-".repeat(60));

        System.out.printf("%-25s: %s%n", "Broj doručka", aranzman.getBrojDorucka() != null ? aranzman.getBrojDorucka().toString() : "");
        System.out.printf("%-25s: %s%n", "Broj ručkova", aranzman.getBrojRuckova() != null ? aranzman.getBrojRuckova().toString() : "");
        System.out.printf("%-25s: %s%n", "Broj večera", aranzman.getBrojVecera() != null ? aranzman.getBrojVecera().toString() : "");
        System.out.println();
    }

    private String skratiTekst(String tekst, int maxDuzina){
        if(tekst == null) return "";
        return tekst.length() <= maxDuzina ? tekst : tekst.substring(0, maxDuzina - 3) + "...";
    }

    public void pregledAranzmanRazdobljeITAK(String unos){
        String uzorak = "^ITAK(?:\\s+([1-9]|[12]\\d|3[01])\\.([1-9]|1[0-2])\\.(\\d{4})\\.\\s+([1-9]|[12]\\d|3[01])\\.([1-9]|1[0-2])\\.(\\d{4})\\.)?$";

        Pattern regex = Pattern.compile(uzorak);
        Matcher matcher = provjeriRegex(regex, unos);
        if(matcher == null){
            return;
        }

        String pocetniDatum = null;
        String zavrsniDatum = null;

        if(matcher.group(1) != null){
            pocetniDatum = formatirajDatum(matcher.group(1), matcher.group(2), matcher.group(3));
            zavrsniDatum = formatirajDatum(matcher.group(4), matcher.group(5), matcher.group(6));
        }

        List<Aranzman> aranzmani = repozitorij.dohvatiAranzmanRazdoblje(pocetniDatum, zavrsniDatum);

        ispisiAranzmaneTablicaITAK(aranzmani);
    }

    private String formatirajDatum(String dan, String mjesec, String godina){
        return String.format("%02d.%02d.%s.",
                Integer.parseInt(dan),
                Integer.parseInt(mjesec),
                godina);
    }

    private void ispisiRezervacijeTablicaIRTAOtkaz(List<Rezervacija> rezervacije){
        System.out.printf("%-20s %-20s %-20s %-15s %-25s%n",
                "Ime", "Prezime", "Datum i vrijeme", "Vrsta", "Datum i vrijeme otkaza");
        System.out.println("-".repeat(100));

        for(Rezervacija r : rezervacije){
            String datumOtkaza = "";
            if(r.getStatus() == RezervacijaStatus.OTKAZANA){
                LocalDateTime dtOtkaza = repozitorij.dohvatiVrijemeOtkazivanjaRezervacije(r.getId());
                datumOtkaza = dtOtkaza != null ? formatirajDatumVrijeme(dtOtkaza) : "";
            }

            System.out.printf("%-20s %-20s %-20s %-15s %-25s%n",
                    skratiTekst(r.getIme(), 20),
                    skratiTekst(r.getPrezime(), 20),
                    r.getDatumIVrijeme(),
                    pretvoriStatusUVrstu(r.getStatus()),
                    datumOtkaza);
        }
        System.out.println();
    }

    private void ispisiRezervacijaTablicaIRTA(List<Rezervacija> rezervacije){
        System.out.printf("%-20s %-20s %-20s %-15s%n",
                "Ime", "Prezime", "Datum i vrijeme", "Vrsta");
        System.out.println("-".repeat(75));

        for(Rezervacija r : rezervacije){
            System.out.printf("%-20s %-20s %-20s %-15s%n",
                    skratiTekst(r.getIme(), 20),
                    skratiTekst(r.getPrezime(), 20),
                    r.getDatumIVrijeme(),
                    pretvoriStatusUVrstu(r.getStatus()));
        }
        System.out.println();
    }

    private String pretvoriStatusUVrstu(RezervacijaStatus status){
        switch(status){
            case PRIMLJENA: return "Primljena";
            case AKTIVNA: return "Aktivna";
            case NA_CEKANJU: return "Na čekanju";
            case OTKAZANA: return "Otkazana";
            default: return "";
        }
    }

    private String formatirajDatumVrijeme(LocalDateTime dt){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return dt.format(formatter);
    }

    private void ispisiRezervacijeTablicaIRO(List<Rezervacija> rezervacije){
        System.out.printf("%-20s %-20s %-30s %-15s%n",
                "Datum i vrijeme", "Oznaka aranžmana", "Naziv aranžmana", "Vrsta");
        System.out.println("-".repeat(85));

        for(Rezervacija r : rezervacije){
            Aranzman aranzman = repozitorij.getAranzmanPoOznaci(r.getOznakaAranzmana());
            String nazivAranzmana = aranzman != null ? aranzman.getNaziv() : "";

            System.out.printf("%-20s %-20d %-30s %-15s%n",
                    r.getDatumIVrijeme(),
                    r.getOznakaAranzmana(),
                    skratiTekst(nazivAranzmana, 30),
                    pretvoriStatusUVrstu(r.getStatus()));
        }
        System.out.println();
    }

    private String formatirajDatumVrijeme(String dan, String mjesec, String godina, String sat, String minuta, String sekunda){
        String formatiraniDatum = String.format("%02d.%02d.%s",
                Integer.parseInt(dan),
                Integer.parseInt(mjesec),
                godina);

        String formatiranoVrijeme;
        formatiranoVrijeme = String.format("%02d:%02d:%02d",
                Integer.parseInt(sat),
                Integer.parseInt(minuta),
                Integer.parseInt(sekunda));

        return formatiraniDatum + " " + formatiranoVrijeme;
    }

    public void pregledAranzmanITAP(String unos){
        String uzorak = "^ITAP\\s+(\\d+)$";

        Pattern regex = Pattern.compile(uzorak);
        Matcher matcher = provjeriRegex(regex, unos);
        if(matcher == null){
            return;
        }

        int oznaka = Integer.parseInt(matcher.group(1));
        Aranzman aranzman = repozitorij.getAranzmanPoOznaci(oznaka);
        if(aranzman == null) {
            System.out.println("Aranžman s oznakom " + oznaka + " ne postoji.");
            return;
        }

        ispisiAranzmaneTablicaITAP(aranzman);
    }

    public void pregledRezervacijaAranzmanIRTA(String unos){
        String uzorak = "^IRTA\\s+(\\d+)(?:\\s+(PA|Č|O|PAČ|PAO|ČO|PAČO))?$";

        Pattern regex = Pattern.compile(uzorak);
        Matcher matcher = provjeriRegex(regex, unos);
        if(matcher == null){
            return;
        }

        int oznaka = Integer.parseInt(matcher.group(1));
        String vrste = matcher.group(2);
        Set<RezervacijaStatus> statusi = EnumSet.noneOf(RezervacijaStatus.class);
        boolean prikaziOtkazane = false;

        if(vrste == null || vrste.contains("PA")){
            statusi.add(RezervacijaStatus.PRIMLJENA);
            statusi.add(RezervacijaStatus.AKTIVNA);
        }
        if(vrste == null || vrste.contains("Č")){
            statusi.add(RezervacijaStatus.NA_CEKANJU);
        }
        if(vrste == null || vrste.contains("O")){
            statusi.add(RezervacijaStatus.OTKAZANA);
            prikaziOtkazane = true;
        }

        List<Rezervacija> rezervacije = repozitorij.getRezervacijeZaAranzman(oznaka, statusi);

        if(prikaziOtkazane){
            ispisiRezervacijeTablicaIRTAOtkaz(rezervacije);
        }else{
            ispisiRezervacijaTablicaIRTA(rezervacije);
        }
    }

    public void pregledRezervacijaOsobaIRO(String unos){
        String uzorak = "^IRO\\s+([A-ZČĆĐŠŽ][a-zčćđšž]+)\\s+([A-ZČĆĐŠŽ][a-zčćđšž]+)$";

        Pattern regex = Pattern.compile(uzorak);
        Matcher matcher = provjeriRegex(regex, unos);
        if(matcher == null){
            return;
        }

        String ime = matcher.group(1);
        String prezime = matcher.group(2);
        String punoIme = ime + " " + prezime;

        List<Rezervacija> rezervacije = repozitorij.dohvatiRezervacijePoImenu(punoIme);

        ispisiRezervacijeTablicaIRO(rezervacije);
    }

    public void otkazRezervacijeORTA(String unos){
        String uzorak = "^ORTA\\s+([A-ZČĆĐŠŽ][a-zčćđšž]+)\\s+([A-ZČĆĐŠŽ][a-zčćđšž]+)\\s+(\\d+)$";

        Pattern regex = Pattern.compile(uzorak);
        Matcher matcher = provjeriRegex(regex, unos);
        if(matcher == null){
            return;
        }

        String ime = matcher.group(1);
        String prezime = matcher.group(2);
        int oznaka = Integer.parseInt(matcher.group(3));

        String rezultat = rezervacijaPomocnik.otkaziRezervaciju(ime, prezime, oznaka);
        System.out.println(rezultat);
    }

    public void dodavanjeRezervacijeDRTA(String unos){
        String uzorak = "^DRTA\\s+([A-ZČĆĐŠŽ][a-zčćđšž]+)\\s+([A-ZČĆĐŠŽ][a-zčćđšž]+)\\s+(\\d+)\\s+([1-9]|[12]\\d|3[01])\\.([1-9]|1[0-2])\\.(\\d{4})\\.?\\s+(\\d{1,2}):(\\d{2})(?::(\\d{2}))?$";

        Pattern regex = Pattern.compile(uzorak);
        Matcher matcher = provjeriRegex(regex, unos);
        if(matcher == null){
            return;
        }

        String ime = matcher.group(1);
        String prezime = matcher.group(2);
        int oznaka = Integer.parseInt(matcher.group(3));
        String dan = matcher.group(4);
        String mjesec = matcher.group(5);
        String godina = matcher.group(6);
        String sat = matcher.group(7);
        String minuta = matcher.group(8);
        String sekunda = matcher.group(9);

        Aranzman aranzman = repozitorij.getAranzmanPoOznaci(oznaka);
        if(aranzman == null){
            System.out.println("Neuspješno dodavanje rezervacije: Aranžman s oznakom " + oznaka + " ne postoji.");
            return;
        }

        String datumIVrijeme = formatirajDatumVrijeme(dan, mjesec, godina, sat, minuta, sekunda);

        Rezervacija novaRezervacija = new Rezervacija(ime, prezime, oznaka, datumIVrijeme);
        String rezultat = rezervacijaPomocnik.dodajRezervaciju(novaRezervacija);
        System.out.println(rezultat);
    }
}
