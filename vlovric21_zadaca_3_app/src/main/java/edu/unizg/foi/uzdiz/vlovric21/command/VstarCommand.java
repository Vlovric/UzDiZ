package edu.unizg.foi.uzdiz.vlovric21.command;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.AranzmanKomponenta;
import edu.unizg.foi.uzdiz.vlovric21.memento.AranzmanMemento;
import edu.unizg.foi.uzdiz.vlovric21.memento.MementoSpremiste;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;

public class VstarCommand implements STARCommand{

    private final Aranzman aranzman;
    private final MementoSpremiste spremiste;

    public VstarCommand(Aranzman aranzman, MementoSpremiste spremiste){
        this.aranzman = aranzman;
        this.spremiste = spremiste;
    }

    @Override
    public String izvrsi(){
        AranzmanMemento memento = spremiste.dohvatiMemento(aranzman.getOznaka());
        if(memento == null){
            return "Nema spremljenog stanja za aranžman s oznakom: " + aranzman.getOznaka();
        }
        aranzman.vratiStanje(memento);
        
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
        for(AranzmanKomponenta k : repozitorij.getAranzmanKolekcija().dohvatiDjecu()){
            Aranzman a = (Aranzman) k;
            repozitorij.dodajAranzmanKronologija(a.getOznaka());
        }
        repozitorij.getAranzmanKolekcija().popraviKronologiju();

        return "Vraćeno stanje za aranžman s oznakom: " + aranzman.getOznaka();
    }

}
