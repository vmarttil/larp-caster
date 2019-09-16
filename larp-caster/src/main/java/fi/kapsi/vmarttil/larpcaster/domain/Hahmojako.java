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
 *
 * @author Ville
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

    public Sopivuusmatriisi getYhteensopivuusdata() {
        return yhteensopivuusdata;
    }

    public ArrayList<Tulos> getTulokset() {
        return tulokset;
    }

    public String getAlgoritmi() {
        return algoritmi;
    }

    public int getMinimisopivuus() {
        return minimisopivuus;
    }

    public boolean isPriorisoiHankalatHahmot() {
        return priorisoiHankalatHahmot;
    }

    public ArrayList<Integer> getHankalatHahmot() {
        return hankalatHahmot;
    }

    public HashMap<Integer, Integer> getEsivalitutPelaajat() {
        return esivalitutPelaajat;
    }
    
    // Setters
    
    public void setYhteensopivuusdata(Sopivuusmatriisi yhteensopivuusdata) {
        this.yhteensopivuusdata = yhteensopivuusdata;
    }

    public void setAlgoritmi(String algoritmi) {
        this.algoritmi = algoritmi;
    }

    public void setMinimisopivuus(int minimisopivuus) {
        this.minimisopivuus = minimisopivuus;
        luoEhdokaslistat();
    }

    public void setPriorisoiHankalatHahmot(boolean priorisoiHankalatHahmot) {
        this.priorisoiHankalatHahmot = priorisoiHankalatHahmot;
    }
    
    public void setEhdokaslistatOK(boolean ok) {
        this.ehdokaslistatOK = ok;
    }

    // Lisäykset ja poistot
    
    public void lisaaTulos(Tulos tulos) {
        this.tulokset.add(tulos);
    }
    
    public void poistaTulos(int tuloksenIndeksi) {
        this.tulokset.remove(tuloksenIndeksi);
    }
    
    public void lisaaHankalaHahmo(int hahmotunnus) {
        this.hankalatHahmot.add(hahmotunnus);
    }
    
    public void tyhjennaHankalatHahmot() {
        this.hankalatHahmot.clear();
    }
    
    public void esivalitsePelaaja(int hahmotunnus, int pelaajatunnus) {
        this.esivalitutPelaajat.put(hahmotunnus, pelaajatunnus);
    }
    
    public void poistaEsivalittuPelaaja(int hahmotunnus) {
        this.esivalitutPelaajat.remove(hahmotunnus);
    }
    
    // Operaatiot
    
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
                    String hahmotunnus = hahmo.getAttribute("xml:id");
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
