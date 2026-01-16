package edu.unizg.foi.uzdiz.vlovric21.visitor;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;

import java.util.ArrayList;
import java.util.List;

public class PptarRezervacijaVisitor implements PptarVisitor {

    private final String pojam;
    private final List<Rezervacija> nadeneRezervacije = new ArrayList<>();

    public PptarRezervacijaVisitor(String pojam){
        this.pojam = pojam;
    }

    @Override
    public void posjeti(Aranzman aranzman) {
    }

    @Override
    public void posjeti(Rezervacija rezervacija) {
        String ime = rezervacija.getIme();
        String prezime = rezervacija.getPrezime();

        if(ime.contains(pojam) || prezime.contains(pojam)){
            nadeneRezervacije.add(rezervacija);
        }
    }

    public List<Rezervacija> getNadeneRezervacije() {
        return nadeneRezervacije;
    }

    public String getPojam() {
        return pojam;
    }
}
