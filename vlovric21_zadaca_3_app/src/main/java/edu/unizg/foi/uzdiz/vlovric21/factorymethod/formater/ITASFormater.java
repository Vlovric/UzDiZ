package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaAktivna;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaNaCekanju;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOdgodena;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOtkazana;

import java.util.Collections;
import java.util.List;

public class ITASFormater extends Formater {


    @Override
    public void formatiraj(Object obj) {
        List<Aranzman> aranzmani = sortirajAranzmane((List<Aranzman>) obj);

        String zaglavljeFormat = "%-8s %-10s %-10s %-12s %-10s %-10s %-15s%n";
        String redFormat =       "%-8d %-10d %-10d %-12d %-10d %-10d %15s";

        int sirinaTablice = izracunajSirinuTablice(zaglavljeFormat);

        ispisiNaslovTablice("Statistika aranžmana", sirinaTablice);
        ispisiZaglavlje(zaglavljeFormat, sirinaTablice,
                "Oznaka", "Uk. br.", "Aktivnih", "Na čekanju", "Odgođenih", "Otkazanih", "Uk. prihod");

        for(Aranzman a : aranzmani){
            List<Rezervacija> rezervacije = a.dohvatiSveRezervacije();

            int ukupno = rezervacije.size();
            int aktivne = a.dohvatiRezervacijeSaStatusom(new RezervacijaAktivna()).size();
            int naCekanju = a.dohvatiRezervacijeSaStatusom(new RezervacijaNaCekanju()).size();
            int odgodene = a.dohvatiRezervacijeSaStatusom(new RezervacijaOdgodena()).size();
            int otkazane = a.dohvatiRezervacijeSaStatusom(new RezervacijaOtkazana()).size();

            double ukPrihod = aktivne * a.getCijena();
            System.out.printf(redFormat + "%n",
                    a.getOznaka(),
                    ukupno,
                    aktivne,
                    naCekanju,
                    odgodene,
                    otkazane,
                    formatirajBroj(ukPrihod));
        }
    }
}
