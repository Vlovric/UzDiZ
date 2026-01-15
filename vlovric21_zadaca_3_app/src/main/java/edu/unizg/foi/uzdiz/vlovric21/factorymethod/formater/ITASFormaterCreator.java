package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

public class ITASFormaterCreator extends FormaterCreator {

    @Override
    protected Formater stvoriFormater() {
        return new ITASFormater();
    }
}
