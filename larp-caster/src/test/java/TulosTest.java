/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import fi.kapsi.vmarttil.larpcaster.domain.Hahmojako;
import fi.kapsi.vmarttil.larpcaster.domain.Tulos;
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
        this.hahmojako.setKaytettavaAlgoritmi("peruuttava");
        this.hahmojako.setMinimisopivuus(60);
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
        double sopivuusero = this.hahmojako.getHaunTulokset(0).hae(0).getParasSopivuus() - this.hahmojako.getHaunTulokset(0).hae(0).getHuonoinSopivuus();
        assertEquals(20.0, sopivuusero, 0.1);
    }
    
    @Test
    public void sopivuuskeskiarvonHakuToimii() {
        double ka = this.hahmojako.getHaunTulokset(0).hae(0).getSopivuuskeskiarvo();
        assertEquals(86.0, ka, 0.1);
    }
    
    @Test
    public void mediaanisopivuudenHakuToimii() {
        double med = this.hahmojako.getHaunTulokset(0).hae(0).getMediaanisopivuus();
        assertEquals(87.5, med, 0.1);
    }
    
    @Test
    public void kaytetynAlgoritminHakuToimii() {
        String algoritmi = this.hahmojako.getHaunTulokset(0).hae(0).getAlgoritmi();
        assertEquals("peruuttava", algoritmi);
    }

    @Test
    public void kaytetynMinimisopivuudenHakuToimii() {
        int minimisopivuus = this.hahmojako.getHaunTulokset(0).hae(0).getMinimiyhteensopivuus();
        assertEquals(60, minimisopivuus);
    }
    
    @Test
    public void pelaajienHahmojenHakuToimii() {
        int hahmo = this.hahmojako.getTulokset()[0].hae(0).getPelaajienHahmot()[6];
        assertEquals(6, hahmo);
    }
    
    @Test
    public void hahmottomienPelaajienHakuToimii() {
        this.hahmojako = new Hahmojako();
        try {
            hahmojako.lataaYhteensopivuustiedot("LARPCaster_odytest_1_13-26.xml");
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            System.out.println("");
            System.out.println("VIRHE: Yhteensopivuustietojen lataus ei onnistunut.");
            System.out.println("");
            e1.printStackTrace();
        }
        this.hahmojako.setKaytettavaAlgoritmi("peruuttava");
        this.hahmojako.setMinimisopivuus(20);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmottomatPelaajat()[0];
        assertEquals(1, pelaaja);
    }
    
    @Test
    public void hahmojenPelaajienHakuToimii() {
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmojenPelaajat()[3];
        assertEquals(3, pelaaja);
    }

    @Test
    public void jarjestysnumeronHakuToimii() {
        int jarjestys = this.hahmojako.getTulokset()[0].hae(3).getJarjestysnumero();
        assertEquals(8, jarjestys);
    }
    
    @Test
    public void tuloksenVertailuItseensaToimii() {
        Tulos tulos = this.hahmojako.getTulokset()[0].hae(0);
        boolean identiteetti = tulos.equals(tulos);
        assertEquals(true, identiteetti);
    }
    
    @Test
    public void tuloksenVertailuMuuhunObjektiinToimii() {
        Tulos tulos = this.hahmojako.getTulokset()[0].hae(0);
        
        boolean kaltainen = tulos.equals(this.hahmojako);
        assertEquals(false, kaltainen);
    }
}
