/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

import java.util.Arrays;

/**
 * Tämä luokka määrittää Tulosluettelo-olion, johon voidaan tallentaa Tulos-olioita 
 * tai toisia Tulosluettelo-olioita.
 * @author Ville Marttila
 */

public class Tulosluettelo {
    private int koko = 0;
    private static final int OLETUSKOKO = 10;
    private Tulos[] tulokset;
    
    /**
     * Tämä metodi luo Tulosluettelo-olion ja luo sille oletuskokoisen taulukon johon Tulos-oliot tallennetaan.
     */
    public Tulosluettelo() {
        this.tulokset = new Tulos[OLETUSKOKO];
    }
    
    /**
     * Tämä metodi lisää annetun Tulos-olion tulosluetteloon.
     * @param tulos lisättävä Tulos-olio
     */
    public void lisaa(Tulos tulos) {
        if (this.koko == this.tulokset.length) {
            suurennaLuetteloa();
        }
        this.tulokset[this.koko++] = tulos;
    }
    
    /**
     * Tämä metodi palauttaa indeksin osoittaman Tulos-olion tulosluettelosta.
     * @param indeksi palautettavan tuloksen indeksi
     * @return tämä metodi palauttaa Tulos-olion
     */
    @SuppressWarnings("unchecked")
    public Tulos hae(int indeksi) {
        if (indeksi >= koko || indeksi < 0) {
            throw new IndexOutOfBoundsException("Index: " + indeksi + ", Size " + koko);
        }
        return tulokset[indeksi];
    }
    
    /**
     * Tämä metodi palauttaa tulosluettelon tämänhetkisen pituuden.
     * @return tulosluettelon sisältämien tulosten määrän osoittava kokonaisluku
     */
    public int pituus() {
        return this.koko;
    }
    
    /**
     * Tämä metodi luo alusta määrätyn mittaiseksi rajatun kopion tulosluettelosta.
     * @param kokoAlusta luotavan rajatun tulosluettelon pituus alusta laskien
     * @return tämä metodi palauttaa rajatun mittaisen kopion tulosluettelosta
     */
    public Tulosluettelo rajaa(int kokoAlusta) {
        Tulosluettelo rajattuLuettelo = new Tulosluettelo();
        rajattuLuettelo.tulokset = Arrays.copyOfRange(this.tulokset, 0, kokoAlusta);
        if (kokoAlusta > this.koko) {
            rajattuLuettelo.koko = this.koko;
        } else {
            rajattuLuettelo.koko = kokoAlusta;
        }
        return rajattuLuettelo;
    }
    
    /**
     * Tämä metodi järjestää tulosluettelon laskevaan järjestykseen Tulos-olioiden keskimääräisen sopivuuden mukaisesti.
     */
    public void jarjesta() {
        this.tulokset = Arrays.copyOf(this.tulokset, koko);
        Arrays.sort(this.tulokset);
    }
    
    /**
     * Tämä metodi kaksinkertaistaa tulosluettelon tulosten tallentamiseen käyttämän taulukon koon. 
     */
    private void suurennaLuetteloa() {
        int uusiKoko = this.tulokset.length * 2;
        this.tulokset = Arrays.copyOf(this.tulokset, uusiKoko);
    }
}