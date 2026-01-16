package edu.unizg.foi.uzdiz.vlovric21.strategy_null_object;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

public class VdrRezervacijaUpravitelj implements RezervacijaUpravitelj {
    @Override
    public Boolean upravljajRezervacijom(Aranzman aranzman, Rezervacija rezervacija) {
        return true;
    }
}
