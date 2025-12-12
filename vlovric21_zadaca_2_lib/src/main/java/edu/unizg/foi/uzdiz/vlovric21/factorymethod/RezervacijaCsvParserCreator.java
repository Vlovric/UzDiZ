package edu.unizg.foi.uzdiz.vlovric21.factorymethod;

public class RezervacijaCsvParserCreator extends CsvParserCreator {

    @Override
    protected CsvParser stvoriParser(){
        return new RezervacijaCsvParser();
    }
}
