package edu.unizg.foi.uzdiz.vlovric21.singleton;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.AranzmanKolekcija;
import edu.unizg.foi.uzdiz.vlovric21.composite.AranzmanKomponenta;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.facade.CsvParsiranjeFacade;
import edu.unizg.foi.uzdiz.vlovric21.factorymethod.unos_objekta.CsvAranzmanUnositeljCreator;
import edu.unizg.foi.uzdiz.vlovric21.factorymethod.unos_objekta.CsvObjectCreator;
import edu.unizg.foi.uzdiz.vlovric21.factorymethod.unos_objekta.CsvRezervacijaUnositeljCreator;
import edu.unizg.foi.uzdiz.vlovric21.pomocne.DatumFormater;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaAktivna;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaNova;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOtkazana;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class RepozitorijPodataka {
    private static RepozitorijPodataka instance = new RepozitorijPodataka();
    private static int idBrojacRezervacija = 1;
    private static boolean kronoloskiRedoslijed = true;

    private AranzmanKolekcija aranzmanKolekcija = new AranzmanKolekcija();

    private DatumFormater datumFormater = new DatumFormater();

    private RepozitorijPodataka() {}

    public static RepozitorijPodataka getInstance() {
        return instance;
    }

    public int getIdRezervacije(){
        return idBrojacRezervacija++;
    }

    public Aranzman getAranzmanPoOznaci(int oznaka){
        return aranzmanKolekcija.dohvatiAranzmanPoOznaci(oznaka);
    }

    public boolean postojiAranzmanPoOznaci(int oznaka){
        return aranzmanKolekcija.dohvatiAranzmanPoOznaci(oznaka) != null;
    }

    public void dodajAranzman(Aranzman ar){
        aranzmanKolekcija.dodajDijete(ar);
    }

    public String dodajRezervaciju(Rezervacija rezervacija) {
        int id = getIdRezervacije();
        rezervacija.setId(id);
        rezervacija.setStatus(new RezervacijaNova());
        return aranzmanKolekcija.dodajRezervaciju(rezervacija);
    }

    public boolean postojiKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(Aranzman aranzman, Rezervacija rezervacija){
        return aranzmanKolekcija.postojiKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, rezervacija);
    }

    public int postojiNeKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(Aranzman aranzman, Rezervacija rezervacija){
        return aranzmanKolekcija.postojiNeKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, rezervacija);
    }

    public String otkaziSveRezervacijeAranzmana(int oznaka){
        String rezultat = aranzmanKolekcija.otkaziSveRezervacijeAranzmana(oznaka);
        if(!rezultat.isEmpty()){
            return rezultat;
        }
        return "Otkazan aranžman " + oznaka + " i sve njegove rezervacije.";
    }

    public void resetirajRezervacijeOtkaz(){
        setIdBrojacRezervacija();
        aranzmanKolekcija.resetirajRezervacijeOtkaz();
    }

    public boolean provjeriPreklapanjeDatuma(Aranzman a1, Aranzman a2){
        LocalDate a1Pocetak = datumFormater.parseDatum(a1.getPocetniDatum());
        LocalDate a1Kraj = datumFormater.parseDatum(a1.getZavrsniDatum());
        LocalDate a2Pocetak = datumFormater.parseDatum(a2.getPocetniDatum());
        LocalDate a2Kraj = datumFormater.parseDatum(a2.getZavrsniDatum());

        if(a1Kraj.isBefore(a2Pocetak) || a2Kraj.isBefore(a1Pocetak)){
            return false;
        }
        return true;
    }

    public boolean rezervacijaPrijeDruge(Rezervacija r1, Rezervacija r2){
        DatumFormater datumFormater = RepozitorijPodataka.getInstance().getDatumFormater();
        LocalDateTime dt1 = datumFormater.parseDatumIVrijeme(r1.getDatumIVrijeme());
        LocalDateTime dt2 = datumFormater.parseDatumIVrijeme(r2.getDatumIVrijeme());
        return dt1.isBefore(dt2);
    };

    public List<Aranzman> dohvatiAranzmaneRazdoblje(String pocetniDatum, String zavrsniDatum){
        return aranzmanKolekcija.dohvatiAranzmaneRazdoblje(pocetniDatum, zavrsniDatum);
    }

    public List<Rezervacija> dohvatiRezervacijePoImenu(String punoIme){
        return aranzmanKolekcija.dohvatiRezervacijePoImenu(punoIme);
    }

    public List<Rezervacija> dohvatiRezervacijeZaAranzman(int oznakaAranzmana, List<String> statusi){
        return aranzmanKolekcija.dohvatiRezervacijeZaAranzman(oznakaAranzmana, statusi);
    }

    public String otkaziRezervaciju(String ime, String prezime, int oznaka){
        Rezervacija rezervacija = aranzmanKolekcija.dohvatiAranzmanPoOznaci(oznaka).dohvatiRezervacijuPunoImeNeOtkazanu(ime, prezime);
        if(rezervacija == null){
            return "Ne postoji rezervacija za uneseno ime i prezime na odabranom aranžmanu.";
        }
        rezervacija.setVrijemeOtkaza(LocalDateTime.now());
        String rezultat = rezervacija.otkazi();
        if(rezultat.isEmpty()) {
            return "Rezervacija uspješno otkazana.";
        }
        return rezultat;
    }

    public void obrisiSveAranzmane(){
        aranzmanKolekcija.ukloniSvuDjecu();
        aranzmanKolekcija = new AranzmanKolekcija();
    }

    public void obrisiSveRezervacije(){
        aranzmanKolekcija.obrisiSveRezervacije();
        setIdBrojacRezervacija();
    }

    public void ucitajAranzmaneIzDatoteke(String datoteka){
        List<Map<String, String>> aranzmanRedovi = CsvParsiranjeFacade.ucitajAranzmane(datoteka);
        CsvObjectCreator aranzmanCreator = new CsvAranzmanUnositeljCreator();
        aranzmanCreator.validirajUnesiObjekte(aranzmanRedovi);
    }

    public void ucitajRezervacijeIzDatoteke(String datoteka){
        List<Map<String, String>> rezervacijaRedovi = CsvParsiranjeFacade.ucitajRezervacije(datoteka);

        CsvObjectCreator rezervacijaCreator = new CsvRezervacijaUnositeljCreator();
        rezervacijaCreator.validirajUnesiObjekte(rezervacijaRedovi);
    }

    public void setKronoloskiRedoslijed(boolean kronoloski){
        kronoloskiRedoslijed = kronoloski;
    }

    public boolean getKronoloskiRedoslijed(){
        return kronoloskiRedoslijed;
    }

    public AranzmanKolekcija getAranzmanKolekcija() {
        return aranzmanKolekcija;
    }

    public void setAranzmanKolekcija(AranzmanKolekcija aranzmanKolekcija) {
        this.aranzmanKolekcija = aranzmanKolekcija;
    }

    public void setIdBrojacRezervacija(){
        idBrojacRezervacija = 1;
    }

    public void dodajAranzmanKronologija(int oznaka){
        aranzmanKolekcija.dodajAranzmanUKronologiju(oznaka);
    }

    public DatumFormater getDatumFormater(){
        return datumFormater;
    }

    public void setDatumFormater(DatumFormater datumFormater){
        this.datumFormater = datumFormater;
    }

}
