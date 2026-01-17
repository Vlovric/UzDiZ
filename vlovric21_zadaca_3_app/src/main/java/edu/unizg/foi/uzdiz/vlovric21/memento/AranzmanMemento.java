package edu.unizg.foi.uzdiz.vlovric21.memento;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

import java.util.ArrayList;
import java.util.List;

public class AranzmanMemento {
    private final int oznaka;
    private final List<Rezervacija> rezervacije;

    public AranzmanMemento(int oznaka, List<Rezervacija> rezervacije){
        this.oznaka = oznaka;
        this.rezervacije = new ArrayList<>();

        for(Rezervacija r : rezervacije){
            this.rezervacije.add(new Rezervacija(r));
        }
    }

    public int getOznaka() {
        return oznaka;
    }

    public List<Rezervacija> getRezervacije() {
        return new ArrayList<>(rezervacije);
    }
}
