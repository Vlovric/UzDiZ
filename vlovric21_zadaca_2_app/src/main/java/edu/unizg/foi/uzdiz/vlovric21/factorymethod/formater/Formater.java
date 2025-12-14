package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.pomocne.DatumFormater;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.*;

import java.util.Dictionary;

public abstract class Formater {
    protected DatumFormater datumFormater = new DatumFormater();

    public abstract void formatiraj(Object obj);

    protected String skratiTekst(String tekst, int maxDuzina) {
        if(tekst == null) return "";
        return tekst.length() <= maxDuzina ? tekst : tekst.substring(0, maxDuzina - 3) + "...";
    }
}
