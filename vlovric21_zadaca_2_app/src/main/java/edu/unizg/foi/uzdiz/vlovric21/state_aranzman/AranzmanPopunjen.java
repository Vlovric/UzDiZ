package edu.unizg.foi.uzdiz.vlovric21.state_aranzman;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaNaCekanju;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOtkazana;

public class AranzmanPopunjen implements AranzmanStatus{
    @Override
    public String dodajRezervaciju(Aranzman aranzman, Rezervacija rezervacija) {
        if(rezervacija.getStatus().equals(new RezervacijaOtkazana().getStatusNaziv())){
            aranzman.dodajDijete(rezervacija);
            return "";
        }
        rezervacija.setStatus(new RezervacijaNaCekanju());
        aranzman.dodajDijete(rezervacija);
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
        return "POPUNJEN";
    }
}
