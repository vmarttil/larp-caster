/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.algorithms;

import fi.kapsi.vmarttil.larpcaster.domain.Ehdokaslista;
import fi.kapsi.vmarttil.larpcaster.domain.Hahmojako;
import fi.kapsi.vmarttil.larpcaster.domain.Sopivuusmatriisi;
import fi.kapsi.vmarttil.larpcaster.domain.Tulos;
import fi.kapsi.vmarttil.larpcaster.domain.Tulosluettelo;
import java.time.Duration;
import java.time.Instant;

/**
 * Tämä luokka toteuttaa hahmojaon ratkaisuna kohdistusongelmaan 
 * käyttäen useaa peräkkäistä optimoitua peruuttavaa hakua kierroksittain 
 * kasvavilla ehdokaslistoilla siten, että joka kierroksella mukaan sisällytetään uusia, 
 * laskevan sopivuusraja-arvon ylittäviä pelaajaehdokkaita, kunnes kaikki hahmojaot on laskettu, 
 * sopivuusraja-arvo saavuttaa määritetyn minimisopivuuden tai laskettujen hahmojakojen määrä 
 * ylittää 50 000, joista tulokseen tallennetaan keskimääräiseltä sopivuudeltaan 100 parasta.
 * @author Ville Marttila
 */
public class Peruuttava {
    private Hahmojako hahmojako;
    private String kaytettavaAlgoritmi;
    private Sopivuusmatriisi yhteensopivuusdata;
    private int minimisopivuus;
    private int pelaajamaara;
    private int hahmomaara;
    private int sopivuusraja;
    private int ehdokkaidenMinimimaara;
    private Ehdokaslista[] kaytettavatEhdokaslistat;
    private int[] hahmojenValinnat;
    private boolean[] vapaatPelaajat;
    private int jarjestysnumero;
    private Tulosluettelo tulokset;
    private boolean lopetus;
    
    /**
     * Tämä metodi luo Peruuttava-olion joka laskee hahmojaon käyttämällä useaaa peräkkäistä 
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
        this.sopivuusraja = 100;
        this.ehdokkaidenMinimimaara = 1;
        this.kaytettavatEhdokaslistat = new Ehdokaslista[this.hahmomaara + 1];
        this.tulokset = new Tulosluettelo();
        this.jarjestysnumero = 0;
        this.lopetus = false;
    }

    /**
     * Tämä metodi käynnistää sarjan peruuttavia hakuja jotka laskevat kaikki mahdolliset hahmojaot 
     * laajenevilla pelaajaehdokasluetteloilla alkaen vain kunkin hahmon sopivimman pelaajan sisältävistä 
     * luetteloista.
     * @return tämä metodi palauttaa taulukkolistan joka sisältää löydettyjä 
     * hahmojakoja edustavia Tulos-olioita keskimääräisen sopivuuden mukaisessa 
     * käänteisessä järjestyksessä
     */
    public Tulosluettelo laskeHahmojako() {
        while (this.sopivuusraja >= this.minimisopivuus) {
            alustaTaulukot();
            luoKaytettavatEhdokaslistat();
            etsiRatkaisu(1);
            this.sopivuusraja = this.sopivuusraja - 5;
            this.ehdokkaidenMinimimaara = (100 - this.sopivuusraja) / 5;
        }
        this.tulokset.jarjesta();
        if (this.tulokset.pituus() > 100) {
            this.tulokset = this.tulokset.rajaa(100);
        }
        return this.tulokset;
    }
    
    /**
     * Tämä metodi luo kullekin kierrokselle sen sopivuusrajan ja ehdokkaiden 
     * minimimäärän mukaiset ehdokaslistat kaikille hahmoille. 
     */
    private void luoKaytettavatEhdokaslistat() {
        for (int hahmo = 1; hahmo <= this.hahmomaara; hahmo++) {
            Ehdokaslista ehdokaslista = new Ehdokaslista(this.yhteensopivuusdata.getPelaajaehdokaslista(hahmo), this.sopivuusraja, this.ehdokkaidenMinimimaara);
            this.kaytettavatEhdokaslistat[hahmo] = ehdokaslista;
        }
    }
    
    /**
     * Tämä metodi suorittaa hahmojakojen rekursiivisen haun kutsumalla itseään.
     * @param hahmo sen hahmon indeksi, jolle etsitään pelaajaa
     */
    private void etsiRatkaisu(int hahmo) {
        if (Duration.between(this.hahmojako.getSuorituksenAloitus(), Instant.now()).getSeconds() > 60) {
            this.lopetus = true;
        }
        if (this.lopetus == true) {
            return;
        }
        if (hahmo == this.hahmomaara + 1) {
            this.jarjestysnumero++;
            tallennaTulos();
            if (this.tulokset.pituus() > 50000) {
                this.lopetus = true;
            }
            return;
        }
        for (int i = 0; i < this.kaytettavatEhdokaslistat[hahmo].getPituus(); i++) {
            if (this.vapaatPelaajat[this.kaytettavatEhdokaslistat[hahmo].getEhdokas(i)] == true) {
                this.hahmojenValinnat[hahmo] = this.kaytettavatEhdokaslistat[hahmo].getEhdokas(i);
                this.vapaatPelaajat[this.kaytettavatEhdokaslistat[hahmo].getEhdokas(i)] = false;
                etsiRatkaisu(hahmo + 1);
                this.hahmojenValinnat[hahmo] = 0;
                this.vapaatPelaajat[this.kaytettavatEhdokaslistat[hahmo].getEhdokas(i)] = true;
            }
        } 
    }
    
    /**
     * Tämä metodi alustaa haussa käytetyt hahmojen valitsemien pelaajien ja 
     * vapaiden pelaajien taulukot alkuarvoihinsa ennen haun alkua.
     */
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
    
    /**
     * Tämä metodi tarkistaa, että hahmojako on täydellinen eli kattaa kaikki 
     * hahmot eikä ole kaksoiskappale aiemmin tallennetusta jaosta, ja tallentaa 
     * sen tämän jälkeen tulosluetteloon.
     */
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
        for (int i = 0; i < this.tulokset.pituus(); i++) {
            if (tulos.equals(this.tulokset.hae(i))) {
                kopio = true;
                break;
            }
        }
        if (kopio == false) {
            tulos.setAlgoritmi(this.kaytettavaAlgoritmi);
            tulos.setMinimiyhteensopivuus(this.minimisopivuus);
            tulos.setJarjestysnumero(this.jarjestysnumero);
            this.tulokset.lisaa(tulos);
        }
    }    
}