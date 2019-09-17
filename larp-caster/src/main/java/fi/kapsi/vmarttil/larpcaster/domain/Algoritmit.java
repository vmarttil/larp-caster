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
        // Tallennetaan tulokset Tulos-olioon ja tarkistetaan onko kaikille hahmoille löytynyt pelaaja
        Tulos tulos = new Tulos();
               
        
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
        Tulos tulos = new Tulos();
        
        // Lisätään algoritmin koodi
        
        return tulos;
    }
      
    
    
    
    
    
    // Algoritmien käyttämät apumenetelmät
    
    private Ehdokaslista[] luoHahmojenVieruslistat(Sopivuusmatriisi yhteensopivuusdata, int minimisopivuus) {
        Ehdokaslista[] hahmojenVieruslistat = new Ehdokaslista[yhteensopivuusdata.getHahmomaara() + 1];
        
        
        
        
        
        return hahmojenVieruslistat;
    }
    
}
