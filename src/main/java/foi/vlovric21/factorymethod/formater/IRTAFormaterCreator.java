package foi.vlovric21.factorymethod.formater;

public class IRTAFormaterCreator extends FormaterCreator {

    @Override
    protected Formater stvoriFormater() {
        return new IRTAFormater();
    }
}
