package edu.unizg.foi.uzdiz.vlovric21.composite;

import edu.unizg.foi.uzdiz.vlovric21.memento.AranzmanMemento;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.state_aranzman.*;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOtkazana;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaStatus;
import edu.unizg.foi.uzdiz.vlovric21.visitor.PptarElement;
import edu.unizg.foi.uzdiz.vlovric21.visitor.PptarVisitor;

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

    private List<String> pretplate = new ArrayList<>();

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
    public Aranzman(Aranzman drugi){
        this.oznaka = drugi.oznaka;
        this.naziv = drugi.naziv;
        this.program = drugi.program;
        this.pocetniDatum = drugi.pocetniDatum;
        this.zavrsniDatum = drugi.zavrsniDatum;
        this.vrijemeKretanja = drugi.vrijemeKretanja;
        this.vrijemePovratka = drugi.vrijemePovratka;
        this.cijena = drugi.cijena;
        this.minBrojPutnika = drugi.minBrojPutnika;
        this.maxBrojPutnika = drugi.maxBrojPutnika;
        this.brojNocenja = drugi.brojNocenja;
        this.doplataZaJednokrevetnuSobu = drugi.doplataZaJednokrevetnuSobu;
        this.prijevoz = drugi.prijevoz;
        this.brojDorucka = drugi.brojDorucka;
        this.brojRuckova = drugi.brojRuckova;
        this.brojVecera = drugi.brojVecera;
        this.status = drugi.status;
    }

    @Override
    public void prihvati(PptarVisitor visitor){
        visitor.posjeti(this);
        for(AranzmanKomponenta k : djeca){
            k.prihvati(visitor);
        }
    }

    public AranzmanMemento spremiStanje(){
        return new AranzmanMemento(this, dohvatiSveRezervacije());
    }

    public void vratiStanje(AranzmanMemento memento){
        this.djeca.clear();
        this.pretplate.clear();
        
        Aranzman spremljeniAranzman = memento.getAranzman();
        this.oznaka = spremljeniAranzman.oznaka;
        this.naziv = spremljeniAranzman.naziv;
        this.program = spremljeniAranzman.program;
        this.pocetniDatum = spremljeniAranzman.pocetniDatum;
        this.zavrsniDatum = spremljeniAranzman.zavrsniDatum;
        this.vrijemeKretanja = spremljeniAranzman.vrijemeKretanja;
        this.vrijemePovratka = spremljeniAranzman.vrijemePovratka;
        this.cijena = spremljeniAranzman.cijena;
        this.minBrojPutnika = spremljeniAranzman.minBrojPutnika;
        this.maxBrojPutnika = spremljeniAranzman.maxBrojPutnika;
        this.brojNocenja = spremljeniAranzman.brojNocenja;
        this.doplataZaJednokrevetnuSobu = spremljeniAranzman.doplataZaJednokrevetnuSobu;
        this.prijevoz = spremljeniAranzman.prijevoz;
        this.brojDorucka = spremljeniAranzman.brojDorucka;
        this.brojRuckova = spremljeniAranzman.brojRuckova;
        this.brojVecera = spremljeniAranzman.brojVecera;
        this.status = spremljeniAranzman.status;
        
        this.djeca.addAll(memento.getRezervacije());
        this.pretplate.addAll(memento.getAranzman().pretplate);
    }

    public String dodajPretplatnika(String punoIme){
        if(!pretplate.contains(punoIme)){
            pretplate.add(punoIme);
            return punoIme + " se pretplatio/la za informacije o turističkom aranžmanu s oznakom " + this.oznaka + " te njegovim rezervacijama";
        }
        return punoIme + " je već pretplaćen/a za informacije o turističkom aranžmanu s oznakom " + this.oznaka + "te njegovim rezervacijama";
    }

    public boolean ukloniPretplatnika(String punoIme){
        return pretplate.remove(punoIme);
    }

    public void ukloniSvePretplatnike(){
        pretplate.clear();
    }

    public void obavijesti(String poruka){
        System.out.println();
        for(String ime : pretplate){
            System.out.println("Obavijest za korisnika: " + ime);
            System.out.println("Oznaka aranžmana: " + this.oznaka);
            System.out.println("Poruka: " + poruka);
            System.out.println();
        }
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

    @Override
    public List<Rezervacija> dohvatiSveRezervacije(){
        List<Rezervacija> rezervacije = new ArrayList<>();
        for(AranzmanKomponenta k : djeca){
            rezervacije.addAll(k.dohvatiSveRezervacije());
        }
        return rezervacije;
    }

    @Override
    public String otkazi(){
        return status.otkaziAranzman(this);
    }

    public Rezervacija dohvatiRezervacijuPoID(int id){
        for(Rezervacija r : dohvatiSveRezervacije()){
            if(r.getId() == id){
                return r;
            }
        }
        return null;
    }

    public String dodajRezervaciju(Rezervacija rezervacija){
        return status.dodajRezervaciju(this, rezervacija);
    }

    public void resetirajStanje(){
        djeca.clear();
        postaviUPripremi();
    }

    public List<Rezervacija> dohvatiRezervacijeSaStatusom(RezervacijaStatus status){
        List<Rezervacija> filtriraneRezervacije = new ArrayList<>();
        for(Rezervacija r : dohvatiSveRezervacije()){
            if( r.getStatus().equals(status.getStatusNaziv())){
                filtriraneRezervacije.add(r);
            }
        }
        return filtriraneRezervacije;
    }

    public boolean postojiRezervacijaKorisnikaSaStatusom(String punoIme, RezervacijaStatus status){
        for(Rezervacija r : dohvatiSveRezervacije()){
            if(r.getPunoIme().equals(punoIme) && r.getStatus().equals(status.getStatusNaziv())){
                return true;
            }
        }
        return false;
    }

    public boolean postojiViseRezervacijaKorisnikaStatusom(String punoIme, RezervacijaStatus status){
        int count = 0;
        for(Rezervacija r : dohvatiSveRezervacije()){
            if(r.getPunoIme().equals(punoIme) && r.getStatus().equals(status.getStatusNaziv())){
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
        for(Rezervacija r : dohvatiSveRezervacije()){
            if(r.getPunoIme().equals(punoIme) && !r.jeOtkazana()){
                return r;
            }
        }
        return null;
    }

    public int dohvatiBrojRezervacijaKorisnika(String punoIme){
        int count = 0;
        for(Rezervacija r : dohvatiSveRezervacije()){
            if(r.getPunoIme().equals(punoIme) && !r.jeOtkazana()){
                count++;
            }
        }
        return count;
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

    public boolean jeOtkazan(){
        return status.jeOtkazan();
    }
}
