package foi.vlovric21.factorymethod.formater;

public class ITAKFormaterCreator extends FormaterCreator {

    @Override
    protected Formater stvoriFormater() {
        return new ITAKFormater();
    }
}
