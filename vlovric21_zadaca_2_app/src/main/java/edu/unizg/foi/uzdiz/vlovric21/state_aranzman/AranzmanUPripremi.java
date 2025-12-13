package edu.unizg.foi.uzdiz.vlovric21.state_aranzman;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaAktivna;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOdgodena;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaPrimljena;

import java.util.List;

public class AranzmanUPripremi implements AranzmanStatus{


    @Override
    public String dodajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
        int min = aranzman.getMinBrojPutnika();
        int brojPrimljenihRezervacija = aranzman.dohvatiRezervacijeSaStatusom(new RezervacijaPrimljena()).size();

        if(brojPrimljenihRezervacija < min){
            rezervacija.setStatus(new RezervacijaPrimljena());
            aranzman.dodajDijete(rezervacija);
            return "Rezervacija uspješno dodana";
        }

        boolean postojiPrimljenaKorisnika = aranzman.postojiRezervacijaKorisnikaSaStatusom(rezervacija.getPunoIme(), new RezervacijaPrimljena());
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
        boolean postojiAktivnaPreklapanje = repozitorij.postojiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, rezervacija);

        if(postojiPrimljenaKorisnika || postojiAktivnaPreklapanje){
            rezervacija.setStatus(new RezervacijaOdgodena());
            aranzman.dodajDijete(rezervacija);
            return "Rezervacija dodana sa statusom: " + rezervacija.getStatus();
        }

        List<Rezervacija> rezervacijePostojece = aranzman.dohvatiRezervacijeSaStatusom(new RezervacijaPrimljena());

        rezervacija.setStatus(new RezervacijaPrimljena());
        aranzman.dodajDijete(rezervacija);

        for(Rezervacija r : rezervacijePostojece){
            boolean postojiPrimljenaKorisnikaPostojece = aranzman.postojiRezervacijaKorisnikaSaStatusom(r.getPunoIme(), new RezervacijaPrimljena());
            if(postojiPrimljenaKorisnikaPostojece){
                r.setStatus(new RezervacijaOdgodena());
                System.out.println("Rezervacija korisnika " + r.getPunoIme() + "mijenja se u status ODGOĐENA jer već ima primljenu rezervaciju" +
                        "koja treba postati aktivna.");
            }
            boolean postojiAktivnaPreklapanjePostojece = repozitorij.postojiAktivnaRezervacijaPreklapanjeKorisnik(aranzman, r);
            if(postojiAktivnaPreklapanjePostojece){
                r.setStatus(new RezervacijaOdgodena());
                System.out.println("Rezervacija korisnika " + r.getPunoIme() + "mijenja se u status ODGOĐENA jer već ima aktivnu rezervaciju" +
                        "koja se preklapa.");
            }
        }

        brojPrimljenihRezervacija = aranzman.dohvatiRezervacijeSaStatusom(new RezervacijaPrimljena()).size();
        if(!(brojPrimljenihRezervacija == min)){
            return "Rezervacija uspješno dodana";
        }

        rezervacijePostojece = aranzman.dohvatiRezervacijeSaStatusom(new RezervacijaPrimljena());
        for(Rezervacija r : rezervacijePostojece){
            r.setStatus(new RezervacijaAktivna());
        }
        aranzman.postaviAktivan();
        return "Rezervacija uspješno dodana.";





    }

    @Override
    public String otkaziRezervaciju(Aranzman aranzman, Rezervacija Rezervacija) {
        return "";
    }

    @Override
    public String otkaziAranzman(Aranzman aranzman) {
        return "";
    }

    @Override
    public String getStatusNaziv() {
        return "U_PRIPREMI";
    }
}
