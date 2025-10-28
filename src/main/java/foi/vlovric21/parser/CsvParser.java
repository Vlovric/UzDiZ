package foi.vlovric21.parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CsvParser {
    private static final List<String> zaglavljeAranzman = List.of(
            "Oznaka",
            "Naziv",
            "Program",
            "Početni datum",
            "Završni datum",
            "Vrijeme kretanja",
            "Vrijeme povratka",
            "Cijena",
            "Min broj putnika",
            "Maks broj putnika",
            "Broj noćenja",
            "Doplata za jednokrevetnu sobu",
            "Prijevoz",
            "Broj doručka",
            "Broj ručkova",
            "Broj večera"
    );
    private static final List<String> zaglavljeRezervacija = List.of(
            "Ime",
            "Prezime",
            "Oznaka aranžmana",
            "Datum i  vrijeme"
    );

    public boolean validirajZaglavlje(List<String> ocekivanoZaglavlje, String redZaglavlja){
        List<String> dobivenoZaglavlje = Arrays.stream(redZaglavlja.split(","))
                .map(String::trim)
                .toList();

        return dobivenoZaglavlje.containsAll(ocekivanoZaglavlje);
    }

    public List<String> parsirajRed(String red){

    }

    private boolean validirajRed(List<String> red, CsvTip tip){
        return true;
    }

    public boolean parsirajCsv(String datoteka, CsvTip tip){
        List<String> zaglavlje = tip == CsvTip.ARANZMAN ? zaglavljeAranzman : zaglavljeRezervacija;

        try(BufferedReader br = new BufferedReader(new FileReader(datoteka))){

            String red;
            red = br.readLine();

            if(red == null){
                System.out.println("Datoteka je prazna: " + datoteka);
                return false;
            }
            if(!validirajZaglavlje(zaglavlje, red)){
                System.out.println("Neispravno zaglavlje u datoteci: " + datoteka);
                return false;
            }

            int redniBroj = 0;
            while((red = br.readLine()) != null){
                redniBroj++;
                red = red.trim();
                if(red.isEmpty() || red.startsWith("#")){
                    continue;
                }
                List<String> polja = parsirajRed(red);

            }
        }catch(IOException ex){
            System.out.println("Greška kod čitanja datoteke: " + datoteka);
            return false;
        }
    }
}
