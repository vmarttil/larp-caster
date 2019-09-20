/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fi.kapsi.vmarttil.larpcaster.ui;

import fi.kapsi.vmarttil.larpcaster.domain.Hahmojako;

/**
 * Tämä luokka luo ja käynnistää hahmojaon ja sen käyttöliittymän.
 * @author Ville Marttila
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        Hahmojako hahmojako = new Hahmojako();
        TextUI kayttoliittyma = new TextUI();
        kayttoliittyma.kaynnista(hahmojako);
    }   
    
    
}
