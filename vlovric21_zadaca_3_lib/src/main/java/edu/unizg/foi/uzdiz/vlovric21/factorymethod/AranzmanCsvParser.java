package edu.unizg.foi.uzdiz.vlovric21.factorymethod;

import edu.unizg.foi.uzdiz.vlovric21.singleton.ParsiranjePomagac;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final List<Integer> obaveznaZaglavlja = List.of(0, 1, 2, 3, 4, 7, 8, 9);

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
        if (red.size() != zaglavlje.size()) {
            return "Red nema polja koliko zaglavlje stupaca, red ima: " + red.size() + ", a treba imati: " + zaglavlje.size();
        }
        for (int index : obaveznaZaglavlja) {
            if (red.get(index).isEmpty()) {
                return "Obavezno polje je prazno, stupac: " + index + 1;
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
        } catch (NumberFormatException ex) {
            return "Neispravan broj u jednom od polja brojeva";
        }
        if (!red.get(3).matches(regexDatum) || !red.get(4).matches(regexDatum)) {
            return "Neispravan format datuma u jednom od obaveznih polja";
        }
        return "";
    }

    @Override
    protected void spremiRed(List<String> polja) {
        Map<String, String> aranzmanPodaci = new HashMap<>();
        for (int i = 0; i < zaglavlje.size(); i++) {
            aranzmanPodaci.put(zaglavlje.get(i), polja.get(i));
        }
        ParsiranjePomagac.getInstanca().dodajIspravanRedAranzman(aranzmanPodaci);
    }
}
