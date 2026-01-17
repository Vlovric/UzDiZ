package edu.unizg.foi.uzdiz.vlovric21.memento;

import java.util.HashMap;
import java.util.Map;

public class MementoSpremiste {
    private final Map<Integer, AranzmanMemento> spremljenaStanja = new HashMap<>();

    public void dodajMemento(AranzmanMemento memento){
        spremljenaStanja.put(memento.getAranzman().getOznaka(), memento);
    }

    public AranzmanMemento dohvatiMemento(int oznaka){
        return spremljenaStanja.get(oznaka);
    }
}
