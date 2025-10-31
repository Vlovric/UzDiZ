package foi.vlovric21.pomocne;

import foi.vlovric21.objekti.Rezervacija;
import foi.vlovric21.objekti.RezervacijaStatus;

public class RezervacijaPomocnik {

    public void otkaziRezervaciju(Rezervacija rezervacija){
        switch(rezervacija.getStatus()){
            case RezervacijaStatus.AKTIVNA:
                // rezervacija odlazi u red otkazanih rezervacija
                prenesiURedOtkazanih(rezervacija);
                // uzima prva ispravna rezervacija NA_CEKANJU i postaje AKTIVNA
                dodijeliAktivniStatus();
                break;
            case RezervacijaStatus.PRIMLJENA:
                // rezervacija odlazi u red otkazanih rezervacija
                break;
            case RezervacijaStatus.NA_CEKANJU:
                break;
            default:
                break;
        }
    }

    private void prenesiURedOtkazanih(Rezervacija rezervacija){

    }

    private void dodijeliAktivniStatus(){

    }

    public void dodajRezervaciju(Rezervacija rezervacija){

    }
}
