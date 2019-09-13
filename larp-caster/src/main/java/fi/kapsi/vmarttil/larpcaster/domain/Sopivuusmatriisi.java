/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

/**
 *
 * @author Ville
 */
public class Sopivuusmatriisi {
    final private String[] pelaajatunnukset;
    final private String[] hahmotunnukset;
    final private int[][] sopivuusprosentit;
    final private int pelaajamaara;
    final private int hahmomaara;
    
    
    // NB! Matriisista tehdään pelaajamäärän kokoinen, koska ylijäämäpelaajia 
    // varten matriisiin luodaan haamuhahmot joiden vastaavuusprosentiksi 
    // tulee minimivastaavuus. Näihin hahmoihin päätyminen tarkoittaa, ettei 
    // pelaaja mahdu peliin.
    
    public Sopivuusmatriisi(int pelaajamaara, int hahmomaara) {
        this.pelaajatunnukset = new String[pelaajamaara + 1];
        this.hahmotunnukset = new String[pelaajamaara + 1];
        this.sopivuusprosentit = new int[pelaajamaara + 1][pelaajamaara + 1];
        this.pelaajamaara = pelaajamaara;
        this.hahmomaara = hahmomaara;
    }

    // Getters
    
    public String getPelaajatunnus(int pelaajaindeksi) {
        return this.pelaajatunnukset[pelaajaindeksi];
    }

    public String getHahmotunnus(int hahmoindeksi) {
        return this.hahmotunnukset[hahmoindeksi];
    }

    public int getSopivuusprosentti(int pelaajaindeksi, int hahmoindeksi) {
        return this.sopivuusprosentit[pelaajaindeksi][hahmoindeksi];
    }
    
    public int getPelaajamaara() {
        return this.pelaajamaara;
    }
    
    public int getHahmomaara() {
        return this.hahmomaara;
    }
    
    // Setters
    
    public void setPelaajatunnus(int pelaajaindeksi, String pelaajatunnus) {
        this.pelaajatunnukset[pelaajaindeksi] = pelaajatunnus; 
    }

    public void setHahmotunnus(int hahmoindeksi, String hahmotunnus) {
        this.hahmotunnukset[hahmoindeksi] = hahmotunnus;
    }

    public void setSopivuusprosentti(int pelaajaindeksi, int hahmoindeksi, int sopivuus) {
        this.sopivuusprosentit[pelaajaindeksi][hahmoindeksi] = sopivuus;
    }
    
    
    
    
    
    
}
