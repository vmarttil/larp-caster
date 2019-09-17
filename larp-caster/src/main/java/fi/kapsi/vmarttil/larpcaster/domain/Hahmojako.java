/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Tämä luokka määrittelee hahmojakosovelluksen keskeisen toimintalogiikan ja 
 * hallinnoi sen tietorakenteita.
 * @author Ville Marttila
 */
public class Hahmojako {
    
    private Sopivuusmatriisi yhteensopivuusdata;
    private ArrayList<Tulos> tulokset;
    private String algoritmi;
    private int minimisopivuus;
    // Lisätään toteutukseen myöhemmin
    private boolean priorisoiHankalatHahmot;
    // Lisätään toteutukseen myöhemmin
    private ArrayList<Integer> hankalatHahmot;
    // Lisätään toteutukseen myöhemmin
    private HashMap<Integer,Integer> esivalitutPelaajat;
    private boolean ehdokaslistatOK;
   
    public Hahmojako() {
        this.yhteensopivuusdata = null;
        this.tulokset = new ArrayList<>();
        this.algoritmi = "";
        this.minimisopivuus = 70;
        // Lisätään toteutukseen myöhemmin
        this.priorisoiHankalatHahmot = false;
        // Lisätään toteutukseen myöhemmin
        this.hankalatHahmot = new ArrayList<>();
        // Lisätään toteutukseen myöhemmin
        this.esivalitutPelaajat = new HashMap<>();
        this.ehdokaslistatOK = true;
    }
     
    // Getters

    /**
     * Tämä metodi palauttaa hahmojaon pohjana käytettävän yhteensopivuusdatan.
     * @return metodi palauttaa Sopivuusmatriisi-olion, joka sisältää kaikkien 
     * pelaajien ja hahmojen väliset yhteensopivuusprosentit
     */
    public Sopivuusmatriisi getYhteensopivuusdata() {
        return yhteensopivuusdata;
    }

    /**
     * Tämä metodi palauttaa hahmojakojen tulokset.
     * @return metodi palauttaa taulukkolistan Tulos-olioita, joista kukin 
     * edustaa yhden hahmojaon tuloksia metatietoineen.
     */
    public ArrayList<Tulos> getTulokset() {
        return tulokset;
    }

    /**
     * Tämä metodi palauttaa hahmojakoon käytettävän algoritmin.
     * @return metodi palauttaa käytetyn algoritmin tunnuksen merkkijonona
     */
    public String getAlgoritmi() {
        return algoritmi;
    }

    /**
     * Tämä metodi palauttaa hahmojaossa käytettäväksi määritetyn vähimmäis-
     * yhteensopivuuden.
     * @return metodi palauttaa pienimmän sallitun yhteensopivuusprosentin 
     * kokonaislukuna
     */
    public int getMinimisopivuus() {
        return minimisopivuus;
    }

    /**
     * Tämä metodi palauttaa hankalasti jaettavien hahmojen priorisoinnin 
     * asetuksen.
     * @return metodi palauttaa totuusarvon, joka kertoo priorisoidaanko 
     * hahmojaossa hankalasti jaettavia hahmoja
     */
    public boolean isPriorisoiHankalatHahmot() {
        return priorisoiHankalatHahmot;
    }

    /**
     * Tämä metodi palauttaa hankalasti jaettaviksi määriteltyjen hahmojen 
     * (joilla on vain yksi sopiva pelaajaehdokas) indeksit.
     * @return metodi palauttaa hahmoindeksit sisältävän taulukkolistan
     */
    public ArrayList<Integer> getHankalatHahmot() {
        return hankalatHahmot;
    }
    
    /**
     * Tämä metodi palauttaa ennen hahmojakoa käsin määritetyt hahmon ja 
     * pelaajan yhdistelmät.
     * @return metodi palauttaa määritetyt hahmo-pelaaja-parit sisältävän 
     * hajautustaulun
     */
    public HashMap<Integer, Integer> getEsivalitutPelaajat() {
        return esivalitutPelaajat;
    }
    
    /**
     * Tämä metodi palauttaa tiedon siitä, sisältääkö jokaisen pelaajan 
     * ja hahmon ehdokaslista vähintään yhden sopivan hahmon tai pelaajan.
     * @return metodi palauttaa totuusarvon joka kertoo sisältävätkö kaikki 
     * ehdokaslistat vähintään yhden ehdokkaan
     */
    public boolean getEhdokaslistatOK() {
        return ehdokaslistatOK;
    }
    
    // Setters
    
    /**
     * Täme metodi asettaa annetun sopivuusmatriisin käytettäväksi hahmojaossa.
     * @param yhteensopivuusdata Sopivuusmatriisi-olio, joka sisältää XML-
     * tiedostosta ladatut pelaajien ja hahmojen yhteensopivuustiedot
     */
    public void setYhteensopivuusdata(Sopivuusmatriisi yhteensopivuusdata) {
        this.yhteensopivuusdata = yhteensopivuusdata;
    }

    /**
     * Tämä metodi asettaa hahmojaossa käytettävän algoritmin.
     * @param algoritmi käytettävän algoritmin tunnus merkkijonona
     */
    public void setAlgoritmi(String algoritmi) {
        this.algoritmi = algoritmi;
    }

    /**
     * Tämä metodi asettaa hahmojaossa käytettävän pienimmän sallitun 
     * yhteensopivuusprosentin.
     * @param minimisopivuus käytettävä minimiyhteensopivuus prosenttiarvoa 
     * kuvaavana kokonaislukuna
     */
    public void setMinimisopivuus(int minimisopivuus) {
        this.minimisopivuus = minimisopivuus;
        luoEhdokaslistat();
    }

    /**
     * Tämä metodi määrittää hankalasti jaettavien hahmojen (joilla on vain yksi 
     * sopiva pelaajaehdokas) priorisoinnin käyttöön tai pois käytöstä.
     * @param priorisoiHankalatHahmot totuusarvo, joka määrittää priorisoidaanko 
     * hankalasti jaettavat hahmot
     */
    public void setPriorisoiHankalatHahmot(boolean priorisoiHankalatHahmot) {
        this.priorisoiHankalatHahmot = priorisoiHankalatHahmot;
    }
    
    /**
     * Tämä metodi kertoo sisältääkö jokaisen pelaajan ja hahmon ehdokaslista 
     * vähintään yhden sopivan hahmon tai pelaajan.
     * @param ehdokaslistatOK totuusarvo, joka on totta jos kaikkien pelaajien 
     * ja hahmojen ehdokaslistat vähintään yhden sopivan ehdokkaan
     */
    public void setEhdokaslistatOK(boolean ok) {
        this.ehdokaslistatOK = ok;
    }

    // Lisäykset ja poistot
    
    /**
     * Tämä metodi lisää jollakin algoritmilla lasketun hahmojaon tuloksen 
     * tulosluetteloon.
     * @param tulos Tulos-olio joka sisältää hahmojaon tuloksen ja siihen 
     * liittyvät metatiedot
     */
    public void lisaaTulos(Tulos tulos) {
        this.tulokset.add(tulos);
    }
    
    /**
     * Tämä metodi poistaa tulosluettelosta yhden hahmojaon tuloksen. 
     * @param tuloksenIndeksi kokonaisluku, joka osoittaa poistettavan 
     * tuloksen indeksin
     */
    public void poistaTulos(int tuloksenIndeksi) {
        this.tulokset.remove(tuloksenIndeksi);
    }
    
    /**
     * Tämä metodi lisää hahmon hankalien hahmojen luetteloon. 
     * @param hahmotunnus kokonaisluku, joka osoittaa lisättävän hahmon indeksin
     */
    public void lisaaHankalaHahmo(int hahmotunnus) {
        this.hankalatHahmot.add(hahmotunnus);
    }
    
    /**
     * Tämä metodi tyhjentää hankalien hahmojen luettelon.
     */
    public void tyhjennaHankalatHahmot() {
        this.hankalatHahmot.clear();
    }
    
    /**
     * Tämä metodi tallentaa hahmon ja pelaajan yhdistelmän esivalittujen 
     * pelaajien luetteloon.
     * @param hahmoindeksi kokonaisluku, joka osoittaa sen hahmon indeksin, johon
     * pelaaja yhdistetään
     * @param pelaajaindeksi kokonaisluku, joka osoittaa hahmoon yhdistettävän 
     * pelaajan indeksin
     */
    public void esivalitsePelaaja(int hahmoindeksi, int pelaajaindeksi) {
        this.esivalitutPelaajat.put(hahmoindeksi, pelaajaindeksi);
    }
    
    /**
     * Tämä metodi poistaa hahmon ja pelaajan yhdistelmän esivalittujen 
     * pelaajien luettelosta hahmoindeksin perusteella.
     * @param hahmoindeksi poistettavan hahmon indeksi
     */
    public void poistaEsivalittuPelaaja(int hahmoindeksi) {
        this.esivalitutPelaajat.remove(hahmoindeksi);
    }
    
    // Operaatiot
    
    /**
     * Tämä metodi lataa hahmojaossa käytettävät yhteensopivuustiedot XML-
     * tiedostosta.
     * @param tiedostonimi merkkijono, joka kertoo käytettävän XML-tiedoston 
     * nimen
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException 
     */
    public void lataaYhteensopivuustiedot(String tiedostonimi) throws SAXException, ParserConfigurationException, IOException {    
        File xmlTiedosto = new File(tiedostonimi);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        dBuilder = dbFactory.newDocumentBuilder();
        Document dokumentti = dBuilder.parse(xmlTiedosto);
        dokumentti.getDocumentElement().normalize();
        NodeList pelaajaluettelo = dokumentti.getElementsByTagName("person");
        int pelaajamaara = pelaajaluettelo.getLength();
        int hahmomaara = pelaajaluettelo.item(0).getChildNodes().getLength();
        Sopivuusmatriisi yhteensopivuudet = new Sopivuusmatriisi(pelaajamaara, hahmomaara);
        for (int i = 0; i < pelaajamaara; i++) {
            Element pelaaja = (Element) pelaajaluettelo.item(i);
            String pelaajatunnus = pelaaja.getAttribute("xml:id");
            // Pelaajatunnukset kirjoitetaan indeksitaulukkoon
            yhteensopivuudet.setPelaajatunnus(i + 1, pelaajatunnus);
            NodeList hahmoluettelo = pelaaja.getElementsByTagName("note");
            // Lisätään yhteensopivuustiedot kaikille hahmoille ja tarvittavalle määrälle tyhjiä täytehahmoja
            for (int j = 0; j < pelaajaluettelo.getLength(); j++) {
                if (j < hahmoluettelo.getLength()) {
                    Element hahmo = (Element) hahmoluettelo.item(j);
                    String hahmotunnus = hahmo.getAttribute("n");
                    // Hahmotunnukset kirjoitetaan indeksitaulukkoon, mutta vain kerran
                    if (i == 0) {
                        yhteensopivuudet.setHahmotunnus(j + 1, hahmotunnus);
                    }
                    Element yhteensopivuus = (Element) hahmo.getElementsByTagName("num").item(0);
                    int yhteensopivuusprosentti = Integer.parseInt(yhteensopivuus.getAttribute("value"));
                    // Yhteensopivuusprosentti kirjoitetaan matriisiin
                    yhteensopivuudet.setSopivuusprosentti(i + 1, j + 1, yhteensopivuusprosentti);
                } else {
                    // Lisätään täytehahmoja kunnes hahmoja on yhtä monta kuin pelaajia
                    if (i == 0) {
                        yhteensopivuudet.setHahmotunnus(j + 1, "");
                    }
                    yhteensopivuudet.setSopivuusprosentti(i + 1, j + 1, this.minimisopivuus);
                }
            }
        }
        setYhteensopivuusdata(yhteensopivuudet);
        System.out.println("");
        System.out.println("Yhteensopivuustiedot ladattu tiedostosta " + tiedostonimi);
        luoEhdokaslistat();  
    } 
    
    /**
     * Tämä metodi luo jokaiselle pelaajalle hahmoehdokaslistan ja jokaiselle 
     * hahmolle pelaajaehdokaslistan hahmojaon yhteensopivuusmatriisin 
     * perusteella.
     */
    public void luoEhdokaslistat() {
        ehdokaslistatOK = true;
        int pelaajamaara = yhteensopivuusdata.getPelaajamaara();        
        // Luodaan pelaajille hahmoehdokaslistat
        for (int i=1; i<=pelaajamaara; i++) {
            Ehdokaslista ehdokaslista = new Ehdokaslista(this, yhteensopivuusdata, minimisopivuus, "pelaaja", i);
            yhteensopivuusdata.setHahmoehdokaslista(i, ehdokaslista);
        }
        // Luodaan hahmoille pelaajaehdokaslistat
        for (int i=1; i<=pelaajamaara; i++) {
            Ehdokaslista ehdokaslista = new Ehdokaslista(this, yhteensopivuusdata, minimisopivuus, "hahmo", i);
            yhteensopivuusdata.setPelaajaehdokaslista(i, ehdokaslista);
        }
        if (ehdokaslistatOK == true) {
            System.out.println("Kaikille hahmoille ja pelaajille löytyy minimisopivuuden ylittävä pelaaja tai hahmo.");
        }
    }
    
    /**
     * Tämä metodi laskee optimaalisen hahmojaon yhteesopivuusmatriisin, 
     * valitun algoritmin ja asetettujen ehtojen ja parametrien perusteella.
     */
    public void teeHahmojako() {
        Tulos tulos = null;
        if (this.algoritmi.equals("galeShapleyPelaajaKosii")) {
            tulos = Algoritmit.galeShapleyHahmoKosii(this.yhteensopivuusdata, this.minimisopivuus);
        } else if (this.algoritmi.equals("galeShapleyPelaajaKosii")) {
            tulos = Algoritmit.galeShapleyPelaajaKosii(this.yhteensopivuusdata, this.minimisopivuus);
        }
        this.tulokset.add(tulos);
    }
    
    
}
