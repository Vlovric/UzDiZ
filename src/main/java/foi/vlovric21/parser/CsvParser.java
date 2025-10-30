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

    private static final String regexDatum = "\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\.?";
    private static final String regexDatumVrijeme = "\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\.? \\d{1,2}:\\d{2}(:\\d{2})?";

    public boolean parsirajCsv(String datoteka, CsvTip tip){
        List<String> polja;
        boolean imaZaglavlje;

        try(BufferedReader br = new BufferedReader(new FileReader(datoteka))){
            String red;
            red = makniBOM(br.readLine());

            if(red == null){
                System.out.println("Datoteka je prazna: " + datoteka);
                return false; //TODO vidjet sta s ovim
            }
            imaZaglavlje = provjeriZaglavlje(red, tip);

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
                String greska = validirajRed(polja, tip);
                if(!greska.isEmpty()){
                    ispisiGresku(redniBroj, datoteka, greska, red, polja);
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

    private String makniBOM(String red){
        if(red != null && red.startsWith("\uFEFF")){
            return red.substring(1);
        }
        return red;
    }

    private boolean provjeriZaglavlje(String red, CsvTip tip){
        List<String> polja = parsirajRed(red);
        List<String> ocekivanoZaglavlje = (tip == CsvTip.ARANZMAN) ? zaglavljeAranzman : zaglavljeRezervacija;
        List<Integer> obavezniIndeksi = (tip == CsvTip.ARANZMAN) ? obaveznaZaglavljaAranzman : obaveznaZaglavljaRezervacija;
        int prviObavezniIndeks = obavezniIndeksi.get(0);

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

    private String validirajRed(List<String> red, CsvTip tip){
        if(tip == CsvTip.ARANZMAN){
            return validirajAranzman(red);
        }else{
            return validirajRezervacija(red);
        }
    }

    private String validirajAranzman(List<String> red){
        if(red.size() != zaglavljeAranzman.size()){
            return "Red nema polja koliko zaglavlje stupaca, red ima: " + red.size() + ", a treba imati: " + zaglavljeAranzman.size();
        }
        for(int index : obaveznaZaglavljaAranzman){
            if(red.get(index).isEmpty()){
                return "Obavezno polje je prazno, stupac: " + index+1;
            }
        }
        try { //TODO refaktorirat
            Integer.parseInt(red.get(0)); //Oznaka
            Integer.parseInt(red.get(7)); //Cijena
            Integer.parseInt(red.get(8)); //Min broj putnika
            Integer.parseInt(red.get(9)); //Maks broj putnika

            parseAkoNijePrazno(red.get(10)); //Broj noćenja
            parseAkoNijePrazno(red.get(11)); //Doplata za jednokrevetnu sobu
            parseAkoNijePrazno(red.get(13)); //Broj doručka
            parseAkoNijePrazno(red.get(14)); //Broj ručkova
            parseAkoNijePrazno(red.get(15)); //Broj večera
        }catch(NumberFormatException ex){
            return "Neispravan broj u jednom od polja brojeva";
        }
        if(!red.get(3).matches(regexDatum) || !red.get(4).matches(regexDatum)){
            return "Neispravan format datuma u jednom od obaveznih polja";
        }
        return "";
    }

    private void parseAkoNijePrazno(String vrijednost) throws NumberFormatException {
        if(!vrijednost.isEmpty()){
            Integer.parseInt(vrijednost);
        }
    }

    private String validirajRezervacija(List<String> red){
        if(red.size() != zaglavljeRezervacija.size()){
            return "Red nema polja koliko zaglavlje stupaca";
        }
        for(int index : obaveznaZaglavljaRezervacija){
            if(red.get(index).isEmpty()){
                return "Obavezno polje je prazno, stupac: " + index+1;
            }
        }
        try{
            Integer.parseInt(red.get(2)); //Oznaka aranžmana
        }catch(NumberFormatException ex){
            return "Neispravan broj u polju oznake";
        }

        int oznakaAranzmana = Integer.parseInt(red.get(2));
        boolean postojiAranzman = RepozitorijPodataka.getInstance().getAranzmani().stream()
                .anyMatch(aranzman -> aranzman.getOznaka() == oznakaAranzmana);
        if(!postojiAranzman){
            return "Ne postoji aranžman s oznakom: " + oznakaAranzmana;
        }

        if(!red.get(3).matches(regexDatumVrijeme)){
            return "Neispravan format datuma";
        }
        return "";
    }

    private void ispisiGresku(int redniBroj, String datoteka, String greska, String red, List<String> polja){
        System.out.println("---------------------------------");
        System.out.println("Neispravan red: " + redniBroj + " u datoteci: " + datoteka);
        System.out.println("Razlog: " + greska);
        System.out.println("Red: " + red);
        System.out.println("Polja: "); //TODO maknut prije predaje
        for(int i=0; i<polja.size(); i++){
            System.out.println("[" + i + "] => '" + polja.get(i) + "'");
        }
        System.out.println("---------------------------------");
    }

    private void stvoriObjekt(List<String> polja, CsvTip tip){
        if(tip == CsvTip.ARANZMAN){
            stvoriAranzmanObjekt(polja);
        }else{
            stvoriRezervacijaObjekt(polja);
            //TODO implementirati stvaranje objekta rezervacije
        }
    }

    private void stvoriAranzmanObjekt(List<String> polja){
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
        String prijevoz = polja.get(12).trim(); // TODO: Trimovi mozda suvisni ako u parsirajRed radi trim

        Integer brojNocenja = parseOpcionalniInt(polja.get(10));
        Integer doplata = parseOpcionalniInt(polja.get(11));
        Integer brojDorucka = parseOpcionalniInt(polja.get(13));
        Integer brojRuckova = parseOpcionalniInt(polja.get(14));
        Integer brojVecera = parseOpcionalniInt(polja.get(15));

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
    }

    private Integer parseOpcionalniInt(String vrijednost){
        String skracen = vrijednost.trim();
        return skracen.isEmpty() ? null : Integer.valueOf(skracen);
    }

    private void stvoriRezervacijaObjekt(List<String> polja){

    }
}
