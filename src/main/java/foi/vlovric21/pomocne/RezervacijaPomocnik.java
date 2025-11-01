package foi.vlovric21.pomocne;

import foi.vlovric21.objekti.Aranzman;
import foi.vlovric21.objekti.Rezervacija;
import foi.vlovric21.objekti.RezervacijaStatus;
import foi.vlovric21.singleton.RepozitorijPodataka;

import java.util.ArrayList;
import java.util.List;

public class RezervacijaPomocnik {

    public String dodajRezervaciju(Rezervacija rezervacija){ //TODO: masivno refaktorirat
        int oznaka = rezervacija.getOznakaAranzmana();
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();

        Aranzman aranzman = repozitorij.getAranzmanPoOznaci(oznaka);
        List<Rezervacija> rezervacijeZaAranzman = repozitorij.getRezervacijeZaAranzman(oznaka);

        int minBrojPutnika = aranzman.getMinBrojPutnika();
        int maxBrojPutnika = aranzman.getMaxBrojPutnika();
        int brojRezervacija = 0;

        for(Rezervacija r : rezervacijeZaAranzman){
            if(r.getStatus() == RezervacijaStatus.PRIMLJENA || r.getStatus() == RezervacijaStatus.AKTIVNA){ //TODO: provjerit jel jedno od ovo dvoje 0 da budem sig sam pri ispisu
                brojRezervacija++;
            }
        }

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
                List<Integer> validneRezervacijeId = new ArrayList<>();
                int brojValidnihRezervacija = 0;
                for(Rezervacija r : rezervacijeZaAranzman){
                    if(!provjeriValidnostRezervacije(r, RezervacijaStatus.PRIMLJENA)){
                        r.setStatus(RezervacijaStatus.NA_CEKANJU);
                    }else{
                        validneRezervacijeId.add(r.getId());
                        brojValidnihRezervacija++;
                    }
                }
                if(brojValidnihRezervacija < minBrojPutnika){
                    rezervacija.setStatus(RezervacijaStatus.PRIMLJENA);
                    repozitorij.dodajRezervaciju(rezervacija);
                    return "Uspješno dodavanje rezervacije: rezervacija je PRIMLJENA, potrebno je još " + (minBrojPutnika - brojValidnihRezervacija) + " rezervacija za aktivaciju aranžmana";
                }
                for(int id : validneRezervacijeId){
                    rezervacijeZaAranzman.stream()
                            .filter(r -> r.getId() == id)
                            .forEach(r -> r.setStatus(RezervacijaStatus.AKTIVNA));
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

    public String otkaziRezervaciju(String ime, String prezime, int oznaka){
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();

        List<Rezervacija> rezervacijeZaAranzman = repozitorij.getRezervacijeZaAranzman(oznaka);
        Rezervacija rezervacija = null;

        for(Rezervacija r : rezervacijeZaAranzman){
            if(r.getIme().equals(ime) && r.getPrezime().equals(prezime)){
                rezervacija = r;
                break;
            }
        }
        if(rezervacija == null){
            return "Neuspješno otkazivanje: Ne postoji rezervacija za uneseno ime, prezime i oznaku aranžmana";
        }

        if(rezervacija.getStatus() == RezervacijaStatus.AKTIVNA){
            prenesiURedOtkazanih(rezervacija);
            dodijeliAktivniStatus();
            return "Uspješno otkazivanje rezervacije";
        }else{
            prenesiURedOtkazanih(rezervacija);
            return "Uspješno otkazivanje rezervacije";
        }
    }

    private void prenesiURedOtkazanih(Rezervacija rezervacija){
        RepozitorijPodataka.getInstance().prebaciURedOtkazanih(rezervacija);
    }

    private void dodijeliAktivniStatus(){
        // TODO: implementirati
    }
}
