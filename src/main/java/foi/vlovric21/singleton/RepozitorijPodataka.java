package foi.vlovric21.singleton;

import foi.vlovric21.objekti.Aranzman;
import foi.vlovric21.objekti.Rezervacija;
import foi.vlovric21.objekti.RezervacijaStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.*;

public class RepozitorijPodataka {
    private static RepozitorijPodataka instance = new RepozitorijPodataka();
    private static int idBrojacRezervacija = 1;

    private Map<Integer, Aranzman> aranzmaniPoOznaci = new HashMap<>();
    private Map<Integer, List<Integer>> rezervacijePoAranzmanu = new HashMap<>();
    private Map<Integer, Rezervacija> rezervacijePoId = new HashMap<>();

    private Map<String, List<Integer>> rezervacijePoImenu = new HashMap<>();
    private Map<Integer, LocalDateTime> otkazaneRezervacije = new HashMap<>();

    private static final DateTimeFormatter parser = new DateTimeFormatterBuilder()
            .appendPattern("d.MM.yyyy H:mm")
            .optionalStart().appendPattern(":ss").optionalEnd()
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    private static final DateTimeFormatter datumParser = new DateTimeFormatterBuilder()
            .appendPattern("d.M.yyyy")
            .appendLiteral(".")
            .toFormatter();

    private RepozitorijPodataka() {}

    public static RepozitorijPodataka getInstance() {
        return instance;
    }

    public int generirajIdZaRezervaciju(){
        return idBrojacRezervacija++;
    }

    public List<Aranzman> getAranzmani(){
        return new ArrayList<>(aranzmaniPoOznaci.values());
    }

    public Aranzman getAranzmanPoOznaci(int oznaka){
        return aranzmaniPoOznaci.get(oznaka);
    }

    public Map<Integer, Aranzman> getAranzmaniMapu(){
        return aranzmaniPoOznaci;
    }

    public void dodajAranzman(Aranzman aranzman){
        int oznaka = aranzman.getOznaka();
        aranzmaniPoOznaci.put(oznaka, aranzman);
        rezervacijePoAranzmanu.computeIfAbsent(oznaka, k  -> new ArrayList<>());
    }

    public List<Rezervacija> getRezervacijeLista(){
        return new ArrayList<>(rezervacijePoId.values());
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
        return rezervacije;
    }

    public LocalDateTime dohvatiVrijemeOtkazivanjaRezervacije(int rezervacijaId){
        return otkazaneRezervacije.get(rezervacijaId);
    }

    public void dodajOtkazanuRezervaciju(Rezervacija rezervacija){
        otkazaneRezervacije.put(rezervacija.getId(), LocalDateTime.now());
    }

    public void dodajRezervaciju(Rezervacija rezervacija){
        int oznaka = rezervacija.getOznakaAranzmana();
        int id = generirajIdZaRezervaciju();
        rezervacija.setId(id);

        List<Integer> lista = rezervacijePoAranzmanu.computeIfAbsent(oznaka, k -> new ArrayList<>());
        LocalDateTime dt = parseDatumIVrijeme(rezervacija.getDatumIVrijeme());

        int index = 0; //TODO jel mi ne treba ovdje check ak mi je lista prazna??
        for(Integer rId : lista){
            Rezervacija r = rezervacijePoId.get(rId);
            LocalDateTime rezervacijaDT = parseDatumIVrijeme(r.getDatumIVrijeme());
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

    private LocalDateTime parseDatumIVrijeme(String dv){
        try{
            return LocalDateTime.parse(dv, parser);
        }catch(DateTimeParseException ex){
            return LocalDateTime.MAX;
        }
    }

    private LocalDate parseDatum(String d){
        try{
            return LocalDate.parse(d, datumParser);
        }catch(DateTimeParseException ex){
            return null;
        }
    }

    public boolean postojiRezervacijaKorisnikaStatus(Rezervacija rezervacija, RezervacijaStatus statusZaProvjeru){
        int oznaka = rezervacija.getOznakaAranzmana();
        List<Integer> lista = rezervacijePoAranzmanu.get(oznaka);
        for(Integer id : lista){
            Rezervacija r = rezervacijePoId.get(id);
            if(r.getPunoIme().equals(rezervacija.getPunoIme()) && r.getStatus() == statusZaProvjeru){
                return true;
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

        List<Integer> preklapajuceRezervacijeId = new ArrayList<>();
        for(Rezervacija r : aktivneRezervacije){ //TODO: ovo moze bit empty tak da jel problem?
            Aranzman aranzmanUsporedba = aranzmaniPoOznaci.get(r.getOznakaAranzmana());
            if(trazeniAranzman.getOznaka() == aranzmanUsporedba.getOznaka()){
                continue;
            }
            if(provjeriPreklapanjeDatuma(trazeniAranzman, aranzmanUsporedba)){
                preklapajuceRezervacijeId.add(r.getId());
            }
        }
        return preklapajuceRezervacijeId;
    }

    private boolean provjeriPreklapanjeDatuma(Aranzman a1, Aranzman a2){
        LocalDate a1Pocetak = parseDatum(a1.getPocetniDatum());
        LocalDate a1Kraj = parseDatum(a1.getZavrsniDatum());
        LocalDate a2Pocetak = parseDatum(a2.getPocetniDatum());
        LocalDate a2Kraj = parseDatum(a2.getZavrsniDatum());

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

    private void obrisiRezervacijuPremaId(Rezervacija rezervacija){
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

        LocalDate pocDatum = parseDatum(pocetniDatum);
        LocalDate zavDatum = parseDatum(zavrsniDatum);

        for(Aranzman a : aranzmaniPoOznaci.values()){
            LocalDate aPocDatum = parseDatum(a.getPocetniDatum());
            LocalDate aZavDatum = parseDatum(a.getZavrsniDatum());

            if(aPocDatum.isBefore(pocDatum) || aZavDatum.isAfter(zavDatum)){
                continue;
            }
            rezultat.add(a);
        }
        return rezultat;
    }
}
