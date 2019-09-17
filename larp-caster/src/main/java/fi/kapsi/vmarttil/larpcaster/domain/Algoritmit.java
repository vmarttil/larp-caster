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
        Tulos tulos = new Tulos();
        
        // Lisätään algoritmin koodi
        
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
