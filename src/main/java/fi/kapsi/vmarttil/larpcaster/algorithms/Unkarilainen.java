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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private Set<Integer> vaakalinjat;
    private Set<Integer> pystylinjat;
    private int[] hahmojenValinnat;
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
        this.vaakalinjat = new HashSet<>();
        this.pystylinjat = new HashSet<>();
        this.hahmojenValinnat = new int[this.pelaajamaara + 1];
        for (int i = 0; i <= this.pelaajamaara; i++) {
            this.hahmojenValinnat[i] = 0;
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
            // Lisätään nollien peitoksi minimimäärä vaaka- ja pystylinjoja
            lisaaLinjat();
            // Tarkistetaan onko piirrettyjä linjoja vähintään yhtä monta kuin pelaajia; jos on, keskeytetään silmukka
            if (this.vaakalinjat.size() + this.pystylinjat.size() >= this.pelaajamaara) {
                break;
            }
            // Jos piirrettyjä linjoja ei vielä ole yhtä montaa kuin pelaajia, etsitään pienin arvo joka ei ole yhdelläkään linjalla, vähennetään se riveiltä jotka eivät ole vaakalinjoja ja lisätään se sarakkeisiin jotka ovat pystylinjoja. 
            int pienin = etsiPieninVapaaArvo(); 
            vahennaPieninArvoRiveilta(pienin);            
            lisaaPieninArvoSarakkeisiin(pienin);
        }
//        // Välituloste
//        for (int i = 0; i < this.pelaajamaara; i++) {
//            String tuloste = "";
//            for (int j = 0; j < this.hahmomaara; j++) {
//                tuloste = tuloste + " " + this.kustannusmatriisi[i][j];
//            }
//            System.out.println(tuloste);
//        }

        // Rakennetaan hahmojen ehdokaslistat uusiksi optimaalisten vaihtoehtojen perusteella
        for (int sarake = 0; sarake < this.hahmomaara; sarake++) {
            HashMap<Integer, Integer> ehdokkaat = new HashMap<>();
            for (int rivi = 0; rivi < this.pelaajamaara; rivi++) {
                if (this.kustannusmatriisi[rivi][sarake] == 0) {
                    ehdokkaat.put(rivi + 1, this.yhteensopivuusdata.getSopivuusprosentti(rivi + 1, sarake + 1));
                }
            }
            this.yhteensopivuusdata.getPelaajaehdokaslista(sarake + 1).korvaaLista(ehdokkaat);
        }
        // Välitulostus
        for (int i = 1; i <= this.hahmomaara; i++) {
            String tulostus = String.valueOf(i) + ": ";
            for (int j = 0; j < this.yhteensopivuusdata.getPelaajaehdokaslista(i).getPituus(); j++) {
                tulostus = tulostus + " " + this.yhteensopivuusdata.getPelaajaehdokaslista(i).getEhdokas(j);
            }
            System.out.println(tulostus);
        }
        
        // Haetaan peruuttavalla haulla kaikki eri vaihtoehdot joissa kaikkien rivien nollat ovat eri sarakkeissa
        Peruuttava peruuttavaHaku = new Peruuttava(this.hahmojako);
        this.tulokset = peruuttavaHaku.laskeHahmojako();
        for (int i = 0; i < this.tulokset.size(); i++) {
            this.tulokset.get(i).setAlgoritmi("unkarilainen");
        }
        return this.tulokset;
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
        int[][] matriisi = new int[this.pelaajamaara][this.pelaajamaara];
        for (int i = 0; i < this.pelaajamaara; i++) {
            for (int j = 0; j < this.pelaajamaara; j++) {
                // Eliminoidaan minimisopivuuden alittavat yhdistelmät
                if (this.yhteensopivuusdata.getSopivuusprosentti(i+1, j+1) < this.minimisopivuus) {
                    matriisi[i][j] = 10000;
                // Muussa tapauksessa lasketaan kustannukseksi 100 - sopivuusprosentti
                } else {
                    matriisi[i][j] = 100 - this.yhteensopivuusdata.getSopivuusprosentti(i+1, j+1);
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
        for (int i = 0; i < this.pelaajamaara; i++) {
            int pienin = 100;
            for (int j = 0; j < this.pelaajamaara; j++) {
                if (this.kustannusmatriisi[i][j] < pienin) {
                    pienin = this.kustannusmatriisi[i][j];
                }
            }
            for (int j = 0; j < this.pelaajamaara; j++) {
                this.kustannusmatriisi[i][j] = this.kustannusmatriisi[i][j] - pienin;
            }
        }
    }
    
    /**
     * Tämä metodi etsii jokaisen kustannusmatriisin sarakkeen pienimmän arvon ja 
     * vähentää sen muista sarakkeen arvoista.
     */
    private void vahennaSarakkeidenPienimmatArvot() {
        for (int j = 0; j < this.pelaajamaara; j++) {
            int pienin = 100;
            for (int i = 0; i < this.pelaajamaara; i++) {
                if (this.kustannusmatriisi[i][j] < pienin) {
                    pienin = this.kustannusmatriisi[i][j];
                }
            }
            for (int i = 0; i < this.pelaajamaara; i++) {
                this.kustannusmatriisi[i][j] = this.kustannusmatriisi[i][j] - pienin;
            }
        }
    }
    
    /**
     * Tämä metodi käy kustannusmatriisin läpi ja lisää jokaisen löydetyn 
     * nollan riville tai sarakkeelle vaaka- tai pystylinjan sen mukaan 
     * kummalla on enemmän nollia, edellyttäen, että nolla ei jo ole vaaka- 
     * tai pystylinjalla.
     */
    private void lisaaLinjat() {
        this.vaakalinjat.clear();
        this.pystylinjat.clear();
        for (int i = 0; i < this.pelaajamaara; i++) {
            for (int j = 0; j < this.pelaajamaara; j++) {
                if (this.kustannusmatriisi[i][j] == 0) {
                    if (laskeRivinNollat(i) >= laskeSarakkeenNollat(j)) {
                        this.vaakalinjat.add(i);
                    } else {
                        this.pystylinjat.add(j);
                    }
                }
            }
        }
    }
    
    
    /**
     * Tämä metodi laskee annetun rivin nollien määrän.
     */
    private int laskeRivinNollat(int rivi) {
        int nollia = 0;
        for (int sarake = 0; sarake < this.pelaajamaara; sarake++) {
            if (this.kustannusmatriisi[rivi][sarake] == 0) {
                 nollia++;
            }
        }
        return nollia;
    } 
    
    /**
     * Tämä metodi laskee annetun sarakkeen nollien määrän.
     */
    private int laskeSarakkeenNollat(int sarake) {
        int nollia = 0;
        for (int rivi = 0; rivi < this.pelaajamaara; rivi++) {
            if (this.kustannusmatriisi[rivi][sarake] == 0) {
                 nollia++;
            }
        }
        return nollia;
    } 
    
    /**
     * Tämä metodi etsii kustannusmatriisista pienimmän arvon, joka ei ole 
     * yhdelläkään vaaka- tai pystylinjalla.
     * @return metodi palauttaa matriisin pienimmän vapaan arvon
     */
    private int etsiPieninVapaaArvo() {
        int pienin = 100;
        for (int rivi = 0; rivi < this.pelaajamaara; rivi++) {
            for (int sarake = 0; sarake < this.pelaajamaara; sarake++) {
                if (!this.vaakalinjat.contains(rivi) && !this.pystylinjat.contains(sarake) && this.kustannusmatriisi[rivi][sarake] < pienin) {
                    pienin = this.kustannusmatriisi[rivi][sarake];
                }
            }
        }
        return pienin;
    }
    
    /**
     * Tämä metodi vähentää kustannusmatriisin pienimmän arvon joka ei ole 
     * pysty- eikä vaakalinjalla kaikkien niiden rivien arvoista, jotka *eivät 
     * ole* vaakalinjoja.
     * @param pienin matriisin pienin arvo joka ei ole pysty- eikä vaakalinjalla
     */
    private void vahennaPieninArvoRiveilta(int pienin) {
        for (int rivi = 0; rivi < this.pelaajamaara; rivi++) {
            if (!this.vaakalinjat.contains(rivi)) {
                for (int sarake = 0; sarake < this.pelaajamaara; sarake++) {
                    this.kustannusmatriisi[rivi][sarake] = this.kustannusmatriisi[rivi][sarake] - pienin;
                }
            }
        }
    }
    
    /**
     * Tämä metodi lisää kustannusmatriisin pienimmän arvon joka ei ole 
     * pysty- eikä vaakalinjalla kaikkien niiden sarakkeiden arvoihin, jotka 
     * *ovat* vaakalinjoja.
     * @param pienin matriisin pienin arvo joka ei ole pysty- eikä vaakalinjalla
     */
    private void lisaaPieninArvoSarakkeisiin(int pienin) {
        for (int sarake = 0; sarake < this.pelaajamaara; sarake++) {
            if (this.pystylinjat.contains(sarake)) {
                for (int rivi = 0; rivi < this.pelaajamaara; rivi++) {
                    this.kustannusmatriisi[rivi][sarake] = this.kustannusmatriisi[rivi][sarake] + pienin;
                }
            }
        }
    }
    
    /**
     * Tämä metodi laskee kelvollisten tulosrivien, eli niiden rivien joilla on 
     * nolla oikean hahmon kohdalla
     * @return 
     */
    private int laskeTulosrivit() {
        int tulosriveja = 0;
        for (int rivi = 0; rivi < this.pelaajamaara; rivi++) {
            for (int sarake = 0; sarake < this.hahmomaara; sarake++) {
                if (this.kustannusmatriisi[rivi][sarake] == 0) {
                    tulosriveja++;
                    break;
                }
            }
        }
        return tulosriveja;
    }
    
}
