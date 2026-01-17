package edu.unizg.foi.uzdiz.vlovric21.composite;

import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaStatus;
import edu.unizg.foi.uzdiz.vlovric21.visitor.PptarVisitor;

import java.time.LocalDateTime;
import java.util.List;

public class Rezervacija implements AranzmanKomponenta{
    private int id;
    private String ime;
    private String prezime;
    private int oznakaAranzmana;
    private String datumIVrijeme;

    private RezervacijaStatus status;
    private LocalDateTime vrijemeOtkaza;

    private Aranzman aranzman;

    public Rezervacija(){}
    public Rezervacija(String ime, String prezime, int oznakaAranzmana, String datumIVrijeme) {
        this.ime = ime;
        this.prezime = prezime;
        this.oznakaAranzmana = oznakaAranzmana;
        this.datumIVrijeme = datumIVrijeme;
    }
    public Rezervacija(Rezervacija druga){
        this.id = druga.id;
        this.ime = druga.ime;
        this.prezime = druga.prezime;
        this.oznakaAranzmana = druga.oznakaAranzmana;
        this.datumIVrijeme = druga.datumIVrijeme;
        this.status = druga.status;
        this.vrijemeOtkaza = druga.vrijemeOtkaza;
        this.aranzman = druga.aranzman;
    }

    @Override
    public void prihvati(PptarVisitor visitor){
        visitor.posjeti(this);
    }

    @Override
    public List<Rezervacija> dohvatiSveRezervacije() {
        return List.of(this);
    }

    @Override
    public String otkazi(){
        Aranzman aranzman = this.aranzman;
        return this.status.otkazi(aranzman, this);
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }
    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }
    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getPunoIme() {
        return ime + " " + prezime;
    }

    public int getOznakaAranzmana() {
        return oznakaAranzmana;
    }
    public void setOznakaAranzmana(int oznakaAranzmana) {
        this.oznakaAranzmana = oznakaAranzmana;
    }

    public String getDatumIVrijeme() {
        return datumIVrijeme;
    }
    public void setDatumIVrijeme(String datumIVrijeme) {
        this.datumIVrijeme = datumIVrijeme;
    }

    public String getStatus() {
        return status.getStatusNaziv();
    }
    public void setStatus(RezervacijaStatus status) {
        this.status = status;
    }

    public LocalDateTime getVrijemeOtkaza() {
        return vrijemeOtkaza;
    }

    public void setVrijemeOtkaza(LocalDateTime vrijemeOtkaza) {
        this.vrijemeOtkaza = vrijemeOtkaza;
    }

    public Aranzman getAranzman() {
        return aranzman;
    }

    public void setAranzman(Aranzman aranzman) {
        this.aranzman = aranzman;
    }

    public boolean jeOtkazana(){
        return status.jeOtkazana();
    }
}
