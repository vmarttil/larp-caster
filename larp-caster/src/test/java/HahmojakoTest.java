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
public class HahmojakoTest {
    
    Hahmojako hahmojako;
    
    public HahmojakoTest() {
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
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void yhteensopivuustiedotLatautuvatOikein() {
        int pelaajamaara = hahmojako.getYhteensopivuusdata().getPelaajamaara();
        assertEquals(10, pelaajamaara);
    }
    
    @Test
    public void algoritminAsetusToimii() {
        this.hahmojako.setKaytettavaAlgoritmi("galeShapleyHahmoKosii");
        String algoritmi = hahmojako.getKaytettavaAlgoritmi();
        assertEquals("galeShapleyHahmoKosii", algoritmi);
    }
    
    @Test
    public void minimiyhteensopivuudenAsetusToimii() {
        this.hahmojako.setMinimisopivuus(45);
        int minimisopivuus = hahmojako.getMinimisopivuus();
        assertEquals(45, minimisopivuus);
    }
    
    @Test
    public void ehdokaslistojenLuontiToimiiOikein() {
        this.hahmojako.setMinimisopivuus(50);
        this.hahmojako.luoEhdokaslistat();
        int ehdokas1 = this.hahmojako.getYhteensopivuusdata().getHahmoehdokaslista(1).getEhdokas(0);
        int ehdokas2 = this.hahmojako.getYhteensopivuusdata().getPelaajaehdokaslista(4).getEhdokas(1);
        int ehdokkaat = ehdokas1 + ehdokas2;
        assertEquals(3, ehdokkaat);
    }
    
    @Test
    public void hahmojakoGaleShapleyHahmoSyntDatallaToimiiOikein() {
        this.hahmojako.setKaytettavaAlgoritmi("galeShapleyHahmoKosii");
        this.hahmojako.setMinimisopivuus(50);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset().get(0).getHahmojenPelaajat().get(5);
        assertEquals(5, pelaaja);
    }
    
    @Test
    public void hahmojakoGaleShapleyPelaajaSyntDatallaToimiiOikein() {
        this.hahmojako.setKaytettavaAlgoritmi("galeShapleyPelaajaKosii");
        this.hahmojako.setMinimisopivuus(70);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset().get(0).getHahmojenPelaajat().get(3);
        assertEquals(3, pelaaja);
    }
    
    @Test
    public void hahmojakoGaleShapleyHahmoOdyDatallaToimiiOikein() {
        this.hahmojako = new Hahmojako();
        try {
            hahmojako.lataaYhteensopivuustiedot("LARPCaster_odytest_all.xml");
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            System.out.println("");
            System.out.println("VIRHE: Yhteensopivuustietojen lataus ei onnistunut.");
            System.out.println("");
            e1.printStackTrace();
        }
        this.hahmojako.setKaytettavaAlgoritmi("galeShapleyHahmoKosii");
        this.hahmojako.setMinimisopivuus(50);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset().get(0).getHahmojenPelaajat().get(15);
        assertEquals(85, pelaaja);
    }
    
    @Test
    public void hahmojakoGaleShapleyPelaajaOdyDatallaToimiiOikein() {
        this.hahmojako = new Hahmojako();
        try {
            hahmojako.lataaYhteensopivuustiedot("LARPCaster_odytest_all.xml");
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            System.out.println("");
            System.out.println("VIRHE: Yhteensopivuustietojen lataus ei onnistunut.");
            System.out.println("");
            e1.printStackTrace();
        }
        this.hahmojako.setKaytettavaAlgoritmi("galeShapleyPelaajaKosii");
        this.hahmojako.setMinimisopivuus(50);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset().get(0).getHahmojenPelaajat().get(15);
        assertEquals(85, pelaaja);
    }
}
