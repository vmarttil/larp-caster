/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.ui;

import java.util.Scanner;
import fi.kapsi.vmarttil.larpcaster.domain.*;
import java.io.IOException;
import java.util.ArrayList;
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
    private int pisinHahmotunnus;
    private int pisinPelaajatunnus;
    
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
        this.lukija = new Scanner(System.in);
        while (true) {
            naytaPaavalikko();
            String komento = this.lukija.nextLine();
            if (komento.equals("x")) {
                break;
            } else if (komento.equals("1")) {
                if (hahmojako.getYhteensopivuusdata() == null) {
                    lataaTiedosto();
                    this.pisinHahmotunnus = laskePisinHahmotunnus();
                    this.pisinPelaajatunnus = laskePisinPelaajatunnus();
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
            } else if (komento.equals("5") && hahmojako.getYhteensopivuusdata() != null && this.hahmojako.getEhdokaslistatOK() == true) {
                double suoritusaika = this.hahmojako.teeHahmojako();
                if (suoritusaika == -1) {
                    System.out.println("Algoritmi tuottanut yhtäkään kelvollista hahmojakoa nykyisillä parametreilla.");
                } else { 
                    System.out.println("Suoritusaika: " + suoritusaika + " s");
                }
            } else if (komento.equals("6") && !hahmojako.getTulokset().isEmpty()) {
                naytaLaskennat();
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
            if (this.hahmojako.getEhdokaslistatOK() == true) {
                System.out.println(" 5 - Laske optimoitu hahmojako");
            }
        }
        if (hahmojako.getTulokset().size() > 0) {
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
        String tiedostonimi = this.lukija.nextLine();
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
            String komento = this.lukija.nextLine();
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
            String komento = this.lukija.nextLine();
            if (komento.equals("x")) {
                break;
            } else if (komento.equals("1")) {
                asetaMinimisopivuus();
            } else if (komento.equals("2")) {
                asetaLaskettavatVariaatiot();
            } else if (komento.equals("3")) {
                asetaTuloksiaEnintaanLaskentaaKohden();
            } else if (komento.equals("4")) {
                asetaTuloksiaEnintaanYhteensa();
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
        System.out.println(" 2 - Aseta laskettavien variaatioiden aste (nykyinen:" + this.hahmojako.getLaskettavatVariaatiot() + ", max: 5)");
        System.out.println(" 3 - Aseta hahmojakojen laskentakohtainen enimmäismäärä (nykyinen: " + hahmojako.getTuloksiaEnintaanLaskentaaKohden() + ", max: 100)");
        System.out.println(" 4 - Aseta hahmojakojen yhteisenimmäismäärä (nykyinen: " + hahmojako.getTuloksiaEnintaanYhteensa() + ", max: 100)");
        System.out.println(" x - Takaisin");
        System.out.print("Komento: ");
    }
    
    /**
     * Tämä metodi määrittää käyttöliittymän minimisopivuuden asettamiselle.
     */
    private void asetaMinimisopivuus() {
        System.out.println("");
        System.out.print("Uusi minimiyhteensopivuus (nykyinen: " + hahmojako.getMinimisopivuus() + "%): ");
        int sopivuus = Integer.parseInt(this.lukija.nextLine().replace("%", "").replace(" ", ""));
        hahmojako.setMinimisopivuus(sopivuus);
    }
    
    /**
     * Tämä metodi määrittää käyttöliittymän laskettavien variaatioiden asteen asettamiselle.
     */
    private void asetaLaskettavatVariaatiot() {
        System.out.println("");
        System.out.print("Uusi laskettavien variaatioiden aste (nykyinen: " + hahmojako.getLaskettavatVariaatiot() + "): ");
        int aste = Integer.parseInt(this.lukija.nextLine().replace("%", "").replace(" ", ""));
        if (aste > 5) {
            hahmojako.setLaskettavatVariaatiot(5);
        } else {
            hahmojako.setLaskettavatVariaatiot(aste);
        }
    }
    
    /**
     * Tämä metodi määrittää käyttöliittymän laskentakohtaisen hahmojakojen enimmäismäärän asettamiselle.
     */
    private void asetaTuloksiaEnintaanLaskentaaKohden() {
        System.out.println("");
        System.out.print("Uusi hahmojakojen laskentakohtainen enimmäismäärä (nykyinen: " + hahmojako.getTuloksiaEnintaanLaskentaaKohden() + ", max: 20): ");
        int maara = Integer.parseInt(this.lukija.nextLine().replace(" ", ""));
        if (maara > 20) {
            System.out.println("VIRHE: Tuloksien enimmäismäärä on 20.");
        } else {
            hahmojako.setTuloksiaEnintaanLaskentaaKohden(maara);
        }
    }
    
    /**
     * Tämä metodi määrittää käyttöliittymän hahmojakojen yhteisenimmäismäärän asettamiselle.
     */
    private void asetaTuloksiaEnintaanYhteensa() {
        System.out.println("");
        System.out.print("Uusi hahmojakojen yhteisenimmäismäärä (nykyinen: " + hahmojako.getTuloksiaEnintaanYhteensa() + ", max: 30): ");
        int maara = Integer.parseInt(this.lukija.nextLine().replace(" ", ""));
        if (maara > 30) {
            System.out.println("VIRHE: Tuloksien enimmäismäärä on 30.");
        } else {
            hahmojako.setTuloksiaEnintaanYhteensa(maara);
        }
    }
    
    /**
     * Tämä metodi määrittelee tekstikäyttöliittymän ladattujen yhteensopivuus-
     * tietojen tulostamiseen näytölle.
     */
    private void naytaEhdokaslistat() {
        while (true) {
            naytaEhdokaslistavalikko();
            String komento = this.lukija.nextLine();
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
     * Tämä metodi määrittää tekstikäyttöliittymän tehtyjen laskentojen tulostamiseen näytölle.
     */
    private void naytaLaskennat() {
        while (true) {
            System.out.println("");
            System.out.println("Tehdyt laskennat: ");
            for (int i = 1; i <= hahmojako.getTulokset().size(); i++) {
                System.out.println(" " + i + " - " + tulostettavatNimet.get(hahmojako.getTulokset().get(i - 1).get(0).getAlgoritmi()) + "(" + hahmojako.getTulokset().get(i - 1).get(0).getMinimiyhteensopivuus() + "%): " + "paras ka. sop. " + ((int) (hahmojako.getTulokset().get(i - 1).get(0).getSopivuuskeskiarvo() * 100) / 100.0) + "%");
            }
            System.out.println(" y - Kaikkien laskentojen yhteistulokset");
            System.out.println(" x - Takaisin");
            System.out.print("Valinta: ");
            String komento = this.lukija.nextLine();
            if (komento.equals("x")) {
                break;
            } else if (komento.equals("y")) {
                naytaYhteistulokset();
            } else {
                naytaTulokset(Integer.parseInt(komento) - 1);
            }
        }
    }
    
    private void naytaTulokset(int laskentaNumero) {
        while (true) {
            ArrayList<Tulos> tulokset = hahmojako.getTulokset().get(laskentaNumero);
            int tuloksia = laskeTulokset(tulokset);
            System.out.println("");
            System.out.println("Hahmojaon tulokset laskennalle " + laskentaNumero + 1);
            System.out.println("");
            System.out.println("Käytetty algoritmi: " + tulostettavatNimet.get(tulokset.get(0).getAlgoritmi()));
            System.out.println("Käytetty minimisopivuus: " + tulokset.get(0).getMinimiyhteensopivuus() + "%");
            System.out.println("");
            if (tulokset.get(0).getPelaajattomatHahmot().length > 0) {
                System.out.println("VAROITUS: Kaikille hahmoille ei löytynyt pelaajaa millään jaolla!");
                System.out.println("");
            }        
            tulostaLaskennanHahmojakojenYhteenveto(tulokset, tuloksia);
            System.out.println("");
            System.out.println("Yhteenvetotiedot (" + tuloksia + " parasta jakoa):");        
            System.out.println("Huonoin sopivuus: " + etsiHuonoinSopivuus(tulokset, tuloksia) + "%");
            System.out.println("Paras sopivuus: " + etsiParasSopivuus(tulokset, tuloksia) + "%");
            System.out.println("Keskimääräinen sopivuus: " + ((int) (laskeSopivuuskeskiarvo(tulokset, tuloksia) * 100) / 100.0) + "%");
            naytaLaskennanTulosValikko();
            String komento = this.lukija.nextLine();
            if (komento.equals("x")) {
                break;
            } else if (Integer.parseInt(komento) > 0 && Integer.parseInt(komento) <= tuloksia) {
                tulostaHahmojako(tulokset.get(Integer.parseInt(komento) - 1), komento);
            } else {
                System.out.println("Tuntematon komento.");
            }
        }
        
    }
    
    private void naytaLaskennanTulosValikko() {
        System.out.println("");
        System.out.println("Komennot: ");
        System.out.println(" [n] - Näytä tuloksen [n] tiedot");
        System.out.println(" x - Takaisin");
        System.out.print("Komento: ");
    }
    
    private void naytaYhteistulokset() {
        
        
        
    }
    
    
    
    private void tulostaLaskennanHahmojakojenYhteenveto(ArrayList<Tulos> tulokset, int tuloksia) {
        tulostaYhteenvedonOtsikot(tuloksia);
        for (int hahmo = 1; hahmo <= this.hahmojako.getYhteensopivuusdata().getHahmomaara(); hahmo++) {
            tulostaHahmonPelaajat(tulokset, hahmo, tuloksia); 
        }
        System.out.println("");
        tulostaKeskiarvoisetSopivuudet(tulokset, tuloksia);
    }
    
    private int laskeTulokset(ArrayList<Tulos> tulokset) {
        int tuloksia;
        if (tulokset.size() > this.hahmojako.getTuloksiaEnintaanLaskentaaKohden()) {
            return this.hahmojako.getTuloksiaEnintaanLaskentaaKohden();
        } else {
            return tulokset.size();
        } 
    }
    
    private void tulostaYhteenvedonOtsikot(int tuloksia) {
        int leveys = (this.pisinPelaajatunnus + 1) * tuloksia;
        for (int i = 0; i < ((leveys + this.pisinHahmotunnus - 9) / 2) / 10; i++) {
            System.out.print("          ");    
        }
        for (int i = 0; i < ((leveys + this.pisinHahmotunnus - 9) / 2) % 10 + 1; i++) {
            System.out.print(" ");    
        }
        System.out.println("Pelaajat:");
        System.out.print("Hahmojako # ");
        if (this.pisinHahmotunnus > 11) {
            for (int i = 0; i < this.pisinHahmotunnus - 11; i++) {
                System.out.print(" ");
            }
        }
        for (int i = 1; i <= tuloksia; i++) {
            int valeja = (this.pisinPelaajatunnus - Integer.toString(i).length()) / 2;
            for (int j = 0; j < valeja; j++) {
                System.out.print(" ");
            }
            System.out.print(i);
            for (int j = 0; j < valeja ; j++) {
                System.out.print(" ");    
            }
            System.out.print("|");
        }
        System.out.println("");
        System.out.println("Hahmot: ");
    }
    
    private void tulostaHahmonPelaajat(ArrayList<Tulos> tulokset, int hahmo, int tuloksia) {
        System.out.print(this.hahmojako.getYhteensopivuusdata().getHahmotunnus(hahmo));
        if (this.pisinHahmotunnus < 11) {
            for (int i = 0; i < 12 - this.hahmojako.getYhteensopivuusdata().getHahmotunnus(hahmo).length() + 1; i++) {
                System.out.print(" ");
            }
        } else {
            for (int i = 0; i < this.pisinHahmotunnus - this.hahmojako.getYhteensopivuusdata().getHahmotunnus(hahmo).length() + 1; i++) {
                System.out.print(" ");
            }
        }
        for (int i = 1; i <= tuloksia; i++) {
            String pelaajatunnus;
            if (tulokset.get(i - 1).getHahmojenPelaajat()[hahmo] == 0) {
                pelaajatunnus = "-";
            } else {
                pelaajatunnus = this.hahmojako.getYhteensopivuusdata().getPelaajatunnus(tulokset.get(i - 1).getHahmojenPelaajat()[hahmo]);
            }
            System.out.print(pelaajatunnus);
            for (int j = 0; j < this.pisinPelaajatunnus - pelaajatunnus.length(); j++) {
                System.out.print(" ");    
            }
            System.out.print("|");
        }
        System.out.println("");
    }
    
    private void tulostaKeskiarvoisetSopivuudet(ArrayList<Tulos> tulokset, int tuloksia) {
        System.out.print("Ka. sop. %: ");
        if (this.pisinHahmotunnus > 11) {
            for (int i = 0; i < this.pisinHahmotunnus - 11; i++) {
                System.out.print(" ");
            }
        }
        for (int i = 1; i <= tuloksia; i++) {
            Double yhteensopivuus = ((int) tulokset.get(i - 1).getSopivuuskeskiarvo() * 100) / 100.0;
            System.out.print(yhteensopivuus);
            for (int j = 0; j < this.pisinPelaajatunnus - 5; j++) {
                System.out.print(" ");    
            }
            System.out.print("|");
        }
        System.out.println("");
    }
    
    private int etsiHuonoinSopivuus(ArrayList<Tulos> tulokset, int tuloksia) {
        int huonoinSopivuus = 100;
        for (int i = 0; i < tuloksia; i++) {
            if (tulokset.get(i).getHuonoinSopivuus() < huonoinSopivuus) {
                huonoinSopivuus = tulokset.get(i).getHuonoinSopivuus();
            }
        }
        return huonoinSopivuus;
    } 
    
    private int etsiParasSopivuus(ArrayList<Tulos> tulokset, int tuloksia) {
        int parasSopivuus = 0;
        for (int i = 0; i < tuloksia; i++) {
            if (tulokset.get(i).getParasSopivuus() > parasSopivuus) {
                parasSopivuus = tulokset.get(i).getParasSopivuus();
            }
        }
        return parasSopivuus;
    } 
    
    private double laskeSopivuuskeskiarvo(ArrayList<Tulos> tulokset, int tuloksia) {
        double kokonaissopivuus = 0;
        for (int i = 0; i < tuloksia; i++) {
            kokonaissopivuus = kokonaissopivuus + tulokset.get(i).getSopivuuskeskiarvo();
        }
        double sopivuuskeskiarvo = kokonaissopivuus / tuloksia;
        return sopivuuskeskiarvo;
    }
    
    /**
     * Tämä metodi tulostaa näytölle hahmot, niille valitut pelaajat ja näiden 
     * yhteensopivuuden.
     * @param tulos hahmojaon tulokset sisältävä Tulos-olio
     */
    private void tulostaHahmojako(Tulos tulos, String tuloksenNumero) {
        while(true) {
            System.out.println("Hahmojaon " + tuloksenNumero + " tulokset:");
            System.out.println("Hahmo:                  Pelaaja:                Sopivuus:");
            for (int hahmo = 1; hahmo <= this.hahmojako.getYhteensopivuusdata().getHahmomaara(); hahmo++) {
                String hahmotunnus = hahmojako.getYhteensopivuusdata().getHahmotunnus(hahmo);
                System.out.print(hahmotunnus);
                int valeja = 24 - hahmotunnus.length();
                for (int i = 0; i < valeja; i++) {
                    System.out.print(" ");
                }
                String pelaaja;
                if (tulos.getHahmojenPelaajat()[hahmo] == 0) {
                    pelaaja = "-";
                } else {
                    pelaaja = hahmojako.getYhteensopivuusdata().getPelaajatunnus(tulos.getHahmojenPelaajat()[hahmo]);
                }
                System.out.print(pelaaja);
                valeja = 24 - pelaaja.length();
                for (int i = 0; i < valeja; i++) {
                    System.out.print(" ");
                }
                if (pelaaja.equals("-")) {
                    System.out.println("-");
                } else {
                    System.out.println(hahmojako.getYhteensopivuusdata().getSopivuusprosentti(tulos.getHahmojenPelaajat()[hahmo], hahmo) + " %");
                }
            }
            // tulostaHahmottomatPelaajat(tulos);
            System.out.println("");
            System.out.println("Huonoin sopivuus: " + tulos.getHuonoinSopivuus() + "%");
            System.out.println("Paras sopivuus: " + tulos.getParasSopivuus() + "%");
            System.out.println("Keskimääräinen sopivuus: " + ((int) tulos.getSopivuuskeskiarvo() * 100) / 100.0 + "%");
            System.out.println("Mediaanisopivuus: " + ((int) tulos.getMediaanisopivuus() * 100) / 100.0 + "%");
            System.out.println("");
            System.out.println("Komennot: ");
            System.out.println(" x - Takaisin");
            System.out.println("");
            System.out.print("Komento: ");
            String komento = this.lukija.nextLine();
            if (komento.equals("x")) {
                break;
            } else {
                System.out.println("Tuntematon komento.");
            }
        }
    }
    
    /**
     * Tämä metodi tulostaa näytölle luettelon pelaajista joille ei löytynyt 
     * hahmoa.
     * @param tulos hahmojaon tulokset sisältävä Tulos-olio
     */
    private void tulostaHahmottomatPelaajat(Tulos tulos) {
        if (tulos.getHahmottomatPelaajat().length != 0) {
            System.out.println("");
            System.out.println("Pelaajat joille ei löytynyt hahmoa:");
            for (int i = 0; i < tulos.getHahmottomatPelaajat().length; i++) {
                int pelaajaindeksi = tulos.getHahmottomatPelaajat()[i];
                String pelaaja = hahmojako.getYhteensopivuusdata().getPelaajatunnus(pelaajaindeksi);
                System.out.println(pelaaja);
            }
        }
    }
    
    
    // Yleisiä käyttöliittymän apumetodeja
    
    private int laskePisinHahmotunnus() {
        int pisinHahmotunnus = 0;
        for (int i = 1; i <= this.hahmojako.getYhteensopivuusdata().getHahmomaara(); i++) {
            if (this.hahmojako.getYhteensopivuusdata().getHahmotunnus(i).length() > pisinHahmotunnus) {
                pisinHahmotunnus = this.hahmojako.getYhteensopivuusdata().getHahmotunnus(i).length();
            }
        }
        return pisinHahmotunnus;
    }
    
    private int laskePisinPelaajatunnus() {
        int pisinPelaajatunnus = 5;
        for (int i = 1; i <= this.hahmojako.getYhteensopivuusdata().getPelaajamaara(); i++) {
            if (this.hahmojako.getYhteensopivuusdata().getPelaajatunnus(i).length() > pisinPelaajatunnus) {
                pisinPelaajatunnus = this.hahmojako.getYhteensopivuusdata().getPelaajatunnus(i).length();
            }
        }
        if (pisinPelaajatunnus % 2 == 0) {
            pisinPelaajatunnus++;
        }
        return pisinPelaajatunnus;
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
