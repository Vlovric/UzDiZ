package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class IROFormater extends Formater {

    @Override
    public void formatiraj(Object obj) {
        List<Rezervacija> rezervacije = (List<Rezervacija>) obj;
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();

        if(!kronoloskiRedoslijed()){
            Collections.reverse(rezervacije);
        }

        String zaglavljeFormat = "%-20s %-20s %-30s %-15s%n";
        String redFormat = "%-20s %-20d %-30s %-15s%n";

        int sirinaTablice = izracunajSirinuTablice(zaglavljeFormat);

        ispisiNaslovTablice("Popis rezervacija za osobu", sirinaTablice);

        ispisiZaglavlje(zaglavljeFormat, sirinaTablice, "Datum i vrijeme", "Oznaka aranžmana", "Naziv aranžmana", "Vrsta");

        for(Rezervacija r : rezervacije){
            Aranzman aranzman = r.getAranzman();
            String nazivAranzmana = aranzman != null ? aranzman.getNaziv() : "";

            LocalDateTime dt = datumFormater.parseDatumIVrijeme(r.getDatumIVrijeme());
            String formatiraniDatumVrijeme = datumFormater.formatirajDatumVrijemeIspis(dt);

            System.out.printf(redFormat,
                    formatiraniDatumVrijeme,
                    r.getOznakaAranzmana(),
                    skratiTekst(nazivAranzmana, 30),
                    r.getStatus());
        }
    }
}
