package foi.vlovric21.builder;

import foi.vlovric21.objekti.Aranzman;

public class AranzmanBuilderConcrete implements AranzmanBuilder {
    protected Aranzman aranzman;

    public AranzmanBuilderConcrete(){}

    @Override
    public AranzmanBuilder stvoriMinimalanAranzman(
            int oznaka,
            String naziv,
            String program,
            String pocetniDatum,
            String zavrsniDatum,
            int cijena,
            int minBrojPutnika,
            int maxBrojPutnika
    ) {
        this.aranzman = new Aranzman();
        this.aranzman.setOznaka(oznaka);
        this.aranzman.setNaziv(naziv);
        this.aranzman.setProgram(program);
        this.aranzman.setPocetniDatum(pocetniDatum);
        this.aranzman.setZavrsniDatum(zavrsniDatum);
        this.aranzman.setCijena(cijena);
        this.aranzman.setMinBrojPutnika(minBrojPutnika);
        this.aranzman.setMaxBrojPutnika(maxBrojPutnika);
        return this;
    }

    @Override
    public AranzmanBuilder postaviOznaka(int oznaka) {
        this.aranzman.setOznaka(oznaka);
        return this;
    }

    @Override
    public AranzmanBuilder postaviNaziv(String naziv) {
        this.aranzman.setNaziv(naziv);
        return this;
    }

    @Override
    public AranzmanBuilder postaviProgram(String program) {
        this.aranzman.setProgram(program);
        return this;
    }

    @Override
    public AranzmanBuilder postaviPocetniDatum(String pocetniDatum) {
        this.aranzman.setPocetniDatum(pocetniDatum);
        return this;
    }

    @Override
    public AranzmanBuilder postaviZavrsniDatum(String zavrsniDatum) {
        this.aranzman.setZavrsniDatum(zavrsniDatum);
        return this;
    }

    @Override
    public AranzmanBuilder postaviVrijemeKretanja(String vrijemeKretanja) {
        this.aranzman.setVrijemeKretanja(vrijemeKretanja);
        return this;
    }

    @Override
    public AranzmanBuilder postaviVrijemePovratka(String vrijemePovratka) {
        this.aranzman.setVrijemePovratka(vrijemePovratka);
        return this;
    }

    @Override
    public AranzmanBuilder postaviCijena(int cijena) {
        this.aranzman.setCijena(cijena);
        return this;
    }

    @Override
    public AranzmanBuilder postaviMinBrojPutnika(int minBrojPutnika) {
        this.aranzman.setMinBrojPutnika(minBrojPutnika);
        return this;
    }

    @Override
    public AranzmanBuilder postaviMaxBrojPutnika(int maxBrojPutnika) {
        this.aranzman.setMaxBrojPutnika(maxBrojPutnika);
        return this;
    }

    @Override
    public AranzmanBuilder postaviBrojNocenja(Integer brojNocenja) {
        this.aranzman.setBrojNocenja(brojNocenja);
        return this;
    }

    @Override
    public AranzmanBuilder postaviDoplataZaJednokrevetnuSobu(Integer doplataZaJednokrevetnuSobu) {
        this.aranzman.setDoplataZaJednokrevetnuSobu(doplataZaJednokrevetnuSobu);
        return this;
    }

    @Override
    public AranzmanBuilder postaviPrijevoz(String prijevoz) {
        this.aranzman.setPrijevoz(prijevoz);
        return this;
    }

    @Override
    public AranzmanBuilder postaviBrojDorucka(Integer brojDorucka) {
        this.aranzman.setBrojDorucka(brojDorucka);
        return this;
    }

    @Override
    public AranzmanBuilder postaviBrojRuckova(Integer brojRuckova) {
        this.aranzman.setBrojRuckova(brojRuckova);
        return this;
    }

    @Override
    public AranzmanBuilder postaviBrojVecera(Integer brojVecera) {
        this.aranzman.setBrojVecera(brojVecera);
        return this;
    }

    @Override
    public Aranzman dohvatiAranzman() {
        return this.aranzman;
    }
}
