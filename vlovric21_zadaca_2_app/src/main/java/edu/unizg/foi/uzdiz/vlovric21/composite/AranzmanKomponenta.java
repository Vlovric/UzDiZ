package edu.unizg.foi.uzdiz.vlovric21.composite;

import java.util.Collections;
import java.util.List;

public interface AranzmanKomponenta {

    default void dodajDijete(AranzmanKomponenta komponenta){
        throw new UnsupportedOperationException("Nije podrzano dodavanje djece.");
    }

    default void ukloniDijete(AranzmanKomponenta komponenta){
        throw new UnsupportedOperationException("Nije podrzano uklanjanje djece.");
    }

    default List<AranzmanKomponenta> dohvatiDjecu(){
        return Collections.emptyList();
    }
}
