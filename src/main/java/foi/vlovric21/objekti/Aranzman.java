package foi.vlovric21.objekti;

public class Aranzman {
    private int oznaka;
    private String naziv;
    private String program;
    private String pocetniDatum;
    private String zavrsniDatum;
    private String vrijemeKretanja;
    private String  vrijemePovratka;
    private int cijena;
    private int minBrojPutnika;
    private int maxBrojPutnika;
    private int brojNocenja;
    private int doplataZaJednokrevetnuSobu;
    private String prijevoz;
    private int brojDorucka;
    private int brojRuckova;
    private int brojVecera;

    public Aranzman(){}
    public Aranzman(
            int oznaka,
            String naziv,
            String program,
            String pocetniDatum,
            String zavrsniDatum,
            int cijena,
            int minBrojPutnika,
            int maxBrojPutnika,
            int brojNocenja
    ){
        this.oznaka = oznaka;
        this.naziv = naziv;
        this.program = program;
        this.pocetniDatum = pocetniDatum;
        this.zavrsniDatum = zavrsniDatum;
        this.cijena = cijena;
        this.minBrojPutnika = minBrojPutnika;
        this.maxBrojPutnika = maxBrojPutnika;
        this.brojNocenja = brojNocenja;
    }

    public int getOznaka() {
        return oznaka;
    }
    public void setOznaka(int oznaka) {
        this.oznaka = oznaka;
    }

    public String getNaziv() {
        return naziv;
    }
    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getPocetniDatum() {
        return pocetniDatum;
    }

    public void setPocetniDatum(String pocetniDatum) {
        this.pocetniDatum = pocetniDatum;
    }

    public String getZavrsniDatum() {
        return zavrsniDatum;
    }

    public void setZavrsniDatum(String zavrsniDatum) {
        this.zavrsniDatum = zavrsniDatum;
    }

    public String getVrijemeKretanja() {
        return vrijemeKretanja;
    }

    public void setVrijemeKretanja(String vrijemeKretanja) {
        this.vrijemeKretanja = vrijemeKretanja;
    }

    public String getVrijemePovratka() {
        return vrijemePovratka;
    }

    public void setVrijemePovratka(String vrijemePovratka) {
        this.vrijemePovratka = vrijemePovratka;
    }

    public int getCijena() {
        return cijena;
    }

    public void setCijena(int cijena) {
        this.cijena = cijena;
    }

    public int getMinBrojPutnika() {
        return minBrojPutnika;
    }

    public void setMinBrojPutnika(int minBrojPutnika) {
        this.minBrojPutnika = minBrojPutnika;
    }

    public int getMaxBrojPutnika() {
        return maxBrojPutnika;
    }

    public void setMaxBrojPutnika(int maxBrojPutnika) {
        this.maxBrojPutnika = maxBrojPutnika;
    }

    public int getBrojNocenja() {
        return brojNocenja;
    }

    public void setBrojNocenja(int brojNocenja) {
        this.brojNocenja = brojNocenja;
    }

    public int getDoplataZaJednokrevetnuSobu() {
        return doplataZaJednokrevetnuSobu;
    }

    public void setDoplataZaJednokrevetnuSobu(int doplataZaJednokrevetnuSobu) {
        this.doplataZaJednokrevetnuSobu = doplataZaJednokrevetnuSobu;
    }

    public String getPrijevoz() {
        return prijevoz;
    }

    public void setPrijevoz(String prijevoz) {
        this.prijevoz = prijevoz;
    }

    public int getBrojDorucka() {
        return brojDorucka;
    }

    public void setBrojDorucka(int brojDorucka) {
        this.brojDorucka = brojDorucka;
    }

    public int getBrojRuckova() {
        return brojRuckova;
    }

    public void setBrojRuckova(int brojRuckova) {
        this.brojRuckova = brojRuckova;
    }

    public int getBrojVecera() {
        return brojVecera;
    }

    public void setBrojVecera(int brojVecera) {
        this.brojVecera = brojVecera;
    }

}
