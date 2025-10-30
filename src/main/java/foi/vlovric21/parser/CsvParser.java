package foi.vlovric21.parser;

import foi.vlovric21.builder.AranzmanDirector;
import foi.vlovric21.objekti.Aranzman;
import foi.vlovric21.singleton.RepozitorijPodataka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
    private static final List<String> zaglavljeAranzman = List.of(
            "Oznaka",
            "Naziv",
            "Program",
            "Početni datum",
            "Završni datum",
            "Vrijeme kretanja",
            "Vrijeme povratka",
            "Cijena",
            "Min broj putnika",
            "Maks broj putnika",
            "Broj noćenja",
            "Doplata za jednokrevetnu sobu",
            "Prijevoz",
            "Broj doručka",
            "Broj ručkova",
            "Broj večera"
    );
    private static final List<String> zaglavljeRezervacija = List.of(
            "Ime",
            "Prezime",
            "Oznaka aranžmana",
            "Datum i vrijeme"
    );
    private static final List<Integer> obaveznaZaglavljaAranzman = List.of(0,1,2,3,4,7,8,9);
    private static final List<Integer> obaveznaZaglavljaRezervacija = List.of(0,1,2,3);

    public boolean parsirajCsv(String datoteka, CsvTip tip){
        List<String> polja;
        boolean imaZaglavlje = false;

        try(BufferedReader br = new BufferedReader(new FileReader(datoteka))){
            String red;
            red = br.readLine();
            if(red != null && red.startsWith("\uFEFF")){
                red = red.substring(1);
            }

            if(red == null){
                System.out.println("Datoteka je prazna: " + datoteka);
                return false;
            }

            if(provjeriZaglavlje(red, tip)){
                imaZaglavlje = true;
                System.out.println("Datoteka sadrži zaglavlje: " + datoteka); //TODO uklonit ispis
            }
        }catch(IOException ex){
            System.out.println("Greška kod čitanja datoteke: " + datoteka);
            return false;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(datoteka))){

            String red;
            int redniBroj = 0;
            while((red = br.readLine()) != null){
                redniBroj++;
                if(imaZaglavlje){
                    imaZaglavlje = false;
                    continue;
                }
                red = red.trim();
                if(red.isEmpty() || red.startsWith("#")){
                    continue;
                }
                polja = parsirajRed(red);
                if(!validirajRed(polja, tip)){
                    System.out.println("Neispravan red " + redniBroj + " u datoteci: " + datoteka);
                    continue;
                }
                stvoriObjekt(polja, tip);
            }
        }catch(IOException ex){
            System.out.println("Greška kod čitanja datoteke: " + datoteka);
            return false;
        }
        return true;
    }

    private boolean provjeriZaglavlje(String red, CsvTip tip){
        List<String> polja = parsirajRed(red);
        List<String> ocekivanoZaglavlje = (tip == CsvTip.ARANZMAN) ? zaglavljeAranzman : zaglavljeRezervacija;
        List<Integer> obavezniIndeksi = (tip == CsvTip.ARANZMAN) ? obaveznaZaglavljaAranzman : obaveznaZaglavljaRezervacija;
        Integer prviObavezniIndeks = obavezniIndeksi.get(0);

        if(polja.isEmpty()){
            return false;
        }
        String dobiveno = polja.get(prviObavezniIndeks).trim();
        String ocekivano = ocekivanoZaglavlje.get(prviObavezniIndeks).trim();

        return dobiveno.equals(ocekivano);
        }

    private List<String> parsirajRed(String red){
        List<String> polja = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean unutarNavodnika = false;

        for(int i=0; i<red.length(); i++){
            char znak = red.charAt(i);

            if(znak == '"'){
                unutarNavodnika = !unutarNavodnika;
            }else if(znak == ',' && !unutarNavodnika){
                polja.add(sb.toString().trim());
                sb.setLength(0);
            }else{
                sb.append(znak);
            }
        }

        polja.add(sb.toString().trim());
        return polja;
    }

    private boolean validirajRed(List<String> red, CsvTip tip){
        String regexDatum = "\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\.?";
        String regexDatumVrijeme = "\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\.? \\d{1,2}:\\d{2}(:\\d{2})?";

        if(tip == CsvTip.ARANZMAN){
            if(red.size() != zaglavljeAranzman.size()){
                System.out.println("Red nema polja koliko zaglavlje stupaca"); //TODO uklonit ispis
                return false;
            }
            for(int index : obaveznaZaglavljaAranzman){
                if(red.get(index).isEmpty()){
                    System.out.println("Obavezno polje je prazno, index: " + index); //TODO uklonit ispis
                    return false;
                }
            }
            try { //TODO refaktorirat
                Integer.parseInt(red.get(0)); //Oznaka
                Integer.parseInt(red.get(7)); //Cijena
                Integer.parseInt(red.get(8)); //Min broj putnika
                Integer.parseInt(red.get(9)); //Maks broj putnika

                if(!red.get(10).isEmpty()){
                    Integer.parseInt(red.get(10)); //Broj noćenja
                }
                if(!red.get(11).isEmpty()){
                    Integer.parseInt(red.get(11)); //Doplata za jednokrevetnu sobu
                }
                if(!red.get(13).isEmpty()){
                    Integer.parseInt(red.get(13)); //Broj doručka
                }
                if(!red.get(14).isEmpty()){
                    Integer.parseInt(red.get(14)); //Broj ručkova
                }
                if(!red.get(15).isEmpty()){
                    Integer.parseInt(red.get(15)); //Broj večera
                }
            }catch(NumberFormatException ex){
                System.out.println("Neispravan broj u jednom od polja brojeva"); //TODO uklonit ispis
                return false;
            }
            if(!red.get(3).matches(regexDatum) || !red.get(4).matches(regexDatum)){
                System.out.println("Neispravan format datuma u jednom od obaveznih polja"); //TODO uklonit ispis
                return false;
            }
        }else{
            if(red.size() != zaglavljeRezervacija.size()){
                System.out.println("Red nema polja koliko zaglavlje stupaca"); //TODO uklonit ispis
                return false;
            }
            for(int index : obaveznaZaglavljaRezervacija){
                if(red.get(index).isEmpty()){
                    System.out.println("Obavezno polje je prazno, index: " + index); //TODO uklonit ispis
                    return false;
                }
            }
            try{
                Integer.parseInt(red.get(2)); //Oznaka aranžmana
            }catch(NumberFormatException ex){
                System.out.println("Neispravan broj polja oznake"); //TODO uklonit ispis
                return false;
            }
            if(!red.get(3).matches(regexDatumVrijeme)){
                System.out.println("Neispravan format datuma"); //TODO uklonit ispis
                return false;
            }
        }
        return true;
    }

    private void stvoriObjekt(List<String> polja, CsvTip tip){
        if(tip == CsvTip.ARANZMAN){
            int oznaka = Integer.parseInt(polja.get(0));
            String naziv = polja.get(1);
            String program = polja.get(2);
            String pocetniDatum = polja.get(3);
            String zavrsniDatum = polja.get(4);
            String vrijemeKretanja = polja.get(5);
            String vrijemePovratka = polja.get(6);
            int cijena = Integer.parseInt(polja.get(7));
            int minBrojPutnika = Integer.parseInt(polja.get(8));
            int maxBrojPutnika = Integer.parseInt(polja.get(9));
            int brojNocenja = Integer.parseInt(polja.get(10));

            String prijevoz = polja.get(12).trim(); // TODO: Trimovi mozda suvisni ako u parsirajRed radi trim

            String doplataStr = polja.get(11).trim();
            Integer doplata = doplataStr.isEmpty() ? null : Integer.valueOf(doplataStr);

            String dorucakStr = polja.get(13).trim();
            Integer brojDorucka = dorucakStr.isEmpty() ? null : Integer.valueOf(dorucakStr);

            String ruckStr = polja.get(14).trim();
            Integer brojRuckova = ruckStr.isEmpty() ? null : Integer.valueOf(ruckStr);

            String veceraStr = polja.get(15).trim();
            Integer brojVecera = veceraStr.isEmpty() ? null : Integer.valueOf(veceraStr);

            AranzmanDirector direktor = new AranzmanDirector();
            Aranzman ar = direktor.stvoriAranzman(
                    oznaka,
                    naziv,
                    program,
                    pocetniDatum,
                    zavrsniDatum,
                    cijena,
                    minBrojPutnika,
                    maxBrojPutnika,
                    brojNocenja,
                    vrijemeKretanja,
                    vrijemePovratka,
                    doplata,
                    prijevoz,
                    brojDorucka,
                    brojRuckova,
                    brojVecera
            );
            RepozitorijPodataka.getInstance().dodajAranzman(ar);
        }else{
            //TODO implementirati stvaranje objekta rezervacije
        }
    }
}
