package foi.vlovric21.objekti;

public class Rezervacija {
    private int id;
    private String ime;
    private String prezime;
    private int oznakaAranzmana;
    private String datumIVrijeme;
    private RezervacijaStatus status;

    public Rezervacija(){}
    public Rezervacija(String ime, String prezime, int oznakaAranzmana, String datumIVrijeme) {
        this.ime = ime;
        this.prezime = prezime;
        this.oznakaAranzmana = oznakaAranzmana;
        this.datumIVrijeme = datumIVrijeme;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }
    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }
    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getPunoIme() {
        return ime + " " + prezime;
    }

    public int getOznakaAranzmana() {
        return oznakaAranzmana;
    }
    public void setOznakaAranzmana(int oznakaAranzmana) {
        this.oznakaAranzmana = oznakaAranzmana;
    }

    public String getDatumIVrijeme() {
        return datumIVrijeme;
    }
    public void setDatumIVrijeme(String datumIVrijeme) {
        this.datumIVrijeme = datumIVrijeme;
    }

    public RezervacijaStatus getStatus() {
        return status;
    }
    public void setStatus(RezervacijaStatus status) {
        this.status = status;
    }
}
