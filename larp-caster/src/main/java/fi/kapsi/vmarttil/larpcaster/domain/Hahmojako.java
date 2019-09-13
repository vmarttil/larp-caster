/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Ville
 */
public class Hahmojako {
    
    private Sopivuusmatriisi yhteensopivuusdata;
    private ArrayList<Tulos> tulokset;
    private String algoritmi;
    private int minimisopivuus;
    // Lisätään toteutukseen myöhemmin
    private boolean priorisoiHankalatHahmot;
    // Lisätään toteutukseen myöhemmin
    private ArrayList<Integer> hankalatHahmot;
    // Lisätään toteutukseen myöhemmin
    private HashMap<Integer,Integer> esivalitutPelaajat;
   
    public Hahmojako() {
        this.yhteensopivuusdata = null;
        this.tulokset = new ArrayList<>();
        this.algoritmi = "";
        this.minimisopivuus = 50;
        // Lisätään toteutukseen myöhemmin
        this.priorisoiHankalatHahmot = false;
        // Lisätään toteutukseen myöhemmin
        this.hankalatHahmot = new ArrayList<>();
        // Lisätään toteutukseen myöhemmin
        this.esivalitutPelaajat = new HashMap<>();
    }
     
    // Getters

    public Sopivuusmatriisi getYhteensopivuusdata() {
        return yhteensopivuusdata;
    }

    public ArrayList<Tulos> getTulokset() {
        return tulokset;
    }

    public String getAlgoritmi() {
        return algoritmi;
    }

    public int getMinimisopivuus() {
        return minimisopivuus;
    }

    public boolean isPriorisoiHankalatHahmot() {
        return priorisoiHankalatHahmot;
    }

    public ArrayList<Integer> getHankalatHahmot() {
        return hankalatHahmot;
    }

    public HashMap<Integer, Integer> getEsivalitutPelaajat() {
        return esivalitutPelaajat;
    }
    
    // Setters
    
    public void setYhteensopivuusdata(Sopivuusmatriisi yhteensopivuusdata) {
        this.yhteensopivuusdata = yhteensopivuusdata;
    }

    public void setAlgoritmi(String algoritmi) {
        this.algoritmi = algoritmi;
    }

    public void setMinimisopivuus(int minimisopivuus) {
        this.minimisopivuus = minimisopivuus;
    }

    public void setPriorisoiHankalatHahmot(boolean priorisoiHankalatHahmot) {
        this.priorisoiHankalatHahmot = priorisoiHankalatHahmot;
    }

    // Lisäykset ja poistot
    
    public void lisaaTulos(Tulos tulos) {
        this.tulokset.add(tulos);
    }
    
    public void poistaTulos(int tuloksenIndeksi) {
        this.tulokset.remove(tuloksenIndeksi);
    }
    
    public void lisaaHankalaHahmo(int hahmotunnus) {
        this.hankalatHahmot.add(hahmotunnus);
    }
    
    public void tyhjennaHankalatHahmot() {
        this.hankalatHahmot.clear();
    }
    
    public void esivalitsePelaaja(int hahmotunnus, int pelaajatunnus) {
        this.esivalitutPelaajat.put(hahmotunnus, pelaajatunnus);
    }
    
    public void poistaEsivalittuPelaaja(int hahmotunnus) {
        this.esivalitutPelaajat.remove(hahmotunnus);
    }
    
    // Operaatiot
    
    public void lataaYhteensopivuustiedot(String tiedostonimi) {
        // Tähän tiedoston lataamiseen ja sopivuusmatriisin luomiseen liittyvä koodi
    }
    
    
    public void teeHahmojako() {
        Tulos tulos = null;
        if (this.algoritmi.equals("galeShapleyPelaajaKosii")) {
            tulos = Algoritmit.galeShapleyHahmoKosii(this.yhteensopivuusdata, this.minimisopivuus);
        } else if (this.algoritmi.equals("galeShapleyPelaajaKosii")) {
            tulos = Algoritmit.galeShapleyPelaajaKosii(this.yhteensopivuusdata, this.minimisopivuus);
        }
        this.tulokset.add(tulos);
    }
    
    
}
