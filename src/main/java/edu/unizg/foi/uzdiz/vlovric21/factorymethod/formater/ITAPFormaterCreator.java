package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

public class ITAPFormaterCreator extends FormaterCreator {

    @Override
    protected Formater stvoriFormater() {
        return new ITAPFormater();
    }
}
