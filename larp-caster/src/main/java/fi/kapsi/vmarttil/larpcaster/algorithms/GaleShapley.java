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
import java.util.ArrayList;
import java.util.Collections;


/**
 * Tämä luokka toteuttaa hahmojaon ratkaisuna vakaiden avioliittojen ongelmaan 
 * käyttäen Galen-Shapleyn algoritmia.
 * @author Ville Marttila
 */

public class GaleShapley {
    
    private Hahmojako hahmojako;
    private String kaytettavaAlgoritmi;
    private Sopivuusmatriisi yhteensopivuusdata;
    private int minimisopivuus;
    private int pelaajamaara;
    private int hahmomaara;
    private Ehdokaslista[] ehdokaslistat;
    private int variaationumero;
    private int jarjestysnumero;
    private int[] ehdokkaitaKosittu;
    private int[] pelaajienValinnat;
    private int[] hahmojenValinnat;
    private ArrayList<Tulos> tulokset;
    
    /**
     * Tämä metodi luo GaleShapley-olion joka laskee hahmojaon käyttämällä 
     * Galen-Shapleyn algoritmia.
     * @param hahmojako Hahmojako-olio joka sisältää sen hahmojaon tiedot, johon 
     * tämä laskenta liittyy
     */
    public GaleShapley(Hahmojako hahmojako) {
        this.hahmojako = hahmojako;
        this.kaytettavaAlgoritmi = this.hahmojako.getKaytettavaAlgoritmi();
        this.yhteensopivuusdata = this.hahmojako.getYhteensopivuusdata();
        this.pelaajamaara = this.yhteensopivuusdata.getPelaajamaara();
        this.hahmomaara = this.yhteensopivuusdata.getHahmomaara();
        this.minimisopivuus = this.hahmojako.getMinimisopivuus();
        this.jarjestysnumero = 0;
        this.variaationumero = 0;
        this.tulokset = new ArrayList<>();
    }
    
    /**
     * Tämä metodi käynnistää oikean laskenta-algoritmin luokkamuuttujan 
     * kaytettavaAlgoritmi arvon perusteella.
     * @return tämä metodi palauttaa Tulos-olion joka sisältää lasketun
     * hahmojaon tiedot
     */
    public ArrayList<Tulos> laskeHahmojako() {
        laskeVariaatiot();
//        System.out.println(jarjestysvariaatiot.size());
//        if (this.kaytettavaAlgoritmi.equals("galeShapleyHahmoKosii")) {
//            for (int variaatio = 0; variaatio < jarjestysvariaatiot.size();variaatio++) {
//                alustaTaulukot();
//                this.ehdokaslistat = jarjestysvariaatiot.get(variaatio);
//                galeShapleyHahmoKosii();
//            }
//        } else if (this.kaytettavaAlgoritmi.equals("galeShapleyPelaajaKosii")) {
//            galeShapleyPelaajaKosii();
//        }
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
     * Tämä metodi toteuttaa hahmojaon ratkaisuna vakaiden avioliittojen 
     * ongelmaan käyttäen Galen-Shapleyn algoritmia siten, että hahmot 
     * toimivat kosivana osapuolena, jolloin tulos on optimaalinen hahmojen 
     * asettamien vaatimusten suhteen.
     */
    
    private void galeShapleyHahmoKosii() {
        boolean vapaitaHahmoja = true;
        while (vapaitaHahmoja == true) {
            vapaitaHahmoja = false;
            // Kukin vapaa hahmo kosii vuorollaan seuraavaa listallaan olevaa pelaajaa 
            for (int hahmo = 1; hahmo <= pelaajamaara; hahmo++) {
                if (kosiPelaajaa(hahmo) == true) {
                    vapaitaHahmoja = true;
                }
            }
        }
        // Tallennetaan tulokset Tulos-olioon
        this.jarjestysnumero++;
        tallennaTulos();               
    }
    
    private boolean kosiPelaajaa(int hahmo) {
        // Tarkistetaan onko hahmo edelleen vapaa ja onko sillä listallaan vielä kosimattomia pelaajia
        if (this.hahmojenValinnat[hahmo] == 0 && this.ehdokkaitaKosittu[hahmo] < this.ehdokaslistat[hahmo].getPituus()) {
            // Tarkistetaan onko kosittava pelaaja edelleen vapaa ja jos on, yhdistetään hahmo ja pelaaja
            int seuraavaEhdokas = this.ehdokaslistat[hahmo].getEhdokas(this.ehdokkaitaKosittu[hahmo]);
            this.ehdokkaitaKosittu[hahmo] = this.ehdokkaitaKosittu[hahmo] + 1;
            if (this.pelaajienValinnat[seuraavaEhdokas] == 0) {
                lisaaHahmo(hahmo, seuraavaEhdokas);                
            // Jos kosittava pelaaja on varattu, tarkistetaan onko kosiva hahmo sopivampi kuin aiempi; jos on, vaihdetaan
            } else if (yhteensopivuusdata.getSopivuusprosentti(seuraavaEhdokas, hahmo) > yhteensopivuusdata.getSopivuusprosentti(seuraavaEhdokas, this.pelaajienValinnat[seuraavaEhdokas])) {
                vaihdaHahmo(hahmo, seuraavaEhdokas);                
            // Jos kosiva hahmo on yhtä sopiva kuin aiempi, valitaan se, joka sopii harvemmalle pelaajalle eli jolla on lyhyempi ehdokaslista
            } else if (yhteensopivuusdata.getSopivuusprosentti(seuraavaEhdokas, hahmo) == yhteensopivuusdata.getSopivuusprosentti(seuraavaEhdokas, this.pelaajienValinnat[seuraavaEhdokas])) {
                if (this.ehdokaslistat[hahmo].getPituus() < this.ehdokaslistat[this.pelaajienValinnat[seuraavaEhdokas]].getPituus()) {
                    vaihdaHahmo(hahmo, seuraavaEhdokas);
                }
            }
            return true;
        }
        return false;     
    }
    
    /**
     * Tämä metodi toteuttaa hahmojaon ratkaisuna vakaiden avioliittojen 
     * ongelmaan käyttäen Galen-Shapleyn algoritmia siten, että pelaajat 
     * toimivat kosivana osapuolena, jolloin tulos on optimaalinen pelaajien 
     * toiveiden suhteen.
     */
    
    private void galeShapleyPelaajaKosii() {
        boolean vapaitaPelaajia = true;
        while (vapaitaPelaajia == true) {
            vapaitaPelaajia = false;
            // Kukin vapaa pelaaja kosii vuorollaan seuraavaa listallaan olevaa hahmoa 
            for (int pelaaja = 1; pelaaja <= pelaajamaara; pelaaja++) {
                if (kosiHahmoa(pelaaja) == true) {
                    vapaitaPelaajia = true;
                }
            }
        }
        // Tallennetaan tulokset Tulos-olioon
        tallennaTulos();               
    }
    
    private boolean kosiHahmoa(int pelaaja) {
        // Tarkistetaan onko hahmo edelleen vapaa ja onko sillä listallaan vielä kosimattomia pelaajia
        if (this.pelaajienValinnat[pelaaja] == 0 && this.ehdokkaitaKosittu[pelaaja] < yhteensopivuusdata.getHahmoehdokaslista(pelaaja).getPituus()) {
            // Tarkistetaan onko kosittava hahmo edelleen vapaa ja jos on, yhdistetään pelaaja ja hahmo
            int seuraavaEhdokas = yhteensopivuusdata.getHahmoehdokaslista(pelaaja).getEhdokas(this.ehdokkaitaKosittu[pelaaja]);
            this.ehdokkaitaKosittu[pelaaja] = this.ehdokkaitaKosittu[pelaaja] + 1;
            if (this.hahmojenValinnat[seuraavaEhdokas] == 0) {
                lisaaPelaaja(pelaaja, seuraavaEhdokas);                
            // Jos kosittava hahmo on varattu, tarkistetaan onko kosiva pelaaja sopivampi kuin aiempi; jos on, vaihdetaan
            } else if (yhteensopivuusdata.getSopivuusprosentti(pelaaja, seuraavaEhdokas) > yhteensopivuusdata.getSopivuusprosentti(this.hahmojenValinnat[seuraavaEhdokas], seuraavaEhdokas)) {
                vaihdaPelaaja(pelaaja, seuraavaEhdokas);                
            // Jos kosiva pelaaja on yhtä sopiva kuin aiempi, valitaan se, joka sopii harvemmalle hahmolle eli jolla on lyhyempi ehdokaslista
            } else if (yhteensopivuusdata.getSopivuusprosentti(pelaaja, seuraavaEhdokas) == yhteensopivuusdata.getSopivuusprosentti(this.hahmojenValinnat[seuraavaEhdokas], seuraavaEhdokas)) {
                if (yhteensopivuusdata.getHahmoehdokaslista(pelaaja).getPituus() < yhteensopivuusdata.getHahmoehdokaslista(this.hahmojenValinnat[seuraavaEhdokas]).getPituus()) {
                    vaihdaPelaaja(pelaaja, seuraavaEhdokas);
                }
            }
            return true;
        }
        return false;     
    }
  
    // Algoritmien käyttämät apumenetelmät
    
    private void alustaTaulukot() {
        // Taulukko johon on merkitty kuinka montaa ehdokaslistansa ehdokasta kukin hahmo tai pelaaja on kosinut
        this.ehdokkaitaKosittu = new int[this.pelaajamaara + 1];
        for (int i = 1; i <= this.pelaajamaara; i++) {
            this.ehdokkaitaKosittu[i] = 0;
        }
        // Taulukko johon on merkitty pelaaja, joka on hyväksynyt hahmon kosinnan tai jonka kosinnan hahmo on hyväksynyt (0 = ei hyväksyttyä kosintaa)
        this.hahmojenValinnat = new int[this.pelaajamaara + 1];
        for (int i = 1; i <= this.pelaajamaara; i++) {
            this.hahmojenValinnat[i] = 0;
        }
        // Taulukko johon on merkitty hahmo, jolta kukin pelaaja on hyväksynyt kosinnan tai joka on hyväksynyt pelaajan kosinnan (0 = ei hyväksyttyä kosintaa)
        this.pelaajienValinnat = new int[this.pelaajamaara + 1];
        for (int i = 1; i <= this.pelaajamaara; i++) {
            this.pelaajienValinnat[i] = 0;
        }
    }
    
    private void laskeVariaatiot() {
        if (this.kaytettavaAlgoritmi.contains("HahmoKosii")) {
            Ehdokaslista[] hahmojenEhdokaslistat = new Ehdokaslista[this.pelaajamaara + 1];
            for (int hahmo = 1; hahmo <= this.pelaajamaara; hahmo++) {
                hahmojenEhdokaslistat[hahmo] = this.yhteensopivuusdata.getPelaajaehdokaslista(hahmo);
            }
            laskePelaajaehdokasvariaatiot(hahmojenEhdokaslistat);
        } 
        if (this.kaytettavaAlgoritmi.contains("PelaajaKosii")) {
            Ehdokaslista[] pelaajienEhdokaslistat = new Ehdokaslista[this.pelaajamaara + 1];
            for (int pelaaja = 1; pelaaja <= this.pelaajamaara; pelaaja++) {
                pelaajienEhdokaslistat[pelaaja] = this.yhteensopivuusdata.getHahmoehdokaslista(pelaaja);
            }
            //this.jarjestysvariaatiot.add(hahmojenEhdokaslistat);
            laskeHahmoehdokasvariaatiot(pelaajienEhdokaslistat);
        }
        
    }
        
    /**
     * Tämä metodi laskee variaatiot, jotka syntyvät vaihtamalla kaikkien pelaajaehdokaslistojen sellaisten kahden ensimmäisen ehdokkaan paikkoja, joiden sopivuudet ovat samat, ja laskee hahmojaon kullakin variaatiolla.
     * @param hahmojenEhdokaslistat seuraavan variaation laskentaan käytettävien ehdokaslistojen taulukko
     */
    private void laskePelaajaehdokasvariaatiot(Ehdokaslista[] hahmojenEhdokaslistat) {
        for (int hahmo = 1; hahmo <= this.hahmomaara; hahmo++) {
            if (hahmojenEhdokaslistat[hahmo].getPituus() > 1) {
                if (this.yhteensopivuusdata.getSopivuusprosentti(hahmojenEhdokaslistat[hahmo].getEhdokas(0), hahmo) == this.yhteensopivuusdata.getSopivuusprosentti(hahmojenEhdokaslistat[hahmo].getEhdokas(1), hahmo)) {
                        Ehdokaslista[] uudetEhdokaslistat = new Ehdokaslista[hahmojenEhdokaslistat.length];
                        for (int i = 1; i < hahmojenEhdokaslistat.length; i++) {
                            uudetEhdokaslistat[i] = new Ehdokaslista(hahmojenEhdokaslistat[i]);
                        }
                        uudetEhdokaslistat[hahmo].poistaEhdokas(0);
                        // Lasketaan hahmojako tällä variaatiolla
                        alustaTaulukot();
                        this.ehdokaslistat = uudetEhdokaslistat;
                        galeShapleyHahmoKosii();
                        this.variaationumero++;
                        System.out.println(this.variaationumero + " variaatiota laskettu");
                        if (this.variaationumero > 500000) {
                            break;
                        }
                        laskePelaajaehdokasvariaatiot(uudetEhdokaslistat);
                    }
                
            }
        }
    }
    
    /**
     * Tämä metodi laskee variaatiot, jotka syntyvät vaihtamalla kaikkien hahmoehdokaslistojen sellaisten kahden ensimmäisen ehdokkaan paikkoja, joiden sopivuudet ovat samat, ja laskee hahmojaon kullakin variaatiolla.
     * @param pelaajienEhdokaslistat seuraavan variaation laskentaan käytettävien ehdokaslistojen taulukko
     */
    private void laskeHahmoehdokasvariaatiot(Ehdokaslista[] pelaajienEhdokaslistat) {
        for (int pelaaja = 1; pelaaja <= this.pelaajamaara; pelaaja++) {
            if (pelaajienEhdokaslistat[pelaaja].getPituus() > 1) {
                if (this.yhteensopivuusdata.getSopivuusprosentti(pelaaja, pelaajienEhdokaslistat[pelaaja].getEhdokas(0)) == this.yhteensopivuusdata.getSopivuusprosentti(pelaaja, pelaajienEhdokaslistat[pelaaja].getEhdokas(1))) {
                        Ehdokaslista[] uudetEhdokaslistat = new Ehdokaslista[pelaajienEhdokaslistat.length];
                        for (int i = 1; i < pelaajienEhdokaslistat.length; i++) {
                            uudetEhdokaslistat[i] = new Ehdokaslista(pelaajienEhdokaslistat[i]);
                        }
                        uudetEhdokaslistat[pelaaja].poistaEhdokas(0);
                        // Lasketaan hahmojako tällä variaatiolla
                        alustaTaulukot();
                        this.ehdokaslistat = uudetEhdokaslistat;
                        galeShapleyPelaajaKosii();
                        this.variaationumero++;
                        System.out.println(this.variaationumero + " variaatiota laskettu");
                        if (this.variaationumero > 10000) {
                            break;
                        }
                        laskeHahmoehdokasvariaatiot(uudetEhdokaslistat);
                    }
                
            }
        }
    }
    
    
    private void lisaaHahmo(int hahmo, int seuraavaEhdokas) {
        this.pelaajienValinnat[seuraavaEhdokas] = hahmo;
        this.hahmojenValinnat[hahmo] = seuraavaEhdokas;
    }
    
    private void vaihdaHahmo(int hahmo, int seuraavaEhdokas) {
        this.hahmojenValinnat[this.pelaajienValinnat[seuraavaEhdokas]] = 0;
        this.hahmojenValinnat[hahmo] = seuraavaEhdokas;
        this.pelaajienValinnat[seuraavaEhdokas] = hahmo;
    }

    private void lisaaPelaaja(int pelaaja, int seuraavaEhdokas) {
        this.hahmojenValinnat[seuraavaEhdokas] = pelaaja;
        this.pelaajienValinnat[pelaaja] = seuraavaEhdokas;
    }
    
    private void vaihdaPelaaja(int pelaaja, int seuraavaEhdokas) {
        this.pelaajienValinnat[this.hahmojenValinnat[seuraavaEhdokas]] = 0;
        this.pelaajienValinnat[pelaaja] = seuraavaEhdokas;
        this.hahmojenValinnat[seuraavaEhdokas] = pelaaja;
    }
    
//    private void tallennaTulokset(String algoritmi, Sopivuusmatriisi yhteensopivuusdata, int[] pelaajienValinnat, int[] hahmojenValinnat, int minimisopivuus) {
//        this.tulos.setAlgoritmi(algoritmi);
//        this.tulos.setMinimiyhteensopivuus(minimisopivuus);
//        this.tulos.setPrioriteetti(1);
//        this.tulos.setJarjestysnumero(1);
//        this.tulos.taytaTulokset(yhteensopivuusdata, pelaajienValinnat, hahmojenValinnat);
//    }
    
    private void tallennaTulos() {
        Tulos tulos = new Tulos(this.yhteensopivuusdata, this.pelaajienValinnat, this.hahmojenValinnat);
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
        }
    }
    
}
