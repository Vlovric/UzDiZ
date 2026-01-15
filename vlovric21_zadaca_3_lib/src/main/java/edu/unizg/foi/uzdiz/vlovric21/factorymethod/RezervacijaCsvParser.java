package edu.unizg.foi.uzdiz.vlovric21.factorymethod;

import edu.unizg.foi.uzdiz.vlovric21.singleton.ParsiranjePomagac;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RezervacijaCsvParser extends CsvParser {
    private static final List<String> zaglavlje = List.of(
            "Ime",
            "Prezime",
            "Oznaka aranžmana",
            "Datum i vrijeme"
    );

    private static final List<Integer> obaveznaZaglavlja = List.of(0, 1, 2, 3);

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
            Integer.parseInt(red.get(2)); //Oznaka aranžmana
        } catch (NumberFormatException ex) {
            return "Neispravan broj u polju oznake";
        }

        if (!red.get(3).matches(regexDatumVrijeme)) {
            return "Neispravan format datuma";
        }
        return "";
    }

    @Override
    protected void spremiRed(List<String> polja) {
        Map<String, String> aranzmanPodaci = new HashMap<>();
        for (int i = 0; i < zaglavlje.size(); i++) {
            aranzmanPodaci.put(zaglavlje.get(i), polja.get(i));
        }
        ParsiranjePomagac.getInstanca().dodajIspravanRedRezervacija(aranzmanPodaci);
    }
}
