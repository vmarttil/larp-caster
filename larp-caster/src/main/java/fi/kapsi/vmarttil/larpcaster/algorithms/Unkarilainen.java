/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.algorithms;

import fi.kapsi.vmarttil.larpcaster.domain.Hahmojako;
import fi.kapsi.vmarttil.larpcaster.domain.Sopivuusmatriisi;
import fi.kapsi.vmarttil.larpcaster.domain.Tulos;
import fi.kapsi.vmarttil.larpcaster.domain.Tulosluettelo;
import java.time.Duration;
import java.time.Instant;

/**
 * Tämä luokka toteuttaa hahmojaon ratkaisuna kohdistusongelmaan käyttäen sen ratkaisemiseen Kuhnin-Munkresin algoritmia eli niin sanottua unkarilaista menetelmää, joka perustuu 
 * yhteensopivuusmatriisista lasketun kustannusmatriisin manipuloimiseen ja minimisumman löytämiseen hahmojen ja pelaajien muodostamien parien kustannuksille siten, että kukin hahmo 
 * liitetään yhteen pelaajaan ja kukin pelaaja yhteen hahmoon. Koska optimaalisia ratkaisuja on monessa tapauksessa useita – suurempien matriisien tapauksessa usein huomattavia määriä, 
 * etsitään vaihtoehtoisia optimaalisia ratkaisuja soveltamalla optimoituun kustannusmatriisiin peruuttavaa hakua samalla tavoin kuin klassisessa kuningatarongelmassa, kunnes on 
 * laskettu 50 000 vaihtoehtoista optimiratkaisua joista tulokseen tallennetaan keskimääräiseltä sopivuudeltaan 100 parasta.
 * @author Ville Marttila
 */
public class Unkarilainen {
    private Hahmojako hahmojako;
    private String kaytettavaAlgoritmi;
    private Sopivuusmatriisi yhteensopivuusdata;
    private int minimisopivuus;
    private int pelaajamaara;
    private int hahmomaara;
    private long aloitusAika;
    private int[][] kustannusmatriisi;
    private int[] osoituksetRiveilla;
    private boolean[] blokatutRivit;
    private boolean[] blokatutSarakkeet;
    private boolean[] merkitytRivit;
    private boolean[] merkitytSarakkeet;
    private boolean[] viivatutRivit;
    private boolean[] viivatutSarakkeet;
    private boolean yksiselitteinenRatkaisu;
    private int[] hahmojenValinnat;
    private boolean[] vapaatPelaajat;
    private int jarjestysnumero;
    private Tulosluettelo tulokset;

    /**
     * Tämä metodi luo Unkarilainen-olion joka laskee hahmojaon käyttämällä nk. unkarilaista menetelmää eli Kuhnin-Munkresin algoritmia.
     * @param hahmojako Hahmojako-olio joka sisältää sen hahmojaon tiedot, johon tämä laskenta liittyy
     */
    public Unkarilainen(Hahmojako hahmojako) {
        this.hahmojako = hahmojako;
        this.kaytettavaAlgoritmi = this.hahmojako.getKaytettavaAlgoritmi();
        this.yhteensopivuusdata = this.hahmojako.getYhteensopivuusdata();
        this.minimisopivuus = this.hahmojako.getMinimisopivuus();
        this.pelaajamaara = this.yhteensopivuusdata.getPelaajamaara();
        this.hahmomaara = this.yhteensopivuusdata.getHahmomaara();
        this.kustannusmatriisi = laskeKustannusmatriisi();
        this.osoituksetRiveilla = new int[this.pelaajamaara + 1];
        this.blokatutRivit = new boolean[this.pelaajamaara + 1];
        this.blokatutSarakkeet = new boolean[this.pelaajamaara + 1];
        this.merkitytRivit = new boolean[this.pelaajamaara + 1];
        this.merkitytSarakkeet = new boolean[this.pelaajamaara + 1];
        this.viivatutRivit = new boolean[this.pelaajamaara + 1];
        this.viivatutSarakkeet = new boolean[this.pelaajamaara + 1];
        this.hahmojenValinnat = new int[this.pelaajamaara + 1];
        this.vapaatPelaajat = new boolean[this.pelaajamaara + 1];
        for (int i = 0; i <= this.pelaajamaara; i++) {
            this.osoituksetRiveilla[i] = 0;
            this.vapaatPelaajat[i] = true;
        }
        this.jarjestysnumero = 0;
        this.tulokset = new Tulosluettelo();
    }
    
    /**
     * Tämä metodi käynnistää kokonaissopivuuden maksimoivan hahmojaon laskennan nk. unkarilaisella menetelmällä eli Kuhnin-Munkresin algoritmilla.
     * @return tämä metodi palauttaa Tulosluettelo-olion joka sisältää yhden tai useampia optimaalisia hahmojakoja
     */
    public Tulosluettelo laskeHahmojako() {
        vahennaRivienPienimmatArvot();
        vahennaSarakkeidenPienimmatArvot();
        while (true) {
            if (Duration.between(this.hahmojako.getSuorituksenAloitus(), Instant.now()).getSeconds() > this.hahmojako.getLaskennanAikakatkaisu()) {
                break;
            }
            osoitaSarakkeetRiveille();
            if (tarkistaOsoitukset() == true) {
                this.yksiselitteinenRatkaisu = true;
                break;
            }
            merkitseSarakkeet();
            int mahdollisiaOsoituksia = viivaaRivitJaSarakkeet();
            if (mahdollisiaOsoituksia >= this.pelaajamaara) {
                break;
            }
            muokkaaMatriisia();
            nollaaMuuttujat();
        }
        lueTulokset();
        return this.tulokset;
    }
    
    /**
     * Tämä metodi lukee tulokset Kuhnin-Munkresin algoritmin suorittamisen jälkeen kutsumalla alimetodeja jotka 
     * lukevat tai laskevat optimaaliset hahmojaot ja tallentavat ne tulosluetteloon.
     */
    private void lueTulokset() {
        if (this.yksiselitteinenRatkaisu == true) {
            lueRatkaisu();
            if (this.hahmojako.getDiagnostiikkatila() == true) {
                tulostaStatus();
            }
        } else {
            laskeMahdollisetRatkaisut();
        }
        this.tulokset.jarjesta();
        if (this.tulokset.pituus() > 100) {
            this.tulokset = this.tulokset.rajaa(100);
        }
    }
    
    /**
     * Tämä metodi laskee algoritmin käyttämän kustannusmatriisin pelaajien ja hahmojen yhteeensopivuustietojen perusteella
     * @return tämä metodi palauttaa kunkin pelaaja-hahmo yhdistelmän "kustannuksen" eli käänteisen yhteensopivuuden sisältävän 
     * kaksiulotteisen kokonaislukutaulukon
     */
    private int[][] laskeKustannusmatriisi() {
        int[][] matriisi = new int[this.pelaajamaara + 1][this.pelaajamaara + 1];
        for (int i = 1; i <= this.pelaajamaara; i++) {
            for (int j = 1; j <= this.pelaajamaara; j++) {
                // Eliminoidaan minimisopivuuden alittavat yhdistelmät
                if (this.yhteensopivuusdata.getSopivuusprosentti(i, j) < this.minimisopivuus) {
                    matriisi[i][j] = 100;
                // Muussa tapauksessa lasketaan kustannukseksi 100 - sopivuusprosentti
                } else {
                    matriisi[i][j] = 100 - this.yhteensopivuusdata.getSopivuusprosentti(i, j);
                }
            }
        }                
        return matriisi;
    }
    
    /**
     * Tämä metodi etsii jokaisen kustannusmatriisin rivin pienimmän arvon ja vähentää sen muista rivin arvoista.
     */
    private void vahennaRivienPienimmatArvot() {
        for (int rivi = 1; rivi <= this.pelaajamaara; rivi++) {
            int pienin = 100;
            for (int sarake = 1; sarake <= this.pelaajamaara; sarake++) {
                if (this.kustannusmatriisi[rivi][sarake] < pienin) {
                    pienin = this.kustannusmatriisi[rivi][sarake];
                }
            }
            for (int sarake = 1; sarake <= this.pelaajamaara; sarake++) {
                this.kustannusmatriisi[rivi][sarake] = this.kustannusmatriisi[rivi][sarake] - pienin;
            }
        }
    }
    
    /**
     * Tämä metodi etsii jokaisen kustannusmatriisin sarakkeen pienimmän arvon ja vähentää sen muista sarakkeen arvoista.
     */
    private void vahennaSarakkeidenPienimmatArvot() {
        for (int sarake = 1; sarake <= this.pelaajamaara; sarake++) {
            int pienin = 100;
            for (int rivi = 1; rivi <= this.pelaajamaara; rivi++) {
                if (this.kustannusmatriisi[rivi][sarake] < pienin) {
                    pienin = this.kustannusmatriisi[rivi][sarake];
                }
            }
            for (int rivi = 1; rivi <= this.pelaajamaara; rivi++) {
                this.kustannusmatriisi[rivi][sarake] = this.kustannusmatriisi[rivi][sarake] - pienin;
            }
        }
    }
    
    /**
     * Tämä metodi käy läpi rivejä ja sarakkeita ja osoittaa riville sarakkeen jos rivillä tai sarakkeessa on tasan yksi vapaa 
     * nolla ja jatkaa tätä kunnes uusia osoituksia ei enää voi tehdä.
     */
    private void osoitaSarakkeetRiveille() {
        boolean toistetaan = true;
        boolean useitaNollia = false;
        // Käydään rivejä läpi kunnes uusia osoituksia ei enää löydy
        while (toistetaan == true) {
            if (tarkistaRivit() == true) {
                toistetaan = true;
            } else {
                toistetaan = false;
            }
        }
        toistetaan = true;
        // Käydään sarakkeet läpi kunnes uusia osoituksia ei enää löydy
        while (toistetaan == true) {
            if (tarkistaSarakkeet() == true) {
                toistetaan = true;
            } else {
                toistetaan = false;
            }
        }
    }
    
    /**
     * Tämä metodi käy kustannustaulukon rivit kertaalleen läpi ja määrittää osoitukset niille riveille joilla on vain yksi nolla.
     * @return tämä metodi palauttaa totuusarvon joka kertoo tuleeko se ajaa uudelleen
     */
    private boolean tarkistaRivit() {
        boolean toistetaan = false;
        boolean useitaNollia = false;
        for (int rivi = 1; rivi <= this.pelaajamaara; rivi++) {
            if (this.osoituksetRiveilla[rivi] == 0) {
                int nollia = 0;
                int nollaSarakkeessa = 0;
                for (int sarake = 1; sarake <= this.pelaajamaara; sarake++) {
                    if (this.kustannusmatriisi[rivi][sarake] == 0 && this.blokatutSarakkeet[sarake] == false && this.osoituksetRiveilla[rivi] != sarake) {
                        nollia++;
                        nollaSarakkeessa = sarake;
                    }
                }
                if (nollia == 1) {
                    toistetaan = maaritaRivinOsoitus(rivi, nollaSarakkeessa, useitaNollia);
                } else if (nollia > 1) {
                    this.osoituksetRiveilla[rivi] = 0;
                    useitaNollia = true;
                }
            }
        }
        return toistetaan;
    }
    
    /**
     * Tämä metodi tallentaa rivin perusteella tunnistetun osoituksen ja merkitsee osoituksen sarakkeen blokatuksi.
     * @param rivi tarkasteltava rivi
     * @param nollaSarakkeessa sarake, josta yksittäinen nolla on löytynyt
     * @param useitaNollia totuusarvo joka kertoo, onko tätä riviä edeltäviltä riveiltä löytynyt useita nollia
     * @return tämä metodi palauttaa totuusarvon, joka kertoo tuleeko rivientarkistusmetodi toistaa
     */
    private boolean maaritaRivinOsoitus(int rivi, int nollaSarakkeessa, boolean useitaNollia) {
        this.osoituksetRiveilla[rivi] = nollaSarakkeessa;
        this.blokatutSarakkeet[nollaSarakkeessa] = true;
        if (useitaNollia == true) {
            return true;
        }
        return false;
    }
    
    /**
     * Tämä metodi käy kustannustaulukon sarakkeet kertaalleen läpi ja määrittää osoitukset niille sarakkeille joissa on vain yksi nolla.
     * @return tämä metodi palauttaa totuusarvon joka kertoo tuleeko se ajaa uudelleen
     */
    private boolean tarkistaSarakkeet() {
        boolean toistetaan = false;
        boolean useitaNollia = false;
        for (int sarake = 1; sarake <= this.pelaajamaara; sarake++) {
            if (this.blokatutSarakkeet[sarake] == false) {
                int nollia = 0;
                int nollaRivilla = 0;
                for (int rivi = 1; rivi <= this.pelaajamaara; rivi++) {
                    if (this.kustannusmatriisi[rivi][sarake] == 0 && this.blokatutRivit[rivi] == false && this.osoituksetRiveilla[rivi] != sarake) {
                        nollia++;
                        nollaRivilla = rivi;
                    }
                }
                if (nollia == 1) {
                    toistetaan = maaritaSarakkeenOsoitus(sarake, nollaRivilla, useitaNollia);
                } else if (nollia > 1) {
                    useitaNollia = true;
                }
            }
        }
        return toistetaan;
    }

    /**
     * Tämä metodi tallentaa sarakkeen perusteella tunnistetun osoituksen ja merkitsee osoituksen rivin blokatuksi.
     * @param sarake tarkasteltava sarake
     * @param nollaRivilla rivi, jolta yksittäinen nolla on löytynyt
     * @param useitaNollia totuusarvo joka kertoo, onko tätä saraketta edeltävistä sarakkeista löytynyt useita nollia
     * @return tämä metodi palauttaa totuusarvon, joka kertoo tuleeko sarakkeidentarkistusmetodi toistaa
     */
    private boolean maaritaSarakkeenOsoitus(int sarake, int nollaRivilla, boolean useitaNollia) {
        this.osoituksetRiveilla[nollaRivilla] = sarake;
        this.blokatutRivit[nollaRivilla] = true;
        if (useitaNollia == true) {
            return true;
        }
        return false;
    }
    
    /**
     * Tämä metodi tarkistaa, onko kaikille riveille osoitettu sarake.
     * @return tämä metodi palauttaa totuusarvon joka kertoo onko kaikille riveille löytynyt täsmälleen yksi kelvollinen sarake
     */
    private boolean tarkistaOsoitukset() {
        int osoituksia = 0;
        for (int rivi = 1; rivi <= this.pelaajamaara; rivi++) {
            if (this.osoituksetRiveilla[rivi] > 0) {
                osoituksia++;
            } else {
                // Merkitään osoittamattomat rivit seuraavaa vaihetta varten
                this.merkitytRivit[rivi] = true;
            }
        }
        if (osoituksia == this.pelaajamaara) {
            return true;
        } else {
            return false;
        }
    }
    
   /**
    * Tämä metodi merkitsee sarakkeet, joissa on nollia aiemmin merkityillä riveillä ja kutsuu lopuksi metodia 
    * merkitseRivit() jos uusia sarakkeita merkitään.
    */
    private void merkitseSarakkeet() {
        boolean sarakkeitaMerkitty = false;
        for (int rivi = 1; rivi <= this.pelaajamaara; rivi++) { 
            if (this.merkitytRivit[rivi] == true) {
                for (int sarake = 1; sarake <= this.pelaajamaara; sarake++) {
                    if (this.kustannusmatriisi[rivi][sarake] == 0 && this.merkitytSarakkeet[sarake] == false) {
                        this.merkitytSarakkeet[sarake] = true;
                        sarakkeitaMerkitty = true;
                    }
                }
            }
        }
        if (sarakkeitaMerkitty == true) {
            merkitseRivit();
        }
    }
    
    /**
    * Tämä metodi merkitsee rivit, joilla on osoitus aiemmin merkityssä sarakkeessa ja kutsuu lopuksi metodia 
    * merkitseSarakkeet() jos uusia rivejä merkitään.
    */
    private void merkitseRivit() {
        boolean rivejaMerkitty = false;
        for (int sarake = 1; sarake <= this.pelaajamaara; sarake++) { 
            if (this.merkitytSarakkeet[sarake] == true) {
                for (int rivi = 1; rivi <= this.pelaajamaara; rivi++) {
                    if (this.osoituksetRiveilla[rivi] == sarake && this.merkitytRivit[rivi] == false) {
                        this.merkitytRivit[rivi] = true;
                        rivejaMerkitty = true;
                    }
                }
            }
        }
        if (rivejaMerkitty == true) {
            merkitseSarakkeet();
        }
    }
    
    /**
     * Tämä metodi yliviivaa merkitsemättömät rivit ja merkityt sarakkeet ja palauttaa tämänhetkisessä matriisissa mahdollisten osoitusten enimmäismäärän.
     * @return tämä metodi palauttaa mahdollisten osoitusten enimmäismäärän kokonaislukuna
     */
    private int viivaaRivitJaSarakkeet() {
        int viivattu = 0;
        for (int rivi = 1; rivi <= this.pelaajamaara; rivi++) {
            if (this.merkitytRivit[rivi] == false) {
                this.viivatutRivit[rivi] = true;
                viivattu++;
            }
        }
        for (int sarake = 1; sarake <= this.pelaajamaara; sarake++) {
            if (this.merkitytSarakkeet[sarake] == true) {
                this.viivatutSarakkeet[sarake] = true;
                viivattu++;
            }
        }
        return viivattu;
    }

    /**
    * Tämä metodi muokkaa matriisia synnyttäen siihen uusia nollakohtia. 
    */    
    private void muokkaaMatriisia() {
        int pieninArvo = etsiPieninVapaaArvo();
        vahennaPieninVapaaArvo(pieninArvo);
        lisaaPieninVapaaArvo(pieninArvo);
        
    }

    /**
     * Tämä metodi etsii kustannusmatriisista pienimmän arvon, joka ei ole viivatulla rivillä tai viivatussa sarakkeessa.
     * @return metodi palauttaa matriisin pienimmän vapaan arvon
     */
    private int etsiPieninVapaaArvo() {
        int pienin = 100;
        for (int rivi = 1; rivi <= this.pelaajamaara; rivi++) {
            if (this.viivatutRivit[rivi] == false) {
                for (int sarake = 1; sarake <= this.pelaajamaara; sarake++) {
                    if (this.viivatutSarakkeet[sarake] == false && this.kustannusmatriisi[rivi][sarake] < pienin) {
                        pienin = this.kustannusmatriisi[rivi][sarake];
                    }
                }
            }
        }
        return pienin;
    }

    /**
     * Tämä metodi vähentää kustannusmatriisin pienimmän vapaan arvon kaikista matriisin arvoista jotka eivät ole viivatulla rivillä tai viivatussa sarakkeessa.
     * @param pienin matriisin pienin arvo jota ei ole viivattu yli
     */
    private void vahennaPieninVapaaArvo(int pienin) {
        for (int rivi = 1; rivi <= this.pelaajamaara; rivi++) {
            if (this.viivatutRivit[rivi] == false) {
                for (int sarake = 1; sarake <= this.pelaajamaara; sarake++) {
                    if (this.viivatutSarakkeet[sarake] == false) {
                        this.kustannusmatriisi[rivi][sarake] = this.kustannusmatriisi[rivi][sarake] - pienin;
                    }
                }
            }
        }
    }
    
    /**
     * Tämä metodi lisää kustannusmatriisin pienimmän vapaan arvon kaikkiin matriisin arvoihin jotka ovat viivatun rivin ja sarakkeen risteyskohdassa.
     * @param pienin matriisin pieninarvo jota ei ole viivattu yli
     */
    private void lisaaPieninVapaaArvo(int pienin) {
        for (int sarake = 1; sarake <= this.pelaajamaara; sarake++) {
            if (this.viivatutSarakkeet[sarake] == true) {
                for (int rivi = 1; rivi <= this.pelaajamaara; rivi++) {
                    if (this.viivatutRivit[rivi] == true) {
                        this.kustannusmatriisi[rivi][sarake] = this.kustannusmatriisi[rivi][sarake] + pienin;
                    }
                }
            }
        }
    }
    
    
    /**
     * Tämä metodi asettaa kustannusmatriisin manipuloinnissa käytettävät taulukkomuuttujat alkuarvoihinsa.
     */
    private void nollaaMuuttujat() {
        for (int i = 1; i <= this.pelaajamaara; i++) {
            this.osoituksetRiveilla[i] = 0;
            this.blokatutRivit[i] = false;
            this.blokatutSarakkeet[i] = false;
            this.merkitytRivit[i] = false;
            this.merkitytSarakkeet[i] = false;
            this.viivatutRivit[i] = false;
            this.viivatutSarakkeet[i] = false;
        }
    }
    
    /**
     * Tämä metodi lukee ratkaisun suoraan kustannusmatriisista siinä tapauksessa että matriisille on löytynyt yksiselitteinen optimiratkaisu.
     */
    private void lueRatkaisu() {
        for (int hahmo = 1; hahmo <= this.hahmomaara; hahmo++) {
            this.hahmojenValinnat[hahmo] = this.osoituksetRiveilla[hahmo];
        }
        tallennaTulos();
    }
    
    /**
     * Tämä metodi käynnistää peruuttavan haun otimiratkaisujen etsimiseksi optimoidusta kustannusmatriisista siinä tapauksessa, että optimiratkaisuja on useita.
     */
    private void laskeMahdollisetRatkaisut() {
        int hahmo = 1;
        laskeRatkaisu(hahmo);
    }
    
    /**
     * Tämä metodi etsii peruuttavan haun avulla optimiratkaisuja optimoidusta kustannusmatriisista kunnes 
     * se on joko löytänyt kaikki mahdolliset optimiratkaisut tai löydettyjen ratkaisujen määrä ylittää 50 000. 
     * @param hahmo sen hahmon indeksi, jolle etsitään pelaajaa
     */
    private void laskeRatkaisu(int hahmo) {
        if (hahmo == this.hahmomaara + 1) {
            tallennaTulos();
            if (this.hahmojako.getDiagnostiikkatila() == true && this.tulokset.pituus() % 10000 == 0) {
                tulostaStatus();
            }
            return;
        }
        for (int pelaaja = 1; pelaaja <= this.pelaajamaara; pelaaja++) {
            if (this.kustannusmatriisi[pelaaja][hahmo] == 0 && this.vapaatPelaajat[pelaaja] == true) {
                this.hahmojenValinnat[hahmo] = pelaaja;
                this.vapaatPelaajat[pelaaja] = false;
                if (this.tulokset.pituus() < this.hahmojako.getTulostenEnimmaismaara() && Duration.between(this.hahmojako.getSuorituksenAloitus(), Instant.now()).getSeconds() < this.hahmojako.getLaskennanAikakatkaisu()) {
                    laskeRatkaisu(hahmo + 1);
                }
                this.hahmojenValinnat[hahmo] = 0;
                this.vapaatPelaajat[pelaaja] = true;
            }
        } 
    }
    
    /**
     * Tämä metodi tarkistaa, että hahmojako on kelvollinen ja tallentaa sen tämän jälkeen tulosluetteloon.
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
        boolean kopio = onKopio(tulos);
        if (kopio == false) {
            this.jarjestysnumero++;
            tulos.setAlgoritmi(this.kaytettavaAlgoritmi);
            tulos.setMinimiyhteensopivuus(this.minimisopivuus);
            tulos.setJarjestysnumero(this.jarjestysnumero);
            this.tulokset.lisaa(tulos);
        }
    }
    
    /**
     * Tämä metodi tarkistaa, ettei hahmojako ole kopio jostain aiemmin tallennetusta hahmojaosta.
     * @param tulos tarkistettava hahmojako
     * @return tämä metodi palauttaa totuusarvon joka kertoo onko hahmojako kopio
     */
    private boolean onKopio(Tulos tulos) {
        boolean kopio = false;
        for (int i = 0; i < this.tulokset.pituus(); i++) {
            if (tulos.equals(this.tulokset.hae(i))) {
                return true;
            }
        }
        return false;
    }

/**
     * Tämä metodi tulostaa tämänhetkisen statuksen diagnostisia tarkoituksia varten.
     */
    private void tulostaStatus() {
        Instant nykyhetki = Instant.now();
        Duration kesto = Duration.between(this.hahmojako.getSuorituksenAloitus(), nykyhetki);
        long aika = (kesto.getSeconds() * 1000) + (kesto.getNano() / 1000000);
        System.out.println(this.tulokset.pituus() + " uniikkia tulosta laskettu " + aika  + " millisekunnissa.");
    }

}