/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

import java.util.TreeMap;

/**
 *
 * @author Ville
 */
public class Ehdokaslista {
    private int[] lista;
    private int seuraava;
    
    public Ehdokaslista(Sopivuusmatriisi sopivuusprosentit, int minimisopivuus, String rooli, int indeksi) {
        TreeMap<Integer, Integer> naapurit = new TreeMap<>();
        if (rooli.equals("pelaaja")) {
            for (int i=1; i<=sopivuusprosentit.getPelaajamaara()+1; i++) {
                if (sopivuusprosentit.getSopivuusprosentti(indeksi, i) >= minimisopivuus) {
                    naapurit.put(sopivuusprosentit.getSopivuusprosentti(indeksi, i), i);
                }
            }
        } else if (rooli.equals("hahmo")) {
            for (int i=1; i<=sopivuusprosentit.getPelaajamaara()+1; i++) {
                if (sopivuusprosentit.getSopivuusprosentti(i, indeksi) >= minimisopivuus) {
                    naapurit.put(sopivuusprosentit.getSopivuusprosentti(i, indeksi), i);
                }
            }
        }
        this.lista = new int[naapurit.size()];
        for (int i=0; i < naapurit.size(); i++) {
            this.lista[i] = naapurit.pollLastEntry().getValue();
        }
        this.seuraava = 0;
    }
    
    public int seuraavaEhdokas() {
        this.seuraava++;
        return this.lista[this.seuraava-1];
    }
}
