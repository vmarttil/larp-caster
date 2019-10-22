/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Tämä luokka määrittelee yksittäisen hahmon tai pelaajan ehdokaslistan, 
 * joka sisältää luettelon kaikista sopivista pelaajista tai hahmoista niiden 
 * sopivuusjärjestyksessä sekä laskurin käsiteltyjen ehdokkaiden seuraamiseksi.
 * @author Ville Marttila
 */
public class Ehdokaslista {
    private int[] ehdokaslista;
    private int[] yhteensopivuuslista;
    
    /**
     * Tämä metodi luo Ehdokaslista-olion, joka sisältää tiedot yhden hahmon tai 
     * pelaajan mahdollisista pelaaja- tai hahmoehdokkaista sekä niiden yhteensopivuuksista.
     * @param hahmojako Hahmojako-olio, joka sisältää sen hahmojaon tiedot johon 
     * tämä ehdokaslista liittyy
     * @param rooli merkkijono 'pelaaja' tai 'hahmo' joka kertoo, onko kyseessä 
     * pelaajan hahmoehdokaslista vai hahmon pelaajaehdokaslista
     * @param indeksi kokonaisluku, joka kertoo sen pelaajan tai hahmon 
     * indeksin jolle ehdokaslista kuuluu
     */
    public Ehdokaslista(Hahmojako hahmojako, String rooli, int indeksi) {
        if (rooli.equals("hahmo")) {
            this.ehdokaslista = new int[hahmojako.getYhteensopivuusdata().getPelaajamaara()];
            this.yhteensopivuuslista = new int[hahmojako.getYhteensopivuusdata().getPelaajamaara()];
        } else {
            this.ehdokaslista = new int[hahmojako.getYhteensopivuusdata().getHahmomaara()];
            this.yhteensopivuuslista = new int[hahmojako.getYhteensopivuusdata().getHahmomaara()];
        }
        
        
        
        
        
        HashMap<Integer, Integer> naapurit = new HashMap<>();
        if (rooli.equals("pelaaja")) {
            for (int i = 1; i <= hahmojako.getYhteensopivuusdata().getPelaajamaara(); i++) {
                if (hahmojako.getYhteensopivuusdata().getSopivuusprosentti(indeksi, i) >= hahmojako.getMinimisopivuus()) {
                    naapurit.put(i, hahmojako.getYhteensopivuusdata().getSopivuusprosentti(indeksi, i));
                }
            }
            if (naapurit.size() == 0) {
                hahmojako.setEhdokaslistatOK(false);
                System.out.println("VAROITUS: Pelaajalle " + hahmojako.getYhteensopivuusdata().getPelaajatunnus(indeksi) + " ei löydy yhtään minimisopivuuden ylittävää hahmoa.");
            }
        } else if (rooli.equals("hahmo")) {
            for (int i = 1; i <= hahmojako.getYhteensopivuusdata().getPelaajamaara(); i++) {
                if (hahmojako.getYhteensopivuusdata().getSopivuusprosentti(i, indeksi) >= hahmojako.getMinimisopivuus()) {
                    naapurit.put(i, hahmojako.getYhteensopivuusdata().getSopivuusprosentti(i, indeksi));
                }
            }
            if (naapurit.size() == 0) {
                hahmojako.setEhdokaslistatOK(false);
                System.out.println("VAROITUS: Hahmolle " + hahmojako.getYhteensopivuusdata().getHahmotunnus(indeksi) + " ei löydy yhtään minimisopivuuden ylittävää pelaajaa.");
            }
        }
        this.ehdokaslista = naapurit.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).map(Map.Entry::getKey).mapToInt(i->i).toArray();
    }

    /**
     * Tämä metodi luo itsenäisen kopion annetusta ehdokaslistasta
     * @param lahde kopioitava ehdokaslista
     */
    public Ehdokaslista(Ehdokaslista lahde) {
        ehdokaslista = new int[lahde.getPituus()];
        for (int i = 0; i < lahde.getPituus(); i++) {
            ehdokaslista[i] = lahde.getEhdokas(i);
        }
    }
    
    /**
     * Tämä metodi palauttaa ehdokaslistan pituuden.
     * @return metodi palauttaa ehdokaslistan pituuden kokonaislukuna
     */
    public int getPituus() {
        return this.ehdokaslista.length;
    }
    
    /**
     * Tämä metodi palauttaa indeksin osoittaman ehdokkaan.
     * @param index indeksi joka osoittaa palautettavan ehdokkaan
     * @return metodi palauttaa valitun ehdokkaan indeksinumeron kokonaislukuna
     */
    public int getEhdokas(int index) {
        return this.ehdokaslista[index];
    }
    
    /**
     * Tämä metodi asettaa annetun ehdokkaan indeksin osoittaman kohdan 
     * ehdokkaaksi, korvaten aiemman ehdokkaan.
     * @param ehdokas asetettavan ehdokkaan indeksinumero
     * @param index indeksi joka osoittaa asetettavan ehdokkaan paikan
     */
    public void setEhdokas(int ehdokas, int index) {
        this.ehdokaslista[index] = ehdokas;
    }
    
    /**
     * Tämä metodi poistaa annetun indeksin kohdalla olevan ehdokkaan listasta, 
     * siirtää seuraavia ehdokkaita yhden askelen ylöspäin ja lyhentää listan.
     * @param index indeksi jonka kohdalta ehdokas poistetaan
     */
    
    public void poistaEhdokas(int index) {
        int[] uusiLista = new int[this.ehdokaslista.length - 1];
        for (int i = 0; i < index; i++) {
            uusiLista[i] = this.ehdokaslista[i];
        }
        for (int i = index + 1; i < this.ehdokaslista.length; i++) {
            uusiLista[i - 1] = this.ehdokaslista[i];
        }
        this.ehdokaslista = uusiLista;
    }
    
    /**
     * Tämä metodi korvaa ehdokaslistan sisällön uusilla ehdokkailla
     * @param ehdokkaat HashMap-objekti joka sisältää avaimina ehdokkaiden
     * indeksit ja arvoina niiden yhteensopivuusarvot
     */
    public void korvaaLista(HashMap<Integer,Integer> ehdokkaat) {
        this.ehdokaslista = ehdokkaat.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).map(Map.Entry::getKey).mapToInt(i->i).toArray();
    }
    
    
}

