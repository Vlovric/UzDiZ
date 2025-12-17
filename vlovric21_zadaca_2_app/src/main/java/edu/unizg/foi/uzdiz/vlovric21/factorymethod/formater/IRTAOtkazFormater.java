package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.pomocne.DatumFormater;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOtkazana;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class IRTAOtkazFormater extends Formater {
    private DatumFormater datumFormater = new DatumFormater();

    @Override
    public void formatiraj(Object obj){
        List<Rezervacija> rezervacije = (List<Rezervacija>) obj;

        if(!kronoloskiRedoslijed()){
            Collections.reverse(rezervacije);
        }

        String zaglavljeFormat = "%-20s %-20s %-20s %-15s %-25s%n";
        String redFormat = "%-20s %-20s %-20s %-15s %-25s%n";

        int sirinaTablice = izracunajSirinuTablice(zaglavljeFormat);

        ispisiNaslovTablice("Pregled rezervacija za aranžman", sirinaTablice);

        ispisiZaglavlje(zaglavljeFormat, sirinaTablice, "Ime", "Prezime", "Datum i vrijeme", "Vrsta", "Datum i vrijeme otkaza");

        for(Rezervacija r : rezervacije){
            LocalDateTime dt = datumFormater.parseDatumIVrijeme(r.getDatumIVrijeme());
            String formatiraniDatumVrijeme = datumFormater.formatirajDatumVrijemeIspis(dt);

            String datumOtkaza = "";
            if(r.jeOtkazana()){
                LocalDateTime dtOtkaza = r.getVrijemeOtkaza();
                datumOtkaza = dtOtkaza != null ? datumFormater.formatirajDatumVrijemeIspis(dtOtkaza) : "";
            }

            System.out.printf(redFormat,
                    skratiTekst(r.getIme(), 20),
                    skratiTekst(r.getPrezime(), 20),
                    formatiraniDatumVrijeme,
                    r.getStatus(),
                    datumOtkaza);
        }
    }
}
