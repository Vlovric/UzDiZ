package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.objekti.Aranzman;

import java.time.LocalDate;

public class ITAPFormater extends Formater {
    private static int brojCrtica = 50;

    @Override
    public void formatiraj(Object obj) {
        Aranzman aranzman = (Aranzman) obj;

        LocalDate pocDatum = datumFormater.parseDatum(aranzman.getPocetniDatum());
        LocalDate zavDatum = datumFormater.parseDatum(aranzman.getZavrsniDatum());

        System.out.printf("%-25s: %d%n", "Oznaka", aranzman.getOznaka());
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %s%n", "Naziv", aranzman.getNaziv());
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %s%n", "Program", aranzman.getProgram());
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %s%n", "Početni datum", datumFormater.formatirajDatumIspis(pocDatum));
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %s%n", "Završni datum", datumFormater.formatirajDatumIspis(zavDatum));
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %s%n", "Vrijeme kretanja",
                aranzman.getVrijemeKretanja() != null ? aranzman.getVrijemeKretanja() : "");
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %s%n", "Vrijeme povratka",
                aranzman.getVrijemePovratka() != null ? aranzman.getVrijemePovratka() : "");
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %d%n", "Cijena", aranzman.getCijena());
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %s%n", "Doplata za 1/1 sobu",
                aranzman.getDoplataZaJednokrevetnuSobu() != null ?
                        aranzman.getDoplataZaJednokrevetnuSobu().toString() : "");
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %d%n", "Min broj putnika", aranzman.getMinBrojPutnika());
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %d%n", "Maks broj putnika", aranzman.getMaxBrojPutnika());
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %d%n", "Broj noćenja", aranzman.getBrojNocenja());
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %s%n", "Prijevoz",
                aranzman.getPrijevoz() != null ? aranzman.getPrijevoz() : "");
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %s%n", "Broj doručka",
                aranzman.getBrojDorucka() != null ? aranzman.getBrojDorucka().toString() : "");
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %s%n", "Broj ručkova",
                aranzman.getBrojRuckova() != null ? aranzman.getBrojRuckova().toString() : "");
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %s%n", "Broj večera",
                aranzman.getBrojVecera() != null ? aranzman.getBrojVecera().toString() : "");
        System.out.println();
    }
}
