/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import fi.kapsi.vmarttil.larpcaster.domain.Ehdokaslista;
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
 * @author vmarttil
 */
public class EhdokaslistaTest {
    
    Hahmojako hahmojako;
    
    public EhdokaslistaTest() {
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
        this.hahmojako.setMinimisopivuus(50);
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    
    @Test
    public void pelaajaehdokkaidenPuutteenTarkistusToimii() {
        this.hahmojako.setMinimisopivuus(90);
        assertEquals(false, this.hahmojako.getEhdokaslistatOK());
    }
    
    @Test
    public void karsitunEhdokaslistanLuontiToimii() {
        Ehdokaslista rajattuLista = new Ehdokaslista(this.hahmojako.getYhteensopivuusdata().getHahmoehdokaslista(2), 70, 1);
        assertEquals(2, rajattuLista.getPituus());
    }
    
    @Test
    public void ehdokkaanAsettaminenToimii() {
        Ehdokaslista lista = this.hahmojako.getYhteensopivuusdata().getHahmoehdokaslista(3);
        lista.setEhdokas(7, 60, 4);
        lista.setEhdokas(4, 70, 5);
        assertEquals(7, lista.getEhdokas(4));
    }
    
    @Test
    public void ehdokkaanPoistaminenToimii() {
        Ehdokaslista lista = this.hahmojako.getYhteensopivuusdata().getHahmoehdokaslista(5);
        lista.poistaEhdokas(2);
        assertEquals(8, lista.getEhdokas(2));
    }
}
