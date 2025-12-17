package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.decorator.PodnozjeAranzmanFormatDecorator;

public class ITAKFormaterCreator extends FormaterCreator {

    @Override
    protected Formater stvoriFormater() {
        return new PodnozjeAranzmanFormatDecorator(new ITAKFormater());
    }
}
