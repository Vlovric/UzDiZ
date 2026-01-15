package edu.unizg.foi.uzdiz.vlovric21.factorymethod.unos_objekta;

import java.util.List;
import java.util.Map;

public abstract class CsvObjectUnositelj {

    protected abstract boolean validirajObjekt(Map<String, String> red);
    protected abstract void unesiObjekt(Map<String, String> red);

    protected Integer parseOpcionalniInt(String vrijednost){
        String skracen = vrijednost.trim();
        return skracen.isEmpty() ? null : Integer.valueOf(skracen);
    }

    public void validirajUnesiObjekte(List<Map<String, String>> redovi){
        for(Map<String, String> red : redovi){
            if(!validirajObjekt(red)){
                return;
            }
            unesiObjekt(red);
        }
    }
}
