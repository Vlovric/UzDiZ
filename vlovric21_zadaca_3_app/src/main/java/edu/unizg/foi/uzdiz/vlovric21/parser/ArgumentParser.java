package edu.unizg.foi.uzdiz.vlovric21.parser;

import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.strategy_null_object.JdrRezervacijaUpravitelj;
import edu.unizg.foi.uzdiz.vlovric21.strategy_null_object.VdrRezervacijaUpravitelj;

public class ArgumentParser {
    private String aranzmaniDatoteka;
    private String rezervacijeDatoteka;

    public boolean parsirajArgumente(String[] args){
        boolean jdr = false;
        boolean vdr = false;

        for(int i=0; i< args.length; i++){
            String arg = args[i];
            switch(arg){
                case "--ta":
                    if(i+1 >= args.length){
                        return false;
                    }
                    aranzmaniDatoteka = args[i+1];
                    i++;
                    break;
                case "--rta":
                    if(i+1 >= args.length){
                        return false;
                    }
                    rezervacijeDatoteka = args[i+1];
                    i++;
                    break;
                case "--jdr":
                    if(vdr){
                        return false;
                    }
                    jdr = true;
                    break;
                case "--vdr":
                    if(jdr){
                        return false;
                    }
                    vdr = true;
                    break;
                default:
                    return false;
            }
        }
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
        if(jdr){
             repozitorij.setRezervacijaUpravitelj(new JdrRezervacijaUpravitelj());
        }
        if(vdr){
            repozitorij.setRezervacijaUpravitelj(new VdrRezervacijaUpravitelj());
        }
        return true;
    }

    public String getAranzmaniDatoteka(){
        return aranzmaniDatoteka;
    }
    public String getRezervacijeDatoteka(){
        return rezervacijeDatoteka;
    }
}
