/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Tämä luokka määrittelee yksittäisen hahmon tai pelaajan ehdokaslistan, 
 * joka sisältää luettelon kaikista sopivista pelaajista tai hahmoista niiden 
 * sopivuusjärjestyksessä sekä laskurin käsiteltyjen ehdokkaiden seuraamiseksi.
 * @author Ville Marttila
 */
public class Ehdokaslista {
    private int[] lista;
    
    /**
     * Tämä metodi luo Ehdokaslista-olion, joka sisältää tiedot yhden hahmon tai 
     * pelaajan mahdollisista pelaaja- tai hahmoehdokkaista.
     * @param hahmojako Hahmojako-olio, joka sisältää sen hahmojaon tiedot johon 
     * tämä ehdokaslista liittyy
     * @param sopivuusprosentit Sopivuusmatriisi-olio joka sisältää tiedon 
     * jokaisen hahmon ja pelaajan keskinäisestä yhteensopivuudesta
     * @param minimisopivuus kokonaisluku joka kertoo pienimmän yhteensopivuus-
     * prosentin jolla hahmo tai pelaaja sisällytetään ehdokaslistaan
     * @param rooli merkkijono 'pelaaja' tai 'hahmo' joka kertoo, onko kyseessä 
     * pelaajan hahmoehdokaslista vai hahmon pelaajaehdokaslista
     * @param indeksi kokonaisluku, joka kertoo sen pelaajan tai hahmon 
     * taulukkoindeksin jolle ehdokaslista kuuluu
     */
    public Ehdokaslista(Hahmojako hahmojako, Sopivuusmatriisi sopivuusprosentit, int minimisopivuus, String rooli, int indeksi) {
        HashMap<Integer, Integer> naapurit = new HashMap<>();
        if (rooli.equals("pelaaja")) {
            for (int i = 1; i <= sopivuusprosentit.getPelaajamaara(); i++) {
                if (sopivuusprosentit.getSopivuusprosentti(indeksi, i) >= minimisopivuus) {
                    naapurit.put(i, sopivuusprosentit.getSopivuusprosentti(indeksi, i));
                }
            }
            if (naapurit.size() == 0) {
                hahmojako.setEhdokaslistatOK(false);
                System.out.println("VAROITUS: Pelaajalle " + sopivuusprosentit.getPelaajatunnus(indeksi) + " ei löydy yhtään minimisopivuuden ylittävää hahmoa.");
            }
        } else if (rooli.equals("hahmo")) {
            for (int i = 1; i <= sopivuusprosentit.getPelaajamaara(); i++) {
                if (sopivuusprosentit.getSopivuusprosentti(i, indeksi) >= minimisopivuus) {
                    naapurit.put(i, sopivuusprosentit.getSopivuusprosentti(i, indeksi));
                }
            }
            if (naapurit.size() == 0) {
                hahmojako.setEhdokaslistatOK(false);
                System.out.println("VAROITUS: Hahmolle " + sopivuusprosentit.getHahmotunnus(indeksi) + " ei löydy yhtään minimisopivuuden ylittävää pelaajaa.");
            }
        }
        this.lista = naapurit.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).map(Map.Entry::getKey).mapToInt(i->i).toArray();
    }

    /**
     * Tämä metodi palauttaa ehdokaslistan pituuden.
     * @return metodi palauttaa ehdokaslistan pituuden kokonaislukuna
     */
    public int getPituus() {
        return this.lista.length;
    }
    
    /**
     * Tämä metodi palauttaa indeksin osoittaman ehdokkaan.
     * @param index indeksi joka osoittaa palautettavan ehdokkaan
     * @return metodi palauttaa valitun ehdokkaan indeksinumeron kokonaislukuna
     */
    public int getEhdokas(int index) {
        return this.lista[index];
    }
}
