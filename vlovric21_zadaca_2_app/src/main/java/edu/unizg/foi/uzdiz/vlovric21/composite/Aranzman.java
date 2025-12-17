package edu.unizg.foi.uzdiz.vlovric21.composite;

import edu.unizg.foi.uzdiz.vlovric21.state_aranzman.AranzmanAktivan;
import edu.unizg.foi.uzdiz.vlovric21.state_aranzman.AranzmanPopunjen;
import edu.unizg.foi.uzdiz.vlovric21.state_aranzman.AranzmanStatus;
import edu.unizg.foi.uzdiz.vlovric21.state_aranzman.AranzmanUPripremi;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOtkazana;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Aranzman implements AranzmanKomponenta{
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
    private Integer brojNocenja;
    private Integer doplataZaJednokrevetnuSobu;
    private String prijevoz;
    private Integer brojDorucka;
    private Integer brojRuckova;
    private Integer brojVecera;

    private AranzmanStatus status = new AranzmanUPripremi();

    private final List<AranzmanKomponenta> djeca = new ArrayList<>();

    public Aranzman(){}
    public Aranzman(
            int oznaka,
            String naziv,
            String program,
            String pocetniDatum,
            String zavrsniDatum,
            int cijena,
            int minBrojPutnika,
            int maxBrojPutnika
    ){
        this.oznaka = oznaka;
        this.naziv = naziv;
        this.program = program;
        this.pocetniDatum = pocetniDatum;
        this.zavrsniDatum = zavrsniDatum;
        this.cijena = cijena;
        this.minBrojPutnika = minBrojPutnika;
        this.maxBrojPutnika = maxBrojPutnika;
    }

    @Override
    public void dodajDijete(AranzmanKomponenta komponenta){
        djeca.add(komponenta);
    }

    @Override
    public void ukloniDijete(AranzmanKomponenta komponenta){
        djeca.remove(komponenta);
    }

    @Override
    public List<AranzmanKomponenta> dohvatiDjecu() {
        return djeca;
    }

    public List<Rezervacija> dohvatiSveRezervacije(){
        List<Rezervacija> rezervacije = new ArrayList<>();
        for(AranzmanKomponenta k : djeca){
            if(k instanceof Rezervacija r){
                rezervacije.add(r);
            }
        }
        return rezervacije;
    }

    public Rezervacija dohvatiRezervacijuPoID(int id){
        for(AranzmanKomponenta k : djeca){
            if(k instanceof Rezervacija r && r.getId() == id){
                return r;
            }
        }
        return null;
    }

    public String dodajRezervaciju(Rezervacija rezervacija){
        return status.dodajRezervaciju(this, rezervacija);
    }

    public void otkaziSveRezervacije(){
        for(AranzmanKomponenta k : djeca){
            if(k instanceof Rezervacija r){
                r.setVrijemeOtkaza(LocalDateTime.now());
                r.otkazi();
            }
        }
    }

    public void resetirajStanje(){
        djeca.clear();
        postaviUPripremi();
    }

    public List<Rezervacija> dohvatiRezervacijeSaStatusom(RezervacijaStatus status){
        List<Rezervacija> filtriraneRezervacije = new ArrayList<>();
        for(AranzmanKomponenta k : djeca){
            if(k instanceof Rezervacija r && r.getStatus().equals(status.getStatusNaziv())){
                filtriraneRezervacije.add(r);
            }
        }
        return filtriraneRezervacije;
    }

    public boolean postojiRezervacijaKorisnikaSaStatusom(String punoIme, RezervacijaStatus status){
        for(AranzmanKomponenta k : djeca){
            if(k instanceof Rezervacija r && r.getPunoIme().equals(punoIme) && r.getStatus().equals(status.getStatusNaziv())){
                return true;
            }
        }
        return false;
    }

    public boolean postojiViseRezervacijaKorisnikaStatusom(String punoIme, RezervacijaStatus status){
        int count = 0;
        for(AranzmanKomponenta k : djeca){
            if(k instanceof Rezervacija r && r.getPunoIme().equals(punoIme) && r.getStatus().equals(status.getStatusNaziv())){
                count++;
                if(count > 1){
                    return true;
                }
            }
        }
        return false;
    }

    public Rezervacija dohvatiRezervacijuPunoImeNeOtkazanu(String ime, String prezime){
        String punoIme = ime + " " + prezime;
        for(AranzmanKomponenta k : djeca){
            if(k instanceof Rezervacija r && r.getPunoIme().equals(punoIme) && !r.getStatus().equals(new RezervacijaOtkazana().getStatusNaziv())){
                return r;
            }
        }
        return null;
    }

    public void postaviUPripremi(){
        this.status = new AranzmanUPripremi();
    }

    public void postaviAktivan(){
        this.status = new AranzmanAktivan();
    }

    public void postaviPopunjen(){
        this.status = new AranzmanPopunjen();
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

    public Integer getBrojNocenja() {
        return brojNocenja;
    }

    public void setBrojNocenja(Integer brojNocenja) {
        this.brojNocenja = brojNocenja;
    }

    public Integer getDoplataZaJednokrevetnuSobu() {
        return doplataZaJednokrevetnuSobu;
    }

    public void setDoplataZaJednokrevetnuSobu(Integer doplataZaJednokrevetnuSobu) {
        this.doplataZaJednokrevetnuSobu = doplataZaJednokrevetnuSobu;
    }

    public String getPrijevoz() {
        return prijevoz;
    }

    public void setPrijevoz(String prijevoz) {
        this.prijevoz = prijevoz;
    }

    public Integer getBrojDorucka() {
        return brojDorucka;
    }

    public void setBrojDorucka(Integer brojDorucka) {
        this.brojDorucka = brojDorucka;
    }

    public Integer getBrojRuckova() {
        return brojRuckova;
    }

    public void setBrojRuckova(Integer brojRuckova) {
        this.brojRuckova = brojRuckova;
    }

    public Integer getBrojVecera() {
        return brojVecera;
    }

    public void setBrojVecera(Integer brojVecera) {
        this.brojVecera = brojVecera;
    }

    public String getStatus() {
        return status.getStatusNaziv();
    }

    public void setStatus(AranzmanStatus status) {
        this.status = status;
    }
}
