package foi.vlovric21.builder;

import foi.vlovric21.objekti.Aranzman;

public interface AranzmanBuilder {
    public AranzmanBuilder stvoriMinimalanAranzman(
            int oznaka,
            String naziv,
            String program,
            String pocetniDatum,
            String zavrsniDatum,
            int cijena,
            int minBrojPutnika,
            int maxBrojPutnika
    );

    public AranzmanBuilder postaviOznaka(int oznaka);
    public AranzmanBuilder postaviNaziv(String naziv);
    public AranzmanBuilder postaviProgram(String program);
    public AranzmanBuilder postaviPocetniDatum(String pocetniDatum);
    public AranzmanBuilder postaviZavrsniDatum(String zavrsniDatum);
    public AranzmanBuilder postaviVrijemeKretanja(String vrijemeKretanja);
    public AranzmanBuilder postaviVrijemePovratka(String vrijemePovratka);
    public AranzmanBuilder postaviCijena(int cijena);
    public AranzmanBuilder postaviMinBrojPutnika(int minBrojPutnika);
    public AranzmanBuilder postaviMaxBrojPutnika(int maxBrojPutnika);
    public AranzmanBuilder postaviBrojNocenja(Integer brojNocenja);
    public AranzmanBuilder postaviDoplataZaJednokrevetnuSobu(Integer doplataZaJednokrevetnuSobu);
    public AranzmanBuilder postaviPrijevoz(String prijevoz);
    public AranzmanBuilder postaviBrojDorucka(Integer brojDorucka);
    public AranzmanBuilder postaviBrojRuckova(Integer brojRuckova);
    public AranzmanBuilder postaviBrojVecera(Integer brojVecera);

    public Aranzman dohvatiAranzman();
}
