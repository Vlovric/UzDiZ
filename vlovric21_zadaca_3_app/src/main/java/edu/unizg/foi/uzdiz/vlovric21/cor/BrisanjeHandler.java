package edu.unizg.foi.uzdiz.vlovric21.cor;

public abstract class BrisanjeHandler {
    protected BrisanjeHandler sljedeciHandler;

    public void postaviSljedeci(BrisanjeHandler sljedeciHandler) {
        this.sljedeciHandler = sljedeciHandler;
    }

    public void pozoviSljedeci(String opcija){
        if (sljedeciHandler != null) {
            sljedeciHandler.odradiZahtjev(opcija);
        }
    }

    public abstract void odradiZahtjev(String opcija);
}
