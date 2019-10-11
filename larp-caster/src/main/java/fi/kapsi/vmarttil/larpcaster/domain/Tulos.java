/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Tämä luokka määrittelee hahmojaon tuloksen ja siihen liittyvien metatietojen 
 * tallentamiseen käytettävät tietorakenteet.
 * @author Ville Marttila
 */
public class Tulos implements Comparable<Tulos> {
    private TreeMap<Integer, Integer> pelaajienHahmot;
    private TreeMap<Integer, Integer> hahmojenPelaajat;
    private TreeSet<Integer> hahmottomatPelaajat;
    private TreeSet<Integer> pelaajattomatHahmot;
    private String algoritmi;
    int minimiyhteensopivuus;
    int huonoinSopivuus;
    double sopivuuskeskiarvo;
    double mediaanisopivuus;
    int parasSopivuus;
    int jarjestysnumero;
    int prioriteetti;
    
    /**
     * Tämä metodi luo Tulos-olion, johon tallennetaan yhden hahmojaon tulokset 
     * ja siihen liittyvät metatiedot.
     */
    public Tulos() {
        this.pelaajienHahmot = new TreeMap<>();
        this.hahmojenPelaajat = new TreeMap<>();
        this.hahmottomatPelaajat = new TreeSet<>();
        this.pelaajattomatHahmot = new TreeSet<>();
        this.huonoinSopivuus = 100;
        this.sopivuuskeskiarvo = 0;
        this.mediaanisopivuus = 0;
        this.parasSopivuus = 0;
    }
    
    // Getters
    
    /**
     * Tämä metodi palauttaa tiedot hahmoille valituista pelaajista.
     * @return metodi palauttaa kokonaislukupareja sisältävän TreeMap-olion, 
     * jossa avain sisältää hahmon indeksinumeron ja arvo sille valitun pelaajan
     * indeksinumeron
     */
    public TreeMap<Integer, Integer> getHahmojenPelaajat() { 
        return this.hahmojenPelaajat;
    }

    /**
     * Tämä metodi palauttaa tiedot pelaajille valituista hahmoista.
     * @return metodi palauttaa kokonaislukupareja sisältävän TreeMap-olion, 
     * jossa avain sisältää pelaajan indeksinumeron ja arvo sille valitun hahmon
     * indeksinumeron
     */
    public TreeMap<Integer, Integer> getPelaajienHahmot() { 
        return this.pelaajienHahmot;
    }
    
    /**
     * Tämä metodi palauttaa listan pelaajista, joille ei löytynyt hahmoa.
     * @return metodi palauttaa TreeSet-olion, joka sisältää pelaajien 
     * indeksinumerot kokonaislukuina
     */
    public TreeSet<Integer> getHahmottomatPelaajat() {
        return this.hahmottomatPelaajat;
    }
    
    /**
     * Tämä metodi palauttaa listan hahmoista, joille ei löytynyt pelaajaa.
     * @return metodi palauttaa TreeSet-olion, joka sisältää hahmojen 
     * indeksinumerot kokonaislukuina
     */
    public TreeSet<Integer> getPelaajattomatHahmot() {
        return this.pelaajattomatHahmot;
    }

    /**
     * Tämä metodi palauttaa tuloksen edustamaan hahmojakoon käytetyn 
     * algoritmin.
     * @return metodi palauttaa käytetyn algoritmin tunnuksen merkkijonona
     */
    public String getAlgoritmi() {
        return this.algoritmi;
    }
 
    /**
     * Tämä metodi palauttaa tuloksen edustamassa hahmojaossa käytetyn 
     * minimiyhteensopivuuden.
     * @return metodi palauttaa minimiyhteensopivuusprosentin kokonaislukuna
     */
    public int getMinimiyhteensopivuus() {
        return this.minimiyhteensopivuus;
    }

    public double getHuonoinSopivuus() {
        return this.huonoinSopivuus;
    }
    
    /**
     * Tämä metodi palauttaa hahmojaon keskimääräisen sopivuuden.
     * @return metodi palauttaa keskimääräisen sopivuuden liukulukuna
     */
    public double getSopivuuskeskiarvo() {
        return this.sopivuuskeskiarvo;
    }

    public double getMediaanisopivuus() {
        return this.mediaanisopivuus;
    }
    
    public double getParasSopivuus() {
        return this.parasSopivuus;
    }
    
    public int getPrioriteetti() {
        return this.prioriteetti;
    }
    
    public int getJarjestysnumero() {
        return this.jarjestysnumero;
    }
    
    // Setters

    /**
     * Tämä metodi tallentaa tulokseen sen edustamaan hahmojakoon käytetyn 
     * algoritmin.
     * @param algoritmi käytetyn algoritmin tunnus
     */
    public void setAlgoritmi(String algoritmi) {
        this.algoritmi = algoritmi;
    }    

    /**
     * Tämä metodi tallentaa tulokseen sen edustamassa hahmojaossa käytetyn 
     * minimiyhteensopivuusprosentin.
     * @param minimiyhteensopivuus käytetyn algoritmin tunnus
     */
    public void setMinimiyhteensopivuus(int minimiyhteensopivuus) {
        this.minimiyhteensopivuus = minimiyhteensopivuus;
    }

    public void setPrioriteetti(int prioriteetti) {
        this.prioriteetti = prioriteetti;
    }
    
    public void setJarjestysnumero(int jarjestysnumero) {
        this.jarjestysnumero = jarjestysnumero;
    }
    
    /**
     * Tämä metodi tallentaa valitun algoritmin laskemat hahmojaon tulokset 
     * Tulos-olion taulukoihin.
     * @param sopivuusmatriisi hahmojaossa käytetyt yhteensopivuustiedot 
     * sisältävä Sopivuusmatriisi-olio
     * @param pelaajienValinnat taulukko, joka sisältää kullekin pelaajalle 
     * hahmojaossa valitun hahmon
     * @param hahmojenValinnat taulukko, joka sisältää kullekin hahmolle 
     * hahmojaossa valitun pelaajan
     */
    public void taytaTulokset(Sopivuusmatriisi sopivuusmatriisi, int[] pelaajienValinnat, int[] hahmojenValinnat) {
        int[] sopivuudet = new int[sopivuusmatriisi.getHahmomaara()];
        int kokonaissopivuus = 0;
        for (int i = 1; i < pelaajienValinnat.length; i++) {
            if (pelaajienValinnat[i] > sopivuusmatriisi.getHahmomaara() || pelaajienValinnat[i] == 0) {
                hahmottomatPelaajat.add(i);
            } else {
                pelaajienHahmot.put(i, pelaajienValinnat[i]);
            }
        }
        for (int i = 1; i < hahmojenValinnat.length; i++) {
            if (i > sopivuusmatriisi.getHahmomaara()) {  
            } else if  (hahmojenValinnat[i] == 0) {
                pelaajattomatHahmot.add(i);
            } else {
                hahmojenPelaajat.put(i, hahmojenValinnat[i]);
                int sopivuus = sopivuusmatriisi.getSopivuusprosentti(hahmojenValinnat[i], i);
                sopivuudet[i - 1] = sopivuus;
                kokonaissopivuus = kokonaissopivuus + sopivuus;
                if (sopivuus < this.huonoinSopivuus) {
                    this.huonoinSopivuus = sopivuus;
                }
                if (sopivuus > this.parasSopivuus) {
                    this.parasSopivuus = sopivuus;
                }
            }
        }
        this.sopivuuskeskiarvo = (double) kokonaissopivuus / sopivuusmatriisi.getHahmomaara();
        Arrays.sort(sopivuudet);
        
        if (sopivuudet.length % 2 == 0) {
            this.mediaanisopivuus = (sopivuudet[sopivuudet.length / 2 - 1] + sopivuudet[sopivuudet.length / 2]) / 2.0;
        } else {
            this.mediaanisopivuus = sopivuudet[sopivuudet.length / 2];
        }
    }
    
    @Override
    public int compareTo( final Tulos o) {
        return Double.compare(this.sopivuuskeskiarvo, o.sopivuuskeskiarvo);
    }
}
