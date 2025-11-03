package foi.vlovric21.pomocne;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DatumFormater {

    public String formatirajDatum(String dan, String mjesec, String godina){
        return String.format("%02d.%02d.%s.",
                Integer.parseInt(dan),
                Integer.parseInt(mjesec),
                godina);
    }

    public String formatirajDatumVrijeme(LocalDateTime dt){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        return dt.format(formatter);
    }

    public String formatirajDatumVrijeme(String dan, String mjesec, String godina, String sat, String minuta, String sekunda){
        String formatiraniDatum = String.format("%02d.%02d.%s",
                Integer.parseInt(dan),
                Integer.parseInt(mjesec),
                godina);

        String formatiranoVrijeme;
        formatiranoVrijeme = String.format("%02d:%02d:%02d",
                Integer.parseInt(sat),
                Integer.parseInt(minuta),
                Integer.parseInt(sekunda));

        return formatiraniDatum + " " + formatiranoVrijeme;
    }
}
