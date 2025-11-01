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
    private Map<Integer, List<Rezervacija>> rezervacijePoAranzmanu = new HashMap<>();

    private Map<Integer, Rezervacija> rezervacijePoId = new HashMap<>();
    private Map<String, List<Integer>> rezervacijePoImenu = new HashMap<>();

    private List<Rezervacija> otkazaneRezervacije = new ArrayList<>();
    private List<Rezervacija> izbaceneRezervacije = new ArrayList<>();

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
        List<Rezervacija> sveRezervacije = new ArrayList<>();
        for(List<Rezervacija> lista : rezervacijePoAranzmanu.values()){
            sveRezervacije.addAll(lista);
        }
        return sveRezervacije;
    }

    public List<Rezervacija> getRezervacijeZaAranzman(int oznakaAranzmana){
        return rezervacijePoAranzmanu.getOrDefault(oznakaAranzmana, new ArrayList<>());
    }

    public void dodajRezervaciju(Rezervacija rezervacija){
        int oznaka = rezervacija.getOznakaAranzmana();

        List<Rezervacija> lista = rezervacijePoAranzmanu.computeIfAbsent(oznaka, k -> new ArrayList<>());
        LocalDateTime dt = parseDatumIVrijeme(rezervacija.getDatumIVrijeme());

        int index = 0; //TODO jel mi ne treba ovdje check ak mi je lista prazna??
        for(Rezervacija r : lista){
            LocalDateTime rezervacijaDT = parseDatumIVrijeme(r.getDatumIVrijeme());
            if(dt.isAfter(rezervacijaDT)){
                index++;
            }else{
                break;
            }
        }
        lista.add(index, rezervacija);
        rezervacijePoImenu.computeIfAbsent(rezervacija.getPunoIme(), k -> new ArrayList<>()).add(rezervacija.getId());
        rezervacijePoId.computeIfAbsent(rezervacija.getId(), k -> rezervacija);
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

    public boolean postojiAktivnaRezervacija(Rezervacija rezervacija, RezervacijaStatus statusZaProvjeru){
        int oznaka = rezervacija.getOznakaAranzmana();
        List<Rezervacija> lista = rezervacijePoAranzmanu.get(oznaka);
        for(Rezervacija r : lista){
            if(r.getPunoIme().equals(rezervacija.getPunoIme()) && r.getStatus() == statusZaProvjeru){
                return true;
            }
        }
        return false;
    }

    public boolean postojiAktivnaPreklapanje(Rezervacija rezervacija){
        String punoIme = rezervacija.getPunoIme();
        Aranzman trazeniAranzman = aranzmaniPoOznaci.get(rezervacija.getOznakaAranzmana());

        List<Integer> rezervacijeKorisnikaId = rezervacijePoImenu.getOrDefault(punoIme, new ArrayList<>());

        List<Rezervacija> aktivneRezervacije = new ArrayList<>();
        for(Integer id : rezervacijeKorisnikaId) {
            if (rezervacijePoId.get(id).getStatus() == RezervacijaStatus.AKTIVNA) {
                aktivneRezervacije.add(rezervacijePoId.get(id));
            }
        }

        for(Rezervacija r : aktivneRezervacije){
            Aranzman aranzmanUsporedba = aranzmaniPoOznaci.get(r.getOznakaAranzmana());
            if(trazeniAranzman.getOznaka() == aranzmanUsporedba.getOznaka()){
                continue;
            }
            if(provjeriPreklapanjeDatuma(trazeniAranzman, aranzmanUsporedba)){
                return true;
            }
        }
        return false;
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

    public void ukloniRezervaciju(Rezervacija rezervacija){
        int oznaka = rezervacija.getOznakaAranzmana();
        List<Rezervacija> lista = rezervacijePoAranzmanu.get(oznaka);
        if(lista != null){
            lista.remove(rezervacija);
        }
        izbaceneRezervacije.add(rezervacija);
    }
}
