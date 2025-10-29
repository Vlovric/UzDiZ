package foi.vlovric21.parser;

import foi.vlovric21.builder.AranzmanDirector;
import foi.vlovric21.objekti.Aranzman;
import foi.vlovric21.singleton.RepozitorijPodataka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private static final List<Integer> obaveznaZaglavljaAranzman = List.of(0,1,2,3,4,7,8,9,10);
    private static final List<Integer> obaveznaZaglavljaRezervacija = List.of(0,1,2,3);

    public boolean parsirajCsv(String datoteka, CsvTip tip){
        List<String> zaglavlje = tip == CsvTip.ARANZMAN ? zaglavljeAranzman : zaglavljeRezervacija;
        List<String> polja;

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
            if(!validirajZaglavlje(zaglavlje, red)){
                System.out.println("Neispravno zaglavlje u datoteci: " + datoteka);
            }

            int redniBroj = 0;
            while((red = br.readLine()) != null){
                redniBroj++;
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

    private boolean validirajZaglavlje(List<String> ocekivanoZaglavlje, String redZaglavlja){
        List<String> dobivenoZaglavlje = Arrays.stream(redZaglavlja.split(","))
                .map(String::trim)
                .toList();

        return dobivenoZaglavlje.containsAll(ocekivanoZaglavlje);
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
            try { //TODO uvest za opcionalne validaciju i refaktorirat
                Integer.parseInt(red.get(0)); //Oznaka
                Integer.parseInt(red.get(7)); //Cijena
                Integer.parseInt(red.get(8)); //Min broj putnika
                Integer.parseInt(red.get(9)); //Maks broj putnika
                Integer.parseInt(red.get(10)); //Broj noćenja
            }catch(NumberFormatException ex){
                System.out.println("Neispravan broj u jednom od obaveznih polja"); //TODO uklonit ispis
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

            String prijevoz = polja.get(12).trim();

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
