package foi.vlovric21.factorymethod.formater;

import foi.vlovric21.objekti.Aranzman;

import java.util.List;

public class ITAKFormater extends Formater{

    @Override
    public void formatiraj(Object obj){
        List<Aranzman> aranzmani = (List<Aranzman>) obj;
        System.out.printf("%-8s %-30s %-18s %-18s %-18s %-18s %-10s %-18s %-18s%n",
                "Oznaka", "Naziv", "Početni datum", "Završni datum", "Vrijeme kretanja",
                "Vrijeme povratka", "Cijena", "Min broj putnika", "Maks broj putnika");
        System.out.println("-".repeat(160));

        for(Aranzman a : aranzmani) {
            System.out.printf("%-8d %-30s %-18s %-18s %-18s %-18s %-10d %-18d %-18d%n",
                    a.getOznaka(),
                    skratiTekst(a.getNaziv(), 30),
                    a.getPocetniDatum(),
                    a.getZavrsniDatum(),
                    a.getVrijemeKretanja() != null ? a.getVrijemeKretanja() : "",
                    a.getVrijemePovratka() != null ? a.getVrijemePovratka() : "",
                    a.getCijena(),
                    a.getMinBrojPutnika(),
                    a.getMaxBrojPutnika());
        }
        System.out.println();
    }
}
