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
 *
 * @author Ville
 */
public class Ehdokaslista {
    private int[] lista;
    private int seuraava;
    
    public Ehdokaslista(Hahmojako hahmojako, Sopivuusmatriisi sopivuusprosentit, int minimisopivuus, String rooli, int indeksi) {
        HashMap<Integer, Integer> naapurit = new HashMap<>();
        if (rooli.equals("pelaaja")) {
            for (int i=1; i<=sopivuusprosentit.getPelaajamaara(); i++) {
                if (sopivuusprosentit.getSopivuusprosentti(indeksi, i) >= minimisopivuus) {
                    naapurit.put(i, sopivuusprosentit.getSopivuusprosentti(indeksi, i));
                }
            }
            if (naapurit.size() == 0) {
                hahmojako.setEhdokaslistatOK(false);
                System.out.println("VAROITUS: Pelaajalle " + sopivuusprosentit.getPelaajatunnus(indeksi) + " ei löydy yhtään minimisopivuuden ylittävää hahmoa.");
            }
        } else if (rooli.equals("hahmo")) {
            for (int i=1; i<=sopivuusprosentit.getPelaajamaara(); i++) {
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
        this.seuraava = 0;
    }
    
    public int seuraavaEhdokas() {
        this.seuraava++;
        if (this.seuraava > lista.length) {
            return 0;
        } else {
            return this.lista[this.seuraava-1];
        }
    }
    
    public void nollaaSeuraava() {
        this.seuraava = 0;
    }
}
