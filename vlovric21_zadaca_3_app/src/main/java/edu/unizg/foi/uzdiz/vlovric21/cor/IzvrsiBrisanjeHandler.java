package edu.unizg.foi.uzdiz.vlovric21.cor;

import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;

public class IzvrsiBrisanjeHandler extends BrisanjeHandler{

    @Override
    public void odradiZahtjev(String opcija) {
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();

        if(opcija.equals("A")){
            repozitorij.obrisiSveAranzmane();
            System.out.println("Obrisani su svi aranžmani i njihove rezervacije.");
        }else{
            repozitorij.obrisiSveRezervacije();
            System.out.println("Obrisane su sve rezervacije.");
        }
    }
}
