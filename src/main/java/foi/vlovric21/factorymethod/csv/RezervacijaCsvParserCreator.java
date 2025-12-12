package foi.vlovric21.factorymethod.csv;

import foi.vlovric21.parser.CsvParser;

public class RezervacijaCsvParserCreator extends CsvParserCreator {

    @Override
    protected CsvParser stvoriParser(){
        return new RezervacijaCsvParser();
    }
}
