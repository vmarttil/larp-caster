/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

/**
 * Tämä luokka määrittelee hahmojaossa käytettävien yhteensopivuustietojen ja 
 * niihin liittyvien metatietojen tallennukseen käytettävät tietorakenteet.
 * 
 * NB! Matriisi on molemmissa ulottuvuuksissa pelaajamäärän kokoinen, koska 
 * ylijäämäpelaajia varten matriisiin luodaan haamuhahmot joiden yhteensopivuus-
 * prosentiksi tulee minimiyhteensopivuus. Näihin hahmoihin päätyminen 
 * tarkoittaa, ettei kyseinen pelaaja mahdu peliin.
 * 
 * @author Ville Marttila
 */
public class Sopivuusmatriisi {
    final private String[] pelaajatunnukset;
    final private String[] hahmotunnukset;
    final private int[][] sopivuusprosentit;
    final private int pelaajamaara;
    final private int hahmomaara;
    final private Ehdokaslista[] pelaajienHahmoehdokkaat;
    final private Ehdokaslista[] hahmojenPelaajaehdokkaat;
     
    /**
     * Tämä metodi luo Sopivuusmatriisi-olion, johon tallennetaan hahmojaossa 
     * käytettävät yhteensopivuustiedot ja niihin liittyvät metatiedot.
     * @param pelaajamaara pelaajien määrä ladatuissa yhteensopivuustiedoissa
     * @param hahmomaara hahmojen määrä ladatuissa yhteensopivuustiedoissa
     */
    public Sopivuusmatriisi(int pelaajamaara, int hahmomaara) {
        this.pelaajatunnukset = new String[pelaajamaara + 1];
        this.hahmotunnukset = new String[pelaajamaara + 1];
        this.sopivuusprosentit = new int[pelaajamaara + 1][pelaajamaara + 1];
        this.pelaajamaara = pelaajamaara;
        this.hahmomaara = hahmomaara;
        this.pelaajienHahmoehdokkaat = new Ehdokaslista[pelaajamaara + 1];
        this.hahmojenPelaajaehdokkaat = new Ehdokaslista[pelaajamaara + 1];
    }

    // Getters
    
    /**
     * Tämä metodi palauttaa annetun indeksin osoittaman pelaajan tunnuksen.
     * @param pelaajaindeksi kokonaisluku, joka osoittaa sen pelaajan, jonka 
     * tunnus palautetaan
     * @return metodi palauttaa pelaajan tunnuksen merkkijonona
     */
    public String getPelaajatunnus(int pelaajaindeksi) {
        return this.pelaajatunnukset[pelaajaindeksi];
    }

    /**
     * Tämä metodi palauttaa annetun indeksin osoittaman hahmon tunnuksen.
     * @param hahmoindeksi kokonaisluku, joka osoittaa sen hahmon, jonka 
     * tunnus palautetaan
     * @return metodi palauttaa hahmon tunnuksen merkkijonona
     */
    public String getHahmotunnus(int hahmoindeksi) {
        return this.hahmotunnukset[hahmoindeksi];
    }

    /**
     * Tämä metodi palauttaa valitun hahmon ja pelaajan välisen 
     * yhteensopivuusprosentin.
     * @param pelaajaindeksi kokonaisluku joka osoittaa valitun pelaajan 
     * indeksin
     * @param hahmoindeksi kokonaisluku joka osoittaa valitun hahmon indeksin
     * @return metodi palauttaa yhteensopivuusprosentin kokonaislukuna (0-100)
     */
    public int getSopivuusprosentti(int pelaajaindeksi, int hahmoindeksi) {
        return this.sopivuusprosentit[pelaajaindeksi][hahmoindeksi];
    }
    
    /**
     * Tämä metodi palauttaa yhteensopivuustietojen mukaisen pelaajamäärän.
     * @return metodi palauttaa pelaajamäärän kokonaislukuna
     */
    public int getPelaajamaara() {
        return this.pelaajamaara;
    }
    
    /**
     * Tämä metodi palauttaa yhteensopivuustietojen mukaisen hahmomäärän.
     * @return metodi palauttaa hahmomäärän kokonaislukuna
     */
    public int getHahmomaara() {
        return this.hahmomaara;
    }
    
    /**
     * Tämä metodi palauttaa valitun pelaajan hahmoehdokaslistan.
     * @param pelaaja kokonaisluku joka osoittaa valitun pelaajan indeksin
     * @return metodi palauttaa hahmoehdokaslistan sisältävän Ehdokaslista-olion
     */
    public Ehdokaslista getHahmoehdokaslista(int pelaaja) {
        return this.pelaajienHahmoehdokkaat[pelaaja];
    }
    
    /**
     * Tämä metodi palauttaa valitun hahmon pelaajaehdokaslistan.
     * @param hahmo kokonaisluku joka osoittaa valitun hahmon indeksin
     * @return metodi palauttaa pelaajaehdokaslistan sisältävän 
     * Ehdokaslista-olion
     */
    public Ehdokaslista getPelaajaehdokaslista(int hahmo) {
        return this.hahmojenPelaajaehdokkaat[hahmo];
    }
    
    // Setters
    
    /**
     * Tämä metodi lisää annetun pelaajatunnuksen pelaajatunnusluetteloon 
     * indeksin osoittamalle kohdalle.
     * @param pelaajaindeksi kokonaisluku, joka osoittaa indeksin johon
     * pelaajatunnus yhdistetään
     * @param pelaajatunnus lisättävä pelaajatunnus merkkijonona
     */
    public void setPelaajatunnus(int pelaajaindeksi, String pelaajatunnus) {
        this.pelaajatunnukset[pelaajaindeksi] = pelaajatunnus; 
    }

    /**
     * Tämä metodi lisää annetun hahmotunnuksen hahmotunnusluetteloon 
     * indeksin osoittamalle kohdalle.
     * @param hahmoindeksi kokonaisluku, joka osoittaa indeksin johon 
     * hahmotunnus yhdistetään
     * @param hahmotunnus lisättävä hahmotunnus merkkijonona
     */
    public void setHahmotunnus(int hahmoindeksi, String hahmotunnus) {
        this.hahmotunnukset[hahmoindeksi] = hahmotunnus;
    }

    /**
     * Tämä metodi lisää valitun pelaajan ja hahmon välisen sopivuusprosentin 
     * sopivuusmatriisiin.
     * @param pelaajaindeksi kokonaisluku joka osoittaa valitun pelaajan 
     * indeksin 
     * @param hahmoindeksi kokonaisluku joka osoittaa valitun hahmon indeksin
     * @param sopivuus sopivuusprosentti kokonaislukuna (0-100)
     */
    public void setSopivuusprosentti(int pelaajaindeksi, int hahmoindeksi, int sopivuus) {
        this.sopivuusprosentit[pelaajaindeksi][hahmoindeksi] = sopivuus;
    }
    
    /**
     * Tämä metodi lisää valitun pelaajan hahmoehdokaslistan hahmoehdokas-
     * listojen luetteloon.
     * @param pelaaja kokonaisluku joka osoittaa valitun pelaajan indeksin
     * @param hahmoehdokkaat Ehdokaslista-olio joka sisältää pelaajan hahmo-
     * ehdokaslistan
     */
    public void setHahmoehdokaslista(int pelaaja, Ehdokaslista hahmoehdokkaat) {
        this.pelaajienHahmoehdokkaat[pelaaja] = hahmoehdokkaat;
    }

    /**
     * Tämä metodi lisää valitun hahmon pelaajaehdokaslistan pelaajaehdokas-
     * listojen luetteloon.
     * @param hahmo kokonaisluku joka osoittaa valitun hahmon indeksin
     * @param pelaajaehdokkaat Ehdokaslista-olio joka sisältää hahmon pelaaja-
     * ehdokaslistan
     */    
    public void setPelaajaehdokaslista(int hahmo, Ehdokaslista pelaajaehdokkaat) {
        this.hahmojenPelaajaehdokkaat[hahmo] = pelaajaehdokkaat;
    }
}
