package foi.vlovric21;

import foi.vlovric21.parser.ArgumentParser;
import foi.vlovric21.parser.CsvParser;
import foi.vlovric21.parser.CsvTip;

public class Main {
    public static void main(String[] args) {
        ArgumentParser parser = new ArgumentParser();

        if(!parser.parsirajArgumente(args)){
            System.out.println("Neispravni argumenti");
            return;
        }

        ucitajPodatke(parser.getAranzmaniDatoteka(), parser.getRezervacijeDatoteka());
        interaktivniNacinRada();
    }

    static void ucitajPodatke(String aranzmaniDatoteka, String rezervacijeDatoteka){
        CsvParser csvParser = new CsvParser();
        csvParser.parsirajCsv(aranzmaniDatoteka, CsvTip.ARANZMAN);
        csvParser.parsirajCsv(rezervacijeDatoteka, CsvTip.REZERVACIJA);
    }
    static void interaktivniNacinRada(){
    }

}