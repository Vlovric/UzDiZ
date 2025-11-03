package foi.vlovric21.pomocne;

import foi.vlovric21.objekti.Aranzman;
import foi.vlovric21.objekti.Rezervacija;
import foi.vlovric21.objekti.RezervacijaStatus;
import foi.vlovric21.singleton.RepozitorijPodataka;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class RezervacijaPomocnik {

    private Set<RezervacijaStatus> osnovniStatusi = EnumSet.of(
            RezervacijaStatus.AKTIVNA,
            RezervacijaStatus.PRIMLJENA,
            RezervacijaStatus.NA_CEKANJU
    );

    private Set<RezervacijaStatus> aktPrimStatusi = EnumSet.of(
            RezervacijaStatus.AKTIVNA,
            RezervacijaStatus.PRIMLJENA
    );

    public String dodajRezervaciju(Rezervacija rezervacija){ //TODO: masivno refaktorirat
        int oznaka = rezervacija.getOznakaAranzmana();
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();

        Aranzman aranzman = repozitorij.getAranzmanPoOznaci(oznaka);
        List<Rezervacija> rezervacijeZaAranzman = repozitorij.getRezervacijeZaAranzman(oznaka, aktPrimStatusi);

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
                validno = provjeriValidnostRezervacije(rezervacija, RezervacijaStatus.PRIMLJENA);
                if(!validno){
                    return "Neuspješno dodavanje rezervacije: već postoji primljena rezervacija za tog korisnika, a ovaj unos bi aktivirao aranžman";
                }
                List<Integer> validneRezervacijeId = new ArrayList<>();
                int brojValidnihRezervacija = 0;
                for(Rezervacija r : rezervacijeZaAranzman){
                    if(!provjeriValidnostRezervacije(r, RezervacijaStatus.PRIMLJENA)){
                        neispravnaRezervacija(r);
                    }else{
                        validneRezervacijeId.add(r.getId());
                        brojValidnihRezervacija++;
                    }
                }
                if(provjeriValidnostRezervacije(rezervacija, RezervacijaStatus.PRIMLJENA)){
                    brojValidnihRezervacija++;
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

    private void neispravnaRezervacija(Rezervacija rezervacija){
        System.out.println("Rezervacija korisnika " + rezervacija.getPunoIme() + " za ovaj aranžman je neispravna i briše se.");
        RepozitorijPodataka.getInstance().obrisiRezervacijuIzAranzmana(rezervacija);
    }

    private boolean provjeriValidnostRezervacije(Rezervacija rezervacija, RezervacijaStatus statusZaProvjeru){
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
        boolean postojiAktivna = repozitorij.postojiRezervacijaKorisnikaStatus(rezervacija, statusZaProvjeru);
        boolean postojiPreklapanje = !repozitorij.dohvatiPreklapanjaStatus(rezervacija, RezervacijaStatus.AKTIVNA).isEmpty();
        if(postojiAktivna || postojiPreklapanje){
            return false;
        }
        return true;
    }

    public String otkaziRezervaciju(String ime, String prezime, int oznaka){
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();

        List<Rezervacija> rezervacijeZaAranzman = repozitorij.getRezervacijeZaAranzman(oznaka, osnovniStatusi);
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

            Aranzman aranzman = repozitorij.getAranzmanPoOznaci(oznaka);
            List<Rezervacija> preostaleRezervacije = repozitorij.getRezervacijeZaAranzman(oznaka, aktPrimStatusi);
            List<Rezervacija> sveRezervacije = repozitorij.getRezervacijeZaAranzman(oznaka, osnovniStatusi);

            if(preostaleRezervacije.size() < aranzman.getMinBrojPutnika()){

                dodijeliAktivniStatus(sveRezervacije);
                preostaleRezervacije = repozitorij.getRezervacijeZaAranzman(oznaka, aktPrimStatusi);

                if(preostaleRezervacije.size() < aranzman.getMinBrojPutnika()){
                    for(Rezervacija r : preostaleRezervacije){
                        if(r.getStatus() == RezervacijaStatus.AKTIVNA){
                            r.setStatus(RezervacijaStatus.PRIMLJENA);
                        }
                    }
                }
            }
            else{
                dodijeliAktivniStatus(rezervacijeZaAranzman);
            }
            return "Uspješno otkazivanje rezervacije";
        }else{
            prenesiURedOtkazanih(rezervacija);
            return "Uspješno otkazivanje rezervacije";
        }
    }

    private void prenesiURedOtkazanih(Rezervacija rezervacija){
        RepozitorijPodataka.getInstance().prebaciURedOtkazanih(rezervacija);
    }

    private void dodijeliAktivniStatus(List<Rezervacija> rezervacijeZaAranzman){
        for(Rezervacija r : rezervacijeZaAranzman){
            if(r.getStatus() != RezervacijaStatus.NA_CEKANJU){
                continue;
            }
            boolean validno = provjeriValidnostRezervacije(r, RezervacijaStatus.AKTIVNA);
            if(!validno) {
                neispravnaRezervacija(r);
                continue;
            }
            r.setStatus(RezervacijaStatus.AKTIVNA);
            return;
        }
    }
}
