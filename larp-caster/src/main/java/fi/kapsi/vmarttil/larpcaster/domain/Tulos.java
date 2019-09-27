/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Tämä luokka määrittelee hahmojaon tuloksen ja siihen liittyvien metatietojen 
 * tallentamiseen käytettävät tietorakenteet.
 * @author Ville Marttila
 */
public class Tulos {
    private TreeMap<Integer, Integer> pelaajienHahmot;
    private TreeMap<Integer, Integer> hahmojenPelaajat;
    private TreeSet<Integer> hahmottomatPelaajat;
    private TreeSet<Integer> pelaajattomatHahmot;
    private String algoritmi;
    private int kierroksia;
    int minimiyhteensopivuus;
    double kulunutAika;
    
    /**
     * Tämä metodi luo Tulos-olion, johon tallennetaan yhden hahmojaon tulokset 
     * ja siihen liittyvät metatiedot.
     */
    public Tulos() {
        this.pelaajienHahmot = new TreeMap<>();
        this.hahmojenPelaajat = new TreeMap<>();
        this.hahmottomatPelaajat = new TreeSet<>();
        this.pelaajattomatHahmot = new TreeSet<>();
    }
    
    // Getters

    
    public TreeMap<Integer, Integer> getHahmojenPelaajat() { 
        return this.hahmojenPelaajat;
    }

    public TreeMap<Integer, Integer> getPelaajienHahmot() { 
        return this.pelaajienHahmot;
    }
    
    public TreeSet<Integer> getHahmottomatPelaajat() {
        return this.hahmottomatPelaajat;
    }
    
    public TreeSet<Integer> getPelaajattomatHahmot() {
        return this.pelaajattomatHahmot;
    }

    public String getAlgoritmi() {
        return this.algoritmi;
    }
    
    public int getKierroksia() {
        return this.kierroksia;
    }

    public int getMinimiyhteensopivuus() {
        return this.minimiyhteensopivuus;
    }

    public double getKulunutAika() {
        return this.kulunutAika;
    }

    // Setters

    public void setAlgoritmi(String algoritmi) {
        this.algoritmi = algoritmi;
    }    
    
    public void setKierroksia(int kierroksia) {
        this.kierroksia = kierroksia;
    }

    public void setMinimiyhteensopivuus(int minimiyhteensopivuus) {
        this.minimiyhteensopivuus = minimiyhteensopivuus;
    }

    public void setKulunutAika(double kulunutAika) {
        this.kulunutAika = kulunutAika;
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
        for (int i = 1; i < pelaajienValinnat.length; i++) {
            if (pelaajienValinnat[i] == 0) {
                hahmottomatPelaajat.add(i);
            } else {
                pelaajienHahmot.put(i, pelaajienValinnat[i]);
            }
        }
        for (int i = 1; i < hahmojenValinnat.length; i++) {
            if (hahmojenValinnat[i] == 0) {
                pelaajattomatHahmot.add(i);
            } else {
                hahmojenPelaajat.put(i, hahmojenValinnat[i]);
            }
        }
    }
}
