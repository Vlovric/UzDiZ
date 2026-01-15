package edu.unizg.foi.uzdiz.vlovric21.state_aranzman;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

public class AranzmanOtkazan implements AranzmanStatus{

    @Override
    public String dodajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
        return "Nije moguće dodati rezervaciju jer je aranžman OTKAZAN.";
    }

    @Override
    public String otkaziAranzman(Aranzman aranzman) {
        return "Nije moguće otkazati aražman jer je  već OTKAZAN.";
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
