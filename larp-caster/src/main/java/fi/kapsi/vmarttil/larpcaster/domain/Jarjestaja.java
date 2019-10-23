/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

import java.util.Arrays;

/**
 * Tämä luokka toteuttaa kokonaislukutaulukon tai -taulukkoparin (synkronoidusti 
 * ensimmäisen annetun taulukon mukaan) järjestämisen suurimmasta pienimpään 
 * käyttäen kekojärjestämistä.
 * @author Ville Marttila
 */

public class Jarjestaja {

    /**
     * Tämä metodi järjestää yksittäisen kokonaislukutaulukon sisällön 
     * suurimmasta pienimpään
     * @param taulukkoA järjestettävä kokonaislukutaulukko
     */
    public void jarjesta(int[] taulukkoA) {
        int koko = taulukkoA.length;
        for (int i = koko / 2 - 1; i >= 0; i--) {
            rakennaKeko(taulukkoA, koko, i);
        }
        for (int i=koko-1; i>=0; i--) {
            int x = taulukkoA[0];
            taulukkoA[0] = taulukkoA[i];
            taulukkoA[i] = x;
            rakennaKeko(taulukkoA, i, 0);
        }
    }

    /**
     * Tämä metodi rakentaa rekursiivisesti taulukon A solmun i alla olevan alipuun.
     * @param taulukkoA järjestettävä taulukko
     * @param keonKoko jarjestettavan taulukon ja siihen käytettävän keon koko
     * @param i operaation juurisolmu
     */
    void rakennaKeko(int[] taulukkoA, int keonKoko, int i) {
        int pienin = i; // Alustetaan pienin juureksi
        int vasemmanLapsenIndeksi  = 2*i + 1; 
        int oikeanLapsenIndeksi  = 2*i + 2;
        if (vasemmanLapsenIndeksi < keonKoko && taulukkoA[vasemmanLapsenIndeksi] < taulukkoA[pienin]) {
            pienin = vasemmanLapsenIndeksi;
        }
        if (oikeanLapsenIndeksi < keonKoko && taulukkoA[oikeanLapsenIndeksi] < taulukkoA[pienin]) {
            pienin = oikeanLapsenIndeksi;
        }
        if (pienin != i) {
            int vaihdettava = taulukkoA[i];
            taulukkoA[i] = taulukkoA[pienin];
            taulukkoA[pienin] = vaihdettava;
            rakennaKeko(taulukkoA, keonKoko, pienin);
        }
    }
    
    /**
     * Tämä metodi järjestää kokonaislukutaulukkoparin sisällöt synkronisesti 
     * suurimmasta pienimpään
     * @param taulukkoA järjestämisen perusteena toimiva kokonaislukutaulukko
     * @param taulukkoB taulukon A mukana järjestettävä taulukko
     */
    public int[] jarjesta(int[] taulukkoA, int[] taulukkoB) {
        int koko = taulukkoA.length;
        for (int i = koko / 2 - 1; i >= 0; i--) {
            rakennaKeko(taulukkoA, taulukkoB, koko, i);
        }
        for (int i=koko-1; i>=0; i--) {
            int x = taulukkoA[0];
            int y = taulukkoB[0];
            taulukkoA[0] = taulukkoA[i];
            taulukkoB[0] = taulukkoB[i];
            taulukkoA[i] = x;
            taulukkoB[i] = y;
            rakennaKeko(taulukkoA, taulukkoB, i, 0);
        }
        return taulukkoA;
    }
    
    /** 
     * Tämä metodi rakentaa rekursiivisesti taulukoiden A ja B solmujenn i alla 
     * olevat alipuut.
     * @param taulukkoA järjestämisen perusteena toimiva taulukko
     * @param taulukkoB taulukon A mukana järjestettävä taulukko
     * @param keonKoko jarjestettavan taulukon ja siihen käytettävän keon koko
     * @param i operaation juurisolmu
     */
    void rakennaKeko(int[] taulukkoA, int[] taulukkoB, int keonKoko, int i) {
        int pienin = i;
        int vasemmanLapsenIndeksi  = 2*i + 1; 
        int oikeanLapsenIndeksi  = 2*i + 2;
        if (vasemmanLapsenIndeksi < keonKoko && taulukkoA[vasemmanLapsenIndeksi] < taulukkoA[pienin]) {
            pienin = vasemmanLapsenIndeksi;
        }
        if (oikeanLapsenIndeksi < keonKoko && taulukkoA[oikeanLapsenIndeksi] < taulukkoA[pienin]) {
            pienin = oikeanLapsenIndeksi;
        }
        if (pienin != i) {
            int vaihdettavaA = taulukkoA[i];
            int vaihdettavaB = taulukkoB[i];
            taulukkoA[i] = taulukkoA[pienin];
            taulukkoB[i] = taulukkoB[pienin];
            taulukkoA[pienin] = vaihdettavaA;
            taulukkoB[pienin] = vaihdettavaB;
            rakennaKeko(taulukkoA, taulukkoB, keonKoko, pienin);
        }
    }   
}