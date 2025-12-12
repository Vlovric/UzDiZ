package foi.vlovric21.factorymethod.csv;

import foi.vlovric21.parser.CsvParser;
import foi.vlovric21.parser.CsvTip;

public abstract class CsvParserCreator {

    protected abstract CsvParser stvoriParser();

    public void parsirajCsv(String datoteka){
        CsvParser parser = stvoriParser();
        parser.parsirajCsv(datoteka);
    }
}
