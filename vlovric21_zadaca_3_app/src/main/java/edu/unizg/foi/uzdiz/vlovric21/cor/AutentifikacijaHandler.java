package edu.unizg.foi.uzdiz.vlovric21.cor;

public class AutentifikacijaHandler extends BrisanjeHandler{

    @Override
    public void odradiZahtjev(String opcija){
        String lozinka = "admin123";

        System.out.println("Kako biste potvrdili brisanje, unesite lozinku (admin123): ");
        String unesenaLozinka = System.console().readLine();

        if(!unesenaLozinka.equals(lozinka)){
            System.out.println("Pogrešna lozinka, lozinka je 'admin123'. Brisanje se neće izvršiti.");
            return;
        }
        System.out.println();
        pozoviSljedeci(opcija);
    }
}
