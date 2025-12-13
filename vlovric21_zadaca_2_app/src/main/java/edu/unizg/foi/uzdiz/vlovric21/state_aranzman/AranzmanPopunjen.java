package edu.unizg.foi.uzdiz.vlovric21.state_aranzman;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

public class AranzmanPopunjen implements AranzmanStatus{
    @Override
    public void dodajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {

    }

    @Override
    public String getStatusNaziv() {
        return "POPUNJEN";
    }
}
