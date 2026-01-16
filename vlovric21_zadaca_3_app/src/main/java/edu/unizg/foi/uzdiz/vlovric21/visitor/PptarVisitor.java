package edu.unizg.foi.uzdiz.vlovric21.visitor;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

public interface PptarVisitor {
    void posjeti(Aranzman aranzman);
    void posjeti(Rezervacija rezervacija);
}
