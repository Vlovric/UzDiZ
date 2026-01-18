package edu.unizg.foi.uzdiz.vlovric21.state_aranzman;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.AranzmanKomponenta;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaNaCekanju;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOtkazana;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AranzmanPopunjen implements AranzmanStatus{
    @Override
    public String dodajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
        if(rezervacija.jeOtkazana()){
            aranzman.dodajDijete(rezervacija);
            return "";
        }
        if(RepozitorijPodataka.getInstance().upravljajRezervacijom(aranzman, rezervacija)){
            return "";
        }
        rezervacija.setStatus(new RezervacijaNaCekanju());
        aranzman.dodajDijete(rezervacija);
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
        return "POPUNJEN";
    }

    @Override
    public boolean jeOtkazan() {
        return false;
    }
}
