package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.pomocne.DatumFormater;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Dictionary;
import java.util.Locale;

public abstract class Formater {
    protected DatumFormater datumFormater = new DatumFormater();
    private static final DecimalFormatSymbols simboli;
    private static final DecimalFormat df;

    static{
        simboli = new DecimalFormatSymbols(Locale.getDefault());
        simboli.setDecimalSeparator(',');
        simboli.setGroupingSeparator('.');
        df = new DecimalFormat("#,##0.00", simboli);
    }

    public abstract void formatiraj(Object obj);

    protected String skratiTekst(String tekst, int maxDuzina) {
        if(tekst == null) return "";
        return tekst.length() <= maxDuzina ? tekst : tekst.substring(0, maxDuzina - 3) + "...";
    }

    protected void ispisiNaslovTablice(String naslov, int sirinaTablice){
        System.out.println();
        System.out.println("Tablica: " + naslov);
        ispisiLiniju(sirinaTablice);
    }

    protected void ispisiLiniju(int sirina){
        System.out.println("-".repeat(Math.max(0, sirina)));
    }

    protected void ispisiZaglavlje(String format, int sirina, Object... stupci){
        System.out.printf(format + "%n", stupci);
        ispisiLiniju(sirina);
    }

    protected String formatirajBroj(Number broj){
        if (broj == null) return "";
        String b = df.format(broj);
        if(b.endsWith(",00")){
            return b.substring(0, b.length() - 3);
        }
        return df.format(broj);
    }
}
