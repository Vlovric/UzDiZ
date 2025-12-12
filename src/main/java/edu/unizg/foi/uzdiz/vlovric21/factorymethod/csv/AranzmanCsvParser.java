package edu.unizg.foi.uzdiz.vlovric21.factorymethod.csv;

import edu.unizg.foi.uzdiz.vlovric21.builder.AranzmanDirector;
import edu.unizg.foi.uzdiz.vlovric21.objekti.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.parser.CsvParser;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;

import java.util.List;

public class AranzmanCsvParser extends CsvParser {
    private static final List<String> zaglavlje = List.of(
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

    private static final List<Integer> obaveznaZaglavlja = List.of(0,1,2,3,4,7,8,9);

    @Override
    protected List<String> dohvatiZaglavlje(){
        return zaglavlje;
    }
    @Override
    protected List<Integer> dohvatiObaveznaZaglavlja(){
        return obaveznaZaglavlja;
    }

    @Override
    protected String validirajRed(List<String> red) {
        if(red.size() != zaglavlje.size()){
            return "Red nema polja koliko zaglavlje stupaca, red ima: " + red.size() + ", a treba imati: " + zaglavlje.size();
        }
        for(int index : obaveznaZaglavlja){
            if(red.get(index).isEmpty()){
                return "Obavezno polje je prazno, stupac: " + index+1;
            }
        }
        try {
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
        if(RepozitorijPodataka.getInstance().getAranzmaniMapu().containsKey(Integer.parseInt(red.get(0)))) {
            return "Aranžman s oznakom" + red.get(0) + "već postoji";
        }
        if(!red.get(3).matches(regexDatum) || !red.get(4).matches(regexDatum)){
            return "Neispravan format datuma u jednom od obaveznih polja";
        }
        return "";
    }

    @Override
    protected void stvoriObjekt(List<String> polja) {
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
        String prijevoz = polja.get(12).trim();

        Integer brojNocenja = parseOpcionalniInt(polja.get(10));
        Integer doplata = parseOpcionalniInt(polja.get(11));
        Integer brojDorucka = parseOpcionalniInt(polja.get(13));
        Integer brojRuckova = parseOpcionalniInt(polja.get(14));
        Integer brojVecera = parseOpcionalniInt(polja.get(15));

        AranzmanDirector direktor = new AranzmanDirector();
        Aranzman ar = direktor.stvoriKompletanAranzman(
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
}
