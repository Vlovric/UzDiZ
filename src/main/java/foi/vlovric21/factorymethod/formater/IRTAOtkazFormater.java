package foi.vlovric21.factorymethod.formater;

import foi.vlovric21.objekti.Rezervacija;
import foi.vlovric21.objekti.RezervacijaStatus;
import foi.vlovric21.pomocne.DatumFormater;
import foi.vlovric21.singleton.RepozitorijPodataka;

import java.time.LocalDateTime;
import java.util.List;

public class IRTAOtkazFormater extends Formater{
    private RepozitorijPodataka repozitorij = RepozitorijPodataka.getInstance();
    private DatumFormater datumFormater = new DatumFormater();

    @Override
    public void formatiraj(Object obj){
        List<Rezervacija> rezervacije = (List<Rezervacija>) obj;
        System.out.printf("%-20s %-20s %-20s %-15s %-25s%n",
                "Ime", "Prezime", "Datum i vrijeme", "Vrsta", "Datum i vrijeme otkaza");
        System.out.println("-".repeat(100));

        for(Rezervacija r : rezervacije){
            String datumOtkaza = "";
            if(r.getStatus() == RezervacijaStatus.OTKAZANA){
                LocalDateTime dtOtkaza = repozitorij.dohvatiVrijemeOtkazivanjaRezervacije(r.getId());
                datumOtkaza = dtOtkaza != null ? datumFormater.formatirajDatumVrijeme(dtOtkaza) : "";
            }

            System.out.printf("%-20s %-20s %-20s %-15s %-25s%n",
                    skratiTekst(r.getIme(), 20),
                    skratiTekst(r.getPrezime(), 20),
                    r.getDatumIVrijeme(),
                    pretvoriStatusUVrstu(r.getStatus()),
                    datumOtkaza);
        }
        System.out.println();
    }
}
