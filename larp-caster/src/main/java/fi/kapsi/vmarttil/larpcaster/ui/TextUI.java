/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.ui;

import java.util.Scanner;
import fi.kapsi.vmarttil.larpcaster.domain.*;
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
            System.out.println("Komennot: ");
            System.out.println(" 1 - Lataa yhteensopivuustiedosto");
            System.out.println(" 2 - Valitse algoritmi");
            System.out.println(" 3 - Aseta reunaehdot ja optimoinnit");
            if (hahmojako.getYhteensopivuusdata() != null) {
                System.out.println(" 4 - Tulosta data");
                System.out.println(" 5 - Laske optimoitu hahmojako");
            }
            System.out.println(" X - Lopeta");
            System.out.println("");
            System.out.print("Komento: ");
            String komento = lukija.nextLine();
            if (komento.equals("X")) {
                break;
            } else if (komento.equals("1")) {
                lataaTiedosto();
            } else if (komento.equals("2")) {
                valitseAlgoritmi();
            } else if (komento.equals("3")) {
                asetaEhdot();
            } else if (komento.equals("4") && hahmojako.getYhteensopivuusdata() != null) {
                tulostaEhdokaslistat();
            } else if (komento.equals("5") && hahmojako.getYhteensopivuusdata() != null) {
                this.hahmojako.teeHahmojako();
            } else {
                System.out.println("Tuntematon komento.");
            }
        }
    }
            
    private void lataaTiedosto() { 
        System.out.println("");
        System.out.print("Tiedoston nimi: ");
        String tiedostonimi = lukija.nextLine();
        File xmlTiedosto = new File(tiedostonimi);
        try {
            this.hahmojako.lataaYhteensopivuustiedot(xmlTiedosto);
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
        System.out.println("");
        System.out.println("Komennot: ");
        System.out.println(" 1 - Tulosta pelaajien hahmoehdokaslistat");
        System.out.println(" 2 - Tulosta hahmojen pelaajaehdokaslistat");
        System.out.println(" 3 - Tulosta yhteensopivuusmatriisi");
        System.out.println(" X - Takaisin");
        System.out.print("Komento: ");
        String komento = lukija.nextLine();
        if (komento.equals("X")) {
        } else if (komento.equals("1")) {
            tulostaHahmoehdokaslistat();
        } else if (komento.equals("2")) {
            tulostaPelaajaehdokaslistat();
        } else if (komento.equals("3")) {
            tulostaYhteensopivuusmatriisi();
        } else {
            System.out.println("Tuntematon komento.");
        }
    }
    
    private void tulostaHahmoehdokaslistat() {
        Sopivuusmatriisi yhteensopivuudet = this.hahmojako.getYhteensopivuusdata();
        System.out.println("");
        System.out.println("Pelaajien potentiaaliset hahmoehdokkaat");
        System.out.println("");
        System.out.println("Pelaaja:                Hahmoehdokkaat (sopivuus):");
        for (int i=1; i<=yhteensopivuudet.getPelaajamaara();i++) {
            System.out.println(yhteensopivuudet.getPelaajatunnus(i));
            Ehdokaslista ehdokaslista = yhteensopivuudet.getHahmoehdokaslista(i);
            while(true) {
                int ehdokas = ehdokaslista.seuraavaEhdokas();
                if (ehdokas == 0) {
                    break;
                } else {
                    String tunnus = yhteensopivuudet.getHahmotunnus(ehdokas);
                    int sopivuus = yhteensopivuudet.getSopivuusprosentti(i, ehdokas);
                    System.out.println("                        " + tunnus + " (" + sopivuus + "%)");
                }
            }
            System.out.println("");
        }
    }
    
    private void tulostaPelaajaehdokaslistat() {
        Sopivuusmatriisi yhteensopivuudet = this.hahmojako.getYhteensopivuusdata();
        System.out.println("");
        System.out.println("Hahmojen potentiaaliset pelaajaehdokkaat");
        System.out.println("");
        System.out.println("Hahmo:                  Pelaajaehdokkaat (sopivuus):");
        for (int i=1; i<=yhteensopivuudet.getPelaajamaara();i++) {
            System.out.println(yhteensopivuudet.getHahmotunnus(i));
            Ehdokaslista ehdokaslista = yhteensopivuudet.getPelaajaehdokaslista(i);
            if (!yhteensopivuudet.getHahmotunnus(i).equals("")) {
                while(true) {
                    int ehdokas = ehdokaslista.seuraavaEhdokas();
                    if (ehdokas == 0) {
                        break;
                    } else {
                        String tunnus = yhteensopivuudet.getPelaajatunnus(ehdokas);
                        int sopivuus = yhteensopivuudet.getSopivuusprosentti(ehdokas, i);
                        System.out.println("                        " + tunnus + " (" + sopivuus + "%)");
                    }
                }
            }
            System.out.println("");
        }       
    }
    
    private void tulostaYhteensopivuusmatriisi() {
        Sopivuusmatriisi yhteensopivuudet = this.hahmojako.getYhteensopivuusdata();
        System.out.println("");
        System.out.println("Pelaajien ja hahmojen yhteensopivuus:");
        System.out.println("");
        
    }
    
    
    
}
