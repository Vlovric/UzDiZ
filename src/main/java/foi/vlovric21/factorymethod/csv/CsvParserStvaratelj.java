package foi.vlovric21.factorymethod.csv;

import foi.vlovric21.parser.CsvParser;
import foi.vlovric21.parser.CsvTip;
// Creator
public class CsvParserStvaratelj {
    public static CsvParser stvoriParser(CsvTip tip){
        switch(tip){
            case CsvTip.ARANZMAN:
                return new AranzmanCsvParser();
            case CsvTip.REZERVACIJA:
                return new RezervacijaCsvParser();
            default:
                throw new IllegalArgumentException();
        }
    }
}
