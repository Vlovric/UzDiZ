package foi.vlovric21.pomocne;

import foi.vlovric21.objekti.Aranzman;
import foi.vlovric21.singleton.RepozitorijPodataka;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KomandePomocnik {
    private static String porukaPogreske = "Neispravani ili nedostajući parametri za komandu.";
    private static RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();

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
        boolean pa = false;
        boolean c = false;
        boolean o = false;

        if(vrste == null){
            pa = true;
            c = true;
            o = true;
        }else{
            if(vrste.contains("PA")){
                pa = true;
            }
            if(vrste.contains("Č")){
                c = true;
            }
            if(vrste.contains("O")){
                o = true;
            }
        }


    }

    public String pregledRezervacijaOsobaIRO(String unos){
        Pattern regex = Pattern.compile("");
        Matcher matcher = provjeriRegex(regex, unos);
        if(matcher == null){
            return "";
        }
        return "";
    }

    public String otkazRezervacijeORTA(String unos){
        Pattern regex = Pattern.compile("");
        Matcher matcher = provjeriRegex(regex, unos);
        if(matcher == null){
            return "";
        }
        return "";
    }

    public String dodavanjeRezervacijeDRTA(String unos){
        Pattern regex = Pattern.compile("");
        Matcher matcher = provjeriRegex(regex, unos);
        if(matcher == null){
            return "";
        }
        return "";
    }
}
