/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

import fi.kapsi.vmarttil.larpcaster.algorithms.GaleShapley;
import fi.kapsi.vmarttil.larpcaster.algorithms.Peruuttava;
import fi.kapsi.vmarttil.larpcaster.algorithms.Unkarilainen;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Tämä luokka määrittelee hahmojakosovelluksen keskeisen toimintalogiikan ja 
 * hallinnoi sen tietorakenteita.
 * @author Ville Marttila
 */
public class Hahmojako {
    private Sopivuusmatriisi yhteensopivuusdata;
    private Tulosluettelo[] tulokset;
    private Tulosluettelo yhteistulokset;
    private String kaytettavaAlgoritmi;
    private int minimisopivuus;
    private int tuloksiaEnintaanLaskentaaKohden;
    private int tuloksiaEnintaanYhteensa;
    private int enimmaisvariaatioaste;
    private int tulostenEnimmaismaara;
    private int laskennanAikakatkaisu;
    private boolean ehdokaslistatOK;
    private boolean diagnostiikkatila;
    Instant suorituksenAloitus;
   
    /**
     * Tämä metodi luo Hahmojako-olion, joka ohjaa hahmojaon laskentaa ja siihen 
     * käytettäviä tietorakenteita.
     */
    public Hahmojako() {
        this.yhteensopivuusdata = null;
        this.tulokset = new Tulosluettelo[0];
        this.yhteistulokset = new Tulosluettelo();
        this.kaytettavaAlgoritmi = "";
        this.minimisopivuus = 50;
        this.tuloksiaEnintaanLaskentaaKohden = 20;
        this.tuloksiaEnintaanYhteensa = 20;
        this.enimmaisvariaatioaste = 0;
        this.tulostenEnimmaismaara = 50000;
        this.laskennanAikakatkaisu = 60;
        this.ehdokaslistatOK = true;
        this.diagnostiikkatila = false;
    }
     
    // Getters

    /**
     * Tämä metodi palauttaa hahmojaon pohjana käytettävän yhteensopivuusdatan.
     * @return metodi palauttaa Sopivuusmatriisi-olion, joka sisältää kaikkien 
     * pelaajien ja hahmojen väliset yhteensopivuusprosentit
     */
    public Sopivuusmatriisi getYhteensopivuusdata() {
        return this.yhteensopivuusdata;
    }

    /**
     * Tämä metodi palauttaa kaikkien hahmojakolaskentojen tulokset.
     * @return metodi palauttaa Tulosluettelo-olioita sisältävän taulukon, jossa 
     * kukin Tulosluettelo sisältää kaikki yhden hahmojakojen laskennan tulokset metatietoineen.
     */
    public Tulosluettelo[] getTulokset() {
        return this.tulokset;
    }

    /**
     * Tämä metodi palauttaa annetun indeksinumeron mukaisen hahmojaon laskennan 
     * tulokset.
     * @param haku palautettavan laskennan indeksinumero
     * @return metodi palauttaa Tulosluettelo-olion joka sisältää yksittäisten 
     * hahmojakojen tiedot sisältäviä Tulos-olioita
     */
    public Tulosluettelo getHaunTulokset(int haku) {
        return this.tulokset[haku];
    }
    
    /**
     * Tämä metodi palauttaa kaikista hahmojakolaskennoista kootut sopivimmat 
     * tulokset.
     * @return metodi palauttaa Tulosluettelo-olion joka sisältää yksittäisten 
     * hahmojakojen tiedot sisältäviä Tulos-olioita
     */
    public Tulosluettelo getYhteistulokset() {
        return this.yhteistulokset;
    }
    
    /**
     * Tämä metodi palauttaa tällä hetkellä hahmojakoon käytettäväksi määritetyn
     * algoritmin.
     * @return metodi palauttaa käytetyn algoritmin tunnuksen merkkijonona
     */
    public String getKaytettavaAlgoritmi() {
        return this.kaytettavaAlgoritmi;
    }

    /**
     * Tämä metodi palauttaa hahmojaossa käytettäväksi määritetyn vähimmäis-
     * yhteensopivuuden.
     * @return metodi palauttaa pienimmän sallitun yhteensopivuusprosentin 
     * kokonaislukuna
     */
    public int getMinimisopivuus() {
        return this.minimisopivuus;
    }
    
    /**
     * Tämä metodi palauttaa yksittäistä laskentaa kohden näytettävien tulosten 
     * enimmäismäärän.
     * @return metodi palauttaa suurimman näytettävän laskentakohtaisen tulosmäärän 
     * kokonaislukuna
     */
    public int getTuloksiaEnintaanLaskentaaKohden() {
        return this.tuloksiaEnintaanLaskentaaKohden;
    }
    
    /**
     * Tämä metodi palauttaa yhteensä näytettävien tulosten enimmäismäärän.
     * @return metodi palauttaa suurimman näytettävän tulosmäärän kokonaislukuna
     */
    public int getTuloksiaEnintaanYhteensa() {
        return this.tuloksiaEnintaanYhteensa;
    }
    
    /**
     * Tämä metodi palauttaa Galen-Sapleyn ja Kuhnin-Munkresin algoritmeilla tehtävässä 
     * hahmojaossa varianttien laskemiseen käytettävän variaation enimmäisasteen, eli 
     * kuinka monen tason syvyydelle hakupuun kutakin haaraa seurataan ennen kuin peruutetaan 
     * takaisin päin ja siirrytään seuraavaan haaraan; matalat arvot ohjaavat hakua 
     * enemmän leveys- kuin syvyyssuuntaan ja sekä rajoittavat haun enimmäiskestoa että 
     * jakavat tuloksien variaatiota tasaisemmin eri hahmoille.
     * @return metodi palauttaa suurimman sallitun variaation asteen kokonaislukuna
     */
    public int getEnimmaisvariaatioaste() {
        return this.enimmaisvariaatioaste;
    }
    
    /**
     * Tämä metodi palauttaa yhden hahmojakolaskennan yhteydessä laskettavien tulosten enimmäismäärän, 
     * jonka täytyttyä laskenta lopetetaan aikavaativuuden hillitsemiseksi.
     * @return metodi palauttaa laskettavien tulosten enimmäismäärän kokonaislukuna
     */
    public int getTulostenEnimmaismaara() {
        return this.tulostenEnimmaismaara;
    }
    
    /**
     * Tämä metodi palauttaa hahmojakolaskentaan käytettävän enimmäisajan, 
     * jonka jälkeen laskenta lopetetaan aikavaativuuden hillitsemiseksi.
     * @return metodi palauttaa enimmäisajan sekunteja kuvaavana kokonaislukuna
     */
    public int getLaskennanAikakatkaisu() {
        return this.laskennanAikakatkaisu;
    }
    
    /**
     * Tämä metodi palauttaa tiedon siitä, sisältääkö jokaisen pelaajan 
     * ja hahmon ehdokaslista vähintään yhden sopivan hahmon tai pelaajan.
     * @return metodi palauttaa totuusarvon joka kertoo sisältävätkö kaikki 
     * ehdokaslistat vähintään yhden ehdokkaan
     */
    public boolean getEhdokaslistatOK() {
        return this.ehdokaslistatOK;
    }
    
    
    
    /**
     * Tämä metodi palauttaa käynnissä olevan hahmojaon aloitushetken Instant-
     * olion muodossa.
     * @return tämä metodi palauttaa suorituksen aloitushetkeä edustavan Instant-olion
     */
    public Instant getSuorituksenAloitus() {
        return this.suorituksenAloitus;
    }
    
    /**
     * Tämä metodi palauttaa tiedon siitä, onko diagnostiikkatila käytössä.
     * @return totuusarvo joka kertoo onko diagnostiikkatila käytössä
     */
    public boolean getDiagnostiikkatila() {
        return this.diagnostiikkatila;
    }
    
    // Setters
    
    /**
     * Tämä metodi asettaa hahmojaossa käytettävän algoritmin.
     * @param algoritmi käytettävän algoritmin tunnus merkkijonona
     */
    public void setKaytettavaAlgoritmi(String algoritmi) {
        this.kaytettavaAlgoritmi = algoritmi;
    }

    /**
     * Tämä metodi asettaa hahmojaossa käytettävän pienimmän sallitun 
     * yhteensopivuusprosentin.
     * @param minimisopivuus käytettävä minimiyhteensopivuus prosenttiarvoa 
     * kuvaavana kokonaislukuna
     */
    public void setMinimisopivuus(int minimisopivuus) {
        this.minimisopivuus = minimisopivuus;
        paivitaSopivuusmatriisi();
        luoEhdokaslistat();
    }
    
    /**
     * Tämä metodi asettaa yksittäistä laskentaa kohden näytettävien tulosten 
     * enimmäismäärän.
     * @param enimmaismaara suurin näytettävä laskentakohtainen tulosmäärä 
     * kokonaislukuna
     */
    public void setTuloksiaEnintaanLaskentaaKohden(int enimmaismaara) {
        this.tuloksiaEnintaanLaskentaaKohden = enimmaismaara;
    }
    
    /**
     * Tämä metodi asettaa yhteensä näytettävien tulosten enimmäismäärän.
     * @param enimmaismaara suurin näytettävä tulosmäärä kokonaislukuna
     */
    public void setTuloksiaEnintaanYhteensa(int enimmaismaara) {
        this.tuloksiaEnintaanYhteensa = enimmaismaara;
    } 
    
    /**
     * Tämä metodi asettaa Galen-Shapleyn algoritmilla tehtävässä hahmojaossa ehdokaslistavarianttien laskemiseen 
     * käytettävän variaation enimmäisasteen, eli kuinka monen tason syvyydelle hakupuun kutakin haaraa 
     * seurataan ennen kuin peruutetaan takaisin päin ja siirrytään seuraavaan haaraan; matalat arvot 
     * ohjaavat hakua enemmän leveys- kuin syvyyssuuntaan ja sekä rajoittavat haun enimmäiskestoa että 
     * jakavat tuloksien variaatiota tasaisemmin eri hahmoille.
     * @param aste suurin sallittu variaation aste
     */
    public void setEnimmaisvariaatioaste(int aste) {
        this.enimmaisvariaatioaste = aste;
    }
    
    /**
     * Tämä metodi asettaa yhden hahmojakolaskennan yhteydessä laskettavien tulosten enimmäismäärän, 
     * jonka täytyttyä laskenta lopetetaan laskenta-ajan pitämiseksi kohtuullisena.
     * @param enimmaismaara laskettavien tulosten enimmäismäärä
     */
    public void setTulostenEnimmaismaara(int enimmaismaara) {
        this.tulostenEnimmaismaara = enimmaismaara;
    } 
    
    /**
     * Tämä metodi asettaa hahmojakolaskentaan käytettävän enimmäisajan, jonka jälkeen tulosten 
     * laskenta lopetetaan aikavaativuuden hillitsemiseksi.
     * @param aika laskentaan käytettävä enimmäisaika
     */
    public void setLaskennanAikakatkaisu(int aika) {
        this.laskennanAikakatkaisu = aika;
    }
    
    /**
     * Tämä metodi kertoo sisältääkö jokaisen pelaajan ja hahmon ehdokaslista 
     * vähintään yhden sopivan hahmon tai pelaajan.
     * @param ok totuusarvo, joka on totta jos kaikkien pelaajien 
     * ja hahmojen ehdokaslistat vähintään yhden sopivan ehdokkaan
     */
    public void setEhdokaslistatOK(boolean ok) {
        this.ehdokaslistatOK = ok;
    }

    /**
     * Tämä metodi asettaa diagnostiikkatilan käyttöön tai pois käytöstä.
     * @return totuusarvo joka kertoo onko diagnostiikkatila käytössä
     */
    public void setDiagnostiikkatila(boolean kaytossa) {
        this.diagnostiikkatila = kaytossa;
    }
    
    // Lisäykset ja poistot
    
    /**
     * Tämä metodi lisää jollakin algoritmilla lasketut tulokset sisältävän 
     * Tulosluettelo-olion tulokset tulosten luetteloon.
     * @param tulos Tulos-olioita sisältävä Tulosluettelo-olio 
     */
    public void lisaaTulos(Tulosluettelo tulos) {
        int uusiKoko = this.tulokset.length + 1;
        this.tulokset = Arrays.copyOf(this.tulokset, uusiKoko);
        this.tulokset[uusiKoko - 1] = tulos;
    }
    
    // Operaatiot
    
    /**
     * Tämä metodi lataa hahmojaossa käytettävät yhteensopivuustiedot XML-
     * tiedostosta ja kutsuu alimetodeja jotka luovat niistä yhteensopivuusmatriisin 
     * sekä pelaajien ja hahmojen ehdokasluettelot.
     * @param tiedostonimi merkkijono, joka kertoo käytettävän XML-tiedoston nimen
     */
    public void lataaYhteensopivuustiedot(String tiedostonimi) throws SAXException, ParserConfigurationException, IOException {    
        NodeList pelaajaluettelo = lataaSolmuluettelo(tiedostonimi);
        int pelaajamaara = pelaajaluettelo.getLength();
        for (int i = 0; i < pelaajamaara; i++) {
            Element pelaaja = (Element) pelaajaluettelo.item(i);
            String pelaajatunnus = pelaaja.getAttribute("xml:id");
            // Pelaajatunnukset kirjoitetaan indeksitaulukkoon
            this.yhteensopivuusdata.setPelaajatunnus(i + 1, pelaajatunnus);
            NodeList hahmoluettelo = pelaaja.getElementsByTagName("note");
            // Lisätään yhteensopivuustiedot kaikille hahmoille ja tarvittavalle määrälle tyhjiä täytehahmoja
            lisaaYhteensopivuustiedot(i, pelaajamaara, hahmoluettelo);
        }
        System.out.println("");
        System.out.println("Yhteensopivuustiedot ladattu tiedostosta " + tiedostonimi);
        System.out.println(this.yhteensopivuusdata.getHahmomaara() + " hahmoa, " + this.yhteensopivuusdata.getPelaajamaara() + " pelaajaa");
        luoEhdokaslistat();  
    } 
    
    
    /**
     * Tämä metodi lataa pelaajien yhteensopivuustiedot solmuluettelona annetusta x.
     * @param tiedostonimi merkkijono, joka kertoo käytettävän XML-tiedoston nimen
     * @return metodi palauttaa luettelon pelaajien yhteensopivuustiedoista solmuluettelona
     */
    private NodeList lataaSolmuluettelo(String tiedostonimi) throws SAXException, ParserConfigurationException, IOException {    
        File xmlTiedosto = new File(tiedostonimi);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        dBuilder = dbFactory.newDocumentBuilder();
        Document dokumentti = dBuilder.parse(xmlTiedosto);
        dokumentti.getDocumentElement().normalize();
        NodeList pelaajaluettelo = dokumentti.getElementsByTagName("person");
        luoSopivuusmatriisi(dokumentti);        
        return pelaajaluettelo;
    }
    
    /**
     * Tämä metodi luo tyhjän sopivuusmatriisin annetun XML-tiedostosta muodostetun 
     * Document-olion sisältämien tietojen perusteella
     * @param dokumentti yhteensopivuustiedot sisältävä Document-objekti
     */
    private void luoSopivuusmatriisi(Document dokumentti) {
        int pelaajamaara = dokumentti.getElementsByTagName("person").getLength();
        int hahmomaara = dokumentti.getElementsByTagName("note").getLength() / dokumentti.getElementsByTagName("person").getLength();
        this.yhteensopivuusdata = new Sopivuusmatriisi(pelaajamaara, hahmomaara);
    }
    
    /**
     * Tämä metodi lisää hahmojen ja pelaajien väliset yhteensopivuustiedot 
     * XML-tiedoston pohjalta muodostetusta Document-oliosta yhteensopivuusmatriisiin. 
     * @param pelaaja pelaajan järjestysnumero
     * @param pelaajamaara pelaajien kokonaismäärä
     * @param hahmoluettelo pelaajan ja hahmojen yhteensopivuudet sisältävä solmuluettelo-olio
     */
    private void lisaaYhteensopivuustiedot(int pelaaja, int pelaajamaara, NodeList hahmoluettelo) {
        for (int j = 0; j < pelaajamaara; j++) {
            if (j < hahmoluettelo.getLength()) {
                Element hahmo = (Element) hahmoluettelo.item(j);
                String hahmotunnus = hahmo.getAttribute("n");
                // Hahmotunnukset kirjoitetaan indeksitaulukkoon, mutta vain kerran
                if (pelaaja == 0) {
                    this.yhteensopivuusdata.setHahmotunnus(j + 1, hahmotunnus);
                }
                Element yhteensopivuus = (Element) hahmo.getElementsByTagName("num").item(0);
                int yhteensopivuusprosentti = Integer.parseInt(yhteensopivuus.getAttribute("value"));
                // Yhteensopivuusprosentti kirjoitetaan matriisiin
                this.yhteensopivuusdata.setSopivuusprosentti(pelaaja + 1, j + 1, yhteensopivuusprosentti);
            } else {
                // Lisätään täytehahmoja kunnes hahmoja on yhtä monta kuin pelaajia
                if (pelaaja == 0) {
                    this.yhteensopivuusdata.setHahmotunnus(j + 1, "");
                }
                this.yhteensopivuusdata.setSopivuusprosentti(pelaaja + 1, j + 1, this.minimisopivuus);
            }
        }
    }
    
    /**
     * Tämä metodi päivittää nykyisen minimisopivuuden sopivuusmatriisin 
     * mahdollisiin täytehahmoihin.
     */
    private void paivitaSopivuusmatriisi() {        
        for (int i = 1; i <= yhteensopivuusdata.getPelaajamaara(); i++) {
            for (int j = yhteensopivuusdata.getHahmomaara() + 1; j <= yhteensopivuusdata.getPelaajamaara(); j++) {          
                this.yhteensopivuusdata.setSopivuusprosentti(i, j, this.minimisopivuus);
            }
        }
    }
    
    /**
     * Tämä metodi luo jokaiselle pelaajalle hahmoehdokaslistan ja jokaiselle 
     * hahmolle pelaajaehdokaslistan hahmojaon yhteensopivuusmatriisin 
     * perusteella.
     */
    public void luoEhdokaslistat() {
        this.ehdokaslistatOK = true;
        int pelaajamaara = yhteensopivuusdata.getPelaajamaara();        
        // Luodaan pelaajille hahmoehdokaslistat
        for (int i = 1; i <= pelaajamaara; i++) {
            Ehdokaslista ehdokaslista = new Ehdokaslista(this, "pelaaja", i);
            yhteensopivuusdata.setHahmoehdokaslista(i, ehdokaslista);
        }
        // Luodaan hahmoille pelaajaehdokaslistat
        for (int i = 1; i <= pelaajamaara; i++) {
            Ehdokaslista ehdokaslista = new Ehdokaslista(this, "hahmo", i);
            yhteensopivuusdata.setPelaajaehdokaslista(i, ehdokaslista);
        }
        if (this.ehdokaslistatOK == true) {
            System.out.println("Kaikille hahmoille löytyy minimisopivuuden ylittävä pelaaja.");
        }
    }
    
    /**
     * Tämä metodi laskee optimaalisen hahmojaon yhteesopivuusmatriisin, 
     * valitun algoritmin ja asetettujen ehtojen ja parametrien perusteella.
     * @return metodi palauttaa hahmojaon laskentaan kuluneen ajan sekunteina
     */
    public long teeHahmojako() {
        this.suorituksenAloitus = Instant.now();
        Tulosluettelo tulokset = new Tulosluettelo();
        if (this.kaytettavaAlgoritmi.contains("galeShapley")) {
            GaleShapley algoritmi = new GaleShapley(this);
            tulokset = algoritmi.laskeHahmojako();
        } else if (this.kaytettavaAlgoritmi.contains("peruuttava")) {
            Peruuttava algoritmi = new Peruuttava(this);
            tulokset = algoritmi.laskeHahmojako();
        } else if (this.kaytettavaAlgoritmi.contains("unkarilainen")) {
            Unkarilainen algoritmi = new Unkarilainen(this);
            tulokset = algoritmi.laskeHahmojako();    
        }
        if (tulokset.pituus() > 0) {
            lisaaTulos(tulokset);
            paivitaYhteistulokset(tulokset);
        } else {
            return -1;
        }
        Instant suorituksenLopetus = Instant.now();
        Duration suorituksenKesto = Duration.between(suorituksenAloitus, suorituksenLopetus);
        long suoritusaika = (suorituksenKesto.getSeconds() * 100) + (suorituksenKesto.getNano() / 10000000);
        return suoritusaika;
    }
    
    /**
     * Tämä metodi päivittää kaikkien tehtyjen hahmojakolaskentojen tuloksista 
     * muodostetut yhteistulokset uuden laskennan jälkeen.
     * @param tulokset laskennan tuloksena syntyneet ja yhteistuloksiin 
     * lisättävät tulokset sisältävä Tulosluettelo-olio
     */
    private void paivitaYhteistulokset(Tulosluettelo tulokset) {
        for (int i = 0; i < tulokset.pituus(); i++) {
            Tulos tulos = tulokset.hae(i);
            boolean kopio = false;
            for (int j = 0; j < this.yhteistulokset.pituus(); j++) {
                if (tulos.equals(this.yhteistulokset.hae(j))) {
                    kopio = true;
                }
            }
            if (kopio == false) {
                this.yhteistulokset.lisaa(tulos);
            }
        }
        this.yhteistulokset.jarjesta();
        if (this.yhteistulokset.pituus() > this.tuloksiaEnintaanYhteensa) {
            this.yhteistulokset = this.yhteistulokset.rajaa(this.tuloksiaEnintaanYhteensa);
        }
    }
}
