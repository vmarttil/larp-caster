/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.ui;

import java.util.Scanner;
import fi.kapsi.vmarttil.larpcaster.domain.Hahmojako;
import fi.kapsi.vmarttil.larpcaster.domain.Sopivuusmatriisi;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;

/**
 *
 * @author Ville
 */
public class TextUI {
    
    Hahmojako hahmojako;
    Scanner lukija;
    
    public TextUI() {
        
    }
    
    public void kaynnista(Hahmojako hahmojako) throws Exception {
        this.hahmojako = hahmojako;
        lukija = new Scanner(System.in);
        while (true) {
            System.out.println("LARPCaster-työkalu");
            System.out.println("");
            System.out.println(" 1 - Lataa yhteensopivuustiedosto");
            System.out.println(" 2 - Valitse algoritmi");
            System.out.println(" 3 - Aseta reunaehdot ja optimoinnit");
            if (hahmojako.getYhteensopivuusdata() != null) {
                System.out.println(" 4 - Tulosta ehdokaslistat");
                System.out.println(" 5 - Laske optimoitu hahmojako");
            }
            System.out.println(" X - Lopeta");
            System.out.println("");
            System.out.print("Komento: ");
            String komento = lukija.nextLine();
            if (komento.equals("X")) {
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
                    tulostaEhdokaslistat();
                    break;
                case "5":
                    if (hahmojako.getYhteensopivuusdata() != null) {    
                        this.hahmojako.teeHahmojako();
                    }
                    break;
                    
                default:
                    break;
            }
        }
    }
            
    private void lataaTiedosto() { 
        System.out.println("");
        System.out.print("Tiedoston nimi: ");
        String tiedostonimi = lukija.nextLine();
        File xmlTiedosto = new File(tiedostonimi);
        try {
            hahmojako.lataaYhteensopivuustiedot(xmlTiedosto);
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            System.out.println("");
            System.out.println("VIRHE: Yhteensopivuustietojen lataus ei onnistunut.");
            System.out.println("");
            e1.printStackTrace();
        }
        System.out.println("Yhteensopivuustiedot ladattu tiedostosta " + tiedostonimi);
    }
    
    private void valitseAlgoritmi() {
        // Käyttöliittymä algoritmin valitsemiseksi
    }
    
    private void asetaEhdot() {
        // Käyttöliittymä reunaehtojen ja optiomointien asettamiseksi
    }
    
    private void tulostaEhdokaslistat() {
        // Ehdokaslistojen tulostus
    }
    
}
