package edu.unizg.foi.uzdiz.vlovric21.pomocne;

import edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater.FormaterCreator;
import edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater.FormaterFactory;
import edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater.FormaterTip;
import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.*;

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

        List<Aranzman> aranzmani = repozitorij.dohvatiAranzmaneRazdoblje(pocetniDatum, zavrsniDatum);

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
        String uzorak = "^IRTA\\s+(\\d+)(?:\\s+([A-ZČĆĐŠŽa-zčćđšž]+))?$";

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
        List<String> statusi = new java.util.ArrayList<>();
        boolean prikaziOtkazane = false;

        if(vrste == null){
            statusi.add(new RezervacijaPrimljena().getStatusNaziv());
            statusi.add(new RezervacijaAktivna().getStatusNaziv());
            statusi.add(new RezervacijaNaCekanju().getStatusNaziv());
            statusi.add(new RezervacijaOtkazana().getStatusNaziv());
            statusi.add(new RezervacijaOdgodena().getStatusNaziv());
            prikaziOtkazane = true;
        }else{
            String temp = vrste;

            while(!temp.isEmpty()){
                if(temp.startsWith("PA")){
                    statusi.add(new RezervacijaPrimljena().getStatusNaziv());
                    statusi.add(new RezervacijaAktivna().getStatusNaziv());
                    temp = temp.substring(2);

                }else if(temp.startsWith("Č")){
                    statusi.add(new RezervacijaNaCekanju().getStatusNaziv());
                    temp = temp.substring(1);

                } else if(temp.startsWith("OD")){
                    statusi.add(new RezervacijaOdgodena().getStatusNaziv());
                    temp = temp.substring(2);

                }else if(temp.startsWith("O")){
                    statusi.add(new RezervacijaOtkazana().getStatusNaziv());
                    prikaziOtkazane = true;
                    temp = temp.substring(1);

                }else{
                    System.out.println("Neispravni parametri za komandu.");
                    return;
                }
            }
        }

        List<Rezervacija> rezervacije = repozitorij.dohvatiRezervacijeZaAranzman(oznaka, statusi);

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

        String rezultat = repozitorij.otkaziRezervaciju(ime, prezime, oznaka);
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
        String rezultat = repozitorij.dodajRezervaciju(novaRezervacija);
        System.out.println(rezultat);
    }

    public void otkazAranzmanaOTA(String unos){
        //TODO nakon implementacije otkazivanja aranzmana
    }

    public void postavljanjeIspisaIP(String unos){
        //TODO
    }

    public void brisanjePodatakaBP(String unos){
        //TODO nakon implementacije otkazivanja aranzmana
    }

    public void ucitavanjePodatakaUP(String unos){
        //TODO
    }

    public void ispisStatistikeITAS(String unos){
        //TODO
    }
}
