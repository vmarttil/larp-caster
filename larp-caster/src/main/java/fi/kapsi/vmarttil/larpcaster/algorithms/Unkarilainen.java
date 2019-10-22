/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.algorithms;

import fi.kapsi.vmarttil.larpcaster.domain.Hahmojako;
import fi.kapsi.vmarttil.larpcaster.domain.Sopivuusmatriisi;
import fi.kapsi.vmarttil.larpcaster.domain.Tulos;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Ville
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
    private ArrayList<Tulos> tulokset;

 
    /**
     * Tämä metodi luo Unkarilainen-olion joka laskee hahmojaon käyttämällä 
     * nk. unkarilaista menetelmää eli Kuhnin-Munkresin algoritmia.
     * @param hahmojako Hahmojako-olio joka sisältää sen hahmojaon tiedot, johon 
     * tämä laskenta liittyy
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
        this.tulokset = new ArrayList<>();
    }
    
    
    /**
     * Tämä metodi käynnistää kokonaissopivuuden maksimoivan hahmojaon laskennan 
     * nk. unkarilaisella menetelmällä eli Kuhnin-Munkresin algoritmilla.
     * @return tämä metodi palauttaa Tulos-olion joka sisältää kokonais-
     * yhteensopivuudeltaan maksimoidun hahmojaon
     */
    public ArrayList<Tulos> laskeHahmojako() {
        vahennaRivienPienimmatArvot();
        vahennaSarakkeidenPienimmatArvot();
        while (true) {
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
        if (this.yksiselitteinenRatkaisu == true) {
            int[] hahmojenValinnat = lueRatkaisu();
            kirjaaRatkaisu(hahmojenValinnat);
        } else {
            long maara = laskeRatkaisujenMaara();
            System.out.println("Mahdollisten ratkaisujen määrä: " + maara);
            laskeMahdollisetRatkaisut();
        }
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
    
    // Luokan metodien käyttämät yksityiset apumetodit
    
    /**
     * Tämä metodi laskee algoritmin käyttämän kustannusmatriisin pelaajien ja 
     * hahmojen yhteeensopivuustietojen perusteella
     * @return tämä metodi palauttaa kunkin pelaaja-hahmo yhdistelmän 
     * "kustannuksen" eli käänteisen yhteensopivuuden sisältävän kaksiulotteisen
     * kokonaislukutaulukon
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
     * Tämä metodi etsii jokaisen kustannusmatriisin rivin pienimmän arvon ja 
     * vähentää sen muista rivin arvoista.
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
     * Tämä metodi etsii jokaisen kustannusmatriisin sarakkeen pienimmän arvon ja 
     * vähentää sen muista sarakkeen arvoista.
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
     * Tämä metodi käy läpi rivejä ja sarakkeita ja osoittaa riville sarakkeen 
     * jos rivillä tai sarakkeessa on tasan yksi vapaa nolla ja jatkaa tätä 
     * kunnes uusia osoituksia ei enää voi tehdä.
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
     * Tämä metodi käy kustannustaulukon rivit kertaalleen läpi ja määrittää 
     * osoitukset niille riveille joilla on vain yksi nolla.
     * @return tämä metodi palauttaa totuusarvon joka kertoo tuleeko se ajaa 
     * uudelleen
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
                    this.osoituksetRiveilla[rivi] = nollaSarakkeessa;
                    this.blokatutSarakkeet[nollaSarakkeessa] = true;
                    if (useitaNollia == true) {
                        toistetaan = true;
                    }
                } else if (nollia > 1) {
                    this.osoituksetRiveilla[rivi] = 0;
                    useitaNollia = true;
                }
            }
        }
        return toistetaan;
    }
    
    
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
                    this.osoituksetRiveilla[nollaRivilla] = sarake;
                    this.blokatutRivit[nollaRivilla] = true;
                    if (useitaNollia == true) {
                        toistetaan = true;
                    }
                } else if (nollia > 1) {
                    useitaNollia = true;
                }
            }
        }
        return toistetaan;
    }

    /**
     * Tämä metodi tarkistaa, onko kaikille riveille osoitettu sarake.
     * @return tämä metodi palauttaa totuusarvon joka kertoo onko kaikille 
     * riveille löytynyt täsmälleen yksi kelvollinen sarake
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
    * Tämä metodi merkitsee sarakkeet, joissa on nollia aiemmin merkityillä 
    * riveillä ja kutsuu lopuksi metodia merkitseRivit() jos uusia sarakkeita 
    * merkitään.
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
    * Tämä metodi merkitsee rivit, joilla on osoitus aiemmin merkityssä 
    * sarakkeessa ja kutsuu lopuksi metodia merkitseSarakkeet() jos uusia rivejä 
    * merkitään.
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
     * Tämä metodi yliviivaa merkitsemättömät rivit ja merkityt sarakkeet 
     * ja palauttaa tämänhetkisessä matriisissa mahdollisten osoitusten 
     * enimmäismäärän.
     * @return tämä metodi palauttaa mahdollisten osoitusten enimmäismäärän 
     * kokonaislukuna
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
     * Tämä metodi etsii kustannusmatriisista pienimmän arvon, joka ei ole 
     * viivatulla rivillä tai viivatussa sarakkeessa.
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
     * Tämä metodi vähentää kustannusmatriisin pienimmän vapaan arvon kaikista 
     * matriisin arvoista jotka eivät ole viivatulla rivillä tai viivatussa 
     * sarakkeessa.
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
     * Tämä metodi lisää kustannusmatriisin pienimmän vapaan arvon kaikkiin 
     * matriisin arvoihin jotka ovat viivatun rivin ja sarakkeen risteyskohdassa.
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
     * Tämä metodi asettaa kustannusmatriisin manipuloinnissa käytettävät 
     * taulukkomuuttujat alkuarvoihinsa.
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
    
    private int[] lueRatkaisu() {
        int[] hahmojenValinnat = new int[this.hahmomaara + 1];
        for (int hahmo = 1; hahmo <= this.hahmomaara; hahmo++) {
            hahmojenValinnat[hahmo] = this.osoituksetRiveilla[hahmo];
        }
        return hahmojenValinnat;
    }
    
    private long laskeRatkaisujenMaara() {
        long kertoma = 1;
        for (int sarake = 1; sarake <= this.hahmomaara; sarake++) {
            int nollia = 0;
            for (int rivi = 1; rivi <= this.pelaajamaara; rivi++) {
                if (this.kustannusmatriisi[rivi][sarake] == 0) {
                    nollia++;
                }
            }
            kertoma = kertoma * nollia;
        }
        return kertoma;
    }
    
    private void laskeMahdollisetRatkaisut() {
        int hahmo = 1;
        laskeRatkaisu(hahmo);
    }
    
    private void laskeRatkaisu(int hahmo) {
        if (hahmo == this.hahmomaara + 1) {
            kirjaaRatkaisu(this.hahmojenValinnat);
            return;
        }
        for (int pelaaja = 1; pelaaja <= this.pelaajamaara; pelaaja++) {
            if (this.kustannusmatriisi[pelaaja][hahmo] == 0 && this.vapaatPelaajat[pelaaja] == true) {
                this.hahmojenValinnat[hahmo] = pelaaja;
                this.vapaatPelaajat[pelaaja] = false;
                laskeRatkaisu(hahmo + 1);
                this.hahmojenValinnat[hahmo] = 0;
                this.vapaatPelaajat[pelaaja] = true;
            }
        } 
    }
    
    private void kirjaaRatkaisu(int[] hahmojenValinnat) {
       int[] pelaajienValinnat = new int[this.pelaajamaara + 1];
        for (int i = 1; i <= this.pelaajamaara; i++) {
            pelaajienValinnat[i] = 0;
        }
        for (int i = 1; i <= this.hahmomaara; i++) {
            int pelaaja = hahmojenValinnat[i];
            pelaajienValinnat[pelaaja] = i;
        }
        Tulos tulos = new Tulos(this.yhteensopivuusdata, pelaajienValinnat, hahmojenValinnat);
        boolean kopio = false;
        for (Tulos vanhaTulos : this.tulokset) {
            if (tulos.equals(vanhaTulos)) {
                kopio = true;
                break;
            }
        }
        if (kopio == false) {
            tulos.setAlgoritmi(this.kaytettavaAlgoritmi);
            tulos.setMinimiyhteensopivuus(this.minimisopivuus);
            tulos.setJarjestysnumero(this.jarjestysnumero);
            this.tulokset.add(tulos);
            this.jarjestysnumero++;
        }
    }
    
}
