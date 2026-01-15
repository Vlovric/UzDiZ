package edu.unizg.foi.uzdiz.vlovric21.decorator;

import edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater.Formater;

public abstract class FormaterDecorator extends Formater {

    protected final Formater formater;

    protected FormaterDecorator(Formater formater) {
        this.formater = formater;
    }

    @Override
    public void formatiraj(Object obj) {
        formater.formatiraj(obj);
    }
}
