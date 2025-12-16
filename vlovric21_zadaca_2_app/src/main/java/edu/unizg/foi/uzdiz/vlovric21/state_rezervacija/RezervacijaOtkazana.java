package edu.unizg.foi.uzdiz.vlovric21.state_rezervacija;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

public class RezervacijaOtkazana implements RezervacijaStatus{

    @Override
    public String otkazi(Aranzman aranzman, Rezervacija kontekst){
        return "Ne moze se otkazati već otkazana rezervacija.";
    }

    @Override
    public String getStatusNaziv() {
        return "OTKAZANA";
    }
}
