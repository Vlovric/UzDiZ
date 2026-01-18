package edu.unizg.foi.uzdiz.vlovric21.cor;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.AranzmanKomponenta;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;

import java.util.List;

public class PohranaHandler extends BrisanjeHandler{


    @Override
    public void odradiZahtjev(String opcija) {
        System.out.println("Pohranjivanje svih podataka u Memento prije brisanja...");

        RepozitorijPodataka repo = RepozitorijPodataka.getInstance();
        List<AranzmanKomponenta> aranzmani = repo.getAranzmanKolekcija().dohvatiDjecu();

        for(AranzmanKomponenta k : aranzmani){
            Aranzman a = (Aranzman) k;
            if(repo.getMementoSpremiste().dohvatiMemento(a.getOznaka()) == null){
                repo.getMementoSpremiste().dodajMemento(a.spremiStanje());
                System.out.println("Pohranjen aranžman " + a.getOznaka() + " u Memento.");
            }
        }
        System.out.println();

        pozoviSljedeci(opcija);
    }
}
