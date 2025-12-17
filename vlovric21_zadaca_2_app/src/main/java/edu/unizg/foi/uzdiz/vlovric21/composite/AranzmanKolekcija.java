package edu.unizg.foi.uzdiz.vlovric21.composite;

import edu.unizg.foi.uzdiz.vlovric21.pomocne.DatumFormater;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaAktivna;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaNova;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOtkazana;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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

    @Override
    public List<Rezervacija> dohvatiSveRezervacije() {
        List<Rezervacija> rezultat = new ArrayList<>();
        for(AranzmanKomponenta k : djeca){
            rezultat.addAll(k.dohvatiSveRezervacije());
        }
        return rezultat;
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

    private final Comparator<Rezervacija> rezervacijaPrijeDruge = (r1, r2) -> {
        DatumFormater datumFormater = RepozitorijPodataka.getInstance().getDatumFormater();
        LocalDateTime dt1 = datumFormater.parseDatumIVrijeme(r1.getDatumIVrijeme());
        LocalDateTime dt2 = datumFormater.parseDatumIVrijeme(r2.getDatumIVrijeme());
        return dt1.compareTo(dt2);
    };

    private String ponovnoUnosenjeRezervacija(Aranzman aranzman, Rezervacija rezervacija){
        List<Rezervacija> rezervacije = new ArrayList<>();
        for(AranzmanKomponenta k : aranzman.dohvatiDjecu()){
            if(k instanceof Rezervacija r){
                if(!r.jeOtkazana()){
                    r.setStatus(new RezervacijaNova());
                }
                rezervacije.add(r);
            }
        }

        if(rezervacija != null){
            rezervacije.add(rezervacija);
        }
        rezervacije.sort(rezervacijaPrijeDruge);

        aranzman.resetirajStanje();
        String rezultatDodavanja = "";

        for(Rezervacija r : rezervacije) {
            rezultatDodavanja = aranzman.dodajRezervaciju(r);
        }

        return rezultatDodavanja;
    }

    public String dodajRezervaciju(Rezervacija rezervacija){
        int id = rezervacija.getId();
        Aranzman aranzman = dohvatiAranzmanPoOznaci(rezervacija.getOznakaAranzmana());
        rezervacija.setAranzman(aranzman);

        String rezultatDodavanja = ponovnoUnosenjeRezervacija(aranzman, rezervacija);

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
        List<Integer> kopijaKronologije = new ArrayList<>(aranzmaniKronologija);
        aranzmaniKronologija.clear();

        for(int oznaka : kopijaKronologije){
            Aranzman aranzman = dohvatiAranzmanPoOznaci(oznaka);
            ponovnoUnosenjeRezervacija(aranzman, null);
        }
        if(!aranzmaniKronologija.isEmpty()){
            popraviKronologiju();
        }
    }

    public void otkaziSveRezervacijeAranzmana(int oznaka){
        Aranzman aranzman = dohvatiAranzmanPoOznaci(oznaka);
        aranzman.otkazi();
    }

    private int dohvatiPreklapajuciAranzman(Aranzman aranzman, Rezervacija rezervacija, boolean kronoloski){
        String punoIme = rezervacija.getPunoIme();
        RepozitorijPodataka repo = RepozitorijPodataka.getInstance();

        for(AranzmanKomponenta k : dohvatiDjecu()) {
            if (!(k instanceof Aranzman a) || a.getOznaka() == aranzman.getOznaka()) {
                continue;
            }
            if (!repo.provjeriPreklapanjeDatuma(aranzman, a)) {
                continue;
            }
            for (Rezervacija r : a.dohvatiSveRezervacije()) {
                if (!r.getPunoIme().equals(punoIme)) {
                    continue;
                }
                if (r.getStatus().equals(new RezervacijaAktivna().getStatusNaziv())) {
                    boolean uvjet;
                    if (kronoloski) {
                        uvjet = repo.rezervacijaPrijeDruge(r, rezervacija);
                    } else {
                        uvjet = repo.rezervacijaPrijeDruge(rezervacija, r);
                    }

                    if (uvjet) return a.getOznaka();
                }
            }
        }
        return -1;
    }

    public  boolean postojiKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(Aranzman aranzman, Rezervacija rezervacija){
        return dohvatiPreklapajuciAranzman(aranzman, rezervacija, true) != -1;
    }

    public int postojiNeKronoloskiAktivnaRezervacijaPreklapanjeKorisnik(Aranzman aranzman, Rezervacija rezervacija){
        return dohvatiPreklapajuciAranzman(aranzman, rezervacija, false);
    }

    public List<Aranzman> dohvatiAranzmaneRazdoblje(String pocetniDatum, String zavrsniDatum){
        List<AranzmanKomponenta> aranzmani = dohvatiDjecu();
        List<Aranzman> rezultat = new ArrayList<>();

        if(pocetniDatum == null && zavrsniDatum == null){
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

        sveRezervacije.sort(rezervacijaPrijeDruge);

        for(Rezervacija r : sveRezervacije){
            int noviId = RepozitorijPodataka.getInstance().getIdRezervacije();
            r.setId(noviId);
            if(!r.jeOtkazana()){
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
