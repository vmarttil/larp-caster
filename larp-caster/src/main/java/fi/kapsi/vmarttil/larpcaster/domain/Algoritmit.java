/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Tämä luokka määrittelee kaikki hahmojakoon käytettävissä olevat algoritmit.
 * @author Ville Marttila
 */
public class Algoritmit {
    
    /**
     * Tämä metodi toteuttaa hahmojaon ratkaisuna vakaiden avioliittojen 
     * ongelmaan käyttäen Galen-Shapleyn algoritmia siten, että hahmot 
     * toimivat kosivana osapuolena, jolloin tulos on optimaalinen hahmojen 
     * asettamien vaatimusten suhteen.
     * @param yhteensopivuusdata Sopivuusmatriisi-olio, joka sisältää kaksi 
     * indeksitaulukkoa jotka antavat kullekin hahmo- ja pelaajatunnukselle 
     * ainutkertaisen indeksinumeron sekä kaksiulotteisen taulukon, joka 
     * sisältää kunkin hahmo-pelaaja-yhdistelmän yhteensopivuuden matriisin 
     * muodossa
     * @param minimisopivuus kokonaisluku välillä 0-100, joka määrittää 
     * pienimmän mahdollisen yhteensopivuusarvon, jolla hahmo ja pelaaja 
     * voidaan yhdistää toisiinsa
     * @return metodi palauttaa Tulos-olion, joka sisältää algoritmin tuottaman 
     * hahmojen ja pelaajien parituksen, käytetyn algoritmin ja sen parametrien 
     * tiedot sekä analyyttista metadataa tuloksista
     */
    
    public static Tulos galeShapleyHahmoKosii(Sopivuusmatriisi yhteensopivuusdata, int minimisopivuus) {
        long aloitusAika = System.nanoTime();
        int pelaajamaara = yhteensopivuusdata.getPelaajamaara();
        // Taulukko johon on merkitty kuinka montaa ehdokaslistansa ehdokasta kukin hahmo on kosinut
        int[] ehdokkaitaKosittu = new int[pelaajamaara+1];
        for (int i = 1; i <= pelaajamaara; i++) {
            ehdokkaitaKosittu[i] = 0;
        }
        // Taulukko johon on merkitty pelaaja, joka on hyväksynyt hahmon kosinnan (0 = ei hyväksyttyä kosintaa)
        int[] hahmojenValinnat = new int[pelaajamaara+1];
        for (int i = 1; i <= pelaajamaara; i++) {
            hahmojenValinnat[i] = 0;
        }        
        // Taulukko johon on merkitty hahmo, jolta kukin pelaaja on hyväksynyt kosinnan (0 = ei hyväksyttyä kosintaa)
        int[] pelaajienValinnat = new int[pelaajamaara+1];
        for (int i = 1; i <= pelaajamaara; i++) {
            pelaajienValinnat[i] = 0;
        }
        int kierrokset = 0;
        boolean vapaitaHahmoja = true;
        while (vapaitaHahmoja = true) {
            vapaitaHahmoja = false;
            // Kukin vapaa hahmo kosii vuorollaan seuraavaa listallaan olevaa pelaajaa 
            for (int hahmo = 1; hahmo <= pelaajamaara; hahmo++) {
                // Tarkistetaan onko hahmo edelleen vapaa ja onko sillä listallaan vielä kosimattomia pelaajia
                if (hahmojenValinnat[hahmo] == 0 && ehdokkaitaKosittu[hahmo] < yhteensopivuusdata.getPelaajaehdokaslista(hahmo).getPituus()) {
                    vapaitaHahmoja = true;
                    // Tarkistetaan onko kosittava pelaaja edelleen vapaa ja jos on, yhdistetään hahmo ja pelaaja
                    int seuraavaEhdokas = yhteensopivuusdata.getPelaajaehdokaslista(hahmo).getEhdokas(ehdokkaitaKosittu[hahmo]);
                    ehdokkaitaKosittu[hahmo] = ehdokkaitaKosittu[hahmo] + 1;
                    if (pelaajienValinnat[seuraavaEhdokas] == 0) {
                        pelaajienValinnat[seuraavaEhdokas] = hahmo;
                        hahmojenValinnat[hahmo] = seuraavaEhdokas;
                    // Jos kosittava pelaaja on varattu, tarkistetaan onko kosiva hahmo sopivampi kuin aiempi; jos on, vaihdetaan
                    } else if (yhteensopivuusdata.getSopivuusprosentti(seuraavaEhdokas, hahmo) > yhteensopivuusdata.getSopivuusprosentti(seuraavaEhdokas, pelaajienValinnat[seuraavaEhdokas])) {
                        hahmojenValinnat[pelaajienValinnat[seuraavaEhdokas]] = 0;
                        hahmojenValinnat[hahmo] = seuraavaEhdokas;
                        pelaajienValinnat[seuraavaEhdokas] = hahmo;
                    // Jos kosiva hahmo on yhtä sopiva kuin aiempi, valitaan se, joka sopii harvemmalle pelaajalle eli jolla on lyhyempi ehdokaslista
                    } else if (yhteensopivuusdata.getSopivuusprosentti(seuraavaEhdokas, hahmo) == yhteensopivuusdata.getSopivuusprosentti(seuraavaEhdokas, pelaajienValinnat[seuraavaEhdokas])) {
                        if (yhteensopivuusdata.getPelaajaehdokaslista(hahmo).getPituus() < yhteensopivuusdata.getPelaajaehdokaslista(pelaajienValinnat[seuraavaEhdokas]).getPituus()) {
                            hahmojenValinnat[pelaajienValinnat[seuraavaEhdokas]] = 0;
                            hahmojenValinnat[hahmo] = seuraavaEhdokas;
                            pelaajienValinnat[seuraavaEhdokas] = hahmo;
                        }
                    }
                    // Jos kosiva hahmo ei ole yhtä sopiva kuin aiempi, mitään ei tapahdu ja siirrytään seuraavaan hahmoon
                }
            }
            kierrokset++;
        }
        long lopetusAika = System.nanoTime();
        double kulunutAika = (double) ((int) (lopetusAika - aloitusAika) / 10000000) / 100;
        // Tallennetaan tulokset Tulos-olioon ja tarkistetaan onko kaikille hahmoille löytynyt pelaaja
        Tulos tulos = tallennaTulokset("galeShapleyHahmoKosii", yhteensopivuusdata, pelaajienValinnat, hahmojenValinnat, minimisopivuus, kierrokset, kulunutAika);               
        return tulos;
    }
    
    /**
     * Tämä metodi toteuttaa hahmojaon ratkaisuna vakaiden avioliittojen 
     * ongelmaan käyttäen Galen-Shapleyn algoritmia siten, että pelaajat 
     * toimivat kosivana osapuolena, jolloin tulos on optimaalinen pelaajien 
     * toiveiden suhteen.
     * @param yhteensopivuusdata Sopivuusmatriisi-olio, joka sisältää kaksi 
     * indeksitaulukkoa jotka antavat kullekin hahmo- ja pelaajatunnukselle 
     * ainutkertaisen indeksinumeron sekä kaksiulotteisen taulukon, joka 
     * sisältää kunkin hahmo-pelaaja-yhdistelmän yhteensopivuuden matriisin 
     * muodossa
     * @param minimisopivuus kokonaisluku välillä 0-100, joka määrittää 
     * pienimmän mahdollisen yhteensopivuusarvon, jolla hahmo ja pelaaja 
     * voidaan yhdistää toisiinsa
     * @return metodi palauttaa Tulos-olion, joka sisältää algoritmin tuottaman 
     * hahmojen ja pelaajien parituksen, käytetyn algoritmin ja sen parametrien 
     * tiedot sekä analyyttista metadataa tuloksista
     */
    public static Tulos galeShapleyPelaajaKosii(Sopivuusmatriisi yhteensopivuusdata, int minimisopivuus) {
        long aloitusAika = System.nanoTime();
        int pelaajamaara = yhteensopivuusdata.getPelaajamaara();
        // Taulukko johon on merkitty kuinka montaa ehdokaslistansa ehdokasta kukin pelaaja on kosinut
        int[] ehdokkaitaKosittu = new int[pelaajamaara+1];
        for (int i = 1; i <= pelaajamaara; i++) {
            ehdokkaitaKosittu[i] = 0;
        }
        // Taulukko johon on merkitty hahmo, joka on hyväksynyt pelaajan kosinnan (0 = ei hyväksyttyä kosintaa)
        int[] pelaajienValinnat = new int[pelaajamaara+1];
        for (int i = 1; i <= pelaajamaara; i++) {
            pelaajienValinnat[i] = 0;
        }        
        // Taulukko johon on merkitty pelaaja, jolta kukin hahmo on hyväksynyt kosinnan (0 = ei hyväksyttyä kosintaa)
        int[] hahmojenValinnat = new int[pelaajamaara+1];
        for (int i = 1; i <= pelaajamaara; i++) {
            hahmojenValinnat[i] = 0;
        }
        int kierrokset = 0;
        boolean vapaitaPelaajia = true;
        while (vapaitaPelaajia = true) {
            vapaitaPelaajia = false;
            // Kukin vapaa pelaaja kosii vuorollaan seuraavaa listallaan olevaa hahmoa 
            for (int pelaaja = 1; pelaaja <= pelaajamaara; pelaaja++) {
                // Tarkistetaan onko pelaaja edelleen vapaa ja onko sillä listallaan vielä kosimattomia hahmoja
                if (pelaajienValinnat[pelaaja] == 0 && ehdokkaitaKosittu[pelaaja] < yhteensopivuusdata.getHahmoehdokaslista(pelaaja).getPituus()) {
                    vapaitaPelaajia = true;
                    // Tarkistetaan onko kosittava hahmo edelleen vapaa ja jos on, yhdistetään pelaaja ja hahmo
                    int seuraavaEhdokas = yhteensopivuusdata.getHahmoehdokaslista(pelaaja).getEhdokas(ehdokkaitaKosittu[pelaaja]);
                    ehdokkaitaKosittu[pelaaja] = ehdokkaitaKosittu[pelaaja] + 1;
                    if (hahmojenValinnat[seuraavaEhdokas] == 0) {
                        hahmojenValinnat[seuraavaEhdokas] = pelaaja;
                        pelaajienValinnat[pelaaja] = seuraavaEhdokas;
                    // Jos kosittava hahmo on varattu, tarkistetaan onko kosiva pelaaja sopivampi kuin aiempi; jos on, vaihdetaan
                    } else if (yhteensopivuusdata.getSopivuusprosentti(pelaaja, seuraavaEhdokas) > yhteensopivuusdata.getSopivuusprosentti(hahmojenValinnat[seuraavaEhdokas], seuraavaEhdokas)) {
                        pelaajienValinnat[hahmojenValinnat[seuraavaEhdokas]] = 0;
                        pelaajienValinnat[pelaaja] = seuraavaEhdokas;
                        hahmojenValinnat[seuraavaEhdokas] = pelaaja;
                    // Jos kosiva pelaaja on yhtä sopiva kuin aiempi, valitaan se, joka sopii harvemmalle hahmolle eli jolla on lyhyempi ehdokaslista
                    } else if (yhteensopivuusdata.getSopivuusprosentti(pelaaja, seuraavaEhdokas) == yhteensopivuusdata.getSopivuusprosentti(hahmojenValinnat[seuraavaEhdokas], seuraavaEhdokas)) {
                        if (yhteensopivuusdata.getHahmoehdokaslista(pelaaja).getPituus() < yhteensopivuusdata.getHahmoehdokaslista(hahmojenValinnat[seuraavaEhdokas]).getPituus()) {
                            pelaajienValinnat[hahmojenValinnat[seuraavaEhdokas]] = 0;
                            pelaajienValinnat[pelaaja] = seuraavaEhdokas;
                            hahmojenValinnat[seuraavaEhdokas] = pelaaja;
                        }
                    }
                    // Jos kosiva pelaaja ei ole yhtä sopiva kuin aiempi, mitään ei tapahdu ja siirrytään seuraavaan pelaajaan
                }
            }
            kierrokset++;
        }
        long lopetusAika = System.nanoTime();
        double kulunutAika = (double) ((int) (lopetusAika - aloitusAika) / 10000000) / 100;
        // Tallennetaan tulokset Tulos-olioon ja tarkistetaan onko kaikille hahmoille löytynyt pelaaja
        Tulos tulos = tallennaTulokset("galeShapleyPelaajaKosii" ,yhteensopivuusdata, pelaajienValinnat, hahmojenValinnat, minimisopivuus, kierrokset, kulunutAika);               
        return tulos;
    }
      
    // Algoritmien käyttämät apumenetelmät
    
    private static Tulos tallennaTulokset(String algoritmi, Sopivuusmatriisi yhteensopivuusdata, int[] pelaajienValinnat, int[] hahmojenValinnat, int minimisopivuus, int kierrokset, double kulunutAika) {
        Tulos tulos = new Tulos();
        tulos.setAlgoritmi(algoritmi);
        tulos.setKierroksia(kierrokset);
        tulos.setKulunutAika(kulunutAika);
        tulos.setMinimiyhteensopivuus(minimisopivuus);
        tulos.taytaTulokset(yhteensopivuusdata, pelaajienValinnat, hahmojenValinnat);
        return tulos;
    }
    
}
