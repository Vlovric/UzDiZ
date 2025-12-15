package edu.unizg.foi.uzdiz.vlovric21.state_aranzman;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaAktivna;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOdgodena;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOtkazana;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaPrimljena;

import java.util.List;

public class AranzmanUPripremi implements AranzmanStatus{


    @Override
    public String dodajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
        if(rezervacija.getStatus().equals(new RezervacijaOtkazana().getStatusNaziv())){
            aranzman.dodajDijete(rezervacija);
            return "";
        }

        int min = aranzman.getMinBrojPutnika();
        int brojPrimljenihRezervacija = aranzman.dohvatiRezervacijeSaStatusom(new RezervacijaPrimljena()).size() +1;

        if(brojPrimljenihRezervacija < min){
            rezervacija.setStatus(new RezervacijaPrimljena());
            aranzman.dodajDijete(rezervacija);
            return "";
        }

        boolean postojiPrimljenaKorisnika = aranzman.postojiRezervacijaKorisnikaSaStatusom(rezervacija.getPunoIme(), new RezervacijaPrimljena());
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
        boolean postojiAktivnaPreklapanje = repozitorij.postojiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, rezervacija);

        if(postojiPrimljenaKorisnika || postojiAktivnaPreklapanje){
            rezervacija.setStatus(new RezervacijaOdgodena());
            aranzman.dodajDijete(rezervacija);
            return "";
        }

        List<Rezervacija> rezervacijePostojece = aranzman.dohvatiRezervacijeSaStatusom(new RezervacijaPrimljena());

        rezervacija.setStatus(new RezervacijaPrimljena());
        aranzman.dodajDijete(rezervacija);

        for(Rezervacija r : rezervacijePostojece){
            boolean postojiPrimljenaKorisnikaPostojece = aranzman.postojiViseRezervacijaKorisnikaStatusom(r.getPunoIme(), new RezervacijaPrimljena());
            if(postojiPrimljenaKorisnikaPostojece){
                r.setStatus(new RezervacijaOdgodena());
                //System.out.println("Rezervacija korisnika " + r.getPunoIme() + "mijenja se u status ODGOĐENA jer već ima primljenu rezervaciju koja treba postati aktivna.");
            }
            boolean postojiAktivnaPreklapanjePostojece = repozitorij.postojiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, r);
            if(postojiAktivnaPreklapanjePostojece){
                r.setStatus(new RezervacijaOdgodena());
                //System.out.println("Rezervacija korisnika " + r.getPunoIme() + "mijenja se u status ODGOĐENA jer već ima aktivnu rezervaciju koja se preklapa.");
            }
        }

        brojPrimljenihRezervacija = aranzman.dohvatiRezervacijeSaStatusom(new RezervacijaPrimljena()).size();
        if(!(brojPrimljenihRezervacija == min)){
            return "";
        }

        rezervacijePostojece = aranzman.dohvatiRezervacijeSaStatusom(new RezervacijaPrimljena());
        for(Rezervacija r : rezervacijePostojece){
            r.setStatus(new RezervacijaAktivna());
        }
        aranzman.postaviAktivan();
        return "";





    }

    @Override
    public void otkaziRezervaciju(Aranzman aranzman, Rezervacija Rezervacija) {
    }

    @Override
    public void otkaziAranzman(Aranzman aranzman) {
    }

    @Override
    public String getStatusNaziv() {
        return "U PRIPREMI";
    }
}
