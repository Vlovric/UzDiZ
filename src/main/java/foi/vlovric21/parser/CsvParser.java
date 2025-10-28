package foi.vlovric21.parser;

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
            "Datum i  vrijeme"
    );
    private static final List<Integer> obaveznaZaglavljaAranzman = List.of(0,1,2,3,4,7,8,9,10);
    private static final List<Integer> obaveznaZaglavljaRezervacija = List.of(0,1,2,3);

    public boolean validirajZaglavlje(List<String> ocekivanoZaglavlje, String redZaglavlja){
        List<String> dobivenoZaglavlje = Arrays.stream(redZaglavlja.split(","))
                .map(String::trim)
                .toList();

        return dobivenoZaglavlje.containsAll(ocekivanoZaglavlje);
    }

    public List<String> parsirajRed(String red){
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
        String regexDatumVrijeme = "\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\.? \\d{1,2}:\\d{2}";
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

    public boolean parsirajCsv(String datoteka, CsvTip tip){
        List<String> zaglavlje = tip == CsvTip.ARANZMAN ? zaglavljeAranzman : zaglavljeRezervacija;

        try(BufferedReader br = new BufferedReader(new FileReader(datoteka))){

            String red;
            red = br.readLine();

            if(red == null){
                System.out.println("Datoteka je prazna: " + datoteka);
                return false;
            }
            if(!validirajZaglavlje(zaglavlje, red)){
                System.out.println("Neispravno zaglavlje u datoteci: " + datoteka);
                return false;
            }

            int redniBroj = 0;
            while((red = br.readLine()) != null){
                redniBroj++;
                red = red.trim();
                if(red.isEmpty() || red.startsWith("#")){
                    continue;
                }
                List<String> polja = parsirajRed(red);
                if(!validirajRed(polja, tip)){
                    System.out.println("Neispravan red " + redniBroj + " u datoteci: " + datoteka);
                    continue;
                }
                //System.out.println("Redak " + redniBroj + ":" + "\n");
                /*
                for(int i=0; i<polja.size(); i++){
                    System.out.println(zaglavlje.get(i) + ": " + polja.get(i));
                    System.out.println("\n");
                }
                */
                //kreirat objekt i dodat u Repozitorij
            }
        }catch(IOException ex){
            System.out.println("Greška kod čitanja datoteke: " + datoteka);
            return false;
        }
        return true;
    }
}
