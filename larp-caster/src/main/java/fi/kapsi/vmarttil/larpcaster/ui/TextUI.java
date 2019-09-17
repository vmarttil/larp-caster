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
 * Tämä luokka määrittelee työkalun tekstikäyttöliittymän.
 * @author Ville Marttila
 */
public class TextUI {
    
    Hahmojako hahmojako;
    Scanner lukija;
    
    public TextUI() { 
    }
    
    /**
     * Tämä metodi käynnistää työkalun tekstikäyttöliittymän.
     * @param hahmojako Hahmojako-olio, joka ohjaa työkalun toimintalogiikkaa 
     * ja sisältää kulloiseenkin hahmojakoon liittyvät tiedot.  
     */
    public void kaynnista(Hahmojako hahmojako) throws Exception {
        this.hahmojako = hahmojako;
        lukija = new Scanner(System.in);
        while (true) {
            System.out.println("");
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
            System.out.println(" x - Lopeta");
            System.out.println("");
            System.out.print("Komento: ");
            String komento = lukija.nextLine();
            if (komento.equals("x")) {
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
    
    /**
     * Tämä metodi määrittelee tekstikäyttöliittymän yhteensopivuustiedoston 
     * lataamiselle ja kutsuu latausmetodia.
     */
    private void lataaTiedosto() { 
        System.out.println("");
        System.out.print("Tiedoston nimi: ");
        String tiedostonimi = lukija.nextLine();
        try {
            this.hahmojako.lataaYhteensopivuustiedot(tiedostonimi);
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            System.out.println("");
            System.out.println("VIRHE: Yhteensopivuustietojen lataus ei onnistunut.");
            System.out.println("");
            e1.printStackTrace();
        }
    }
    
    /**
     * Tämä metodi määrittelee tekstikäyttöliittymän hahmojaon laskentaan käytettävän 
     * algoritmin valitsemiselle. 
     */
    private void valitseAlgoritmi() {
        // Käyttöliittymä algoritmin valitsemiseksi
    }
    
    /**
     * Tämä metodi määrittelee tekstikäyttöliittymän hahmojakoa ohjaavien ehtojen 
     * ja parametrien määrittämiselle.
     */
    private void asetaEhdot() {
        while (true) {
            System.out.println("");
            System.out.println("Komennot: ");
            System.out.println(" 1 - Aseta minimiyhteensopivuus (nykyinen: " + hahmojako.getMinimisopivuus() + "%)");
            System.out.println(" x - Takaisin");
            System.out.print("Komento: ");
            String komento = lukija.nextLine();
            if (komento.equals("x")) {
                break;
            } else if (komento.equals("1")) {
                asetaMinimisopivuus();
            } else {
                System.out.println("Tuntematon komento.");
            }
        }
    }
    
    /**
     * Tämä metodi määrittelee tekstikäyttöliittymän ladattujen yhteensopivuus-
     * tietojen tulostamiseen näytölle.
     */
    private void tulostaEhdokaslistat() {
        while (true) {
            System.out.println("");
            System.out.println("Komennot: ");
            System.out.println(" 1 - Tulosta pelaajien hahmoehdokaslistat");
            System.out.println(" 2 - Tulosta hahmojen pelaajaehdokaslistat");
            System.out.println(" 3 - Tulosta yhteensopivuusmatriisi");
            System.out.println(" x - Takaisin");
            System.out.print("Komento: ");
            String komento = lukija.nextLine();
            if (komento.equals("x")) {
                break;
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
    }
    
    /**
     * Tämä metodi tulostaa pelaajien hahmoehdokaslistat näytölle.
     */
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
                    System.out.println("            " + tunnus + " (" + sopivuus + "%)");
                }
            }
            System.out.println("");
        }
    }
    
    /**
     * Tämä metodi tulostaa hahmojen pelaajaehdokaslistat näytölle.
     */
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
                        System.out.println("            " + tunnus + " (" + sopivuus + "%)");
                    }
                }
            }
            System.out.println("");
        }       
    }
    
    /**
     * Tämä metodi tulostaa yhteensopivuusmatriisin sisällön näytölle pelaajittain.
     */
    private void tulostaYhteensopivuusmatriisi() {
        Sopivuusmatriisi yhteensopivuudet = this.hahmojako.getYhteensopivuusdata();
        System.out.println("");
        System.out.println("Pelaajien ja hahmojen yhteensopivuus:");
        System.out.println("");
        for (int i=1; i<=yhteensopivuudet.getPelaajamaara();i++) {
            System.out.println(yhteensopivuudet.getPelaajatunnus(i));
            for (int j=1; j<=yhteensopivuudet.getPelaajamaara();j++) {
                System.out.println("   " + yhteensopivuudet.getHahmotunnus(j) + " (" + yhteensopivuudet.getSopivuusprosentti(i, j) + ")");
            } 
        }
    }
    
    /**
     * Tämä metodi määrittää käyttöliittymän minimisopivuuden asettamiselle.
     */
    private void asetaMinimisopivuus() {
        System.out.println("");
        System.out.print("Uusi minimiyhteensopivuus (nykyinen: " + hahmojako.getMinimisopivuus() + "%): ");
        int sopivuus = Integer.parseInt(lukija.nextLine().replace("%", "").replace(" ", ""));
        hahmojako.setMinimisopivuus(sopivuus);
    }
    
    
    
}
