package edu.unizg.foi.uzdiz.vlovric21.state_aranzman;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaNaCekanju;

public class AranzmanPopunjen implements AranzmanStatus{
    @Override
    public void dodajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
        rezervacija.setStatus(new RezervacijaNaCekanju());
        aranzman.dodajRezervaciju(rezervacija);
        //return "Rezervacija dodana NA ČEKANJE jer je aranžman POPUNJEN.";
        return;
    }

    @Override
    public void otkaziRezervaciju(Aranzman aranzman, Rezervacija Rezervacija) {
        return;
    }

    @Override
    public void otkaziAranzman(Aranzman aranzman) {
        return;
    }

    @Override
    public String getStatusNaziv() {
        return "POPUNJEN";
    }
}
