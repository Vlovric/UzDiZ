package foi.vlovric21.factorymethod.formater;

public class IROFormaterCreator extends FormaterCreator {

    @Override
    protected Formater stvoriFormater() {
        return new IROFormater();
    }
}
