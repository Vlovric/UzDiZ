package foi.vlovric21.singleton;

import foi.vlovric21.objekti.Aranzman;
import foi.vlovric21.objekti.Rezervacija;

import java.util.ArrayList;
import java.util.List;

public class RepozitorijPodataka {
    private static RepozitorijPodataka instance = new RepozitorijPodataka();

    private List<Aranzman> aranzmani = new ArrayList<>();
    private List<Rezervacija> rezervacije = new ArrayList<>();
    private List<Rezervacija> otkazaneRezervacije = new ArrayList<>();
    //imat mapu koja mapira rezervacije na aranzmane da bi im lakse pristupo?

    private RepozitorijPodataka() {}

    public static RepozitorijPodataka getInstance() {
        return instance;
    }

    public List<Aranzman> getAranzmani() {
        return aranzmani;
    }

    public void setAranzmani(List<Aranzman> aranzmani) {
        this.aranzmani = aranzmani;
    }

    public void dodajAranzman(Aranzman aranzman) {
        this.aranzmani.add(aranzman);
    }
}
