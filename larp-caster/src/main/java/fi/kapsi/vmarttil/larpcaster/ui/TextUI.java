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
    
    private static HashMap<String, String> tulostettavatNimet;
    private Hahmojako hahmojako;
    private Scanner lukija;
    
    /**
     * Tämä metodi luo tekstikäyttöliittymän määrittävän TextUI-olion.
     */
    public TextUI() { 
    }
    
    /**
     * Tämä metodi käynnistää työkalun tekstikäyttöliittymän.
     * @param hahmojako Hahmojako-olio, joka ohjaa työkalun toimintalogiikkaa 
     * ja sisältää kulloiseenkin hahmojakoon liittyvät tiedot. 
     * @throws 
     */
    public void kaynnista(Hahmojako hahmojako) throws Exception {
        alustaTulostettavatNimet();
        this.hahmojako = hahmojako;
        lukija = new Scanner(System.in);
        while (true) {
            naytaPaavalikko();
            String komento = lukija.nextLine();
            if (komento.equals("x")) {
                break;
            } else if (komento.equals("1")) {
                if (hahmojako.getYhteensopivuusdata() == null) {
                    lataaTiedosto();
                } else {
                    hahmojako = new Hahmojako();
                    kaynnista(hahmojako);
                }
            } else if (komento.equals("2")) {
                valitseAlgoritmi();
            } else if (komento.equals("3")) {
                asetaEhdot();
            } else if (komento.equals("4") && hahmojako.getYhteensopivuusdata() != null) {
                naytaEhdokaslistat();
            } else if (komento.equals("5") && hahmojako.getYhteensopivuusdata() != null) {
                double suoritusaika = this.hahmojako.teeHahmojako();
                System.out.println("Suoritusaika: " + suoritusaika + " s");
            } else if (komento.equals("6") && !hahmojako.getTulokset().isEmpty()) {
                naytaTulokset();
            } else {
                System.out.println("Tuntematon komento.");
            }
        }
    }
    
    /**
     * Tämä metodi tulostaa näytölle työkalun päävalikon.
     */
    private void naytaPaavalikko() {
        System.out.println("");
        System.out.println("LARPCaster-työkalu");
        System.out.println("");
        System.out.println("Komennot: ");
        if (hahmojako.getYhteensopivuusdata() == null) {
            System.out.println(" 1 - Lataa yhteensopivuustiedosto");
        } else {
            System.out.println(" 1 - Aloita uusi hahmojako");
        }
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
        while (true) {
            String nykyinen = nykyinenAlgoritmi();
            naytaAlgoritmivalikko(nykyinen);
            String komento = lukija.nextLine();
            if (komento.equals("x")) {
                break;
            } else if (komento.equals("1")) {
                hahmojako.setKaytettavaAlgoritmi("galeShapleyHahmoKosii");
            } else if (komento.equals("2")) {
                hahmojako.setKaytettavaAlgoritmi("galeShapleyPelaajaKosii");
            } else if (komento.equals("3")) {
                hahmojako.setKaytettavaAlgoritmi("peruuttava");
            } else if (komento.equals("4")) {
                hahmojako.setKaytettavaAlgoritmi("unkarilainen");
            } else {
                System.out.println("Tuntematon vaihtoehto.");
            }
        }
    }
    
    /**
     * Tämä metodi tulostaa näytölle käytettävän algoritmin valintavalikon.
     */
    private void naytaAlgoritmivalikko(String nykyinen) {
        System.out.println("");
        System.out.println("Vaihtoehdot (nykyinen: " + nykyinen + "): ");
        System.out.println(" 1 - " + tulostettavatNimet.get("galeShapleyHahmoKosii"));
        System.out.println(" 2 - " + tulostettavatNimet.get("galeShapleyPelaajaKosii"));
        System.out.println(" 3 - " + tulostettavatNimet.get("peruuttava"));
        System.out.println(" 4 - " + tulostettavatNimet.get("unkarilainen"));
        System.out.println(" x - Takaisin");
        System.out.print("Komento: ");
    }
    
    /**
     * Tämä metodi tarkistaa käytössä olevan algoritmin ja palauttaa sen 
     * numeron.
     * @return metodi palauttaa käytössä olevaa algoritmia vastaavan merkkijonon
     */
    private String nykyinenAlgoritmi() {
        if (hahmojako.getKaytettavaAlgoritmi().equals("galeShapleyHahmoKosii")) {
            return "1";
        } else if (hahmojako.getKaytettavaAlgoritmi().equals("galeShapleyPelaajaKosii")) {
            return "2";
        } else if (hahmojako.getKaytettavaAlgoritmi().equals("peruuttava")) {
            return "3";
        } else if (hahmojako.getKaytettavaAlgoritmi().equals("unkarilainen")) {
            return "4";
        } else {
            return "ei valittu";
        }
    }
    
    /**
     * Tämä metodi määrittelee tekstikäyttöliittymän hahmojakoa ohjaavien ehtojen 
     * ja parametrien määrittämiselle.
     */
    private void asetaEhdot() {
        while (true) {
            naytaEhtovalikko();
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
     * Tämä metodi tulostaa näytölle ehtojen ja parametrien määrittelyvalikon.
     */
    private void naytaEhtovalikko() {
        System.out.println("");
        System.out.println("Komennot: ");
        System.out.println(" 1 - Aseta minimiyhteensopivuus (nykyinen: " + hahmojako.getMinimisopivuus() + "%)");
        System.out.println(" x - Takaisin");
        System.out.print("Komento: ");
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
            naytaEhdokaslistavalikko();
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
     * Tämä metodi tulostaa näytölle ehdokaslistojen tarkasteluvalikon.
     */
    private void naytaEhdokaslistavalikko() {
        System.out.println("");
        System.out.println("Komennot: ");
        System.out.println(" 1 - Näytä pelaajien hahmoehdokaslistat");
        System.out.println(" 2 - Näytä hahmojen pelaajaehdokaslistat");
        System.out.println(" 3 - Näytä yhteensopivuusmatriisi");
        System.out.println(" x - Takaisin");
        System.out.print("Komento: ");
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
        for (int i = 1; i <= yhteensopivuudet.getPelaajamaara(); i++) {
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
        for (int i = 1; i <= yhteensopivuudet.getPelaajamaara(); i++) {
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
        for (int i = 1; i <= yhteensopivuudet.getPelaajamaara(); i++) {
            System.out.println(yhteensopivuudet.getPelaajatunnus(i));
            for (int j = 1; j <= yhteensopivuudet.getPelaajamaara(); j++) {
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
            for (int i = 1; i <= hahmojako.getTulokset().size(); i++) {
                System.out.println(" " + i + " - " + tulostettavatNimet.get(hahmojako.getTulokset().get(i - 1).getAlgoritmi()) + "(" + hahmojako.getTulokset().get(i - 1).getMinimiyhteensopivuus() + "%): " + hahmojako.getTulokset().get(i - 1).getPrioriteetti() + " (" + hahmojako.getTulokset().get(i - 1).getJarjestysnumero() + ". laskettu) - ka. sop. " + ((int) (hahmojako.getTulokset().get(i - 1).getSopivuuskeskiarvo() * 100) / 100.0) + "%");
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
        tulostaHahmojako(tulos);
        tulostaHahmottomatPelaajat(tulos);
        tulostaPelaajattomatHahmot(tulos);
        System.out.println("");
        System.out.println("Käytetty algoritmi: " + tulostettavatNimet.get(tulos.getAlgoritmi()));
        System.out.println("Käytetty minimisopivuus: " + tulos.getMinimiyhteensopivuus() + "%");
        System.out.println("Huonoin sopivuus: " + tulos.getHuonoinSopivuus() + "%");
        System.out.println("Paras sopivuus: " + tulos.getParasSopivuus() + "%");
        System.out.println("Keskimääräinen sopivuus: " + ((int) (tulos.getSopivuuskeskiarvo() * 100) / 100.0) + "%");
        System.out.println("Mediaanisopivuus: " + ((int) (tulos.getMediaanisopivuus() * 100) / 100.0) + "%");
        System.out.println("");
    }
    
    /**
     * Tämä metodi tulostaa näytölle hahmot, niille valitut pelaajat ja näiden 
     * yhteensopivuuden.
     * @param tulos hahmojaon tulokset sisältävä Tulos-olio
     */
    private void tulostaHahmojako(Tulos tulos) {
        System.out.println("Hahmo:                  Pelaaja:                Sopivuus:");
        for (Integer hahmoindeksi : tulos.getHahmojenPelaajat().keySet()) {
            String hahmo = hahmojako.getYhteensopivuusdata().getHahmotunnus(hahmoindeksi);
            System.out.print(hahmo);
            int valeja = 24 - hahmo.length();
            for (int i = 0; i < valeja; i++) {
                System.out.print(" ");
            }
            String pelaaja = hahmojako.getYhteensopivuusdata().getPelaajatunnus(tulos.getHahmojenPelaajat().get(hahmoindeksi));
            System.out.print(pelaaja);
            valeja = 24 - pelaaja.length();
            for (int i = 0; i < valeja; i++) {
                System.out.print(" ");
            }
            System.out.println(hahmojako.getYhteensopivuusdata().getSopivuusprosentti(tulos.getHahmojenPelaajat().get(hahmoindeksi), hahmoindeksi) + " %");
        }
    }
    
    /**
     * Tämä metodi tulostaa näytölle luettelon pelaajista joille ei löytynyt 
     * hahmoa.
     * @param tulos hahmojaon tulokset sisältävä Tulos-olio
     */
    private void tulostaHahmottomatPelaajat(Tulos tulos) {
        if (!tulos.getHahmottomatPelaajat().isEmpty()) {
            System.out.println("");
            System.out.println("Pelaajat joille ei löytynyt hahmoa:");
            for (Integer pelaajaindeksi : tulos.getHahmottomatPelaajat()) {
                String pelaaja = hahmojako.getYhteensopivuusdata().getPelaajatunnus(pelaajaindeksi);
                System.out.println(pelaaja);
            }
        }
    }
    
    /**
     * Tämä metodi tulostaa näytölle luettelon hahmoista joille ei löytynyt 
     * pelaajaa.
     * @param tulos hahmojaon tulokset sisältävä Tulos-olio
     */
    private void tulostaPelaajattomatHahmot(Tulos tulos) {
        if (!tulos.getPelaajattomatHahmot().isEmpty()) {
            System.out.println("");
            System.out.println("Hahmot joille ei löytynyt pelaajaa:");
            for (Integer hahmoindeksi : tulos.getPelaajattomatHahmot()) {
                String hahmo = hahmojako.getYhteensopivuusdata().getHahmotunnus(hahmoindeksi);
                System.out.println(hahmo);
            }
        }
    }
    
    /**
     * Tämä metodi määrittää algoritmien tulostettavat nimet käyttöliittymää varten
     */
    private void alustaTulostettavatNimet() {
        this.tulostettavatNimet = new HashMap<>();
        tulostettavatNimet.put("galeShapleyHahmoKosii", "Hahmolähtöinen Galen-Shapleyn algoritmi");
        tulostettavatNimet.put("galeShapleyPelaajaKosii", "Pelaajalähtöinen Galen-Shapleyn algoritmi");
        tulostettavatNimet.put("peruuttava", "Peruuttava hakualgoritmi");
        tulostettavatNimet.put("unkarilainen", "Unkarilainen menetelmä (Kuhnin-Munkresin algoritmi)");
    }
    
}
