package edu.unizg.foi.uzdiz.vlovric21.state_aranzman;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

public class AranzmanOtkazan implements AranzmanStatus{

    @Override
    public String dodajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
        return "Nije moguće dodati rezervaciju jer je aranžman OTKAZAN.";
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
        return "OTKAZAN";
    }

    @Override
    public boolean jeOtkazan() {
        return true;
    }
}
