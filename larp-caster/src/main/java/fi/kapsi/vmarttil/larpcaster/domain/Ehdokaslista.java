/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

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
        int pituus = 0;
        if (rooli.equals("hahmo")) {
            this.ehdokaslista = new int[hahmojako.getYhteensopivuusdata().getPelaajamaara()];
            this.yhteensopivuuslista = new int[hahmojako.getYhteensopivuusdata().getPelaajamaara()];
            for (int pelaaja = 1; pelaaja <= hahmojako.getYhteensopivuusdata().getPelaajamaara(); pelaaja++) {
                if (hahmojako.getYhteensopivuusdata().getSopivuusprosentti(pelaaja, indeksi) >= hahmojako.getMinimisopivuus()) {
                    this.ehdokaslista[pituus] = pelaaja;
                    this.yhteensopivuuslista[pituus] = hahmojako.getYhteensopivuusdata().getSopivuusprosentti(pelaaja, indeksi);
                    pituus++;
                }
            }
            if (pituus == 0) {
                hahmojako.setEhdokaslistatOK(false);
                System.out.println("VAROITUS: Hahmolle " + hahmojako.getYhteensopivuusdata().getHahmotunnus(indeksi) + " ei löydy yhtään minimisopivuuden ylittävää pelaajaa.");
            }
        } else {
            this.ehdokaslista = new int[hahmojako.getYhteensopivuusdata().getHahmomaara()];
            this.yhteensopivuuslista = new int[hahmojako.getYhteensopivuusdata().getHahmomaara()];
            for (int hahmo = 1; hahmo <= hahmojako.getYhteensopivuusdata().getHahmomaara(); hahmo++) {
                if (hahmojako.getYhteensopivuusdata().getSopivuusprosentti(indeksi, hahmo) >= hahmojako.getMinimisopivuus()) {
                    this.ehdokaslista[pituus] = hahmo;
                    this.yhteensopivuuslista[pituus] = hahmojako.getYhteensopivuusdata().getSopivuusprosentti(indeksi, hahmo);
                    pituus++;
                }
            }
            if (pituus == 0) {
                System.out.println("VAROITUS: Pelaajalle " + hahmojako.getYhteensopivuusdata().getPelaajatunnus(indeksi) + " ei löydy yhtään minimisopivuuden ylittävää hahmoa.");
            }
        }
        int[] valmisEhdokaslista = new int[pituus];
        int[] valmisYhteensopivuuslista = new int[pituus];
        for (int i = 0; i < pituus; i++) {
            valmisEhdokaslista[i] = this.ehdokaslista[i];
            valmisYhteensopivuuslista[i] = this.yhteensopivuuslista[i];
        }
        jarjesta(valmisYhteensopivuuslista, valmisEhdokaslista);   
    }

    /**
     * Tämä metodi luo itsenäisen kopion annetusta ehdokaslistasta
     * @param lahde kopioitava ehdokaslista
     */
    public Ehdokaslista(Ehdokaslista lahde) {
        this.ehdokaslista = new int[lahde.getPituus()];
        this.yhteensopivuuslista = new int[lahde.getPituus()];
        for (int i = 0; i < lahde.getPituus(); i++) {
            ehdokaslista[i] = lahde.getEhdokas(i);
            yhteensopivuuslista[i] = lahde.getYhteensopivuus(i);
        }
    }
    
    /**
     * Tämä metodi luo annetusta ehdokaslistasta karsitun version, johon 
     * sisältyvät vain annetun minimiehdokasmäärän sisään mahtuvat tai annetun 
     * yhteensopivuusrajan ylittävät ehdokkaat.
     * @param lahde kopioitava ehdokaslista
     * @param yhteensopivuusraja sisällytettävien ehdokkaiden minimiyhteensopivuus
     * @param minimiehdokasmaara sisällytettävien ehdokkaiden vähimmäismäärä
     */
    public Ehdokaslista(Ehdokaslista lahde, int yhteensopivuusraja, int minimiehdokasmaara) {
        int[] uusiEhdokaslista = new int[lahde.getPituus()];
        int[] uusiYhteensopivuuslista = new int[lahde.getPituus()];
        int pituus = 0;
        for (int ehdokas = 0; ehdokas < lahde.getPituus(); ehdokas++) {
            if (ehdokas < minimiehdokasmaara || lahde.getYhteensopivuus(ehdokas) >= yhteensopivuusraja) {
                uusiEhdokaslista[ehdokas] = lahde.getEhdokas(ehdokas);
                uusiYhteensopivuuslista[ehdokas] = lahde.getYhteensopivuus(ehdokas);
                pituus++;
            }
        }
        this.ehdokaslista = new int[pituus];
        this.yhteensopivuuslista = new int[pituus];
        for (int ehdokas = 0; ehdokas < pituus; ehdokas++) {
            this.ehdokaslista[ehdokas] = uusiEhdokaslista[ehdokas];
            this.yhteensopivuuslista[ehdokas] = uusiYhteensopivuuslista[ehdokas];
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
     * Tämä metodi palauttaa indeksin osoittaman ehdokkaan yhteensopivuuden.
     * @param index indeksi joka osoittaa palautettavan yhteensopivuuden
     * @return metodi palauttaa valitun ehdokkaan yhteensopivuusprosentin kokonaislukuna
     */
    public int getYhteensopivuus(int index) {
        return this.yhteensopivuuslista[index];
    }
    
    /**
     * Tämä metodi asettaa annetun ehdokkaan indeksin osoittaman kohdan 
     * ehdokkaaksi, korvaten aiemman ehdokkaan ja päivittää sen yhteensopivuuden 
     * annettuun arvoon.
     * @param ehdokas asetettavan ehdokkaan indeksinumero
     * @param yhteensopivuus asetettavan ehdokkaan yhteensopivuusprosentti
     * @param index indeksi joka osoittaa asetettavan ehdokkaan paikan
     */
    public void setEhdokas(int ehdokas, int yhteensopivuus, int index) {
        this.ehdokaslista[index] = ehdokas;
        this.yhteensopivuuslista[index] = yhteensopivuus;
    }
    
    /**
     * Tämä metodi poistaa annetun indeksin kohdalla olevan ehdokkaan 
     * yhteensopivuuksineen listasta, siirtää seuraavia ehdokkaita yhden 
     * askelen ylöspäin ja lyhentää listan.
     * @param index indeksi jonka kohdalta ehdokas poistetaan
     */
    
    public void poistaEhdokas(int index) {
        int[] uusiEhdokaslista = new int[this.ehdokaslista.length - 1];
        int[] uusiYhteensopivuuslista = new int[this.yhteensopivuuslista.length - 1];
        for (int i = 0; i < index; i++) {
            uusiEhdokaslista[i] = this.ehdokaslista[i];
            uusiYhteensopivuuslista[i] = this.yhteensopivuuslista[i];
        }
        for (int i = index + 1; i < this.ehdokaslista.length; i++) {
            uusiEhdokaslista[i - 1] = this.ehdokaslista[i];
            uusiYhteensopivuuslista[i - 1] = this.yhteensopivuuslista[i];
        }
        this.ehdokaslista = uusiEhdokaslista;
        this.yhteensopivuuslista = uusiYhteensopivuuslista;
    }
    
    // Ehdokaslistan kekojärjestämiseen käytettävät metodit
    
    /**
     * Tämä metodi järjestää kokonaislukutaulukkoparin sisällöt synkronisesti 
     * suurimmasta pienimpään
     * @param yhteensopivuuslista järjestämisen perusteena toimiva kokonaislukutaulukko
     * @param ehdokaslista mukana järjestettävä kokonaislukutaulukko
     */
    private void jarjesta(int[] yhteensopivuuslista, int[] ehdokaslista) {
        int koko = yhteensopivuuslista.length;
        for (int i = koko / 2 - 1; i >= 0; i--) {
            rakennaKeko(yhteensopivuuslista, ehdokaslista, koko, i);
        }
        for (int i=koko-1; i>=0; i--) {
            int x = yhteensopivuuslista[0];
            int y = ehdokaslista[0];
            yhteensopivuuslista[0] = yhteensopivuuslista[i];
            ehdokaslista[0] = ehdokaslista[i];
            yhteensopivuuslista[i] = x;
            ehdokaslista[i] = y;
            rakennaKeko(yhteensopivuuslista, ehdokaslista, i, 0);
        }
        this.yhteensopivuuslista = yhteensopivuuslista;
        this.ehdokaslista = ehdokaslista;
    }
    
    /** 
     * Tämä metodi rakentaa rekursiivisesti taulukoiden solmujen i alla 
     * olevat alipuut.
     * @param yhteensopivuuslista järjestämisen perusteena toimiva taulukko
     * @param ehdokaslista taulukon A mukana järjestettävä taulukko
     * @param keonKoko jarjestettavan taulukon ja siihen käytettävän keon koko
     * @param i operaation juurisolmu
     */
    void rakennaKeko(int[] yhteensopivuuslista, int[] ehdokaslista, int keonKoko, int i) {
        int pienin = i;
        int vasemmanLapsenIndeksi  = 2*i + 1; 
        int oikeanLapsenIndeksi  = 2*i + 2;
        if (vasemmanLapsenIndeksi < keonKoko && yhteensopivuuslista[vasemmanLapsenIndeksi] < yhteensopivuuslista[pienin]) {
            pienin = vasemmanLapsenIndeksi;
        }
        if (oikeanLapsenIndeksi < keonKoko && yhteensopivuuslista[oikeanLapsenIndeksi] < yhteensopivuuslista[pienin]) {
            pienin = oikeanLapsenIndeksi;
        }
        if (pienin != i) {
            int vaihdettavaA = yhteensopivuuslista[i];
            int vaihdettavaB = ehdokaslista[i];
            yhteensopivuuslista[i] = yhteensopivuuslista[pienin];
            ehdokaslista[i] = ehdokaslista[pienin];
            yhteensopivuuslista[pienin] = vaihdettavaA;
            ehdokaslista[pienin] = vaihdettavaB;
            rakennaKeko(yhteensopivuuslista, ehdokaslista, keonKoko, pienin);
        }
    }
    
    
    
}

