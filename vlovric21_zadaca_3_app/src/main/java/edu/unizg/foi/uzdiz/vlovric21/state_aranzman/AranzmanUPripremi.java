package edu.unizg.foi.uzdiz.vlovric21.state_aranzman;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.AranzmanKomponenta;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaAktivna;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOdgodena;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOtkazana;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaPrimljena;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AranzmanUPripremi implements AranzmanStatus {


    @Override
    public String dodajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
        if (rezervacija.jeOtkazana()) {
            aranzman.dodajDijete(rezervacija);
            return "";
        }

        int min = aranzman.getMinBrojPutnika();
        int brojPrimljenihRezervacija = aranzman.dohvatiRezervacijeSaStatusom(new RezervacijaPrimljena()).size() + 1;

        if (brojPrimljenihRezervacija < min) {
            rezervacija.setStatus(new RezervacijaPrimljena());
            aranzman.dodajDijete(rezervacija);
            return "";
        }

        int neKronoloskoPreklapanje = repozitorij.postojiNeKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, rezervacija);
        if (neKronoloskoPreklapanje != -1) {
            repozitorij.dodajAranzmanKronologija(neKronoloskoPreklapanje);
        }
        boolean postojiPrimljenaKorisnika = aranzman.postojiRezervacijaKorisnikaSaStatusom(rezervacija.getPunoIme(), new RezervacijaPrimljena());
        boolean postojiAktivnaPreklapanje = repozitorij.postojiKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, rezervacija);

        if (postojiPrimljenaKorisnika || postojiAktivnaPreklapanje) {
            rezervacija.setStatus(new RezervacijaOdgodena());
            aranzman.dodajDijete(rezervacija);
            return "";
        }

        List<Rezervacija> rezervacijePostojece = aranzman.dohvatiRezervacijeSaStatusom(new RezervacijaPrimljena());

        for (Rezervacija r : rezervacijePostojece) {
            neKronoloskoPreklapanje = repozitorij.postojiNeKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, r);
            if (neKronoloskoPreklapanje != -1) {
                repozitorij.dodajAranzmanKronologija(neKronoloskoPreklapanje);
            }
            boolean postojiPrimljenaKorisnikaPostojece = aranzman.postojiViseRezervacijaKorisnikaStatusom(r.getPunoIme(), new RezervacijaPrimljena());
            if (postojiPrimljenaKorisnikaPostojece) {
                r.setStatus(new RezervacijaOdgodena());
            }
            boolean postojiAktivnaPreklapanjePostojece = repozitorij.postojiKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, r);
            if (postojiAktivnaPreklapanjePostojece) {
                r.setStatus(new RezervacijaOdgodena());
            }
        }

        rezervacija.setStatus(new RezervacijaPrimljena());
        aranzman.dodajDijete(rezervacija);

        brojPrimljenihRezervacija = aranzman.dohvatiRezervacijeSaStatusom(new RezervacijaPrimljena()).size();
        if (!(brojPrimljenihRezervacija == min)) {
            return "";
        }

        rezervacijePostojece = aranzman.dohvatiRezervacijeSaStatusom(new RezervacijaPrimljena());
        for (Rezervacija r : rezervacijePostojece) {
            r.setStatus(new RezervacijaAktivna());
        }
        aranzman.postaviAktivan();
        return "";


    }

    @Override
    public String otkaziAranzman(Aranzman aranzman) {
        List<AranzmanKomponenta> djeca = new ArrayList<>(aranzman.dohvatiDjecu());
        for(AranzmanKomponenta k : djeca){
            try{
                k.otkazi();
            }catch(Exception ignored){
            }
        }
        aranzman.setStatus(new AranzmanOtkazan());
        return "";
    }

    @Override
    public String getStatusNaziv() {
        return "U PRIPREMI";
    }

    @Override
    public boolean jeOtkazan() {
        return false;
    }
}
