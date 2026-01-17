package edu.unizg.foi.uzdiz.vlovric21.memento;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

import java.util.ArrayList;
import java.util.List;

public class AranzmanMemento {
    private final Aranzman aranzman;
    private final List<Rezervacija> rezervacije;

    public AranzmanMemento(Aranzman aranzman, List<Rezervacija> rezervacije){
        this.aranzman = new Aranzman(aranzman);
        this.rezervacije = new ArrayList<>();

        for(Rezervacija r : rezervacije){
            this.rezervacije.add(new Rezervacija(r));
        }
    }

    public Aranzman getAranzman() {
        return aranzman;
    }

    public List<Rezervacija> getRezervacije() {
        return new ArrayList<>(rezervacije);
    }
}
