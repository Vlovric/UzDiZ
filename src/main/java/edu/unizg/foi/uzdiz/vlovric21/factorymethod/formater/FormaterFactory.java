package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

public class FormaterFactory {

    public static FormaterCreator getCreator(FormaterTip tip){
        switch (tip){
            case ITAP:
                return new ITAPFormaterCreator();
            case ITAK:
                return new ITAKFormaterCreator();
            case IRTA_OTKAZ:
                return new IRTAOtkazFormaterCreator();
            case IRTA:
                return new IRTAFormaterCreator();
            default:
                return new IROFormaterCreator();

        }
    }
}
