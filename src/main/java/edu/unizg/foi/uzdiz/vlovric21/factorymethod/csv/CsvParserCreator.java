package edu.unizg.foi.uzdiz.vlovric21.factorymethod.csv;

import edu.unizg.foi.uzdiz.vlovric21.parser.CsvParser;

public abstract class CsvParserCreator {

    protected abstract CsvParser stvoriParser();

    public void parsirajCsv(String datoteka){
        CsvParser parser = stvoriParser();
        parser.parsirajCsv(datoteka);
    }
}
