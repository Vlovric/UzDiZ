package edu.unizg.foi.uzdiz.vlovric21.factorymethod.formater;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;

import java.time.LocalDate;

public class ITAPFormater extends Formater {

    @Override
    public void formatiraj(Object obj) {
        Aranzman aranzman = (Aranzman) obj;

        LocalDate pocDatum = datumFormater.parseDatum(aranzman.getPocetniDatum());
        LocalDate zavDatum = datumFormater.parseDatum(aranzman.getZavrsniDatum());

        String naziv = skratiTekst(aranzman.getNaziv(), 20);

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

        String redFormat = "%-20s: %20s%n";
        int brojCrtica = izracunajSirinuTablice(redFormat);

        ispisiNaslovTablice("Detalji aranžmana", brojCrtica);

        System.out.printf("%-20s: %d%n", "Oznaka", aranzman.getOznaka());
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %s%n", "Naziv", aranzman.getNaziv());
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %s%n", "Program", aranzman.getProgram());
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %s%n", "Početni datum", datumFormater.formatirajDatumIspis(pocDatum));
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %s%n", "Završni datum", datumFormater.formatirajDatumIspis(zavDatum));
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %s%n", "Vrijeme kretanja",
                aranzman.getVrijemeKretanja() != null ? aranzman.getVrijemeKretanja() : "");
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %s%n", "Vrijeme povratka",
                aranzman.getVrijemePovratka() != null ? aranzman.getVrijemePovratka() : "");
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %20s%n", "Cijena", cijena);
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %20s%n", "Doplata za 1/1 sobu", doplata);
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %20s%n", "Min broj putnika", minBrojPutnika);
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %20s%n", "Maks broj putnika", maxBrojPutnika);
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %20s%n", "Broj noćenja", brojNocenja);
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %s%n", "Prijevoz",
                aranzman.getPrijevoz() != null ? aranzman.getPrijevoz() : "");
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %20s%n", "Broj doručka", brojDorucka);
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %20s%n", "Broj ručkova", brojRuckova);
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %20s%n", "Broj večera", brojVecera);
        ispisiLiniju(brojCrtica);
        System.out.printf("%-20s: %s%n", "Status", aranzman.getStatus());
    }
}
