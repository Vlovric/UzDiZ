package edu.unizg.foi.uzdiz.vlovric21.composite;

import edu.unizg.foi.uzdiz.vlovric21.pomocne.DatumFormater;
import edu.unizg.foi.uzdiz.vlovric21.singleton.RepozitorijPodataka;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaNova;
import edu.unizg.foi.uzdiz.vlovric21.state_rezervacija.RezervacijaOtkazana;

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

    public void dodajAranzmanUKronologiju(int oznaka){
        aranzmaniKronologija.add(oznaka);
    }
}
