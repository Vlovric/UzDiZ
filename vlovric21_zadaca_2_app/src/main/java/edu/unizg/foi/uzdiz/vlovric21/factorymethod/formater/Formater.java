package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.pomocne.DatumFormater;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Dictionary;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Formater {
    protected DatumFormater datumFormater = new DatumFormater();
    private static final DecimalFormatSymbols simboli;
    private static final DecimalFormat df;
    private static final Pattern sirinaFormat = Pattern.compile("%([-+ 0,(]*)?(\\d+)?(\\.\\d+)?[a-zA-Z%]");

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

    protected int izracunajSirinuTablice(String format){
        int sirina = 0;
        Matcher matcher = sirinaFormat.matcher(format);
        int prethodniKraj = 0;

        while(matcher.find()){
            sirina += matcher.start() - prethodniKraj;

            String grupa = matcher.group();
            if("%n".equals(grupa)){

            }else if("%%".equals(grupa)){
                sirina += 1;
            }else{
                String sirinaKonkretno = matcher.group(2);
                if(sirinaKonkretno != null){
                    sirina += Integer.parseInt(sirinaKonkretno);
                }
            }
            prethodniKraj = matcher.end();
        }
        sirina += format.length() - prethodniKraj;
        return sirina;
    }

    protected boolean kronoloskiRedoslijed(){
        return RepozitorijPodataka.getInstance().getKronoloskiRedoslijed();
    }
}
