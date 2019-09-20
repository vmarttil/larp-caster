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
    private int kierroksia;
    int minimiyhteensopivuus;
    double kulunutAika;
    
    public Tulos(Sopivuusmatriisi sopivuusmatriisi, int[] pelaajienValinnat, int[] hahmojenValinnat, int kierroksia, int minimiyhteensopivuus, double kulunutAika) {
        this.pelaajienHahmot = new HashMap<>();
        this.hahmojenPelaajat = new HashMap<>();
        this.hahmottomatPelaajat = new ArrayList<>();
        this.kierroksia = kierroksia;
        this.minimiyhteensopivuus = minimiyhteensopivuus;
        this.kulunutAika = kulunutAika;
        for (int i = 1; i <= pelaajienValinnat.length; i++) {
            if (pelaajienValinnat[i] == 0) {
                hahmottomatPelaajat.add(sopivuusmatriisi.getPelaajatunnus(i));
            } else {
                pelaajienHahmot.put(sopivuusmatriisi.getPelaajatunnus(i), sopivuusmatriisi.getHahmotunnus(pelaajienValinnat[i]));
            }
        }
        for (int i = 1; i <= hahmojenValinnat.length; i++) {
            hahmojenPelaajat.put(sopivuusmatriisi.getHahmotunnus(i), sopivuusmatriisi.getPelaajatunnus(hahmojenValinnat[i]));
        }
    }
    
    // Getters

    public HashMap<String,String> getHahmojenPelaajat() { 
        return hahmojenPelaajat;
    }

    public String getHahmonPelaaja(String hahmo) {
        return hahmojenPelaajat.get(hahmo);
    }
    
    public HashMap<String,String> getPelaajienHahmot() { 
        return pelaajienHahmot;
    }

    public String getPelaajanHahmo(String pelaaja) {
        return pelaajienHahmot.get(pelaaja);
    }
    
    public ArrayList<String> getHahmottomatPelaajat() {
        return hahmottomatPelaajat;
    }

    public int getKierroksia() {
        return kierroksia;
    }

    public int getMinimiyhteensopivuus() {
        return minimiyhteensopivuus;
    }

    public double getKulunutAika() {
        return kulunutAika;
    }
    
}
