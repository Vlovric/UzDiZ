package edu.unizg.foi.uzdiz.vlovric21.composite;

import edu.unizg.foi.uzdiz.vlovric21.visitor.PptarElement;

import java.util.Collections;
import java.util.List;

public interface AranzmanKomponenta extends PptarElement {

    default void dodajDijete(AranzmanKomponenta komponenta){
        throw new UnsupportedOperationException("Nije podrzano dodavanje djece.");
    }

    default void ukloniDijete(AranzmanKomponenta komponenta){
        throw new UnsupportedOperationException("Nije podrzano uklanjanje djece.");
    }

    default List<AranzmanKomponenta> dohvatiDjecu(){
        return Collections.emptyList();
    }

    List<Rezervacija> dohvatiSveRezervacije();

    default String otkazi(){
        throw new UnsupportedOperationException("Nije podrzano otkazivanje.");
    }
}
