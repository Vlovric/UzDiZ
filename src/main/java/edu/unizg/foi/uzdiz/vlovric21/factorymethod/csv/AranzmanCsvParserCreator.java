package edu.unizg.foi.uzdiz.vlovric21.factorymethod.csv;

import edu.unizg.foi.uzdiz.vlovric21.parser.CsvParser;

public class AranzmanCsvParserCreator extends CsvParserCreator {

    @Override
    protected CsvParser stvoriParser(){
        return new AranzmanCsvParser();
    }
}
