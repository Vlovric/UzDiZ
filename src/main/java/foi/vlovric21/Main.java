package foi.vlovric21;

import foi.vlovric21.builder.AranzmanDirector;
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
        CsvParser csvParser = new CsvParser();
        csvParser.parsirajCsv(aranzmaniDatoteka, CsvTip.ARANZMAN);
        csvParser.parsirajCsv(rezervacijeDatoteka, CsvTip.REZERVACIJA);
    }
    static void interaktivniNacinRada(){
    }

    static void testirajObjekte(){
        // stvorit ocekivane objekte za aranzmane
        AranzmanDirector director = new AranzmanDirector();
        List<Aranzman> ocekivaniAranzmani = List.of(
                director.stvoriAranzman(1, "Tocno samo obavezno", "Program", "01.01.2025.", "02.01.2025.", 1, 1, 2, null, null, null, null, "", null, null, null),
                director.stvoriAranzman(2, "Tocno sve", "Program", "01.01.2025.", "02.01.2025.", 1, 1, 2, 1, "10:00", "11:00", 10, "avion; bus", 1, 1, 1),
                director.stvoriAranzman(3, "Tocno sve svi brojevi 0", "Program", "01.01.2025.", "02.01.2025.", 0, 0, 0, 0, "10:00", "11:00", 0, "avion;bus", 0, 0, 0),
                director.stvoriAranzman(18, "Tocno razmak cijena", "Program", "01.01.2025.", "02.01.2025.", 1, 1, 2, 1, "10:00", "11:00", null, "", null, null, null),
                director.stvoriAranzman(19, "Tocno razmak cijena", "Program", "01.01.2025.", "02.01.2025.", 1, 1, 2, 1, "10:00", "11:00", null, "", null, null, null),
                director.stvoriAranzman(20, "Tocno razmak cijena", "Program", "01.01.2025.", "02.01.2025.", 1, 1, 2, 1, "10:00", "11:00", null, "", null, null, null),
                director.stvoriAranzman(21, "Tocno datum nema tocku i maknute nule", "Program", "1.01.2025", "02.1.2025", 1, 1, 2, 1, "10:00", "11:00", null, "", null, null, null),
                director.stvoriAranzman(22, "Tocno sve bez razmaka", "Program", "01.01.2025.", "02.01.2025.", 1, 1, 2, 1, "10:00", "11:00", 10, "avion; bus", 1, 1, 1)
        );
        // stvorit ocekivane objekte za rezervacije
        // usporedit s pravima
        List<Aranzman> stvarniAranzmani = RepozitorijPodataka.getInstance().getAranzmani();
        usporediAranzmane(ocekivaniAranzmani, stvarniAranzmani);
        // ispisat rezultate
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
                System.out.println("Očekivano: " + ocekivaniAranzman);
                System.out.println("Stvarno: " + stvarniAranzman);
            } else {
                System.out.println("Aranžman s oznakom " + ocekivaniAranzman.getOznaka() + " je ispravan.");
            }
        }
    }
}
