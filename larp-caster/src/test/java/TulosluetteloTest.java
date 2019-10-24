/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import fi.kapsi.vmarttil.larpcaster.domain.Ehdokaslista;
import fi.kapsi.vmarttil.larpcaster.domain.Hahmojako;
import fi.kapsi.vmarttil.larpcaster.domain.Tulos;
import fi.kapsi.vmarttil.larpcaster.domain.Tulosluettelo;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.SAXException;

/**
 *
 * @author vmarttil
 */
public class TulosluetteloTest {
    
    Hahmojako hahmojako;
    
    public TulosluetteloTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.hahmojako = new Hahmojako();
        try {
            hahmojako.lataaYhteensopivuustiedot("LARPCaster_synthtest_1.xml");
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            System.out.println("");
            System.out.println("VIRHE: Yhteensopivuustietojen lataus ei onnistunut.");
            System.out.println("");
            e1.printStackTrace();
        }
        this.hahmojako.setKaytettavaAlgoritmi("peruuttava");
        this.hahmojako.setMinimisopivuus(60);
        this.hahmojako.teeHahmojako();
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    
    @Test
    public void tuloksenLisaaminenTulosluetteloonToimii() {
        Tulosluettelo luettelo = this.hahmojako.getHaunTulokset(0);
        Tulos tulos = new Tulos(this.hahmojako.getYhteensopivuusdata(),this.hahmojako.getHaunTulokset(0).hae(2).getPelaajienHahmot(), this.hahmojako.getHaunTulokset(0).hae(3).getHahmojenPelaajat());
        luettelo.lisaa(tulos);
        assertEquals(101, luettelo.pituus());
    }
    
    @Test
    public void tuloksenHakuTulosluettelostaToimii() {
        Tulosluettelo luettelo = this.hahmojako.getHaunTulokset(0);
        Tulos tulos = this.hahmojako.getHaunTulokset(0).hae(1);
        assertEquals("peruuttava", tulos.getAlgoritmi());
    }
    
    @Test
    public void tulosLuettelonRajaaminenPienemmäksiToimii() {
        Tulosluettelo rajattuLuettelo = this.hahmojako.getHaunTulokset(0).rajaa(30);
        assertEquals(30, rajattuLuettelo.pituus());
    }
    
    @Test
    public void tulosLuettelonRajaaminenSuuremmaksiEiToimi() {
        Tulosluettelo rajattuLuettelo = this.hahmojako.getHaunTulokset(0).rajaa(150);
        assertEquals(100, rajattuLuettelo.pituus());
    }
    
}
