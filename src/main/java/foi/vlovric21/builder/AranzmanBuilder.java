package foi.vlovric21.builder;

import foi.vlovric21.objekti.Aranzman;

public abstract class AranzmanBuilder {
    public abstract AranzmanBuilder stvoriMinimalanAranzman(
            int oznaka,
            String naziv,
            String program,
            String pocetniDatum,
            String zavrsniDatum,
            int cijena,
            int minBrojPutnika,
            int maxBrojPutnika
    );

    public abstract AranzmanBuilder postaviOznaka(int oznaka);
    public abstract AranzmanBuilder postaviNaziv(String naziv);
    public abstract AranzmanBuilder postaviProgram(String program);
    public abstract AranzmanBuilder postaviPocetniDatum(String pocetniDatum);
    public abstract AranzmanBuilder postaviZavrsniDatum(String zavrsniDatum);
    public abstract AranzmanBuilder postaviVrijemeKretanja(String vrijemeKretanja);
    public abstract AranzmanBuilder postaviVrijemePovratka(String vrijemePovratka);
    public abstract AranzmanBuilder postaviCijena(int cijena);
    public abstract AranzmanBuilder postaviMinBrojPutnika(int minBrojPutnika);
    public abstract AranzmanBuilder postaviMaxBrojPutnika(int maxBrojPutnika);
    public abstract AranzmanBuilder postaviBrojNocenja(Integer brojNocenja);
    public abstract AranzmanBuilder postaviDoplataZaJednokrevetnuSobu(Integer doplataZaJednokrevetnuSobu);
    public abstract AranzmanBuilder postaviPrijevoz(String prijevoz);
    public abstract AranzmanBuilder postaviBrojDorucka(Integer brojDorucka);
    public abstract AranzmanBuilder postaviBrojRuckova(Integer brojRuckova);
    public abstract AranzmanBuilder postaviBrojVecera(Integer brojVecera);

    public abstract Aranzman dohvatiAranzman();
}
