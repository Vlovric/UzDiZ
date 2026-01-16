package edu.unizg.foi.uzdiz.vlovric21.strategy_null_object;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.state_aranzman.AranzmanAktivan;
import edu.unizg.foi.uzdiz.vlovric21.state_aranzman.AranzmanUPripremi;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaAktivna;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOdgodena;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaPrimljena;

import java.util.List;

public class JdrRezervacijaUpravitelj implements RezervacijaUpravitelj {
    @Override
    public Boolean upravljajRezervacijom(Aranzman aranzman, Rezervacija rezervacija) {
        String status = aranzman.getStatus();
        String aktivanStatus = new AranzmanAktivan().getStatusNaziv();
        String uPripremiStatus = new AranzmanUPripremi().getStatusNaziv();

        if(status.equals(aktivanStatus)){
            return upravljajAktivnim(aranzman, rezervacija);
        }else if(status.equals(uPripremiStatus)){
            return upravljajUPripremi(aranzman, rezervacija);
        }
        return false;
    }

    private Boolean upravljajAktivnim(Aranzman aranzman, Rezervacija rezervacija){
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
            return true;
        }

        return false;
    }

    private Boolean upravljajUPripremi(Aranzman aranzman, Rezervacija rezervacija){
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();

        int neKronoloskoPreklapanje = repozitorij.postojiNeKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, rezervacija);
        if (neKronoloskoPreklapanje != -1) {
            repozitorij.dodajAranzmanKronologija(neKronoloskoPreklapanje);
        }
        boolean postojiPrimljenaKorisnika = aranzman.postojiRezervacijaKorisnikaSaStatusom(rezervacija.getPunoIme(), new RezervacijaPrimljena());
        boolean postojiAktivnaPreklapanje = repozitorij.postojiKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, rezervacija);

        if (postojiPrimljenaKorisnika || postojiAktivnaPreklapanje) {
            rezervacija.setStatus(new RezervacijaOdgodena());
            aranzman.dodajDijete(rezervacija);
            return true;
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

        return false;
    }
}
