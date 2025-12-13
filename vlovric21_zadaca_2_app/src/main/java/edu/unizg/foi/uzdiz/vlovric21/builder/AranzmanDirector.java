package edu.unizg.foi.uzdiz.vlovric21.builder;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;

public class AranzmanDirector {
    private AranzmanBuilder builder;

    public AranzmanDirector() {
        this.builder = new AranzmanBuilderConcrete();
    }

    public AranzmanDirector(AranzmanBuilder arBuilder){
        this.builder = arBuilder;
    }

    public Aranzman stvoriKompletanAranzman(
            int oznaka,
            String naziv,
            String program,
            String pocetniDatum,
            String zavrsniDatum,
            int cijena,
            int minBrojPutnika,
            int maxBrojPutnika,
            Integer brojNocenja,
            String vrijemeKretanja,
            String vrijemePovratka,
            Integer doplataZaJednokrevetnuSobu,
            String prijevoz,
            Integer brojDorucka,
            Integer brojRuckova,
            Integer brojVecera
    ) {
        builder.stvoriMinimalanAranzman(oznaka, naziv, program, pocetniDatum, zavrsniDatum, cijena, minBrojPutnika, maxBrojPutnika)
               .postaviVrijemeKretanja(vrijemeKretanja)
               .postaviVrijemePovratka(vrijemePovratka)
                .postaviBrojNocenja(brojNocenja)
               .postaviDoplataZaJednokrevetnuSobu(doplataZaJednokrevetnuSobu)
               .postaviPrijevoz(prijevoz)
               .postaviBrojDorucka(brojDorucka)
               .postaviBrojRuckova(brojRuckova)
               .postaviBrojVecera(brojVecera);
        return builder.dohvatiAranzman();
    }
}
