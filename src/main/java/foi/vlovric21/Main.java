package foi.vlovric21;

import foi.vlovric21.builder.AranzmanDirector;
import foi.vlovric21.factorymethod.CsvParserFactory;
import foi.vlovric21.objekti.Aranzman;
import foi.vlovric21.parser.ArgumentParser;
import foi.vlovric21.parser.CsvParser;
import foi.vlovric21.parser.CsvTip;
import foi.vlovric21.singleton.RepozitorijPodataka;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        ArgumentParser parser = new ArgumentParser();

        if(!parser.parsirajArgumente(args)){
            System.out.println("Neispravni argumenti");
            return;
        }

        ucitajPodatke(parser.getAranzmaniDatoteka(), parser.getRezervacijeDatoteka());
        testirajObjekte();
        interaktivniNacinRada();
    }

    static void ucitajPodatke(String aranzmaniDatoteka, String rezervacijeDatoteka){
        CsvParser aranzmanParser = CsvParserFactory.stvoriParser(CsvTip.ARANZMAN);
        CsvParser rezervacijaParser = CsvParserFactory.stvoriParser(CsvTip.REZERVACIJA);

        aranzmanParser.parsirajCsv(aranzmaniDatoteka);
        rezervacijaParser.parsirajCsv(rezervacijeDatoteka);
    }
    static void interaktivniNacinRada(){
    }

    static void testirajObjekte(){
        // stvorit ocekivane objekte za aranzmane
        AranzmanDirector director = new AranzmanDirector();
        List<Aranzman> ocekivaniAranzmani = List.of(
                director.stvoriKompletanAranzman(1, "Tocno samo obavezno", "Program", "01.01.2025.", "02.01.2025.", 1, 1, 2, null, "", "", null, "", null, null, null),
                director.stvoriKompletanAranzman(2, "Tocno sve", "Program", "01.01.2025.", "02.01.2025.", 1, 1, 2, 1, "10:00", "11:00", 10, "avion; bus", 1, 1, 1),
                director.stvoriKompletanAranzman(3, "Tocno sve svi brojevi 0", "Program", "01.01.2025.", "02.01.2025.", 0, 0, 0, 0, "10:00", "11:00", 0, "avion;bus", 0, 0, 0),
                director.stvoriKompletanAranzman(18, "Tocno razmak cijena", "Program", "01.01.2025.", "02.01.2025.", 1, 1, 2, 1, "10:00", "11:00", null, "", null, null, null),
                director.stvoriKompletanAranzman(19, "Tocno razmak cijena", "Program", "01.01.2025.", "02.01.2025.", 1, 1, 2, 1, "10:00", "11:00", null, "", null, null, null),
                director.stvoriKompletanAranzman(20, "Tocno razmak cijena", "Program", "01.01.2025.", "02.01.2025.", 1, 1, 2, 1, "10:00", "11:00", null, "", null, null, null),
                director.stvoriKompletanAranzman(21, "Tocno datum nema tocku i maknute nule", "Program", "1.01.2025", "02.1.2025", 1, 1, 2, 1, "10:00", "11:00", null, "", null, null, null),
                director.stvoriKompletanAranzman(22, "Tocno sve bez razmaka", "Program", "01.01.2025.", "02.01.2025.", 1, 1, 2, 1, "10:00", "11:00", 10, "avion; bus", 1, 1, 1)
        );
        List<Aranzman> stvarniAranzmani = RepozitorijPodataka.getInstance().getAranzmani();
        // stvorit ocekivane objekte za rezervacije

        //usporedit i ispisat
        usporediAranzmane(ocekivaniAranzmani, stvarniAranzmani);
    }

    static void usporediAranzmane(List<Aranzman> ocekivani, List<Aranzman> stvarni) {
        Map<Integer, Aranzman> stvarniMap = stvarni.stream()
                .collect(Collectors.toMap(Aranzman::getOznaka, aranzman -> aranzman));
        for (Aranzman ocekivaniAranzman : ocekivani) {
            Aranzman stvarniAranzman = stvarniMap.get(ocekivaniAranzman.getOznaka());
            if (stvarniAranzman == null) {
                System.out.println("Nedostaje aranžman s oznakom: " + ocekivaniAranzman.getOznaka());
            } else if (!ocekivaniAranzman.equals(stvarniAranzman)) {
                System.out.println("Aranžman s oznakom " + ocekivaniAranzman.getOznaka() + " se ne podudara.");
                isprintajAranzman(ocekivaniAranzman, stvarniAranzman);
                System.out.println();
            } else {
                System.out.println("Aranžman s oznakom " + ocekivaniAranzman.getOznaka() + " je ispravan.\n");
            }
        }
    }

    static void isprintajAranzman(Aranzman ocekivaniAranzman, Aranzman stvarniAranzman) {
        System.out.println("Oznaka ocekivani: |" + ocekivaniAranzman.getOznaka() + "|");
        System.out.println("Oznaka stvarni  : |" + stvarniAranzman.getOznaka() + "|");
        System.out.println();

        System.out.println("Naziv ocekivani: |" + ocekivaniAranzman.getNaziv() + "|");
        System.out.println("Naziv stvarni  : |" + stvarniAranzman.getNaziv() + "|");
        System.out.println();

        System.out.println("Program ocekivani: |" + ocekivaniAranzman.getProgram() + "|");
        System.out.println("Program stvarni  : |" + stvarniAranzman.getProgram() + "|");
        System.out.println();

        System.out.println("Pocetni datum ocekivani: |" + ocekivaniAranzman.getPocetniDatum() + "|");
        System.out.println("Pocetni datum stvarni  : |" + stvarniAranzman.getPocetniDatum() + "|");
        System.out.println();

        System.out.println("Zavrsni datum ocekivani: |" + ocekivaniAranzman.getZavrsniDatum() + "|");
        System.out.println("Zavrsni datum stvarni  : |" + stvarniAranzman.getZavrsniDatum() + "|");
        System.out.println();

        System.out.println("Cijena ocekivani: |" + ocekivaniAranzman.getCijena() + "|");
        System.out.println("Cijena stvarni  : |" + stvarniAranzman.getCijena() + "|");
        System.out.println();

        System.out.println("Min broj putnika ocekivani: |" + ocekivaniAranzman.getMinBrojPutnika() + "|");
        System.out.println("Min broj putnika stvarni  : |" + stvarniAranzman.getMinBrojPutnika() + "|");
        System.out.println();

        System.out.println("Maks broj putnika ocekivani: |" + ocekivaniAranzman.getMaxBrojPutnika() + "|");
        System.out.println("Maks broj putnika stvarni  : |" + stvarniAranzman.getMaxBrojPutnika() + "|");
        System.out.println();

        System.out.println("Broj nocenja ocekivani: |" + ocekivaniAranzman.getBrojNocenja() + "|");
        System.out.println("Broj nocenja stvarni  : |" + stvarniAranzman.getBrojNocenja() + "|");
        System.out.println();

        System.out.println("Vrijeme kretanja ocekivani: |" + ocekivaniAranzman.getVrijemeKretanja() + "|");
        System.out.println("Vrijeme kretanja stvarni  : |" + stvarniAranzman.getVrijemeKretanja() + "|");
        System.out.println();

        System.out.println("Vrijeme povratka ocekivani: |" + ocekivaniAranzman.getVrijemePovratka() + "|");
        System.out.println("Vrijeme povratka stvarni  : |" + stvarniAranzman.getVrijemePovratka() + "|");
        System.out.println();

        System.out.println("Doplata za jednokrevetnu sobu ocekivani: |" + ocekivaniAranzman.getDoplataZaJednokrevetnuSobu() + "|");
        System.out.println("Doplata za jednokrevetnu sobu stvarni  : |" + stvarniAranzman.getDoplataZaJednokrevetnuSobu() + "|");
        System.out.println();

        System.out.println("Prijevoz ocekivani: |" + ocekivaniAranzman.getPrijevoz() + "|");
        System.out.println("Prijevoz stvarni  : |" + stvarniAranzman.getPrijevoz() + "|");
        System.out.println();

        System.out.println("Broj dorucka ocekivani: |" + ocekivaniAranzman.getBrojDorucka() + "|");
        System.out.println("Broj dorucka stvarni  : |" + stvarniAranzman.getBrojDorucka() + "|");
        System.out.println();

        System.out.println("Broj ruckova ocekivani: |" + ocekivaniAranzman.getBrojRuckova() + "|");
        System.out.println("Broj ruckova stvarni  : |" + stvarniAranzman.getBrojRuckova() + "|");
        System.out.println();

        System.out.println("Broj vecera ocekivani: |" + ocekivaniAranzman.getBrojVecera() + "|");
        System.out.println("Broj vecera stvarni  : |" + stvarniAranzman.getBrojVecera() + "|");
        System.out.println();



    }
}
