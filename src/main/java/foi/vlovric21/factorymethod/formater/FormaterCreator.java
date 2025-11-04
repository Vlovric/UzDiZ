package foi.vlovric21.factorymethod.formater;

// Creator (abstract)
public class FormaterCreator {

    public Formater stvoriFormater(FormaterTip tip){
        switch(tip){
            case FormaterTip.ITAK:
                return new ITAKFormater();
            case FormaterTip.ITAP:
                return new ITAPFormater();
            case FormaterTip.IRO:
                return new IROFormater();
            case FormaterTip.IRTA:
                return new IRTAFormater();
            case FormaterTip.IRTA_OTKAZ:
                return new IRTAOtkazFormater();
            default:
                return null;
        }
    }
}
