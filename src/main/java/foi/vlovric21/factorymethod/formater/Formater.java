package foi.vlovric21.factorymethod.formater;

import foi.vlovric21.objekti.RezervacijaStatus;

// Product apstraktni
public abstract class Formater {

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
            default: return "";
        }
    }
}
