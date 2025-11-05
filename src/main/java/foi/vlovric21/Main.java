package foi.vlovric21;

import foi.vlovric21.factorymethod.csv.CsvParserCreator;
import foi.vlovric21.parser.ArgumentParser;
import foi.vlovric21.parser.CsvParser;
import foi.vlovric21.parser.CsvTip;
import foi.vlovric21.pomocne.KomandePomocnik;
import foi.vlovric21.pomocne.RezervacijaPomocnik;

import java.util.*;

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
        CsvParser aranzmanParser = CsvParserCreator.stvoriParser(CsvTip.ARANZMAN);
        CsvParser rezervacijaParser = CsvParserCreator.stvoriParser(CsvTip.REZERVACIJA);

        aranzmanParser.parsirajCsv(aranzmaniDatoteka);
        rezervacijaParser.parsirajCsv(rezervacijeDatoteka);

        RezervacijaPomocnik rezervacijaPomocnik = new RezervacijaPomocnik();
        rezervacijaPomocnik.ucitajSveInicijalneRezervacije();

    }

    static void interaktivniNacinRada(){
        System.out.println("Započet interaktivni način rada.");
        Scanner scanner = new Scanner(System.in);
        KomandePomocnik komandePomocnik = new KomandePomocnik();

        while(true){
            String unos = scanner.nextLine();

            String[] dijeloviUnosa = unos.split(" ");
            String komanda = dijeloviUnosa[0];

            switch(komanda){
                case "ITAK":
                    komandePomocnik.pregledAranzmanRazdobljeITAK(unos);
                    break;
                case "ITAP":
                    komandePomocnik.pregledAranzmanITAP(unos);
                    break;
                case "IRTA":
                    komandePomocnik.pregledRezervacijaAranzmanIRTA(unos);
                    break;
                case "IRO":
                    komandePomocnik.pregledRezervacijaOsobaIRO(unos);
                    break;
                case "ORTA":
                    komandePomocnik.otkazRezervacijeORTA(unos);
                    break;
                case "DRTA":
                    komandePomocnik.dodavanjeRezervacijeDRTA(unos);
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Neispravna komanda.");
            }
        }
    }
}
