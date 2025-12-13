package edu.unizg.foi.uzdiz.vlovric21.composite;

import java.util.ArrayList;
import java.util.List;

public class AranzmanKolekcija implements AranzmanKomponenta {

    private final List<AranzmanKomponenta> djeca = new ArrayList<>();

    @Override
    public void dodajDijete(AranzmanKomponenta komponenta){
        djeca.add(komponenta);
    }

    @Override
    public void ukloniDijete(AranzmanKomponenta komponenta){
        djeca.remove(komponenta);
    }

    @Override
    public List<AranzmanKomponenta> dohvatiDjecu() {
        return djeca;
    }

    public Aranzman dohvatiAranzmanPoOznaci(int oznaka){
        for(AranzmanKomponenta k : djeca){
            if(k instanceof Aranzman a && a.getOznaka() == oznaka){
                return a;
            }
        }
        return null;
    }
}
