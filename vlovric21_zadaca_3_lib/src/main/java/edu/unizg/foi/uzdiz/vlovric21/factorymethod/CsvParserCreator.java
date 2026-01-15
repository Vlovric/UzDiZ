package edu.unizg.foi.uzdiz.vlovric21.factorymethod;

public abstract class CsvParserCreator {

    protected abstract CsvParser stvoriParser();

    public void parsirajCsv(String datoteka){
        CsvParser parser = stvoriParser();
        parser.parsirajCsv(datoteka);
    }
}