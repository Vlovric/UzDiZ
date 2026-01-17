package edu.unizg.foi.uzdiz.vlovric21.command;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.memento.AranzmanMemento;
import edu.unizg.foi.uzdiz.vlovric21.memento.MementoSpremiste;

public class PstarCommand implements STARCommand{

    private final Aranzman aranzman;
    private final MementoSpremiste spremiste;

    public PstarCommand(Aranzman aranzman, MementoSpremiste spremiste){
        this.aranzman = aranzman;
        this.spremiste = spremiste;
    }

    @Override
    public String izvrsi() {
        AranzmanMemento memento = aranzman.spremiStanje();
        spremiste.dodajMemento(memento);
        return "Spremljeno stanje za aranžman s oznakom: " + aranzman.getOznaka();
    }
}
