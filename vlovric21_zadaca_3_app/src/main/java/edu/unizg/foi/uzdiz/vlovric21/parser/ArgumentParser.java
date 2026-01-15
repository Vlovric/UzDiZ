package edu.unizg.foi.uzdiz.vlovric21.parser;

public class ArgumentParser {
    private String aranzmaniDatoteka;
    private String rezervacijeDatoteka;

    public boolean parsirajArgumente(String[] args){

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
                default:
                    return false;
            }
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
