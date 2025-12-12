package edu.unizg.foi.uzdiz.vlovric21.factorymethod.csv;

import java.util.List;

public class RezervacijaCsvParser{

}

/*
public class RezervacijaCsvParser extends CsvParser {


    int oznakaAranzmana = Integer.parseInt(red.get(2));
    boolean postojiAranzman = RepozitorijPodataka.getInstance().getAranzmaniMapu().containsKey(oznakaAranzmana);
        if (!postojiAranzman) {
        return "Ne postoji aranžman s oznakom: " + oznakaAranzmana;
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
 */
