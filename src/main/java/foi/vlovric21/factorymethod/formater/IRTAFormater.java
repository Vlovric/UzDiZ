package foi.vlovric21.factorymethod.formater;

import foi.vlovric21.objekti.Rezervacija;

import java.time.LocalDateTime;
import java.util.List;

public class IRTAFormater extends Formater {

    @Override
    public void formatiraj(Object obj) {
        List<Rezervacija> rezervacije = (List<Rezervacija>) obj;
        System.out.printf("%-20s %-20s %-20s %-15s%n",
                "Ime", "Prezime", "Datum i vrijeme", "Vrsta");
        System.out.println("-".repeat(75));

        for(Rezervacija r : rezervacije) {
            LocalDateTime dt = datumFormater.parseDatumIVrijeme(r.getDatumIVrijeme());
            String formatiraniDatumVrijeme = datumFormater.formatirajDatumVrijemeIspis(dt);

            System.out.printf("%-20s %-20s %-20s %-15s%n",
                    skratiTekst(r.getIme(), 20),
                    skratiTekst(r.getPrezime(), 20),
                    formatiraniDatumVrijeme,
                    pretvoriStatusUVrstu(r.getStatus()));
        }
        System.out.println();
    }


}
