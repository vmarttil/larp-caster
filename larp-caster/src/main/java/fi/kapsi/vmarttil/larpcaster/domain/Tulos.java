/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Tämä luokka määrittelee hahmojaon tuloksen ja siihen liittyvien metatietojen 
 * tallentamiseen käytettävät tietorakenteet.
 * @author Ville Marttila
 */
public class Tulos implements Comparable<Tulos> {
    private int[] pelaajienHahmot;
    private int[] hahmojenPelaajat;
    private int[] hahmottomatPelaajat;
    private String algoritmi;
    int minimiyhteensopivuus;
    int huonoinSopivuus;
    double sopivuuskeskiarvo;
    double mediaanisopivuus;
    int parasSopivuus;
    int jarjestysnumero;
    String tunnus;
    
    /**
     * Tämä metodi luo Tulos-olion, johon tallennetaan yhden hahmojaon tulokset ja siihen liittyvät metatiedot. 
     * @param sopivuusmatriisi hahmojaon sopivuustiedot sisältävä Sopivuusmatriisi-olio 
     * @param pelaajienValinnat taulukko jossa kukin arvo on kyseisen indeksin osoittamalle pelaajalle valitun hahmon indeksitunnus
     * @param hahmojenValinnat taulukko jossa kukin arvo on kyseisen indeksin osoittamalle hahmolle valitun pelaajan indeksitunnus
     */
    public Tulos(Sopivuusmatriisi sopivuusmatriisi, int[] pelaajienValinnat, int[] hahmojenValinnat) {
        this.huonoinSopivuus = 100;
        this.sopivuuskeskiarvo = 0;
        this.mediaanisopivuus = 0;
        this.parasSopivuus = 0;
        int[] sopivuudet = taytaHahmojenPelaajat(sopivuusmatriisi, hahmojenValinnat);
        taytaPelaajienHahmot(sopivuusmatriisi, pelaajienValinnat);
        laskeSopivuudet(sopivuudet);
        this.tunnus = laskePvm() + algoritmi + jarjestysnumero;
    }
    
    // Luokan konstruktorin käyttämät apumetodit

    /**
     * Tämä metodi täyttää taulukot jotka sisältävät tiedon kullekin hahmolle valitusta pelaajasta ja pelaajattomista hahmoista ja tallentaa samalla tiedon hahmojaon sopivuuksista.
     * @param sopivuusmatriisi hahmojaon laskennassa käytetty Sopivuusmatriisi-olio
     * @param hahmojenValinnat kokonaislukutaulukko joka sisältää hahmojaon hahmoille valitsemat pelaajat, mukaan lukien täytehahmot 
     * @return metodi palauttaa kokonaislukutaulukon, joka sisältää hahmo-pelaaja-parien yhteensopivuudet
     */
    private int[] taytaHahmojenPelaajat(Sopivuusmatriisi sopivuusmatriisi, int[] hahmojenValinnat) {
        this.hahmojenPelaajat = new int[sopivuusmatriisi.getHahmomaara() + 1];
        int[] sopivuudet = new int[sopivuusmatriisi.getHahmomaara()];
        for (int hahmo = 1; hahmo <= sopivuusmatriisi.getHahmomaara(); hahmo++) {
            this.hahmojenPelaajat[hahmo] = hahmojenValinnat[hahmo];
            sopivuudet[hahmo - 1] = sopivuusmatriisi.getSopivuusprosentti(hahmojenValinnat[hahmo], hahmo);
        }
        return sopivuudet;
    }
 
    /**
     * Tämä metodi täyttää taulukot jotka sisältävät tiedon kullekin pelaajalle valitusta hahmosta ja hahmottomista pelaajista.
     * @param sopivuusmatriisi hahmojaon laskennassa käytetty Sopivuusmatriisi-olio
     * @param pelaajienValinnat kokonaislukutaulukko joka sisältää hahmojaon pelaajille valitsemat hahmot, mukaan lukien täytehahmot
     */
    private void taytaPelaajienHahmot(Sopivuusmatriisi sopivuusmatriisi, int[] pelaajienValinnat) {
        this.pelaajienHahmot = new int[sopivuusmatriisi.getPelaajamaara() + 1];
        int hahmottomat = 0;
        for (int pelaaja = 1; pelaaja <= sopivuusmatriisi.getPelaajamaara(); pelaaja++) {
            if (pelaajienValinnat[pelaaja] > sopivuusmatriisi.getHahmomaara()) {
                this.pelaajienHahmot[pelaaja] = 0;
            } else {
                this.pelaajienHahmot[pelaaja] = pelaajienValinnat[pelaaja];
            }
            if (this.pelaajienHahmot[pelaaja] == 0) {
                hahmottomat++;
            }
        }
        this.hahmottomatPelaajat = new int[hahmottomat];
        if (hahmottomat > 0) {
            taytaHahmottomatPelaajat(hahmottomat);
        }
    }
    
    /**
     * Tämä metodi luo ja täyttää kokonaislukutaulukon joka sisältää listan pelaajista joille ei löytynyt hahmoa.
     * @param hahmottomat ilman hahmoa jääneiden pelaajien määrä
     */
    private void taytaHahmottomatPelaajat(int hahmottomat) {
        for (int pelaaja = 1; pelaaja < this.pelaajienHahmot.length; pelaaja++) {
            if (this.pelaajienHahmot[pelaaja] == 0) {
                this.hahmottomatPelaajat[this.hahmottomatPelaajat.length - hahmottomat] = pelaaja;
                hahmottomat--;
            }
        }
    }
    
    /**
     * Tämä metodi laskee hahmojaon sopivuutta koskevat tunnusluvut (keskiarvo, pienin, suurin ja mediaani).
     * @param sopivuudet taulukko joka sisältää listan hahmojen sopivuuksista pelaajille
     */
    private void laskeSopivuudet(int[] sopivuudet) {
        int kokonaissopivuus = 0;
        for (int i = 0; i < sopivuudet.length; i++) {
            kokonaissopivuus = kokonaissopivuus + sopivuudet[i];
            if (sopivuudet[i] < this.huonoinSopivuus) {
                this.huonoinSopivuus = sopivuudet[i];
            }
            if (sopivuudet[i] > this.parasSopivuus) {
                this.parasSopivuus = sopivuudet[i];
            }
        }
        this.sopivuuskeskiarvo = (double) kokonaissopivuus / (double) sopivuudet.length;
        Arrays.sort(sopivuudet);
        if (sopivuudet.length % 2 == 0) {
            this.mediaanisopivuus = (sopivuudet[sopivuudet.length / 2 - 1] + sopivuudet[sopivuudet.length / 2]) / 2.0;
        } else {
            this.mediaanisopivuus = sopivuudet[sopivuudet.length / 2];
        }
    }
    
    /**
     * Tämä metodi palauttaa nykyisen ajankohdan numerojonona muodossa "vvvvkkppttmmss"
     */
    
    private String laskePvm() {
        DateFormat pvmMuoto = new SimpleDateFormat("yyyyMMddHHmmss");
	Date pvm = new Date();
        return pvm.toString();
    }
    
    
    
    // Getters
    
    /**
     * Tämä metodi palauttaa tuloksen yksilöllisen tunnuksen.
     * @return metodi palauttaa merkkijonon joka toimii tuloksen yksilöllisenä tunnuksena
     */
    public String getTunnus() { 
        return this.tunnus;
    }
    
    /**
     * Tämä metodi palauttaa tiedot hahmoille valituista pelaajista.
     * @return metodi palauttaa kokonaislukutaulukon, jossa taulukon rivi on hahmon 
     * indeksi ja sen arvo hahmolle valitun pelaajan indeksi (arvo 0 tarkoittaa, 
     * ettei hahmolle ole löytynyt pelaajaa)
     */
    public int[] getHahmojenPelaajat() { 
        return this.hahmojenPelaajat;
    }
    
    /**
     * Tämä metodi palauttaa tiedot pelaajille valituista hahmoista.
     * @return metodi palauttaa kokonaislukutaulukon, jossa taulukon rivi on pelaajan 
     * indeksi ja sen arvo pelaajalle valitun hahmon indeksi (arvo 0 tarkoittaa, 
     * ettei pelaajalle ole löytynyt hahmoa)
     */
    public int[] getPelaajienHahmot() { 
        return this.pelaajienHahmot;
    }
    
    /**
     * Tämä metodi palauttaa listan pelaajista, joille ei löytynyt hahmoa.
     * @return metodi palauttaa hahmottomien pelaajien indeksit sisältävän 
     * kokonaislukutaulukon
     */
    public int[] getHahmottomatPelaajat() {
        return this.hahmottomatPelaajat;
    }
    
    /**
     * Tämä metodi palauttaa tuloksen edustamaan hahmojakoon käytetyn 
     * algoritmin.
     * @return metodi palauttaa käytetyn algoritmin tunnuksen merkkijonona
     */
    public String getAlgoritmi() {
        return this.algoritmi;
    }
 
    /**
     * Tämä metodi palauttaa tuloksen edustamassa hahmojaossa käytetyn 
     * minimiyhteensopivuuden.
     * @return metodi palauttaa minimiyhteensopivuusprosentin kokonaislukuna
     */
    public int getMinimiyhteensopivuus() {
        return this.minimiyhteensopivuus;
    }

    /**
     * Tämä metodi palauttaa hahmojaon parhaan sopivuuden hahmon ja pelaajan 
     * välillä.
     * @return metodi palauttaa parhaan sopivuuden kokonaislukuna
     */
    public int getParasSopivuus() {
        return this.parasSopivuus;
    }
    
    /**
     * Tämä metodi palauttaa hahmojaon huonoimman sopivuuden hahmon ja pelaajan 
     * välillä.
     * @return metodi palauttaa huonoimman sopivuuden kokonaislukuna
     */
    public int getHuonoinSopivuus() {
        return this.huonoinSopivuus;
    }
    
    /**
     * Tämä metodi palauttaa hahmojaon keskimääräisen sopivuuden hahmojen ja 
     * pelaajien välillä.
     * @return metodi palauttaa keskimääräisen sopivuuden liukulukuna
     */
    public double getSopivuuskeskiarvo() {
        return this.sopivuuskeskiarvo;
    }

    /**
     * Tämä metodi palauttaa hahmojaon mediaanisopivuuden hahmojen ja pelaajien 
     * välillä.
     * @return metodi palauttaa mediaanisopivuuden liukulukuna
     */
    public double getMediaanisopivuus() {
        return this.mediaanisopivuus;
    }

    /**
     * Tämä metodi palauttaa tuloksen järjestysnumeron algoritmin laskemien 
     * tulosten joukossa.
     * @return metodi palauttaa järjestysnumeron kokonaislukuna
     */
    public int getJarjestysnumero() {
        return this.jarjestysnumero;
    }
    
    // Setters

    /**
     * Tämä metodi tallentaa tulokseen sen edustamaan hahmojakoon käytetyn 
     * algoritmin.
     * @param algoritmi käytetyn algoritmin tunnus
     */
    public void setAlgoritmi(String algoritmi) {
        this.algoritmi = algoritmi;
    }    

    /**
     * Tämä metodi tallentaa tulokseen sen edustamassa hahmojaossa käytetyn 
     * minimiyhteensopivuusprosentin.
     * @param minimiyhteensopivuus käytetyn algoritmin tunnus
     */
    public void setMinimiyhteensopivuus(int minimiyhteensopivuus) {
        this.minimiyhteensopivuus = minimiyhteensopivuus;
    }
    
    /**
     * Tämä metodi tallentaa tulokseen sen järjestysnumeron algoritmin laskemien 
     * tulosten joukossa.
     * @param jarjestysnumero tallennettava järjestysnumero kokonaislukuna
     */
    public void setJarjestysnumero(int jarjestysnumero) {
        this.jarjestysnumero = jarjestysnumero;
    }
    
    
    @Override
    public int compareTo(final Tulos o) {
        return Double.compare(o.sopivuuskeskiarvo, this.sopivuuskeskiarvo);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) { 
            return true; 
        }
        if (!(o instanceof Tulos)) { 
            return false; 
        }
        Tulos t = (Tulos) o;
        if (Double.compare(this.sopivuuskeskiarvo, t.sopivuuskeskiarvo) != 0) {
            return false;
        }
        for (int i = 1; i < this.hahmojenPelaajat.length; i++) {
            if (Integer.compare(this.hahmojenPelaajat[i], t.hahmojenPelaajat[i]) != 0) {
                return false;
            }
        }
        return true;
    }
}
