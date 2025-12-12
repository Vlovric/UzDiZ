package edu.unizg.foi.uzdiz.vlovric21;

import edu.unizg.foi.uzdiz.vlovric21.factorymethod.csv.AranzmanCsvParserCreator;
import edu.unizg.foi.uzdiz.vlovric21.factorymethod.csv.CsvParserCreator;
import edu.unizg.foi.uzdiz.vlovric21.factorymethod.csv.RezervacijaCsvParserCreator;
import edu.unizg.foi.uzdiz.vlovric21.parser.ArgumentParser;
import edu.unizg.foi.uzdiz.vlovric21.pomocne.KomandePomocnik;
import edu.unizg.foi.uzdiz.vlovric21.pomocne.RezervacijaPomocnik;

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
        CsvParserCreator aranzmanParser = new AranzmanCsvParserCreator();
        CsvParserCreator rezervacijaParser = new RezervacijaCsvParserCreator();

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
