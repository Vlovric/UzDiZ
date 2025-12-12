package edu.unizg.foi.uzdiz.vlovric21.factorymethod.unos_objekta;

import edu.unizg.foi.uzdiz.vlovric21.objekti.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;

import java.util.Map;

public class CsvRezervacijaUnositelj extends CsvObjectUnositelj {

    @Override
    protected boolean validirajObjekt(Map<String, String> red) {
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
        int oznakaAranzmana = Integer.parseInt(red.get("Oznaka aranžmana"));
        if (!repozitorij.getAranzmaniMapu().containsKey(oznakaAranzmana)) {
            System.out.println("Ne postoji aranžman s oznakom: " + oznakaAranzmana);
            return false;
        }
        return true;
    }

    @Override
    protected void unesiObjekt(Map<String, String> red) {
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();

        int id = repozitorij.generirajIdZaRezervaciju();
        String ime = red.get("Ime");
        String prezime = red.get("Prezime");
        int oznakaAranzmana = Integer.parseInt(red.get("Oznaka aranžmana"));
        String datumIVrijeme = red.get("Datum i vrijeme");

        Rezervacija rezervacija = new Rezervacija(ime, prezime, oznakaAranzmana, datumIVrijeme);
        rezervacija.setId(id);

        repozitorij.dodajInicijalnuRezervaciju(rezervacija);
    }

    /*
    private static final List<String> zaglavlje = List.of(
            "Ime",
            "Prezime",
            "Oznaka aranžmana",
            "Datum i vrijeme"
    );
     */
}
