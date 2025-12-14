package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;

import java.time.LocalDate;

public class ITAPFormater extends Formater {
    private static int brojCrtica = 75;

    @Override
    public void formatiraj(Object obj) {
        Aranzman aranzman = (Aranzman) obj;

        LocalDate pocDatum = datumFormater.parseDatum(aranzman.getPocetniDatum());
        LocalDate zavDatum = datumFormater.parseDatum(aranzman.getZavrsniDatum());

        String cijena = formatirajBroj(aranzman.getCijena());
        String doplata = aranzman.getDoplataZaJednokrevetnuSobu() != null ?
                formatirajBroj(aranzman.getDoplataZaJednokrevetnuSobu()) : "";
        String minBrojPutnika = formatirajBroj(aranzman.getMinBrojPutnika());
        String maxBrojPutnika = formatirajBroj(aranzman.getMaxBrojPutnika());
        String brojNocenja = formatirajBroj(aranzman.getBrojNocenja());
        String brojDorucka = aranzman.getBrojDorucka() != null ?
                formatirajBroj(aranzman.getBrojDorucka()) : "";
        String brojRuckova = aranzman.getBrojRuckova() != null ?
                formatirajBroj(aranzman.getBrojRuckova()) : "";
        String brojVecera = aranzman.getBrojVecera() != null ?
                formatirajBroj(aranzman.getBrojVecera()) : "";

        ispisiNaslovTablice("Detalji aranžmana", brojCrtica);

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
        System.out.printf("%-25s: %25s%n", "Cijena", cijena);
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %25s%n", "Doplata za 1/1 sobu", doplata);
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %25s%n", "Min broj putnika", minBrojPutnika);
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %25s%n", "Maks broj putnika", maxBrojPutnika);
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %25s%n", "Broj noćenja", brojNocenja);
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %s%n", "Prijevoz",
                aranzman.getPrijevoz() != null ? aranzman.getPrijevoz() : "");
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %25s%n", "Broj doručka", brojDorucka);
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %25s%n", "Broj ručkova", brojRuckova);
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %25s%n", "Broj večera", brojVecera);
        System.out.println("-".repeat(brojCrtica));
        System.out.printf("%-25s: %s%n", "Status", aranzman.getStatus());
        System.out.println();
    }
}
