package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;

import java.time.LocalDate;
import java.util.List;

public class ITAKFormater extends Formater {

    @Override
    public void formatiraj(Object obj){
        List<Aranzman> aranzmani = (List<Aranzman>) obj;
        int sirinaTablice = 170;

        ispisiNaslovTablice("Popis aranžmana", sirinaTablice);

        String zaglavljeFormat = "%-8s %-30s %-18s %-18s %-18s %-18s %-10s %-18s %-18s %-15s%n";
        ispisiZaglavlje(zaglavljeFormat, sirinaTablice, "Oznaka", "Naziv", "Početni datum", "Završni datum", "Vrijeme kretanja",
                "Vrijeme povratka", "Cijena", "Min broj putnika", "Maks broj putnika", "Status");

        String redFormat = "%-8d %-30s %-18s %-18s %-18s %-18s %10s %18s %18s %-15s";

        for(Aranzman a : aranzmani) {
            LocalDate pocDatum = datumFormater.parseDatum(a.getPocetniDatum());
            LocalDate zavDatum = datumFormater.parseDatum(a.getZavrsniDatum());

            String cijena = formatirajBroj(a.getCijena());
            String minBrojPutnika = formatirajBroj(a.getMinBrojPutnika());
            String maxBrojPutnika = formatirajBroj(a.getMaxBrojPutnika());

            System.out.printf(redFormat + "%n",
                    a.getOznaka(),
                    skratiTekst(a.getNaziv(), 30),
                    datumFormater.formatirajDatumIspis(pocDatum),
                    datumFormater.formatirajDatumIspis(zavDatum),
                    a.getVrijemeKretanja() != null ? a.getVrijemeKretanja() : "",
                    a.getVrijemePovratka() != null ? a.getVrijemePovratka() : "",
                    cijena,
                    minBrojPutnika,
                    maxBrojPutnika,
                    a.getStatus());
        }
    }
}
