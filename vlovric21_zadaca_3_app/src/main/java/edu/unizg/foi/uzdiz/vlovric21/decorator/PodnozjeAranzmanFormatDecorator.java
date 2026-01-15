package edu.unizg.foi.uzdiz.vlovric21.decorator;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater.Formater;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PodnozjeAranzmanFormatDecorator extends FormaterDecorator {

    public PodnozjeAranzmanFormatDecorator(Formater formater) {
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

        List<Aranzman> aranzmani = ((List<Aranzman>) obj);
        for(Aranzman a : aranzmani){
            ukupno++;
            brPoStat.put(a.getStatus(), brPoStat.getOrDefault(a.getStatus(), 0) + 1);
        }

        if(ukupno == 0){
            System.out.println("Nema dostupnih aranžmana za prikaz statistike.");
            return;
        }

        System.out.println();
        System.out.println("Statistika aranžmana: ");
        System.out.println("Ukupno aranžmana: " + ukupno);
        for(Map.Entry<String, Integer> entry : brPoStat.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
