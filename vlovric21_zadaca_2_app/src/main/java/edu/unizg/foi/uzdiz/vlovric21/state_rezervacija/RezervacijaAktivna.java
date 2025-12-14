package edu.unizg.foi.uzdiz.vlovric21.state_rezervacija;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

public class RezervacijaAktivna implements RezervacijaStatus{

    @Override
    public void otkazi(Aranzman aranzman, Rezervacija kontekst){
        kontekst.setStatus(new RezervacijaOtkazana());
    }

    @Override
    public String getStatusNaziv() {
        return "AKTIVNA";
    }
}
