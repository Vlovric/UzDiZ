package edu.unizg.foi.uzdiz.vlovric21.pomocne;

import edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater.FormaterCreator;
import edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater.FormaterFactory;
import edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater.FormaterTip;
import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.composite.RezervacijaStatus;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KomandePomocnik {
    private static String porukaPogreske = "Neispravani ili nedostajući parametri za komandu.";
    private static RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
    private static RezervacijaPomocnik rezervacijaPomocnik = new RezervacijaPomocnik();
    private static DatumFormater datumFormater = new DatumFormater();

    private Matcher provjeriRegex(Pattern regex, String unos){
        Matcher matcher = regex.matcher(unos.trim());
        if(!matcher.matches()){
            System.out.println(porukaPogreske);
            return null;
        }
        return matcher;
    }

    private void ispisi(Object obj, FormaterTip tip){
        FormaterCreator formaterCreator = FormaterFactory.getCreator(tip);
        formaterCreator.formatiraj(obj);
    }

    public void pregledAranzmanRazdobljeITAK(String unos){
        String uzorak = "^ITAK(?:\\s+(0[1-9]|[12]\\d|3[01])\\.(0[1-9]|1[0-2])\\.(\\d{4})\\.\\s+(0[1-9]|[12]\\d|3[01])\\.(0[1-9]|1[0-2])\\.(\\d{4})\\.)?$";

        Pattern regex = Pattern.compile(uzorak);
        Matcher matcher = provjeriRegex(regex, unos);
        if(matcher == null){
            return;
        }

        String pocetniDatum = null;
        String zavrsniDatum = null;

        if(matcher.group(1) != null){
            pocetniDatum = datumFormater.formatirajDatum(matcher.group(1), matcher.group(2), matcher.group(3));
            zavrsniDatum = datumFormater.formatirajDatum(matcher.group(4), matcher.group(5), matcher.group(6));
        }

        List<Aranzman> aranzmani = repozitorij.dohvatiAranzmanRazdoblje(pocetniDatum, zavrsniDatum);

        ispisi(aranzmani, FormaterTip.ITAK);
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

        ispisi(aranzman, FormaterTip.ITAP);
    }

    public void pregledRezervacijaAranzmanIRTA(String unos){
        String uzorak = "^IRTA\\s+(\\d+)(?:\\s+(PA|Č|O|PAČ|PAO|ČO|PAČO))?$";

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

        FormaterTip formaterTip = prikaziOtkazane ? FormaterTip.IRTA_OTKAZ : FormaterTip.IRTA;

        ispisi(rezervacije, formaterTip);
    }

    public void pregledRezervacijaOsobaIRO(String unos){
        String uzorak = "^IRO\\s+([A-ZČĆĐŠŽa-zčćđšž]+)\\s+([A-ZČĆĐŠŽa-zčćđšž]+)$";

        Pattern regex = Pattern.compile(uzorak);
        Matcher matcher = provjeriRegex(regex, unos);
        if(matcher == null){
            return;
        }

        String ime = matcher.group(1);
        String prezime = matcher.group(2);
        String punoIme = ime + " " + prezime;

        List<Rezervacija> rezervacije = repozitorij.dohvatiRezervacijePoImenu(punoIme);

        ispisi(rezervacije, FormaterTip.IRO);
    }

    public void otkazRezervacijeORTA(String unos){
        String uzorak = "^ORTA\\s+([A-ZČĆĐŠŽa-zčćđšž]+)\\s+([A-ZČĆĐŠŽa-zčćđšž]+)\\s+(\\d+)$";

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
        String uzorak = "^DRTA\\s+([A-ZČĆĐŠŽa-zčćđšž]+)\\s+([A-ZČĆĐŠŽa-zčćđšž]+)\\s+(\\d+)\\s+(0[1-9]|[12]\\d|3[01])\\.(0[1-9]|1[0-2])\\.(\\d{4})\\.?\\s+([01]?\\d|2[0-3]):([0-5]\\d)(?::([0-5]\\d))?$";

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

        String datumIVrijeme = datumFormater.formatirajDatumVrijeme(dan, mjesec, godina, sat, minuta, sekunda);

        Rezervacija novaRezervacija = new Rezervacija(ime, prezime, oznaka, datumIVrijeme);
        String rezultat = rezervacijaPomocnik.dodajRezervaciju(novaRezervacija);
        System.out.println(rezultat);
    }
}
