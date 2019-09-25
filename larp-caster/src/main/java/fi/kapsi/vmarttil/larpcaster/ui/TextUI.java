/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.ui;

import java.util.Scanner;
import fi.kapsi.vmarttil.larpcaster.domain.*;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.*;
import org.xml.sax.SAXException;

/**
 * Tämä luokka määrittelee työkalun tekstikäyttöliittymän.
 * @author Ville Marttila
 */
public class TextUI {
    
    static HashMap<String, String> tulostettavatNimet;
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
        alustaTulostettavatNimet();
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
                System.out.println(" 4 - Näytä lähtödata");
                System.out.println(" 5 - Laske optimoitu hahmojako");
            }
            if (!hahmojako.getTulokset().isEmpty()) {
                System.out.println(" 6 - Näytä tulokset");
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
                naytaEhdokaslistat();
            } else if (komento.equals("5") && hahmojako.getYhteensopivuusdata() != null) {
                this.hahmojako.teeHahmojako();
            } else if (komento.equals("6") && !hahmojako.getTulokset().isEmpty()) {
                naytaTulokset();
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
        String nykyinen = "";
        while (true) {
            if (hahmojako.getKaytettavaAlgoritmi().equals("")) {
                nykyinen = "ei valittu";
            } else if (hahmojako.getKaytettavaAlgoritmi().equals("galeShapleyHahmoKosii")) {
                nykyinen = "1";
            } else if (hahmojako.getKaytettavaAlgoritmi().equals("galeShapleyPelaajaKosii")) {
                nykyinen = "2";
            }
            System.out.println("");
            System.out.println("Vaihtoehdot (nykyinen: " + nykyinen + "): ");
            System.out.println(" 1 - " + tulostettavatNimet.get("galeShapleyHahmoKosii"));
            System.out.println(" 2 - " + tulostettavatNimet.get("galeShapleyPelaajaKosii"));
            System.out.println(" x - Takaisin");
            System.out.print("Komento: ");
            String komento = lukija.nextLine();
            if (komento.equals("x")) {
                break;
            } else if (komento.equals("1")) {
                hahmojako.setKaytettavaAlgoritmi("galeShapleyHahmoKosii");
            } else if (komento.equals("2")) {
                hahmojako.setKaytettavaAlgoritmi("galeShapleyPelaajaKosii");
            } else {
                System.out.println("Tuntematon vaihtoehto.");
            }
        }
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
     * Tämä metodi määrittää käyttöliittymän minimisopivuuden asettamiselle.
     */
    private void asetaMinimisopivuus() {
        System.out.println("");
        System.out.print("Uusi minimiyhteensopivuus (nykyinen: " + hahmojako.getMinimisopivuus() + "%): ");
        int sopivuus = Integer.parseInt(lukija.nextLine().replace("%", "").replace(" ", ""));
        hahmojako.setMinimisopivuus(sopivuus);
    }
    
    /**
     * Tämä metodi määrittelee tekstikäyttöliittymän ladattujen yhteensopivuus-
     * tietojen tulostamiseen näytölle.
     */
    private void naytaEhdokaslistat() {
        while (true) {
            System.out.println("");
            System.out.println("Komennot: ");
            System.out.println(" 1 - Näytä pelaajien hahmoehdokaslistat");
            System.out.println(" 2 - Näytä hahmojen pelaajaehdokaslistat");
            System.out.println(" 3 - Näytä yhteensopivuusmatriisi");
            System.out.println(" x - Takaisin");
            System.out.print("Komento: ");
            String komento = lukija.nextLine();
            if (komento.equals("x")) {
                break;
            } else if (komento.equals("1")) {
                naytaHahmoehdokaslistat();
            } else if (komento.equals("2")) {
                naytaPelaajaehdokaslistat();
            } else if (komento.equals("3")) {
                naytaYhteensopivuusmatriisi();
            } else {
                System.out.println("Tuntematon komento.");
            }
        }
    }
    
    /**
     * Tämä metodi tulostaa pelaajien hahmoehdokaslistat näytölle.
     */
    private void naytaHahmoehdokaslistat() {
        Sopivuusmatriisi yhteensopivuudet = this.hahmojako.getYhteensopivuusdata();
        System.out.println("");
        System.out.println("Pelaajien potentiaaliset hahmoehdokkaat");
        System.out.println("");
        System.out.println("Pelaaja:    Hahmoehdokkaat (sopivuus):");
        for (int i=1; i<=yhteensopivuudet.getPelaajamaara();i++) {
            System.out.println(yhteensopivuudet.getPelaajatunnus(i));
            Ehdokaslista ehdokaslista = yhteensopivuudet.getHahmoehdokaslista(i);
            for (int e = 0; e < ehdokaslista.getPituus(); e++) {
                int ehdokas = ehdokaslista.getEhdokas(e);
                String tunnus = yhteensopivuudet.getHahmotunnus(ehdokas);
                int sopivuus = yhteensopivuudet.getSopivuusprosentti(i, ehdokas);
                System.out.println("            " + tunnus + " (" + sopivuus + "%)");
            }
            System.out.println("");
        }
    }
    
    /**
     * Tämä metodi tulostaa hahmojen pelaajaehdokaslistat näytölle.
     */
    private void naytaPelaajaehdokaslistat() {
        Sopivuusmatriisi yhteensopivuudet = this.hahmojako.getYhteensopivuusdata();
        System.out.println("");
        System.out.println("Hahmojen potentiaaliset pelaajaehdokkaat");
        System.out.println("");
        System.out.println("Hahmo:      Pelaajaehdokkaat (sopivuus):");
        for (int i=1; i<=yhteensopivuudet.getPelaajamaara();i++) {
            System.out.println(yhteensopivuudet.getHahmotunnus(i));
            Ehdokaslista ehdokaslista = yhteensopivuudet.getPelaajaehdokaslista(i);
            if (!yhteensopivuudet.getHahmotunnus(i).equals("")) {
                for (int e = 0; e < ehdokaslista.getPituus(); e++) {
                    int ehdokas = ehdokaslista.getEhdokas(e);
                    String tunnus = yhteensopivuudet.getPelaajatunnus(ehdokas);
                    int sopivuus = yhteensopivuudet.getSopivuusprosentti(ehdokas, i);
                    System.out.println("            " + tunnus + " (" + sopivuus + "%)");
                }
            }
            System.out.println("");
        }       
    }
    
    /**
     * Tämä metodi tulostaa yhteensopivuusmatriisin sisällön näytölle pelaajittain.
     */
    private void naytaYhteensopivuusmatriisi() {
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
     * Tämä metodi määrittää tekstikäyttöliittymän saatujen tulosten tulostamiseen näytölle.
     */
    private void naytaTulokset() {
        while (true) {
            System.out.println("");
            System.out.println("Tehdyt hahmojaot: ");
            for (int i=1; i <= hahmojako.getTulokset().size(); i++) {
                System.out.println(" " + i + " - " + tulostettavatNimet.get(hahmojako.getTulokset().get(i-1).getAlgoritmi()) + "(minimisopivuus: " + hahmojako.getTulokset().get(i-1).getMinimiyhteensopivuus() + "%)");
            }
            System.out.println(" x - Takaisin");
            System.out.print("Valinta: ");
            String komento = lukija.nextLine();
            if (komento.equals("x")) {
                break;
            } else {
                naytaTulos(Integer.parseInt(komento) - 1);
            }
        }
    }
    
    private void naytaTulos(int tulosNumero) {
        Tulos tulos = hahmojako.getTulokset().get(tulosNumero);
        System.out.println("");
        System.out.println("Hahmojaon tulokset");
        System.out.println("");
        if (!tulos.getPelaajattomatHahmot().isEmpty()) {
            System.out.println("VAROITUS: Kaikille hahmoille ei löytynyt pelaajaa!");
            System.out.println("");
        }        
        System.out.println("Hahmo:                  Pelaaja:                Sopivuus:");
        for (Integer hahmoindeksi : tulos.getHahmojenPelaajat().keySet()) {
            String hahmo = hahmojako.getYhteensopivuusdata().getHahmotunnus(hahmoindeksi);
            System.out.print(hahmo);
            int valeja = 24 - hahmo.length();
            for (int i = 0; i < valeja;i++) {
                System.out.print(" ");
            }
            String pelaaja = hahmojako.getYhteensopivuusdata().getHahmotunnus(tulos.getHahmojenPelaajat().get(hahmoindeksi));
            System.out.println(pelaaja);
            valeja = 24 - pelaaja.length();
            for (int i = 0; i < valeja;i++) {
                System.out.print(" ");
            }
            System.out.println(hahmojako.getYhteensopivuusdata().getSopivuusprosentti(hahmoindeksi, tulos.getHahmojenPelaajat().get(hahmoindeksi)) + " %");
        }
        if (!tulos.getHahmottomatPelaajat().isEmpty()) {
            System.out.println("");
            System.out.println("Pelaajat joille ei löytynyt hahmoa:");
            for (Integer pelaajaindeksi : tulos.getHahmottomatPelaajat()) {
                String pelaaja = hahmojako.getYhteensopivuusdata().getPelaajatunnus(pelaajaindeksi);
                System.out.println(pelaaja);
            }
        }
        if (!tulos.getPelaajattomatHahmot().isEmpty()) {
            System.out.println("");
            System.out.println("Hahmot joille ei löytynyt pelaajaa:");
            for (Integer hahmoindeksi : tulos.getPelaajattomatHahmot()) {
                String hahmo = hahmojako.getYhteensopivuusdata().getHahmotunnus(hahmoindeksi);
                System.out.println(hahmo);
            }
        }
        System.out.println("");
        System.out.println("Käytetty algoritmi: " + tulostettavatNimet.get(tulos.getAlgoritmi()));
        System.out.println("Käytetty minimisopivuus: " + tulos.getMinimiyhteensopivuus() + "%");
        System.out.println("Iterointikierroksia: " + tulos.getKierroksia());
        System.out.println("Hahmojaon vaatima aika: " + tulos.getKulunutAika() + " s");
        System.out.println("");
    }
    
    
    /**
     * Tämä metodi määrittää algoritmien tulostettavat nimet käyttöliittymää varten
     */
    private void alustaTulostettavatNimet() {
        this.tulostettavatNimet = new HashMap<>();
        tulostettavatNimet.put("galeShapleyHahmoKosii", "Hahmolähtöinen Galen-Shapleyn algoritmi");
        tulostettavatNimet.put("galeShapleyPelaajaKosii", "Pelaajalähtöinen Galen-Shapleyn algoritmi");
    }
    
}
