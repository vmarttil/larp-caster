/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author vmarttil
 */
public class Yhteenveto {
    Hahmojako hahmojako;
    Sopivuusmatriisi yhteensopivuusdata;
    Tulosluettelo yhteistulokset;
    Laskuri[][] hahmojenYhteenveto;
    Laskuri[][] pelaajienYhteenveto;
    int hahmojakojenMaara;
    
    public Yhteenveto(Hahmojako hahmojako) {
        this.hahmojako = hahmojako;
        this.yhteensopivuusdata = hahmojako.getYhteensopivuusdata();
        this.yhteistulokset = hahmojako.getYhteistulokset();
        this.hahmojenYhteenveto = new Laskuri[yhteensopivuusdata.getHahmomaara() + 1][];
        this.pelaajienYhteenveto = new Laskuri[yhteensopivuusdata.getPelaajamaara() + 1][];
        laskeHahmojenYhteenveto();
        laskePelaajienYhteenveto();
        hahmojakojenMaara = yhteistulokset.pituus();
    }
    
    private void laskeHahmojenYhteenveto() {
        Laskuri[][] hahmojenPelaajat = new Laskuri[yhteensopivuusdata.getHahmomaara() + 1][yhteensopivuusdata.getPelaajamaara() + 1];
        // Alustetaan laskurit
        for (int i = 0; i <= yhteensopivuusdata.getHahmomaara(); i++) {
            for (int j = 0; j <= yhteensopivuusdata.getPelaajamaara(); j++) {
                if (i == 0 || j == 0) {
                    hahmojenPelaajat[i][j] = new Laskuri("-");
                }
                hahmojenPelaajat[i][j] = new Laskuri(yhteensopivuusdata.getPelaajatunnus(j));
            }
        }
        // Käydään hahmot ja niiden pelaajat eri tuloksissa yksi kerrallaan läpi
        for (int h = 1; h <= yhteensopivuusdata.getHahmomaara(); h++) {
            for (int t = 0; t < this.yhteistulokset.pituus(); t++) {
                Tulos tulos = this.yhteistulokset.hae(t);
                hahmojenPelaajat[h][tulos.getHahmojenPelaajat()[h]].kasvata();
            }
        }
        // Lajitellaan kunkin hahmon laskuriluettelo niiden laskemien määrien perusteella
        for (int h = 1; h <= yhteensopivuusdata.getHahmomaara(); h++) {
            Laskuri[] hahmonLaskurilista = hahmojenPelaajat[h];
            Arrays.sort(hahmonLaskurilista);
            List<Laskuri> aktiivisetLaskurit = new ArrayList<>();
            for (int p = 0; p < hahmonLaskurilista.length; p++) {
                if (hahmonLaskurilista[p].getMaara() > 0) {
                    aktiivisetLaskurit.add(hahmonLaskurilista[p]);
                }
            }
            hahmojenYhteenveto[h] = new Laskuri[aktiivisetLaskurit.size()];
            for (int i = 0; i < aktiivisetLaskurit.size(); i++) {
                hahmojenYhteenveto[h][i] = aktiivisetLaskurit.get(i);
            }
        }
    }
    
    private void laskePelaajienYhteenveto() {
        Laskuri[][] pelaajienHahmot = new Laskuri[yhteensopivuusdata.getPelaajamaara() + 1][yhteensopivuusdata.getHahmomaara() + 1];
        // Alustetaan laskurit
        for (int i = 0; i <= yhteensopivuusdata.getPelaajamaara(); i++) {
            for (int j = 0; j <= yhteensopivuusdata.getHahmomaara(); j++) {
                if (i == 0 || j == 0) {
                    pelaajienHahmot[i][j] = new Laskuri("-");
                } else {
                    pelaajienHahmot[i][j] = new Laskuri(yhteensopivuusdata.getHahmotunnus(j));
                }
            }
        }
        // Käydään pelaajat ja niiden hahmot eri tuloksissa yksi kerrallaan läpi
        for (int p = 1; p <= yhteensopivuusdata.getPelaajamaara(); p++) {
            for (int t = 0; t < this.yhteistulokset.pituus(); t++) {
                Tulos tulos = this.yhteistulokset.hae(t);
                int hahmo = tulos.getPelaajienHahmot()[p];
                pelaajienHahmot[p][hahmo].kasvata();
            }
        }
        // Lajitellaan kunkin hahmon laskuriluettelo niiden laskemien määrien perusteella
        for (int p = 1; p <= yhteensopivuusdata.getPelaajamaara(); p++) {
            Laskuri[] pelaajanLaskurilista = pelaajienHahmot[p];
            Arrays.sort(pelaajanLaskurilista);
            List<Laskuri> aktiivisetLaskurit = new ArrayList<>();
            for (int h = 0; h <= yhteensopivuusdata.getHahmomaara(); h++) {
                if (pelaajanLaskurilista[h].getMaara() > 0) {
                    aktiivisetLaskurit.add(pelaajanLaskurilista[h]);
                }
            }
            pelaajienYhteenveto[p] = new Laskuri[aktiivisetLaskurit.size()];
            for (int i = 0; i < aktiivisetLaskurit.size(); i++) {
                pelaajienYhteenveto[p][i] = aktiivisetLaskurit.get(i);
            }
        }    
    }
    
    // Getterit

    public Laskuri[] getHahmonYhteenveto(int hahmonIndeksi) {
        return this.hahmojenYhteenveto[hahmonIndeksi];
    }

    public Laskuri[] getPelaajanYhteenveto(int pelaajanIndeksi) {
        return this.pelaajienYhteenveto[pelaajanIndeksi];
    }

    public int getHahmojakojenMaara() {
        return this.hahmojakojenMaara;
    }
    
}
