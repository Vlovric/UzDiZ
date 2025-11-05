package foi.vlovric21.pomocne;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;

public class DatumFormater {
    private static DateTimeFormatter datumIispis = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
    private static DateTimeFormatter datumVrijemeIspis = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss");

    public String formatirajDatumIspis(LocalDate datum) {
        return datum.format(datumIispis);
    }

    public String formatirajDatumVrijemeIspis(LocalDateTime datumVrijeme) {
        return datumVrijeme.format(datumVrijemeIspis);
    }

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
        if (sekunda != null && !sekunda.isEmpty()) {
            formatiranoVrijeme = String.format("%02d:%02d:%02d",
                    Integer.parseInt(sat),
                    Integer.parseInt(minuta),
                    Integer.parseInt(sekunda));
        } else {
            formatiranoVrijeme = String.format("%02d:%02d",
                    Integer.parseInt(sat),
                    Integer.parseInt(minuta));
        }

        return formatiraniDatum + " " + formatiranoVrijeme;
    }

    private static final DateTimeFormatter parser = new DateTimeFormatterBuilder()
            .appendPattern("d.M.yyyy")
            .optionalStart().appendLiteral(".").optionalEnd()
            .appendLiteral(" ")
            .appendPattern("H:mm")
            .optionalStart().appendLiteral(":").appendPattern("ss").optionalEnd()
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .toFormatter();

    public DateTimeFormatter getParser(){
        return parser;
    }

    private static final DateTimeFormatter datumParser = new DateTimeFormatterBuilder()
            .appendPattern("d.M.yyyy")
            .appendLiteral(".")
            .toFormatter();

    public LocalDate parseDatum(String d){
        try{
            return LocalDate.parse(d, datumParser);
        }catch(DateTimeParseException ex){
            return null;
        }
    }

    public LocalDateTime parseDatumIVrijeme(String dv){
        try{
            return LocalDateTime.parse(dv, parser);
        }catch(DateTimeParseException ex){
            return LocalDateTime.MAX;
        }
    }
}
