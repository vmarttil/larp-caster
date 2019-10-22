/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import fi.kapsi.vmarttil.larpcaster.domain.Hahmojako;
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
 * @author Ville
 */
public class TulosTest {
    
    Hahmojako hahmojako;
    
    public TulosTest() {
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
        this.hahmojako.setKaytettavaAlgoritmi("galeShapleyPelaajaKosii");
        this.hahmojako.setMinimisopivuus(50);
        this.hahmojako.teeHahmojako();
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void huonoimmanJaParhaimmanSopivuudenHakuToimii() {
        double sopivuusero = this.hahmojako.getTulokset().get(0).get(0).getParasSopivuus() - this.hahmojako.getTulokset().get(0).get(0).getHuonoinSopivuus();
        assertEquals(20.0, sopivuusero, 0.1);
    }
    
    @Test
    public void sopivuuskeskiarvonHakuToimii() {
        double ka = this.hahmojako.getTulokset().get(0).get(0).getSopivuuskeskiarvo();
        assertEquals(86.0, ka, 0.1);
    }
    
    @Test
    public void mediaanisopivuudenHakuToimii() {
        double med = this.hahmojako.getTulokset().get(0).get(0).getMediaanisopivuus();
        assertEquals(87.5, med, 0.1);
    }
    
    @Test
    public void pelaajienHahmojenHakuToimii() {
        int hahmo = this.hahmojako.getTulokset().get(0).get(0).getPelaajienHahmot()[6];
        assertEquals(6, hahmo);
    }
    
    @Test
    public void hahmojenPelaajienHakuToimii() {
        int pelaaja = this.hahmojako.getTulokset().get(0).get(0).getHahmojenPelaajat()[3];
        assertEquals(3, pelaaja);
    }

    
}
