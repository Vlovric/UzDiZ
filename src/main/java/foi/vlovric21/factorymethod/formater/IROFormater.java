package foi.vlovric21.factorymethod.formater;

import foi.vlovric21.objekti.Aranzman;
import foi.vlovric21.objekti.Rezervacija;
import foi.vlovric21.singleton.RepozitorijPodataka;

import java.time.LocalDateTime;
import java.util.List;

public class IROFormater extends Formater {

    @Override
    public void formatiraj(Object obj) {
        List<Rezervacija> rezervacije = (List<Rezervacija>) obj;
        RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
        System.out.printf("%-20s %-20s %-30s %-15s%n",
                "Datum i vrijeme", "Oznaka aranžmana", "Naziv aranžmana", "Vrsta");
        System.out.println("-".repeat(85));

        for(Rezervacija r : rezervacije){
            Aranzman aranzman = repozitorij.getAranzmanPoOznaci(r.getOznakaAranzmana());
            String nazivAranzmana = aranzman != null ? aranzman.getNaziv() : "";

            LocalDateTime dt = datumFormater.parseDatumIVrijeme(r.getDatumIVrijeme());
            String formatiraniDatumVrijeme = datumFormater.formatirajDatumVrijemeIspis(dt);

            System.out.printf("%-20s %-20d %-30s %-15s%n",
                    formatiraniDatumVrijeme,
                    r.getOznakaAranzmana(),
                    skratiTekst(nazivAranzmana, 30),
                    pretvoriStatusUVrstu(r.getStatus()));
        }
        System.out.println();
    }
}
