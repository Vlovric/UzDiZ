package edu.unizg.foi.uzdiz.vlovric21.visitor;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

import java.util.ArrayList;
import java.util.List;

public class PptarAranzmanVisitor implements PptarVisitor{

    private final String pojam;
    private final List<Aranzman> nadeniAranzmani = new ArrayList<>();

    public PptarAranzmanVisitor(String pojam){
        this.pojam = pojam;
    }

    @Override
    public void posjeti(Aranzman aranzman) {
        String naziv = aranzman.getNaziv();
        String opis = aranzman.getProgram();

        if(naziv.contains(pojam) || opis.contains(pojam)){
            nadeniAranzmani.add(aranzman);
        }
    }

    @Override
    public void posjeti(Rezervacija rezervacija) {
    }

    public List<Aranzman> getNadeniAranzmani() {
        return nadeniAranzmani;
    }

    public String getPojam() {
        return pojam;
    }
}
