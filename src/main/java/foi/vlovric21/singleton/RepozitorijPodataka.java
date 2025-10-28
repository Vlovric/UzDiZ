package foi.vlovric21.singleton;

import foi.vlovric21.objekti.Aranzman;
import foi.vlovric21.objekti.Rezervacija;

import java.util.List;

public class RepozitorijPodataka {
    private static RepozitorijPodataka instance = new RepozitorijPodataka();

    private List<Aranzman> aranzmani;
    private List<Rezervacija> rezervacije;

    private RepozitorijPodataka() {}

    public static RepozitorijPodataka getInstance() {
        return instance;
    }
}
