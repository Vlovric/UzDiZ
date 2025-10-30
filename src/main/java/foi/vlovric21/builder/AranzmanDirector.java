package foi.vlovric21.builder;

import foi.vlovric21.objekti.Aranzman;

public class AranzmanDirector {
    private AranzmanBuilder builder;

    public AranzmanDirector() {
        this.builder = new AranzmanBuilderConcrete();
    }

    public Aranzman stvoriAranzman(
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
        builder.stvoriAranzman(oznaka, naziv, program, pocetniDatum, zavrsniDatum, cijena, minBrojPutnika, maxBrojPutnika)
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
