package foi.vlovric21.factorymethod.formater;

public abstract class FormaterCreator {

    protected abstract Formater stvoriFormater();

    public void formatiraj(Object obj){
        Formater formater = stvoriFormater();
        formater.formatiraj(obj);
    }

}
