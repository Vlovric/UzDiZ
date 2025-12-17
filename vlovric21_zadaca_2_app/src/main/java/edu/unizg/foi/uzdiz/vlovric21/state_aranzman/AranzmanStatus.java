package edu.unizg.foi.uzdiz.vlovric21.state_aranzman;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

public interface AranzmanStatus {

    String dodajRezervaciju(Aranzman aranzman, Rezervacija rezervacija);

    String otkaziAranzman(Aranzman aranzman);

    String getStatusNaziv();

    boolean jeOtkazan();
}
