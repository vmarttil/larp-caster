/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.ui;

import java.util.Scanner;
import fi.kapsi.vmarttil.larpcaster.domain.Hahmojako;
import fi.kapsi.vmarttil.larpcaster.domain.Sopivuusmatriisi;

/**
 *
 * @author Ville
 */
public class TextUI {
    
    Hahmojako hahmojako;
    
    public TextUI() {
        
    }
    
    public void kaynnista(Hahmojako hahmojako) throws Exception {
        this.hahmojako = hahmojako;
        Scanner lukija = new Scanner(System.in);
        while (true) {
            System.out.println("LARPCaster-työkalu");
            System.out.println("");
            System.out.println(" 1 - Lataa yhteensopivuustiedosto");
            System.out.println(" 2 - Valitse algoritmi");
            System.out.println(" 3 - Aseta reunaehdot ja optimoinnit");
            if (hahmojako.getYhteensopivuusdata() != null) {
                System.out.println(" 4 - Laske optimoitu hahmojako");
            }
            System.out.println(" 5 - Lopeta");
            System.out.println("");
            String komento = lukija.nextLine();
            if (komento.equals("5")) {
                break;
            }
            switch (komento) {
                case "1":
                    lataaTiedosto();
                    break;
                case "2":
                    valitseAlgoritmi();
                    break;
                case "3":
                    asetaEhdot();
                    break;
                case "4":
                    this.hahmojako.teeHahmojako();
                    break;
                default:
                    break;
            }
        }
    }
            
    private void lataaTiedosto() {
        // Käyttöliittymä matriisin täyttämiseen tiedostosta
    }
    
    private void valitseAlgoritmi() {
        // Käyttöliittymä algoritmin valitsemiseksi
    }
    
    private void asetaEhdot() {
        // Käyttöliittymä reunaehtojen ja optiomointien asettamiseksi
    }
    
}
