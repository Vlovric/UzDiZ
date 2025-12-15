package edu.unizg.foi.uzdiz.vlovric21.state_aranzman;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaAktivna;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOdgodena;

public class AranzmanAktivan implements AranzmanStatus{

    @Override
    public String dodajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {

        boolean postojiAktivnaKorisnika = aranzman.postojiRezervacijaKorisnikaSaStatusom(rezervacija.getPunoIme(), new RezervacijaAktivna());
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
        boolean postojiAktivnaPreklapanje = repozitorij.postojiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, rezervacija);

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
    public void otkaziRezervaciju(Aranzman aranzman, Rezervacija Rezervacija) {
        return;
    }

    @Override
    public void otkaziAranzman(Aranzman aranzman) {
        return;
    }

    @Override
    public String getStatusNaziv() {
        return "AKTIVAN";
    }
}
