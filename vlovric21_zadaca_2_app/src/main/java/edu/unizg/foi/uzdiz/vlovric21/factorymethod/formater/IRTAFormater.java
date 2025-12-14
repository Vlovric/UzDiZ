package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

import java.time.LocalDateTime;
import java.util.List;

public class IRTAFormater extends Formater {

    @Override
    public void formatiraj(Object obj) {
        List<Rezervacija> rezervacije = (List<Rezervacija>) obj;
        int sirinaTablice = 85;

        ispisiNaslovTablice("Pregled rezervacija za aranžman", sirinaTablice);

        String zaglavljeFormat = "%-20s %-20s %-20s %-15s%n";
        ispisiZaglavlje(zaglavljeFormat, sirinaTablice, "Ime", "Prezime", "Datum i vrijeme", "Vrsta");

        String redFormat = "%-20s %-20s %-20s %-15s%n";

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
