package edu.unizg.foi.uzdiz.vlovric21.state_aranzman;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.AranzmanKomponenta;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaAktivna;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOdgodena;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOtkazana;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AranzmanAktivan implements AranzmanStatus{

    @Override
    public String dodajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
        if(rezervacija.jeOtkazana()){
            aranzman.dodajDijete(rezervacija);
            return "";
        }

        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
        int neKronoloskoPreklapanje = repozitorij.postojiNeKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, rezervacija);
        if(neKronoloskoPreklapanje != -1){
            repozitorij.dodajAranzmanKronologija(neKronoloskoPreklapanje);
        }
        boolean postojiAktivnaKorisnika = aranzman.postojiRezervacijaKorisnikaSaStatusom(rezervacija.getPunoIme(), new RezervacijaAktivna());
        boolean postojiAktivnaPreklapanje = repozitorij.postojiKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, rezervacija);

        if(postojiAktivnaKorisnika || postojiAktivnaPreklapanje){
            rezervacija.setStatus(new RezervacijaOdgodena());
            aranzman.dodajDijete(rezervacija);
            return "";
        }
        rezervacija.setStatus(new RezervacijaAktivna());
        aranzman.dodajDijete(rezervacija);

        int max = aranzman.getMaxBrojPutnika();
        int brojAktivnihRezervacija = aranzman.dohvatiRezervacijeSaStatusom(new RezervacijaAktivna()).size();
        if(brojAktivnihRezervacija == max){
            aranzman.postaviPopunjen();
        }

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
        return "AKTIVAN";
    }

    @Override
    public boolean jeOtkazan() {
        return false;
    }
}
