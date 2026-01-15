package edu.unizg.foi.uzdiz.vlovric21.factorymethod.unos_objekta;

import java.util.List;
import java.util.Map;

public abstract class CsvObjectCreator {

    protected abstract CsvObjectUnositelj stvoriUnositelja();

    public void validirajUnesiObjekte(List<Map<String, String>> redovi){
        CsvObjectUnositelj unositelj = stvoriUnositelja();
        unositelj.validirajUnesiObjekte(redovi);
    }
}
