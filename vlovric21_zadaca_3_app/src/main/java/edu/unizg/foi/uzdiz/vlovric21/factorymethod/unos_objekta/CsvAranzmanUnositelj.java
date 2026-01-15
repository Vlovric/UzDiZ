package edu.unizg.foi.uzdiz.vlovric21.factorymethod.unos_objekta;

import edu.unizg.foi.uzdiz.vlovric21.builder.AranzmanBuilder;
import edu.unizg.foi.uzdiz.vlovric21.builder.AranzmanBuilderConcrete;
import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;

import java.util.Map;

public class CsvAranzmanUnositelj extends CsvObjectUnositelj {

    @Override
    protected boolean validirajObjekt(Map<String, String> red) {
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
        int oznaka = Integer.parseInt(red.get("Oznaka"));

        if(repozitorij.postojiAranzmanPoOznaci(oznaka)){
            System.out.println("Aranžman s oznakom " + oznaka + " već postoji.");
            return false;
        }
        return true;
    }

    @Override
    protected void unesiObjekt(Map<String, String> red) {
        int oznaka = Integer.parseInt(red.get("Oznaka"));
        String naziv = red.get("Naziv");
        String program = red.get("Program");
        String pocetniDatum = red.get("Početni datum");
        String zavrsniDatum = red.get("Završni datum");
        String vrijemeKretanja = red.get("Vrijeme kretanja");
        String vrijemePovratka = red.get("Vrijeme povratka");
        int cijena = Integer.parseInt(red.get("Cijena"));
        int minBrojPutnika = Integer.parseInt(red.get("Min broj putnika"));
        int maxBrojPutnika = Integer.parseInt(red.get("Maks broj putnika"));
        String prijevoz = red.get("Prijevoz").trim();

        Integer brojNocenja = parseOpcionalniInt(red.get("Broj noćenja"));
        Integer doplata = parseOpcionalniInt(red.get("Doplata za jednokrevetnu sobu"));
        Integer brojDorucka = parseOpcionalniInt(red.get("Broj doručka"));
        Integer brojRuckova = parseOpcionalniInt(red.get("Broj ručkova"));
        Integer brojVecera = parseOpcionalniInt(red.get("Broj večera"));

        AranzmanBuilder builder = new AranzmanBuilderConcrete();
        builder.stvoriMinimalanAranzman(oznaka, naziv, program, pocetniDatum, zavrsniDatum, cijena, minBrojPutnika, maxBrojPutnika)
                .postaviVrijemeKretanja(vrijemeKretanja)
                .postaviVrijemePovratka(vrijemePovratka)
                .postaviBrojNocenja(brojNocenja)
                .postaviDoplataZaJednokrevetnuSobu(doplata)
                .postaviPrijevoz(prijevoz)
                .postaviBrojDorucka(brojDorucka)
                .postaviBrojRuckova(brojRuckova)
                .postaviBrojVecera(brojVecera);

        Aranzman ar = builder.dohvatiAranzman();
        RepozitorijPodataka.getInstance().dodajAranzman(ar);
    }
}
