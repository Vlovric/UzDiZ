package edu.unizg.foi.uzdiz.vlovric21.strategy_null_object;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.state_aranzman.AranzmanAktivan;
import edu.unizg.foi.uzdiz.vlovric21.state_aranzman.AranzmanUPripremi;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOdgodena;

public class VdrRezervacijaUpravitelj implements RezervacijaUpravitelj {

    @Override
    public Boolean upravljajRezervacijom(Aranzman aranzman, Rezervacija rezervacija) {
        int brojRezervacija = aranzman.dohvatiBrojRezervacijaKorisnika(rezervacija.getPunoIme());
        int maxBrojRezervacija = aranzman.getMaxBrojPutnika();

        if((double) brojRezervacija > 1/4.0 * maxBrojRezervacija){
            rezervacija.setStatus(new RezervacijaOdgodena());
            aranzman.dodajDijete(rezervacija);
            return true;
        }
        return false;
    }
}
