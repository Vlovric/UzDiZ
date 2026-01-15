package edu.unizg.foi.uzdiz.vlovric21.factorymethod;

public class AranzmanCsvParserCreator extends CsvParserCreator {

    @Override
    protected CsvParser stvoriParser(){
        return new AranzmanCsvParser();
    }
}
