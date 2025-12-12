package edu.unizg.foi.uzdiz.vlovric21.factorymethod.csv;

import edu.unizg.foi.uzdiz.vlovric21.objekti.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.parser.CsvParser;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;

import java.util.List;

public class RezervacijaCsvParser extends CsvParser {
    private static final List<String> zaglavlje = List.of(
            "Ime",
            "Prezime",
            "Oznaka aranžmana",
            "Datum i vrijeme"
    );

    private static final List<Integer> obaveznaZaglavlja = List.of(0,1,2,3);

    @Override
    protected List<String> dohvatiZaglavlje() {
        return zaglavlje;
    }

    @Override
    protected List<Integer> dohvatiObaveznaZaglavlja() {
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
        try{
            Integer.parseInt(red.get(2)); //Oznaka aranžmana
        }catch(NumberFormatException ex){
            return "Neispravan broj u polju oznake";
        }

        int oznakaAranzmana = Integer.parseInt(red.get(2));
        boolean postojiAranzman = RepozitorijPodataka.getInstance().getAranzmaniMapu().containsKey(oznakaAranzmana);
        if(!postojiAranzman){
            return "Ne postoji aranžman s oznakom: " + oznakaAranzmana;
        }

        if(!red.get(3).matches(regexDatumVrijeme)){
            return "Neispravan format datuma";
        }
        return "";
    }

    @Override
    protected void stvoriObjekt(List<String> polja) {
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();

        int id = repozitorij.generirajIdZaRezervaciju();
        String ime = polja.get(0);
        String prezime = polja.get(1);
        int oznakaAranzmana = Integer.parseInt(polja.get(2));
        String datumIVrijeme = polja.get(3);

        Rezervacija rezervacija = new Rezervacija(ime, prezime, oznakaAranzmana, datumIVrijeme);
        rezervacija.setId(id);

        RepozitorijPodataka.getInstance().dodajInicijalnuRezervaciju(rezervacija);
    }
}
