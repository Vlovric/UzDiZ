package foi.vlovric21.parser;

import foi.vlovric21.builder.AranzmanDirector;
import foi.vlovric21.objekti.Aranzman;
import foi.vlovric21.singleton.RepozitorijPodataka;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class CsvParser {
    protected abstract List<String> dohvatiZaglavlje();
    protected abstract List<Integer> dohvatiObaveznaZaglavlja();
    protected abstract String validirajRed(List<String> red);
    protected abstract void stvoriObjekt(List<String> polja);

    protected static final String regexDatum = "\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\.";
    protected static final String regexDatumVrijeme = "\\d{1,2}\\.\\d{1,2}\\.\\d{4}\\.? \\d{1,2}:\\d{2}(:\\d{2})?";

    public boolean parsirajCsv(String datoteka){
        List<String> polja;
        boolean imaZaglavlje;

        try(BufferedReader br = new BufferedReader(new FileReader(datoteka))){
            String red = makniBOM(br.readLine());

            if(red == null){
                System.out.println("Datoteka je prazna: " + datoteka);
                return false;
            }
            imaZaglavlje = provjeriZaglavlje(red);

        }catch(IOException ex){
            System.out.println("Greška kod čitanja datoteke: " + datoteka);
            return false;
        }

        try(BufferedReader br = new BufferedReader(new FileReader(datoteka))){

            String red;
            int redniBroj = 0;

            while((red = br.readLine()) != null){
                redniBroj++;

                if(imaZaglavlje){
                    imaZaglavlje = false;
                    continue;
                }

                red = red.trim();
                if(red.isEmpty() || red.startsWith("#")){
                    continue;
                }

                polja = parsirajRed(red);
                String greska = validirajRed(polja);
                if(!greska.isEmpty()){
                    ispisiGresku(redniBroj, datoteka, greska, red, polja);
                    continue;
                }
                stvoriObjekt(polja);
            }
        }catch(IOException ex){
            System.out.println("Greška kod čitanja datoteke: " + datoteka);
            return false;
        }
        return true;
    }

    private String makniBOM(String red){
        if(red != null && red.startsWith("\uFEFF")){
            return red.substring(1);
        }
        return red;
    }

    private boolean provjeriZaglavlje(String red){
        List<String> polja = parsirajRed(red);
        List<String> ocekivanoZaglavlje = dohvatiZaglavlje();
        List<Integer> obavezniIndeksi = dohvatiObaveznaZaglavlja();
        int prviObavezniIndeks = obavezniIndeksi.get(0);

        if(polja.isEmpty()){
            return false;
        }
        String dobiveno = polja.get(prviObavezniIndeks).trim();
        String ocekivano = ocekivanoZaglavlje.get(prviObavezniIndeks).trim();

        return dobiveno.equals(ocekivano);
        }

    private List<String> parsirajRed(String red){
        List<String> polja = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean unutarNavodnika = false;

        for(int i=0; i<red.length(); i++){
            char znak = red.charAt(i);

            if(znak == '"'){
                unutarNavodnika = !unutarNavodnika;
            }else if(znak == ',' && !unutarNavodnika){
                polja.add(sb.toString().trim());
                sb.setLength(0);
            }else{
                sb.append(znak);
            }
        }

        polja.add(sb.toString().trim());
        return polja;
    }

    private void ispisiGresku(int redniBroj, String datoteka, String greska, String red, List<String> polja){
        System.out.println("---------------------------------");
        System.out.println("Neispravan red: " + redniBroj + " u datoteci: " + datoteka);
        System.out.println("Razlog: " + greska);
        System.out.println("Red: " + red);
        System.out.println("---------------------------------");
    }

    protected void parseAkoNijePrazno(String vrijednost) throws NumberFormatException {
        if(!vrijednost.isEmpty()){
            Integer.parseInt(vrijednost);
        }
    }

    protected Integer parseOpcionalniInt(String vrijednost){
        String skracen = vrijednost.trim();
        return skracen.isEmpty() ? null : Integer.valueOf(skracen);
    }
}
