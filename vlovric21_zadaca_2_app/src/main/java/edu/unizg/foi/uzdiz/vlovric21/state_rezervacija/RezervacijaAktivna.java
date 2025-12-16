package edu.unizg.foi.uzdiz.vlovric21.state_rezervacija;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;

public class RezervacijaAktivna implements RezervacijaStatus{

    @Override
    public String otkazi(Aranzman aranzman, Rezervacija kontekst){
        kontekst.setStatus(new RezervacijaOtkazana());
        RepozitorijPodataka.getInstance().resetirajRezervacijeOtkaz();
        return "";
    }

    @Override
    public String getStatusNaziv() {
        return "AKTIVNA";
    }
}
