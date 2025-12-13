package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.composite.RezervacijaStatus;
import edu.unizg.foi.uzdiz.vlovric21.pomocne.DatumFormater;

public abstract class Formater {
    protected DatumFormater datumFormater = new DatumFormater();

    public abstract void formatiraj(Object obj);

    protected String skratiTekst(String tekst, int maxDuzina) {
        if(tekst == null) return "";
        return tekst.length() <= maxDuzina ? tekst : tekst.substring(0, maxDuzina - 3) + "...";
    }

    protected String pretvoriStatusUVrstu(RezervacijaStatus status) {
        switch(status) {
            case PRIMLJENA: return "Primljena";
            case AKTIVNA: return "Aktivna";
            case NA_CEKANJU: return "Na čekanju";
            case OTKAZANA: return "Otkazana";
            case NOVA: return "Nova";
            case ODGODENA: return "Odgođena";
            default: return "";
        }
    }
}
