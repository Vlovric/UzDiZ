package edu.unizg.foi.uzdiz.vlovric21.state_rezervacija;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

public interface RezervacijaStatus {

    String otkazi(Aranzman aranzman, Rezervacija kontekst);

    String getStatusNaziv();

    boolean jeOtkazana();
}
