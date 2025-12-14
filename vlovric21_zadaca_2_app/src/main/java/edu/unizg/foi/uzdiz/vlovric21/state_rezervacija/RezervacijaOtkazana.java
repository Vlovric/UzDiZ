package edu.unizg.foi.uzdiz.vlovric21.state_rezervacija;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

public class RezervacijaOtkazana implements RezervacijaStatus{

    @Override
    public void otkazi(Aranzman aranzman, Rezervacija kontekst){

    }

    @Override
    public String getStatusNaziv() {
        return "OTKAZANA";
    }
}
