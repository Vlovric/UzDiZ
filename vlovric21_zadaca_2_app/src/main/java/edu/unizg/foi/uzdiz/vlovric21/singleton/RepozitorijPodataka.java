package edu.unizg.foi.uzdiz.vlovric21.singleton;

import edu.unizg.foi.uzdiz.vlovric21.composite.Aranzman;
import edu.unizg.foi.uzdiz.vlovric21.composite.Rezervacija;
import edu.unizg.foi.uzdiz.vlovric21.composite.RezervacijaStatus;
import edu.unizg.foi.uzdiz.vlovric21.pomocne.DatumFormater;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class RepozitorijPodataka {
    private static RepozitorijPodataka instance = new RepozitorijPodataka();
    private static int idBrojacRezervacija = 1;

    private List<Aranzman> aranzmani = new ArrayList<>();

    private List<Rezervacija> inicijalneRezervacije = new ArrayList<>();



    public Aranzman getAranzman(int oznaka){
        for(Aranzman a : aranzmani){
            if(a.getOznaka() == oznaka){
                return a;
            }
        }
        return null;
    }

    public void dodajAranzman(Aranzman ar){
        aranzmani.add(ar);
    }

    public void dodajRezervaciju(Rezervacija rezervacija){
        int oznaka = rezervacija.getOznakaAranzmana();
        int id = generirajIdZaRezervaciju();
        rezervacija.setId(id);

        Aranzman aranzman = getAranzman(oznaka);
        aranzman.dodajRezervaciju(rezervacija); //TODO moram resortirat i re-dat statuse prema novom sortu pri svakom unosu
    }

    /*
    public void dodajRezervaciju(Rezervacija rezervacija){
        int oznaka = rezervacija.getOznakaAranzmana();
        int id = generirajIdZaRezervaciju();
        rezervacija.setId(id);

        List<Integer> lista = rezervacijePoAranzmanu.computeIfAbsent(oznaka, k -> new ArrayList<>());
        LocalDateTime dt = datumFormater.parseDatumIVrijeme(rezervacija.getDatumIVrijeme());

        int index = 0;
        for(Integer rId : lista){
            Rezervacija r = rezervacijePoId.get(rId);
            LocalDateTime rezervacijaDT = datumFormater.parseDatumIVrijeme(r.getDatumIVrijeme());
            if(dt.isAfter(rezervacijaDT)){
                index++;
            }else{
                break;
            }
        }
        lista.add(index, id);
        rezervacijePoImenu.computeIfAbsent(rezervacija.getPunoIme(), k -> new ArrayList<>()).add(id);
        rezervacijePoId.put(id, rezervacija);
    }
     */


    // Nekoristeni getteri i setteri

    public List<Aranzman> getAranzmani(){
        return aranzmani;
    }

    public void setAranzmani(List<Aranzman> aranzmani){
        this.aranzmani = aranzmani;
    }


    // ------------------------ Staro ------------------------


    private Map<Integer, Aranzman> aranzmaniPoOznaci = new HashMap<>();
    private Map<Integer, List<Integer>> rezervacijePoAranzmanu = new HashMap<>();
    private Map<Integer, Rezervacija> rezervacijePoId = new HashMap<>();

    private Map<String, List<Integer>> rezervacijePoImenu = new HashMap<>();
    private Map<Integer, LocalDateTime> otkazaneRezervacije = new HashMap<>();

    private DatumFormater datumFormater = new DatumFormater();

    private RepozitorijPodataka() {}

    public static RepozitorijPodataka getInstance() {
        return instance;
    }

    public int generirajIdZaRezervaciju(){
        return idBrojacRezervacija++;
    }

    public Aranzman getAranzmanPoOznaci(int oznaka){
        return aranzmaniPoOznaci.get(oznaka);
    }

    public Map<Integer, Aranzman> getAranzmaniMapu(){
        return aranzmaniPoOznaci;
    }

    /*
    public void dodajAranzman(Aranzman aranzman){
        int oznaka = aranzman.getOznaka();
        aranzmaniPoOznaci.put(oznaka, aranzman);
        rezervacijePoAranzmanu.computeIfAbsent(oznaka, k  -> new ArrayList<>());
    }
     */

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

    public List<Aranzman> dohvatiAranzmanRazdoblje(String pocetniDatum, String zavrsniDatum){
        List<Aranzman> rezultat = new ArrayList<>();

        if(pocetniDatum == null && zavrsniDatum == null){
            for(Aranzman a : aranzmaniPoOznaci.values()){
                rezultat.add(a);
            }
            return rezultat;
        }

        LocalDate pocDatum = datumFormater.parseDatum(pocetniDatum);
        LocalDate zavDatum = datumFormater.parseDatum(zavrsniDatum);

        for(Aranzman a : aranzmaniPoOznaci.values()){
            LocalDate aPocDatum = datumFormater.parseDatum(a.getPocetniDatum());
            LocalDate aZavDatum = datumFormater.parseDatum(a.getZavrsniDatum());

            if(aPocDatum.isBefore(pocDatum) || aZavDatum.isAfter(zavDatum)){
                continue;
            }
            rezultat.add(a);
        }
        return rezultat;
    }

    public void obrisiRezervacijuIzAranzmana(Rezervacija rezervacija){
        List<Integer> lista = rezervacijePoAranzmanu.get(rezervacija.getOznakaAranzmana());
        lista.remove(Integer.valueOf(rezervacija.getId()));
        rezervacijePoId.remove(rezervacija.getId());
        List<Integer> listaImena = rezervacijePoImenu.get(rezervacija.getPunoIme());
        listaImena.remove(Integer.valueOf(rezervacija.getId()));
    }
}
