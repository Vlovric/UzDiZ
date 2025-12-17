package edu.unizg.foi.uzdiz.vlovric21.composite;

import edu.unizg.foi.uzdiz.vlovric21.pomocne.DatumFormater;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaAktivna;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaNova;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOtkazana;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AranzmanKolekcija implements AranzmanKomponenta {

    private final List<AranzmanKomponenta> djeca = new ArrayList<>();
    private List<Integer> aranzmaniKronologija = new ArrayList<>();

    @Override
    public void dodajDijete(AranzmanKomponenta komponenta){
        djeca.add(komponenta);
    }

    @Override
    public void ukloniDijete(AranzmanKomponenta komponenta){
        djeca.remove(komponenta);
    }

    @Override
    public List<AranzmanKomponenta> dohvatiDjecu() {
        return djeca;
    }

    public void ukloniSvuDjecu(){
        djeca.clear();
    }

    public Aranzman dohvatiAranzmanPoOznaci(int oznaka){
        for(AranzmanKomponenta k : djeca){
            if(k instanceof Aranzman a && a.getOznaka() == oznaka){
                return a;
            }
        }
        return null;
    }

    public String dodajRezervaciju(Rezervacija rezervacija){
        int id = rezervacija.getId();
        Aranzman aranzman = dohvatiAranzmanPoOznaci(rezervacija.getOznakaAranzmana());
        rezervacija.setAranzman(aranzman);

        List<Rezervacija> tempRezervacije = new ArrayList<>();
        List<AranzmanKomponenta> rezervacije = aranzman.dohvatiDjecu();
        for(AranzmanKomponenta a : rezervacije){
            if(a instanceof Rezervacija r){
                if(r.getStatus().equals(new RezervacijaOtkazana().getStatusNaziv())){
                    tempRezervacije.add(r);
                }else{
                    r.setStatus(new RezervacijaNova());
                    tempRezervacije.add(r);
                }
            }
        }
        tempRezervacije.add(rezervacija);
        DatumFormater datumFormater = RepozitorijPodataka.getInstance().getDatumFormater();
        tempRezervacije.sort((r1, r2) -> {
            LocalDateTime dt1 = datumFormater.parseDatumIVrijeme(r1.getDatumIVrijeme());
            LocalDateTime dt2 = datumFormater.parseDatumIVrijeme(r2.getDatumIVrijeme());
            return dt1.compareTo(dt2);
        });

        aranzman.resetirajStanje();
        String rezultatDodavanja = "";

        for(Rezervacija r : tempRezervacije) {
            rezultatDodavanja = aranzman.dodajRezervaciju(r);
        }

        String statusRezervacije = aranzman.dohvatiRezervacijuPoID(id).getStatus();

        if(!aranzmaniKronologija.isEmpty()){
            popraviKronologiju();
        }

        if(!rezultatDodavanja.isEmpty()){
            return rezultatDodavanja;
        }else{
            return "Rezervacija uspješno dodana. Status rezervacije: " + statusRezervacije;
        }
    }

    private void popraviKronologiju(){
        for(int oznaka : aranzmaniKronologija){
            Aranzman aranzman = dohvatiAranzmanPoOznaci(oznaka);

            List<Rezervacija> tempRezervacije = new ArrayList<>();
            List<AranzmanKomponenta> rezervacije = aranzman.dohvatiDjecu();
            for(AranzmanKomponenta a : rezervacije){
                if(a instanceof Rezervacija r){
                    if(r.getStatus().equals(new RezervacijaOtkazana().getStatusNaziv())){
                        tempRezervacije.add(r);
                    }else{
                        r.setStatus(new RezervacijaNova());
                        tempRezervacije.add(r);
                    }
                }
            }
            DatumFormater datumFormater = RepozitorijPodataka.getInstance().getDatumFormater();
            tempRezervacije.sort((r1, r2) -> {
                LocalDateTime dt1 = datumFormater.parseDatumIVrijeme(r1.getDatumIVrijeme());
                LocalDateTime dt2 = datumFormater.parseDatumIVrijeme(r2.getDatumIVrijeme());
                return dt1.compareTo(dt2);
            });

            aranzman.resetirajStanje();

            for(Rezervacija r : tempRezervacije) {
                aranzman.dodajRezervaciju(r);
            }
        }
        aranzmaniKronologija.clear();
    }

    public void otkaziSveRezervacijeAranzmana(int oznaka){
        Aranzman aranzman = dohvatiAranzmanPoOznaci(oznaka);
        aranzman.otkaziSveRezervacije();
    }

    public  boolean postojiKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(Aranzman aranzman, Rezervacija rezervacija){
        String punoIme = rezervacija.getPunoIme();
        RepozitorijPodataka repo = RepozitorijPodataka.getInstance();

        for(AranzmanKomponenta k : dohvatiDjecu()){
            if(!(k instanceof Aranzman a) || a.getOznaka() == aranzman.getOznaka()){
                continue;
            }
            if(!repo.provjeriPreklapanjeDatuma(aranzman, a)){
                continue;
            }
            for(Rezervacija r : a.dohvatiSveRezervacije()){
                if(!r.getPunoIme().equals(punoIme)){
                    continue;
                }
                if(r.getStatus().equals(new RezervacijaAktivna().getStatusNaziv()) && repo.rezervacijaPrijeDruge(r, rezervacija)){
                    return true;
                }
            }
        }
        return false;
    }

    public int postojiNeKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(Aranzman aranzman, Rezervacija rezervacija){
        String punoIme = rezervacija.getPunoIme();
        RepozitorijPodataka repo = RepozitorijPodataka.getInstance();

        for(AranzmanKomponenta k : dohvatiDjecu()){
            if(!(k instanceof Aranzman a) || a.getOznaka() == aranzman.getOznaka()){
                continue;
            }
            if(!repo.provjeriPreklapanjeDatuma(aranzman, a)){
                continue;
            }
            for(Rezervacija r : a.dohvatiSveRezervacije()){
                if(!r.getPunoIme().equals(punoIme)){
                    continue;
                }
                if(r.getStatus().equals(new RezervacijaAktivna().getStatusNaziv()) && repo.rezervacijaPrijeDruge(rezervacija, r)){
                    return r.getOznakaAranzmana();
                }
            }
        }
        return -1;
    }

    public List<Aranzman> dohvatiAranzmaneRazdoblje(String pocetniDatum, String zavrsniDatum){
        List<AranzmanKomponenta> aranzmani = dohvatiDjecu();

        if(pocetniDatum == null && zavrsniDatum == null){
            List<Aranzman> rezultat = new ArrayList<>();
            for(AranzmanKomponenta k : aranzmani){
                if(k instanceof Aranzman a){
                    rezultat.add(a);
                }
            }
            return rezultat;
        }

        DatumFormater datumFormater = RepozitorijPodataka.getInstance().getDatumFormater();
        LocalDate pocDatum = datumFormater.parseDatum(pocetniDatum);
        LocalDate zavDatum = datumFormater.parseDatum(zavrsniDatum);

        List<Aranzman> rezultat = new ArrayList<>();
        for(AranzmanKomponenta k : aranzmani){
            if(!(k instanceof Aranzman a)){
                continue;
            }
            LocalDate aPocDatum = datumFormater.parseDatum(a.getPocetniDatum());
            LocalDate aZavDatum = datumFormater.parseDatum(a.getZavrsniDatum());

            if(aPocDatum.isBefore(pocDatum) || aZavDatum.isAfter(zavDatum)){
                continue;
            }
            rezultat.add(a);
        }
        return rezultat;
    }

    public void resetirajRezervacijeOtkaz(){
        List<Rezervacija> sveRezervacije = new ArrayList<>();
        for(AranzmanKomponenta a : dohvatiDjecu()){
            if(a instanceof Aranzman ar){
                sveRezervacije.addAll(ar.dohvatiSveRezervacije());
                ar.resetirajStanje();
            }
        }

        DatumFormater datumFormater = RepozitorijPodataka.getInstance().getDatumFormater();
        sveRezervacije.sort((r1, r2) -> {
            LocalDateTime dt1 = datumFormater.parseDatumIVrijeme(r1.getDatumIVrijeme());
            LocalDateTime dt2 = datumFormater.parseDatumIVrijeme(r2.getDatumIVrijeme());
            return dt1.compareTo(dt2);
        });

        for(Rezervacija r : sveRezervacije){
            int noviId = RepozitorijPodataka.getInstance().getIdRezervacije();
            if(r.getStatus().equals(new RezervacijaOtkazana().getStatusNaziv())){
                r.setId(noviId);
            }else{
                r.setId(noviId);
                r.setStatus(new RezervacijaNova());
            }
            Aranzman aranzman = r.getAranzman();
            aranzman.dodajRezervaciju(r);
        }
    }

    public List<Rezervacija> dohvatiRezervacijePoImenu(String punoIme){
        List<Rezervacija> rezultat = new ArrayList<>();
        for(AranzmanKomponenta k : dohvatiDjecu()){
            if(!(k instanceof Aranzman a)){
                continue;
            }
            for(Rezervacija r : a.dohvatiSveRezervacije()){
                if(r.getPunoIme().equals(punoIme)){
                    rezultat.add(r);
                }
            }
        }
        return rezultat;
    }

    public List<Rezervacija> dohvatiRezervacijeZaAranzman(int oznakaAranzmana, List<String> statusi){
        Aranzman aranzman = dohvatiAranzmanPoOznaci(oznakaAranzmana);
        List<Rezervacija> sveRezervacije = aranzman.dohvatiSveRezervacije();
        List<Rezervacija> rezultat = new ArrayList<>();
        for(Rezervacija r : sveRezervacije){
            if(statusi.contains(r.getStatus())){
                rezultat.add(r);
            }
        }
        return rezultat;
    }

    public void obrisiSveRezervacije(){
        for(AranzmanKomponenta k : dohvatiDjecu()){
            if(k instanceof Aranzman a){
                a.resetirajStanje();
            }
        }
    }

    public void dodajAranzmanUKronologiju(int oznaka){
        aranzmaniKronologija.add(oznaka);
    }
}
