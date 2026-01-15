package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class IRTAFormater extends Formater {

    @Override
    public void formatiraj(Object obj) {
        List<Rezervacija> rezervacije = (List<Rezervacija>) obj;

        if(!kronoloskiRedoslijed()){
            Collections.reverse(rezervacije);
        }

        String zaglavljeFormat = "%-20s %-20s %-20s %-15s%n";
        String redFormat = "%-20s %-20s %-20s %-15s%n";

        int sirinaTablice = izracunajSirinuTablice(zaglavljeFormat);

        ispisiNaslovTablice("Pregled rezervacija za aranžman", sirinaTablice);

        ispisiZaglavlje(zaglavljeFormat, sirinaTablice, "Ime", "Prezime", "Datum i vrijeme", "Vrsta");

        for(Rezervacija r : rezervacije) {
            LocalDateTime dt = datumFormater.parseDatumIVrijeme(r.getDatumIVrijeme());
            String formatiraniDatumVrijeme = datumFormater.formatirajDatumVrijemeIspis(dt);

            System.out.printf(redFormat,
                    skratiTekst(r.getIme(), 20),
                    skratiTekst(r.getPrezime(), 20),
                    formatiraniDatumVrijeme,
                    r.getStatus());
        }
    }


}
