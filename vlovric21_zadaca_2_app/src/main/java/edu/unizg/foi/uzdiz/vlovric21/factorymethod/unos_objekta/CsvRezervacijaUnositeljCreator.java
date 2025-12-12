package edu.unizg.foi.uzdiz.vlovric21.factorymethod.unos_objekta;

public class CsvRezervacijaUnositeljCreator extends CsvObjectCreator{

    @Override
    protected CsvObjectUnositelj stvoriUnositelja(){
        return new CsvRezervacijaUnositelj();
    }
}
