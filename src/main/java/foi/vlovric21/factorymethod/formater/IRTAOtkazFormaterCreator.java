package foi.vlovric21.factorymethod.formater;

public class IRTAOtkazFormaterCreator extends FormaterCreator {

    @Override
    protected Formater stvoriFormater() {
        return new IRTAOtkazFormater();
    }
}
