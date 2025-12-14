package edu.unizg.foi.uzdiz.vlovric21.singleton;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.AranzmanKolekcija;
import edu.unizg.foi.uzdiz.vlovric21.composite.AranzmanKomponenta;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.pomocne.DatumFormater;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaAktivna;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaNova;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class RepozitorijPodataka {
    private static RepozitorijPodataka instance = new RepozitorijPodataka();
    private static int idBrojacRezervacija = 1;

    private AranzmanKolekcija aranzmanKolekcija = new AranzmanKolekcija();

    private DatumFormater datumFormater = new DatumFormater();

    private RepozitorijPodataka() {}

    public static RepozitorijPodataka getInstance() {
        return instance;
    }

    public int getIdRezervacije(){
        return idBrojacRezervacija++;
    }

    public Aranzman getAranzmanPoOznaci(int oznaka){
        return aranzmanKolekcija.dohvatiAranzmanPoOznaci(oznaka);
    }

    public boolean postojiAranzmanPoOznaci(int oznaka){
        return aranzmanKolekcija.dohvatiAranzmanPoOznaci(oznaka) != null;
    }

    public void dodajAranzman(Aranzman ar){
        aranzmanKolekcija.dodajDijete(ar);
    }

    public String dodajRezervaciju(Rezervacija rezervacija){
        int id = getIdRezervacije();
        rezervacija.setId(id);
        rezervacija.setStatus(new RezervacijaNova());
        int oznaka = rezervacija.getOznakaAranzmana();
        Aranzman aranzman = aranzmanKolekcija.dohvatiAranzmanPoOznaci(oznaka);

        List<Rezervacija> tempRezervacije = new ArrayList<>();
        List<AranzmanKomponenta> rezervacije = aranzman.dohvatiDjecu();
        for(AranzmanKomponenta a : rezervacije){
            if(a instanceof Rezervacija r){
                r.setStatus(new RezervacijaNova());
                tempRezervacije.add(r);
            }
        }
        tempRezervacije.add(rezervacija);
        tempRezervacije.sort((r1, r2) -> {
            LocalDateTime dt1 = datumFormater.parseDatumIVrijeme(r1.getDatumIVrijeme());
            LocalDateTime dt2 = datumFormater.parseDatumIVrijeme(r2.getDatumIVrijeme());
            return dt1.compareTo(dt2);
        });

        aranzman.resetirajStanje();

        String rezultat = ""; //TODO

        for(Rezervacija r : tempRezervacije) {
            if(r.getId() == id){
                aranzman.dodajRezervaciju(r);
            }else{
                aranzman.dodajRezervaciju(r);
            }
        }

        return rezultat;
        //na kraju na rezultat appendat stanje rezervacije, ako jedino moze bit odgodena u worst case, a ne obrisana
    }

    public boolean postojiAktivnaRezervacijaPreklapanjeKorisnik(Aranzman aranzman, Rezervacija rezervacija){
        String punoIme = rezervacija.getPunoIme();

        for(AranzmanKomponenta k : aranzmanKolekcija.dohvatiDjecu()){
            if(!(k instanceof Aranzman a) || a.getOznaka() == aranzman.getOznaka()){
                continue;
            }
            if(!provjeriPreklapanjeDatuma(aranzman, a)){
                continue;
            }
            for(Rezervacija r : a.dohvatiSveRezervacije()){
                if(!r.getPunoIme().equals(punoIme)){
                    continue;
                }
                if(r.getStatus().equals(new RezervacijaAktivna().getStatusNaziv())){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean provjeriPreklapanjeDatuma(Aranzman a1, Aranzman a2){
        LocalDate a1Pocetak = datumFormater.parseDatum(a1.getPocetniDatum());
        LocalDate a1Kraj = datumFormater.parseDatum(a1.getZavrsniDatum());
        LocalDate a2Pocetak = datumFormater.parseDatum(a2.getPocetniDatum());
        LocalDate a2Kraj = datumFormater.parseDatum(a2.getZavrsniDatum());

        if(a1Kraj.isBefore(a2Pocetak) || a2Kraj.isBefore(a1Pocetak)){
            return false;
        }
        return true;
    }

    public List<Aranzman> dohvatiAranzmaneRazdoblje(String pocetniDatum, String zavrsniDatum){
        List<AranzmanKomponenta> aranzmani = aranzmanKolekcija.dohvatiDjecu();

        if(pocetniDatum == null && zavrsniDatum == null){
            List<Aranzman> rezultat = new ArrayList<>();
            for(AranzmanKomponenta k : aranzmani){
                if(k instanceof Aranzman a){
                    rezultat.add(a);
                }
            }
            return rezultat;
        }

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

    public List<Rezervacija> dohvatiRezervacijePoImenu(String punoIme){
        List<Rezervacija> rezultat = new ArrayList<>();
        for(AranzmanKomponenta k : aranzmanKolekcija.dohvatiDjecu()){
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
        Aranzman aranzman = aranzmanKolekcija.dohvatiAranzmanPoOznaci(oznakaAranzmana);
        List<Rezervacija> sveRezervacije = aranzman.dohvatiSveRezervacije();
        List<Rezervacija> rezultat = new ArrayList<>();
        for(Rezervacija r : sveRezervacije){
            if(statusi.contains(r.getStatus())){
                rezultat.add(r);
            }
        }
        return rezultat;
    }

    public String otkaziRezervaciju(String ime, String prezime, int oznaka){
        return ""; //TODO
    }


    // Nekoristeni getteri i setteri

    public AranzmanKolekcija getAranzmanKolekcija() {
        return aranzmanKolekcija;
    }

    public void setAranzmanKolekcija(AranzmanKolekcija aranzmanKolekcija) {
        this.aranzmanKolekcija = aranzmanKolekcija;
    }

    public void setIdBrojacRezervacija(){
        idBrojacRezervacija = 1;
    }


    // ------------------------ Staro ------------------------

    /*
    private List<Rezervacija> inicijalneRezervacije = new ArrayList<>();


    private Map<Integer, Aranzman> aranzmaniPoOznaci = new HashMap<>();
    private Map<Integer, Rezervacija> rezervacijePoId = new HashMap<>();


    private Map<Integer, LocalDateTime> otkazaneRezervacije = new HashMap<>();
    */




    /*

    public Map<Integer, Aranzman> getAranzmaniMapu(){
        return aranzmaniPoOznaci;
    }

    public void dodajAranzman(Aranzman aranzman){
        int oznaka = aranzman.getOznaka();
        aranzmaniPoOznaci.put(oznaka, aranzman);
        rezervacijePoAranzmanu.computeIfAbsent(oznaka, k  -> new ArrayList<>());
    }

    public void dodajInicijalnuRezervaciju(Rezervacija rezervacija){
        inicijalneRezervacije.add(rezervacija);
    }

    public List<Rezervacija> getInicijalneRezervacije(){
        return inicijalneRezervacije;
    }

    public void obrisiInicijalneRezervacije(){
        inicijalneRezervacije.clear();
    }

    public List<Rezervacija> getRezervacijeZaAranzman(int oznakaAranzmana, Set<RezervacijaStatus> statusi){
        List<Integer> rezervacijeId = rezervacijePoAranzmanu.getOrDefault(oznakaAranzmana, new ArrayList<>());
        List<Rezervacija> rezervacije = new ArrayList<>();
        for(Integer id : rezervacijeId){
            Rezervacija r = rezervacijePoId.get(id);
            if(statusi == null || statusi.isEmpty() || statusi.contains(r.getStatus())){
                rezervacije.add(r);
            }
        }
        if(statusi.contains(RezervacijaStatus.OTKAZANA)){
            for(Integer id : otkazaneRezervacije.keySet()){
                Rezervacija r = rezervacijePoId.get(id);
                if(r.getOznakaAranzmana() == oznakaAranzmana){
                    rezervacije.add(r);
                }
            }
        }
        return rezervacije;
    }

    public LocalDateTime dohvatiVrijemeOtkazivanjaRezervacije(int rezervacijaId){
        return otkazaneRezervacije.get(rezervacijaId);
    }

    public void dodajOtkazanuRezervaciju(Rezervacija rezervacija){
        otkazaneRezervacije.put(rezervacija.getId(), LocalDateTime.now());
    }



    public boolean postojiRezervacijaKorisnikaStatus(Rezervacija rezervacija, RezervacijaStatus statusZaProvjeru){
        int oznaka = rezervacija.getOznakaAranzmana();
        List<Integer> lista = rezervacijePoAranzmanu.get(oznaka);
        if(statusZaProvjeru.equals(RezervacijaStatus.PRIMLJENA)){
            int brojPonavljanja = 0;
            for(Integer id : lista){
                Rezervacija r = rezervacijePoId.get(id);
                if(r.getId() == rezervacija.getId()){
                    continue;
                }
                if(r.getPunoIme().equals(rezervacija.getPunoIme()) && r.getStatus() == statusZaProvjeru){
                    brojPonavljanja++;
                }
            }
            return brojPonavljanja >= 1;
        }else{
            for(Integer id : lista){
                Rezervacija r = rezervacijePoId.get(id);
                if(r.getPunoIme().equals(rezervacija.getPunoIme()) && r.getStatus() == statusZaProvjeru){
                    return true;
                }
            }
        }
        return false;
    }

    public List<Integer> dohvatiPreklapanjaStatus(Rezervacija rezervacija, RezervacijaStatus statusZaProvjeru){
        String punoIme = rezervacija.getPunoIme();
        Aranzman trazeniAranzman = aranzmaniPoOznaci.get(rezervacija.getOznakaAranzmana());

        List<Integer> rezervacijeKorisnikaId = rezervacijePoImenu.getOrDefault(punoIme, new ArrayList<>());

        List<Rezervacija> aktivneRezervacije = new ArrayList<>();
        for(Integer id : rezervacijeKorisnikaId) {
            if (rezervacijePoId.get(id).getStatus() == statusZaProvjeru) {
                aktivneRezervacije.add(rezervacijePoId.get(id));
            }
        }

        List<Integer> preklapajuciAranzmaniId = new ArrayList<>();
        for(Rezervacija r : aktivneRezervacije){
            Aranzman aranzmanUsporedba = aranzmaniPoOznaci.get(r.getOznakaAranzmana());
            if(trazeniAranzman.getOznaka() == aranzmanUsporedba.getOznaka()){
                continue;
            }
            if(provjeriPreklapanjeDatuma(trazeniAranzman, aranzmanUsporedba)){
                preklapajuciAranzmaniId.add(r.getOznakaAranzmana());
            }
        }
        return preklapajuciAranzmaniId;
    }



    public void prebaciURedOtkazanih(Rezervacija rezervacija){
        List<Integer> lista = rezervacijePoAranzmanu.get(rezervacija.getOznakaAranzmana());
        lista.remove(Integer.valueOf(rezervacija.getId()));
        rezervacija.setStatus(RezervacijaStatus.OTKAZANA);
        dodajOtkazanuRezervaciju(rezervacija);
    }

    public List<Rezervacija> dohvatiRezervacijePoImenu(String punoIme){
        List<Integer> rezervacijeId = rezervacijePoImenu.getOrDefault(punoIme, new ArrayList<>());
        List<Rezervacija> rezervacije = new ArrayList<>();
        for(Integer id : rezervacijeId){
            rezervacije.add(rezervacijePoId.get(id));
        }
        return rezervacije;
    }



    public void obrisiRezervacijuIzAranzmana(Rezervacija rezervacija){
        List<Integer> lista = rezervacijePoAranzmanu.get(rezervacija.getOznakaAranzmana());
        lista.remove(Integer.valueOf(rezervacija.getId()));
        rezervacijePoId.remove(rezervacija.getId());
        List<Integer> listaImena = rezervacijePoImenu.get(rezervacija.getPunoIme());
        listaImena.remove(Integer.valueOf(rezervacija.getId()));
    }
    */
}
