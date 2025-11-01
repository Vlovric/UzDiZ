package foi.vlovric21.pomocne;

import foi.vlovric21.objekti.Aranzman;
import foi.vlovric21.objekti.Rezervacija;
import foi.vlovric21.objekti.RezervacijaStatus;
import foi.vlovric21.singleton.RepozitorijPodataka;

import java.util.List;

public class RezervacijaPomocnik {

    public String dodajRezervaciju(Rezervacija rezervacija){ //TODO: masivno refaktorirat
        int oznaka = rezervacija.getOznakaAranzmana();
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();

        Aranzman aranzman = repozitorij.getAranzmanPoOznaci(oznaka);
        List<Rezervacija> rezervacijeZaAranzman = repozitorij.getRezervacijeZaAranzman(oznaka);

        int minBrojPutnika = aranzman.getMinBrojPutnika();
        int maxBrojPutnika = aranzman.getMaxBrojPutnika();
        int brojRezervacija = rezervacijeZaAranzman.size();
        int brojRezervacijaNakonDodavanja = brojRezervacija + 1;

        if(brojRezervacijaNakonDodavanja < minBrojPutnika){
            rezervacija.setStatus(RezervacijaStatus.PRIMLJENA);
            repozitorij.dodajRezervaciju(rezervacija);
            return "Uspješno dodavanje rezervacije: rezervacija je PRIMLJENA, potrebno je još " + (minBrojPutnika - brojRezervacijaNakonDodavanja) + " rezervacija za aktivaciju aranžmana";

        }else if(brojRezervacijaNakonDodavanja <= maxBrojPutnika){
            boolean validno = provjeriValidnostRezervacije(rezervacija, RezervacijaStatus.AKTIVNA);
            if(!validno){
                return "Neuspješno dodavanje rezervacije: postoji aktivna rezervacija u istom aranžmanu ili preklapanje s drugim aranžmanom";
            }
            if(brojRezervacijaNakonDodavanja == minBrojPutnika){
                int brojValidnihRezervacija = 0;
                for(Rezervacija r : rezervacijeZaAranzman){
                    if(provjeriValidnostRezervacije(r, RezervacijaStatus.PRIMLJENA)){
                        r.setStatus(RezervacijaStatus.AKTIVNA);
                        brojValidnihRezervacija++;
                    }
                }
                if(brojValidnihRezervacija < minBrojPutnika){
                    rezervacija.setStatus(RezervacijaStatus.PRIMLJENA);
                    repozitorij.dodajRezervaciju(rezervacija);
                    return "Uspješno dodavanje rezervacije: rezervacija je PRIMLJENA, potrebno je još " + (minBrojPutnika - brojValidnihRezervacija) + " rezervacija za aktivaciju aranžmana";
                }
                rezervacija.setStatus(RezervacijaStatus.AKTIVNA);
                repozitorij.dodajRezervaciju(rezervacija);
                return "Uspješno dodavanje rezervacije: rezervacija je AKTIVNA";
            }
            rezervacija.setStatus(RezervacijaStatus.AKTIVNA);
            repozitorij.dodajRezervaciju(rezervacija);
            return "Uspješno dodavanje rezervacije: rezervacija je AKTIVNA";
        }else{
            rezervacija.setStatus(RezervacijaStatus.NA_CEKANJU);
            repozitorij.dodajRezervaciju(rezervacija);
            return "Uspješno dodavanje rezervacije: rezervacija je NA ČEKANJU, maksimalan broj putnika je dostignut";
        }
    }

    private boolean provjeriValidnostRezervacije(Rezervacija rezervacija, RezervacijaStatus statusZaProvjeru){
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
        if(repozitorij.postojiAktivnaRezervacija(rezervacija, statusZaProvjeru) || repozitorij.postojiAktivnaPreklapanje(rezervacija)){
            return false;
        }
        return true;
    }

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
}
