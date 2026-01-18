package edu.unizg.foi.uzdiz.vlovric21.command;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.AranzmanKomponenta;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.memento.AranzmanMemento;
import edu.unizg.foi.uzdiz.vlovric21.memento.MementoSpremiste;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;

public class VstarCommand implements STARCommand{

    private Aranzman aranzman;
    private final MementoSpremiste spremiste;
    private final int oznaka;

    public VstarCommand(Aranzman aranzman, MementoSpremiste spremiste, int oznaka){
        this.aranzman = aranzman;
        this.spremiste = spremiste;
        this.oznaka = oznaka;
    }

    @Override
    public String izvrsi(){
        AranzmanMemento memento = spremiste.dohvatiMemento(oznaka);
        if(memento == null){
            return "Nema spremljenog stanja za aranžman s oznakom: " + oznaka;
        }
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();

        if(aranzman == null){
            aranzman = new Aranzman(memento.getAranzman());
            repozitorij.dodajAranzman(aranzman);
        }

        aranzman.vratiStanje(memento);

        for(AranzmanKomponenta k : repozitorij.getAranzmanKolekcija().dohvatiDjecu()){
            Aranzman a = (Aranzman) k;
            repozitorij.dodajAranzmanKronologija(a.getOznaka());
        }
        repozitorij.getAranzmanKolekcija().popraviKronologiju();

        return "Vraćeno stanje za aranžman s oznakom: " + aranzman.getOznaka();
    }

}
