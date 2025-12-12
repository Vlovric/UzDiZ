package edu.unizg.foi.uzdiz.vlovric21.factorymethod.csv;

import java.util.List;

public class AranzmanCsvParser{

}
/*
public class AranzmanCsvParser extends CsvParser {

    if (RepozitorijPodataka.getInstance().getAranzmaniMapu().containsKey(Integer.parseInt(red.get(0)))) {
        return "Aranžman s oznakom" + red.get(0) + "već postoji";
    }


    protected Integer parseOpcionalniInt(String vrijednost){
        String skracen = vrijednost.trim();
        return skracen.isEmpty() ? null : Integer.valueOf(skracen);
    }

    @Override
    protected void stvoriObjekt(List<String> polja) {
        int oznaka = Integer.parseInt(polja.get(0));
        String naziv = polja.get(1);
        String program = polja.get(2);
        String pocetniDatum = polja.get(3);
        String zavrsniDatum = polja.get(4);
        String vrijemeKretanja = polja.get(5);
        String vrijemePovratka = polja.get(6);
        int cijena = Integer.parseInt(polja.get(7));
        int minBrojPutnika = Integer.parseInt(polja.get(8));
        int maxBrojPutnika = Integer.parseInt(polja.get(9));
        String prijevoz = polja.get(12).trim();

        Integer brojNocenja = parseOpcionalniInt(polja.get(10));
        Integer doplata = parseOpcionalniInt(polja.get(11));
        Integer brojDorucka = parseOpcionalniInt(polja.get(13));
        Integer brojRuckova = parseOpcionalniInt(polja.get(14));
        Integer brojVecera = parseOpcionalniInt(polja.get(15));

        AranzmanDirector direktor = new AranzmanDirector();
        Aranzman ar = direktor.stvoriKompletanAranzman(
                oznaka,
                naziv,
                program,
                pocetniDatum,
                zavrsniDatum,
                cijena,
                minBrojPutnika,
                maxBrojPutnika,
                brojNocenja,
                vrijemeKretanja,
                vrijemePovratka,
                doplata,
                prijevoz,
                brojDorucka,
                brojRuckova,
                brojVecera
        );
        RepozitorijPodataka.getInstance().dodajAranzman(ar);
    }
}
*/