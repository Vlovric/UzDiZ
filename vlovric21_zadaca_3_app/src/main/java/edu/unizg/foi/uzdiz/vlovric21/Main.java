package edu.unizg.foi.uzdiz.vlovric21;

import edu.unizg.foi.uzdiz.vlovric21.parser.ArgumentParser;
import edu.unizg.foi.uzdiz.vlovric21.pomocne.KomandePomocnik;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArgumentParser parser = new ArgumentParser();

        if(!parser.parsirajArgumente(args)){
            System.out.println("Upisane nepoznate opcije");
            return;
        }

        ucitajPodatke(parser.getAranzmaniDatoteka(), parser.getRezervacijeDatoteka());
        interaktivniNacinRada();
    }

    static void ucitajPodatke(String aranzmaniDatoteka, String rezervacijeDatoteka){

        if(aranzmaniDatoteka != null){
            RepozitorijPodataka.getInstance().ucitajAranzmaneIzDatoteke(aranzmaniDatoteka);
        }
        if(rezervacijeDatoteka != null){
            RepozitorijPodataka.getInstance().ucitajRezervacijeIzDatoteke(rezervacijeDatoteka);
        }
    }

    static void interaktivniNacinRada(){
        System.out.println("Započet interaktivni način rada.");
        Scanner scanner = new Scanner(System.in);
        KomandePomocnik komandePomocnik = new KomandePomocnik();

        while(true){
            System.out.println();
            System.out.print("> ");
            String unos = scanner.nextLine();

            String[] dijeloviUnosa = unos.split(" ");
            String komanda = dijeloviUnosa[0];

            System.out.println("Pozvano: " + unos);

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
                case "OTA":
                    komandePomocnik.otkazAranzmanaOTA(unos);
                    break;
                case "IP":
                    komandePomocnik.postavljanjeIspisaIP(unos);
                    break;
                case "BP":
                    komandePomocnik.brisanjePodatakaBP(unos);
                    break;
                case "UP":
                    komandePomocnik.ucitavanjePodatakaUP(unos);
                    break;
                case "ITAS":
                    komandePomocnik.ispisStatistikeITAS(unos);
                    break;
                case "POD":
                    komandePomocnik.dodavanjePodnozjaPOD();
                    break;
                case "PPTAR":
                    komandePomocnik.pretrazivanjePodatakaPPTAR(unos);
                    break;
                case "PSTAR":
                    komandePomocnik.spremiStanjeAranzmanaPSTAR(unos);
                    break;
                case "VSTAR":
                    komandePomocnik.vratiStanjeAranzmanaVSTAR(unos);
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Neispravna komanda.");
            }
        }
    }
}