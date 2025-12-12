package edu.unizg.foi.uzdiz.vlovric21.facade;

import edu.unizg.foi.uzdiz.vlovric21.factorymethod.AranzmanCsvParserCreator;
import edu.unizg.foi.uzdiz.vlovric21.factorymethod.CsvParserCreator;
import edu.unizg.foi.uzdiz.vlovric21.factorymethod.RezervacijaCsvParserCreator;
import edu.unizg.foi.uzdiz.vlovric21.singleton.ParsiranjePomagac;

import java.util.List;
import java.util.Map;

public class CsvParsiranjeFacade {

    public static List<Map<String, String>> ucitajAranzmane(String putanja){
        ParsiranjePomagac.getInstanca().getIspravniRedoviAranzman().clear();
        CsvParserCreator aranzmanParser = new AranzmanCsvParserCreator();
        aranzmanParser.parsirajCsv(putanja);
        return ParsiranjePomagac.getInstanca().getIspravniRedoviAranzman();
    }

    public static List<Map<String, String>> ucitajRezervacije(String putanja){
        ParsiranjePomagac.getInstanca().getIspravniRedoviRezervacija().clear();
        CsvParserCreator rezervacijaParser = new RezervacijaCsvParserCreator();
        rezervacijaParser.parsirajCsv(putanja);
        return ParsiranjePomagac.getInstanca().getIspravniRedoviRezervacija();
    }

}
