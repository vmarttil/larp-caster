/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.algorithms;

import fi.kapsi.vmarttil.larpcaster.domain.Hahmojako;
import fi.kapsi.vmarttil.larpcaster.domain.Sopivuusmatriisi;
import fi.kapsi.vmarttil.larpcaster.domain.Tulos;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author Ville
 */
public class Peruuttava {
    private Hahmojako hahmojako;
    private String kaytettavaAlgoritmi;
    private Sopivuusmatriisi yhteensopivuusdata;
    private int minimisopivuus;
    private int pelaajamaara;
    private int hahmomaara;
    private long aloitusAika;
    private int[] hahmojenValinnat;
    private boolean[] vapaatPelaajat;
    private int jarjestysnumero;
    private Instant edellinenLoytohetki;
    private ArrayList<Tulos> tulokset;
    private boolean lopetus;
    
    /**
     * Tämä metodi luo Peruuttava-olion joka laskee hahmojaon käyttämällä 
     * optimoitua peruuttavaa hakua.
     * @param hahmojako Hahmojako-olio joka sisältää sen hahmojaon tiedot, johon 
     * tämä laskenta liittyy
     */
    public Peruuttava(Hahmojako hahmojako) {
        this.hahmojako = hahmojako;
        this.kaytettavaAlgoritmi = this.hahmojako.getKaytettavaAlgoritmi();
        this.yhteensopivuusdata = this.hahmojako.getYhteensopivuusdata();
        this.pelaajamaara = this.yhteensopivuusdata.getPelaajamaara();
        this.hahmomaara = this.yhteensopivuusdata.getHahmomaara();
        this.minimisopivuus = this.hahmojako.getMinimisopivuus();
        this.tulokset = new ArrayList<>();
        this.jarjestysnumero = 0;
        this.lopetus = false;
        alustaTaulukot();
    }

    /**
     * Tämä metodi käynnistää rekursiivisen hahmojakojen laskennan peruuttavalla 
     * hakualgoritmilla.
     * @return tämä metodi palauttaa TreeMap-tietorakenteen, joka sisältää 
     * sopivuusjärjestyksessä joukon Tulos-olioita joista kukin sisältää 
     * lasketun hahmojaon
     */
    public ArrayList<Tulos> laskeHahmojako() {
        this.aloitusAika = System.nanoTime();
        int hahmo = 1;
        this.edellinenLoytohetki = hahmojako.getSuorituksenAloitus();
        etsiRatkaisu(hahmo);
        Collections.sort(this.tulokset);
        Collections.reverse(this.tulokset);
        ArrayList<Tulos> tulosluettelo;
        System.out.println("Ratkaisuja laskettu: " + this.tulokset.size());
        if (this.tulokset.size() > 100) {
            tulosluettelo = new ArrayList<Tulos>(this.tulokset.subList(0, 100));
        } else {
            tulosluettelo = this.tulokset;
        }
        return tulosluettelo;
    }
    
    /**
     * Tämä metodi suorittaa hahmojakojen rekursiivisen haun kutsumalla itseään
     * @param hahmo sen hahmon indeksi, jolle etsitään pelaajaa
     * @param aloitusAika kulloisenkin hahmojaon laskennan aloitusaika
     */
    private void etsiRatkaisu(int hahmo) {
        if (this.lopetus == true) {
            return;
        }
        if (hahmo == this.hahmomaara + 1) {
            kirjaaTulos();
            this.aloitusAika = System.nanoTime();
            return;
        }
        for (int i = 0; i < this.yhteensopivuusdata.getPelaajaehdokaslista(hahmo).getPituus(); i++) {
            if (this.vapaatPelaajat[this.yhteensopivuusdata.getPelaajaehdokaslista(hahmo).getEhdokas(i)] == true) {
                this.hahmojenValinnat[hahmo] = this.yhteensopivuusdata.getPelaajaehdokaslista(hahmo).getEhdokas(i);
                this.vapaatPelaajat[this.yhteensopivuusdata.getPelaajaehdokaslista(hahmo).getEhdokas(i)] = false;
                etsiRatkaisu(hahmo + 1);
                this.hahmojenValinnat[hahmo] = 0;
                this.vapaatPelaajat[this.yhteensopivuusdata.getPelaajaehdokaslista(hahmo).getEhdokas(i)] = true;
            }
        } 
    }

    private void kirjaaTulos() {
        long lopetusAika = System.nanoTime();
        double kulunutAika = (double) ((int) (lopetusAika - this.aloitusAika) / 10000) / 100;
        this.jarjestysnumero++;
        tallennaTulos();
        Instant tuloksenLoytohetki = Instant.now();
        if (Duration.between(this.edellinenLoytohetki, tuloksenLoytohetki).getSeconds() > 1) {
            this.lopetus = true;
        }
        if (kulunutAika > 1.0) {
            this.lopetus = true;
        }
        if (this.tulokset.size() > 30000) {
            this.lopetus = true;
        }
         Duration tuloksenLoytoaika = Duration.between(hahmojako.getSuorituksenAloitus(), tuloksenLoytohetki);
         System.out.println(this.jarjestysnumero + " tulosta löydetty ajassa " + tuloksenLoytoaika.getSeconds() + " sekuntia");
         this.edellinenLoytohetki = tuloksenLoytohetki;
    }
    
    
    private void alustaTaulukot() {
        this.hahmojenValinnat = new int[this.hahmomaara + 1];
        for (int i = 1; i <= this.hahmomaara; i++) {
            this.hahmojenValinnat[i] = 0;
        }
        this.vapaatPelaajat = new boolean[this.pelaajamaara + 1];
        for (int i = 1; i <= this.pelaajamaara; i++) {
            this.vapaatPelaajat[i] = true;
        }
    }
    
    private void tallennaTulos() {
        int[] pelaajienValinnat = new int[this.pelaajamaara + 1];
        for (int i = 1; i <= this.pelaajamaara; i++) {
            pelaajienValinnat[i] = 0;
        }
        for (int i = 1; i <= this.hahmomaara; i++) {
            int pelaaja = this.hahmojenValinnat[i];
            pelaajienValinnat[pelaaja] = i;
        }
        Tulos tulos = new Tulos(this.yhteensopivuusdata, pelaajienValinnat, this.hahmojenValinnat);
        boolean kopio = false;
        for (Tulos vanhaTulos : this.tulokset) {
            if (tulos.equals(vanhaTulos)) {
                kopio = true;
                break;
            }
        }
        if (kopio == false) {
            tulos.setAlgoritmi("peruuttava");
            tulos.setMinimiyhteensopivuus(this.minimisopivuus);
            tulos.setJarjestysnumero(this.jarjestysnumero);
            this.tulokset.add(tulos);
        }
    }    
}