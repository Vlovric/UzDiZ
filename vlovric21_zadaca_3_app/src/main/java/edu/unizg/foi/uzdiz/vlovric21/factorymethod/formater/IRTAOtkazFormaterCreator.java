package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.decorator.PodnozjeRezervacijaFormatDecorator;

public class IRTAOtkazFormaterCreator extends FormaterCreator {

    private final String naslov;

    public IRTAOtkazFormaterCreator(){
        this.naslov = null;
    }

    public IRTAOtkazFormaterCreator(String naslov){
        this.naslov = naslov;
    }

    @Override
    protected Formater stvoriFormater() {
        return naslov == null ? new PodnozjeRezervacijaFormatDecorator(new IRTAOtkazFormater()) : new PodnozjeRezervacijaFormatDecorator(new IRTAOtkazFormater(naslov));
    }
}
