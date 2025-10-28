package foi.vlovric21.parser;

public class ArgumentParser {
    private String aranzmaniDatoteka;
    private String rezervacijeDatoteka;

    public boolean parsirajArgumente(String[] args){

        if(args.length != 4){
            return false;
        }

        for(int i = 0; i < args.length; i+=2){
            String zastavica = args[i];
            String datoteka = args[i+1];

            switch(zastavica){
                case "--ta":
                    aranzmaniDatoteka = datoteka;
                    break;
                case "--rta":
                    rezervacijeDatoteka = datoteka;
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
