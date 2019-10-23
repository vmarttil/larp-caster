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
 * @author vmarttil
 */





public class Tulosluettelo {
    private int koko = 0;
    private static final int OLETUSKOKO = 10;
    private Tulos[] tulokset;
    
    public Tulosluettelo() {
        this.tulokset = new Tulos[OLETUSKOKO];
    }
    
    public void lisaa(Tulos tulos) {
        if (this.koko == this.tulokset.length) {
            suurennaLuetteloa();
        }
        this.tulokset[this.koko++] = tulos;
    }
    
    @SuppressWarnings("unchecked")
    public Tulos hae(int indeksi) {
        if (indeksi >= koko || indeksi < 0) {
            throw new IndexOutOfBoundsException("Index: " + indeksi + ", Size " + koko );
        }
        return tulokset[indeksi];
    }
    
    public int pituus() {
        return this.koko;
    }
    
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
    
    public void jarjesta() {
        this.tulokset = Arrays.copyOf(this.tulokset, koko);
        Arrays.sort(this.tulokset);
    }
    
    private void suurennaLuetteloa() {
        int uusiKoko = this.tulokset.length * 2;
        this.tulokset = Arrays.copyOf(this.tulokset, uusiKoko);
    }
    
    
    
    
}