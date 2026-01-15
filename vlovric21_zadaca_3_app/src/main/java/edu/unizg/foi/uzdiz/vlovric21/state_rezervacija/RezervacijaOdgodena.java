package edu.unizg.foi.uzdiz.vlovric21.state_rezervacija;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

import java.time.LocalDateTime;

public class RezervacijaOdgodena implements RezervacijaStatus{

    @Override
    public String otkazi(Aranzman aranzman, Rezervacija kontekst){
        kontekst.setStatus(new RezervacijaOtkazana());
        kontekst.setVrijemeOtkaza(LocalDateTime.now());
        return "";
    }

    @Override
    public String getStatusNaziv() {
        return "ODGODENA";
    }

    @Override
    public boolean jeOtkazana() {
        return false;
    }
}
