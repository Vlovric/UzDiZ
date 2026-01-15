package edu.unizg.foi.uzdiz.vlovric21.decorator;

import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater.Formater;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PodnozjeRezervacijaFormatDecorator extends FormaterDecorator {

    public PodnozjeRezervacijaFormatDecorator(Formater formater) {
        super(formater);
    }

    @Override
    public void formatiraj(Object obj) {
        super.formatiraj(obj);

        if(!RepozitorijPodataka.getInstance().isPodnozje()){
            return;
        }

        Map<String, Integer> brPoStat = new HashMap<>();
        int ukupno = 0;

        List<Rezervacija> rezervacije = ((List<Rezervacija>) obj);
        for(Rezervacija r : rezervacije){
            ukupno++;
            brPoStat.put(r.getStatus(), brPoStat.getOrDefault(r.getStatus(), 0) + 1);
        }

        if(ukupno == 0){
            System.out.println("Nema dostupnih rezervacija za prikaz statistike.");
            return;
        }

        System.out.println();
        System.out.println("Statistika rezervacija: ");
        System.out.println("Ukupno rezervacija: " + ukupno);
        for(Map.Entry<String, Integer> entry : brPoStat.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }


    }
}
