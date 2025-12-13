package edu.unizg.foi.uzdiz.vlovric21.state_rezervacija;

import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

public class RezervacijaNova implements RezervacijaStatus{

    @Override
    public void otkazi(Rezervacija kontekst){
        kontekst.setStatus(new RezervacijaOtkazana());
    }

    @Override
    public String getStatusNaziv() {
        return "NOVA";
    }
}
