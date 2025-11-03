package foi.vlovric21.pomocne;

import foi.vlovric21.objekti.Aranzman;
import foi.vlovric21.objekti.Rezervacija;
import foi.vlovric21.objekti.RezervacijaStatus;
import foi.vlovric21.singleton.RepozitorijPodataka;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class RezervacijaPomocnik {
    private static RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();

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

        Aranzman aranzman = repozitorij.getAranzmanPoOznaci(oznaka);
        List<Rezervacija> rezervacijeZaAranzman = repozitorij.getRezervacijeZaAranzman(oznaka, aktPrimStatusi);

        int minBrojPutnika = aranzman.getMinBrojPutnika();
        int maxBrojPutnika = aranzman.getMaxBrojPutnika();

        int brojRezervacijaNakonDodavanja = rezervacijeZaAranzman.size() + 1;

        if(brojRezervacijaNakonDodavanja < minBrojPutnika){
            return dodajPrimljenaRezervacija(rezervacija, brojRezervacijaNakonDodavanja, minBrojPutnika);
        }else if(brojRezervacijaNakonDodavanja <= maxBrojPutnika){
            return dodajAktivnaRezervacija(rezervacija, rezervacijeZaAranzman, brojRezervacijaNakonDodavanja, minBrojPutnika);
        }else{
            return dodajNaCekanjuRezervacija(rezervacija);
        }
    }

    private String dodajPrimljenaRezervacija(Rezervacija rezervacija, int brojRezervacija, int minBrojPutnika){
        rezervacija.setStatus(RezervacijaStatus.PRIMLJENA);
        repozitorij.dodajRezervaciju(rezervacija);
        return "Uspješno dodavanje rezervacije: rezervacija je PRIMLJENA, potrebno je još " + (minBrojPutnika - brojRezervacija) + " rezervacija za aktivaciju aranžmana";
    }

    private String dodajAktivnaRezervacija(Rezervacija rezervacija, List<Rezervacija> rezervacijeZaAranzman, int brojRezervacijaNakonDodavanja, int minBrojPutnika){
        boolean validno = provjeriValidnostRezervacije(rezervacija, RezervacijaStatus.AKTIVNA);

        if(!validno){
            return "Neuspješno dodavanje rezervacije: postoji aktivna rezervacija u istom aranžmanu ili preklapanje s drugim aranžmanom";
        }

        if(brojRezervacijaNakonDodavanja == minBrojPutnika){
            return aktivirajAranzman(rezervacija, rezervacijeZaAranzman, minBrojPutnika);
        }

        return unesiRezervacijuKaoAktivnu(rezervacija);
    }

    private boolean provjeriValidnostRezervacije(Rezervacija rezervacija, RezervacijaStatus statusZaProvjeru){
        boolean postojiAktivna = repozitorij.postojiRezervacijaKorisnikaStatus(rezervacija, statusZaProvjeru);
        boolean postojiPreklapanje = !repozitorij.dohvatiPreklapanjaStatus(rezervacija, RezervacijaStatus.AKTIVNA).isEmpty();
        if(postojiAktivna || postojiPreklapanje){
            return false;
        }
        return true;
    }

    private String aktivirajAranzman(Rezervacija novaRezervacija, List<Rezervacija> postojeceRezervacije, int minBrojPutnika){
        boolean validno = provjeriValidnostRezervacije(novaRezervacija, RezervacijaStatus.PRIMLJENA);

        if(!validno){
            return "Neuspješno dodavanje rezervacije: već postoji primljena rezervacija za tog korisnika, a ovaj unos bi aktivirao aranžman";
        }

        List<Integer> validneRezervacijeId = validirajPostojeceRezervacije(postojeceRezervacije);
        int brojValidnihRezervacija = izracunajBrojValidnihRezervacija(novaRezervacija, validneRezervacijeId.size());

        if(brojValidnihRezervacija < minBrojPutnika){
            return dodajPrimljenaRezervacija(novaRezervacija, brojValidnihRezervacija, minBrojPutnika);
        }

        aktivirajValidneRezervacije(postojeceRezervacije, validneRezervacijeId);

        return unesiRezervacijuKaoAktivnu(novaRezervacija);
    }

    private List<Integer> validirajPostojeceRezervacije(List<Rezervacija> rezervacije){
        List<Integer> validneRezervacijeId = new ArrayList<>();
        for (Rezervacija r : rezervacije) {
            if (!provjeriValidnostRezervacije(r, RezervacijaStatus.PRIMLJENA)) {
                neispravnaRezervacija(r);
            } else {
                validneRezervacijeId.add(r.getId());
            }
        }
        return validneRezervacijeId;
    }

    private void neispravnaRezervacija(Rezervacija rezervacija){
        System.out.println("Rezervacija korisnika " + rezervacija.getPunoIme() + " za ovaj aranžman je neispravna i briše se.");
        repozitorij.obrisiRezervacijuIzAranzmana(rezervacija);
    }

    private int izracunajBrojValidnihRezervacija(Rezervacija novaRezervacija, int brojValidnih) {
        int broj = brojValidnih;
        if (provjeriValidnostRezervacije(novaRezervacija, RezervacijaStatus.PRIMLJENA)) {
            broj++;
        }
        return broj;
    }

    private void aktivirajValidneRezervacije(List<Rezervacija> sveRezervacije, List<Integer> validneRezervacijeId){
        for (int id : validneRezervacijeId) {
            sveRezervacije.stream()
                    .filter(r -> r.getId() == id)
                    .forEach(r -> r.setStatus(RezervacijaStatus.AKTIVNA));
        }
    }

    private String unesiRezervacijuKaoAktivnu(Rezervacija rezervacija){
        rezervacija.setStatus(RezervacijaStatus.AKTIVNA);
        repozitorij.dodajRezervaciju(rezervacija);
        return "Uspješno dodavanje rezervacije: rezervacija je AKTIVNA";
    }

    private String dodajNaCekanjuRezervacija(Rezervacija rezervacija){
        rezervacija.setStatus(RezervacijaStatus.NA_CEKANJU);
        repozitorij.dodajRezervaciju(rezervacija);
        return "Uspješno dodavanje rezervacije: rezervacija je NA ČEKANJU, maksimalan broj putnika je dostignut";
    }

    public String otkaziRezervaciju(String ime, String prezime, int oznaka){
        Rezervacija rezervacija = pronadiRezervaciju(ime, prezime, oznaka);

        if(rezervacija == null){
            return "Neuspješno otkazivanje: Ne postoji rezervacija za uneseno ime, prezime i oznaku aranžmana";
        }

        if(rezervacija.getStatus() == RezervacijaStatus.AKTIVNA){
            return otkaziAktivnuRezervaciju(rezervacija, oznaka);
        }else{
            prenesiURedOtkazanih(rezervacija);
            return "Uspješno otkazivanje rezervacije";
        }
    }

    private Rezervacija pronadiRezervaciju(String ime, String prezime, int oznaka){
        List<Rezervacija> rezervacijeZaAranzman = repozitorij.getRezervacijeZaAranzman(oznaka, osnovniStatusi);

        for (Rezervacija r : rezervacijeZaAranzman) {
            if (r.getIme().equals(ime) && r.getPrezime().equals(prezime)) {
                return r;
            }
        }
        return null;
    }

    private String otkaziAktivnuRezervaciju(Rezervacija rezervacija, int oznaka){
        prenesiURedOtkazanih(rezervacija);

        Aranzman aranzman = repozitorij.getAranzmanPoOznaci(oznaka);
        List<Rezervacija> preostaleRezervacije = repozitorij.getRezervacijeZaAranzman(oznaka, aktPrimStatusi);
        List<Rezervacija> sveRezervacije = repozitorij.getRezervacijeZaAranzman(oznaka, osnovniStatusi);

        if(preostaleRezervacije.size() < aranzman.getMinBrojPutnika()){
            deaktivirajAranzmanAkoTreba(oznaka, sveRezervacije, aranzman.getMinBrojPutnika());
        }else{
            dodijeliAktivniStatus(sveRezervacije);
        }
        return "Uspješno otkazivanje rezervacije";
    }

    private void prenesiURedOtkazanih(Rezervacija rezervacija){
        repozitorij.prebaciURedOtkazanih(rezervacija);
    }

    private void deaktivirajAranzmanAkoTreba(int oznaka, List<Rezervacija> sveRezervacije, int minBrojPutnika){
        dodijeliAktivniStatus(sveRezervacije);

        List<Rezervacija> preostaleRezervacije = repozitorij.getRezervacijeZaAranzman(oznaka, aktPrimStatusi);

        if(preostaleRezervacije.size() < minBrojPutnika){
            prebaciAktivneUPrimljene(preostaleRezervacije);
        }
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

    private void prebaciAktivneUPrimljene(List<Rezervacija> rezervacije){
        for (Rezervacija r : rezervacije) {
            if (r.getStatus() == RezervacijaStatus.AKTIVNA) {
                r.setStatus(RezervacijaStatus.PRIMLJENA);
            }
        }
    }
}
