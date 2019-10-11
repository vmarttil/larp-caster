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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
    private String kaytettavaAlgoritmi;
    private int minimisopivuus;
    // Lisätään toteutukseen myöhemmin
    private boolean priorisoiHankalatHahmot;
    // Lisätään toteutukseen myöhemmin
    private ArrayList<Integer> hankalatHahmot;
    // Lisätään toteutukseen myöhemmin
    private HashMap<Integer, Integer> esivalitutPelaajat;
    private boolean ehdokaslistatOK;
    Instant suorituksenAloitus;
   
    /**
     * Tämä metodi luo Hahmojako-olion, joka ohjaa hahmojaon laskentaa ja siihen 
     * käytettäviä tietorakenteita.
     */
    public Hahmojako() {
        this.yhteensopivuusdata = null;
        this.tulokset = new ArrayList<>();
        this.kaytettavaAlgoritmi = "";
        this.minimisopivuus = 50;
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
    public String getKaytettavaAlgoritmi() {
        return kaytettavaAlgoritmi;
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
    
    public Instant getSuorituksenAloitus() {
        return this.suorituksenAloitus;
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
     * @param ok totuusarvo, joka on totta jos kaikkien pelaajien 
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
    
    private void luoSopivuusmatriisi(Document dokumentti) {
        int pelaajamaara = dokumentti.getElementsByTagName("person").getLength();
        int hahmomaara = dokumentti.getElementsByTagName("note").getLength() / dokumentti.getElementsByTagName("person").getLength();
        this.yhteensopivuusdata = new Sopivuusmatriisi(pelaajamaara, hahmomaara);
    }
    
    private void lisaaYhteensopivuustiedot(int i, int pelaajamaara, NodeList hahmoluettelo) {
        for (int j = 0; j < pelaajamaara; j++) {
            if (j < hahmoluettelo.getLength()) {
                Element hahmo = (Element) hahmoluettelo.item(j);
                String hahmotunnus = hahmo.getAttribute("n");
                // Hahmotunnukset kirjoitetaan indeksitaulukkoon, mutta vain kerran
                if (i == 0) {
                    this.yhteensopivuusdata.setHahmotunnus(j + 1, hahmotunnus);
                }
                Element yhteensopivuus = (Element) hahmo.getElementsByTagName("num").item(0);
                int yhteensopivuusprosentti = Integer.parseInt(yhteensopivuus.getAttribute("value"));
                // Yhteensopivuusprosentti kirjoitetaan matriisiin
                this.yhteensopivuusdata.setSopivuusprosentti(i + 1, j + 1, yhteensopivuusprosentti);
            } else {
                // Lisätään täytehahmoja kunnes hahmoja on yhtä monta kuin pelaajia
                if (i == 0) {
                    this.yhteensopivuusdata.setHahmotunnus(j + 1, "");
                }
                this.yhteensopivuusdata.setSopivuusprosentti(i + 1, j + 1, this.minimisopivuus);
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
        ehdokaslistatOK = true;
        int pelaajamaara = yhteensopivuusdata.getPelaajamaara();        
        // Luodaan pelaajille hahmoehdokaslistat
        for (int i = 1; i <= pelaajamaara; i++) {
            Ehdokaslista ehdokaslista = new Ehdokaslista(this, yhteensopivuusdata, minimisopivuus, "pelaaja", i);
            yhteensopivuusdata.setHahmoehdokaslista(i, ehdokaslista);
        }
        // Luodaan hahmoille pelaajaehdokaslistat
        for (int i = 1; i <= pelaajamaara; i++) {
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
    public long teeHahmojako() {
        this.suorituksenAloitus = Instant.now();
        if (this.kaytettavaAlgoritmi.contains("galeShapley")) {
            GaleShapley algoritmi = new GaleShapley(this);
            Tulos tulos = algoritmi.laskeHahmojako();
            lisaaTulos(tulos);
        } else if (this.kaytettavaAlgoritmi.contains("peruuttava")) {
            Peruuttava algoritmi = new Peruuttava(this);
            List<Tulos> tulokset = algoritmi.laskeHahmojako();
            int prioriteetti = 1;
            for (Tulos tulos : tulokset) {
                tulos.setPrioriteetti(prioriteetti);
                lisaaTulos(tulos);
                prioriteetti++;
            }
        } else if (this.kaytettavaAlgoritmi.contains("unkarilainen")) {
            Unkarilainen algoritmi = new Unkarilainen(this);
            List<Tulos> tulokset = algoritmi.laskeHahmojako();
            int prioriteetti = 1;
            for (Tulos tulos : tulokset) {
                tulos.setPrioriteetti(prioriteetti);
                lisaaTulos(tulos);
                prioriteetti++;
            }
        }
        
        Instant suorituksenLopetus = Instant.now();
        Duration suorituksenKesto = Duration.between(suorituksenAloitus, suorituksenLopetus);
        long suoritusaika = suorituksenKesto.getSeconds();
        return suoritusaika;
    }
    
}
