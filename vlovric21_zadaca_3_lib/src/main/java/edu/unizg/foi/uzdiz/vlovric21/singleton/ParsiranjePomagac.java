package edu.unizg.foi.uzdiz.vlovric21.singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParsiranjePomagac {
    private static ParsiranjePomagac instanca = new ParsiranjePomagac();
    int brojGresaka = 1;
    List<Map<String, String>> ispravniRedoviAranzman = new ArrayList<>();
    List<Map<String, String>> ispravniRedoviRezervacija = new ArrayList<>();

    private ParsiranjePomagac() {}

    public static ParsiranjePomagac getInstanca() {
        return instanca;
    }

    public int getBrojGresaka(){
        return brojGresaka++;
    }

    public int setBrojGresaka(){
        return brojGresaka;
    }

    public List<Map<String, String>> getIspravniRedoviAranzman() {
        return ispravniRedoviAranzman;
    }

    public void dodajIspravanRedAranzman(Map<String, String> red) {
        ispravniRedoviAranzman.add(red);
    }

    public List<Map<String, String>> getIspravniRedoviRezervacija() {
        return ispravniRedoviRezervacija;
    }

    public void dodajIspravanRedRezervacija(Map<String, String> red) {
        ispravniRedoviRezervacija.add(red);
    }
}
