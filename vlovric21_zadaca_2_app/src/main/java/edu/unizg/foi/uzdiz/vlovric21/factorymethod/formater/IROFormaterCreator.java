package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.decorator.PodnozjeRezervacijaFormatDecorator;

public class IROFormaterCreator extends FormaterCreator {

    @Override
    protected Formater stvoriFormater() {
        return new PodnozjeRezervacijaFormatDecorator(new IROFormater());
    }
}
