package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.decorator.PodnozjeRezervacijaFormatDecorator;

public class IRTAOtkazFormaterCreator extends FormaterCreator {

    @Override
    protected Formater stvoriFormater() {
        return new PodnozjeRezervacijaFormatDecorator(new IRTAOtkazFormater());
    }
}
