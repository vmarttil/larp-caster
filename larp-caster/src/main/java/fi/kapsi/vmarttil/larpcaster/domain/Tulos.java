/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Tämä luokka määrittelee hahmojaon tuloksen ja siihen liittyvien metatietojen 
 * tallentamiseen käytettävät tietorakenteet.
 * @author Ville Marttila
 */
public class Tulos {
    private HashMap<String,String> pelaajienHahmot;
    private HashMap<String,String> hahmojenPelaajat;
    private ArrayList<String> hahmottomatPelaajat;
    private ArrayList<String> pelaajattomatHahmot;
    private String algoritmi;
    private int kierroksia;
    int minimiyhteensopivuus;
    double kulunutAika;
    
    public Tulos() {
        this.pelaajienHahmot = new HashMap<>();
        this.hahmojenPelaajat = new HashMap<>();
        this.hahmottomatPelaajat = new ArrayList<>();
        this.pelaajattomatHahmot = new ArrayList<>();
    }
    
    // Getters

    public HashMap<String,String> getHahmojenPelaajat() { 
        return this.hahmojenPelaajat;
    }

    public String getHahmonPelaaja(String hahmo) {
        return this.hahmojenPelaajat.get(hahmo);
    }
    
    public HashMap<String,String> getPelaajienHahmot() { 
        return this.pelaajienHahmot;
    }

    public String getPelaajanHahmo(String pelaaja) {
        return this.pelaajienHahmot.get(pelaaja);
    }
    
    public ArrayList<String> getHahmottomatPelaajat() {
        return this.hahmottomatPelaajat;
    }
    
        public ArrayList<String> getPelaajattomatHahmot() {
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
    
    public void taytaTulokset(Sopivuusmatriisi sopivuusmatriisi, int[] pelaajienValinnat, int[] hahmojenValinnat) {
        for (int i = 1; i < pelaajienValinnat.length; i++) {
            if (pelaajienValinnat[i] == 0) {
                hahmottomatPelaajat.add(sopivuusmatriisi.getPelaajatunnus(i));
            } else {
                pelaajienHahmot.put(sopivuusmatriisi.getPelaajatunnus(i), sopivuusmatriisi.getHahmotunnus(pelaajienValinnat[i]));
            }
        }
        for (int i = 1; i < hahmojenValinnat.length; i++) {
            if (hahmojenValinnat[i] == 0) {
                pelaajattomatHahmot.add(sopivuusmatriisi.getHahmotunnus(i));
            } else {
                hahmojenPelaajat.put(sopivuusmatriisi.getHahmotunnus(i), sopivuusmatriisi.getPelaajatunnus(hahmojenValinnat[i]));
            }
        }
    }
}
