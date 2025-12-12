package edu.unizg.foi.uzdiz.vlovric21;

import edu.unizg.foi.uzdiz.vlovric21.facade.CsvParsiranjeFacade;
import edu.unizg.foi.uzdiz.vlovric21.parser.ArgumentParser;
import edu.unizg.foi.uzdiz.vlovric21.pomocne.KomandePomocnik;
import edu.unizg.foi.uzdiz.vlovric21.pomocne.RezervacijaPomocnik;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

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

        List<Map<String, String>> aranzmanRedovi = CsvParsiranjeFacade.ucitajAranzmane(aranzmaniDatoteka);
        List<Map<String, String>> rezervacijaRedovi = CsvParsiranjeFacade.ucitajRezervacije(rezervacijeDatoteka);

        //moram buildat objekte i ucitat ih u repozitorij (zasad)

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