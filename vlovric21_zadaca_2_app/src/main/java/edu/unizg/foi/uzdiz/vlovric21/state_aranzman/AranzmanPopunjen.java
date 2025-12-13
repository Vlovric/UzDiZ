package edu.unizg.foi.uzdiz.vlovric21.state_aranzman;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaNaCekanju;

public class AranzmanPopunjen implements AranzmanStatus{
    @Override
    public String dodajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
        rezervacija.setStatus(new RezervacijaNaCekanju());
        aranzman.dodajRezervaciju(rezervacija);
        return "Rezervacija dodana NA ČEKANJE jer je aranžman POPUNJEN.";
    }

    @Override
    public String otkaziRezervaciju(Aranzman aranzman, Rezervacija Rezervacija) {
        return "";
    }

    @Override
    public String otkaziAranzman(Aranzman aranzman) {
        return "";
    }

    @Override
    public String getStatusNaziv() {
        return "POPUNJEN";
    }
}
