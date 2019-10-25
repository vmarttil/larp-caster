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
        assertEquals(4, ehdokkaat);
    }
    
    @Test
    public void yhteistulostenHakuToimii() {
        this.hahmojako.setKaytettavaAlgoritmi("unkarilainen");
        this.hahmojako.setMinimisopivuus(70);
        this.hahmojako.teeHahmojako();
        this.hahmojako.setKaytettavaAlgoritmi("peruuttava");
        this.hahmojako.setMinimisopivuus(50);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getYhteistulokset().hae(0).getHahmojenPelaajat()[4];
        assertEquals(4, pelaaja);
    }
    
    @Test
    public void tulostenEnimmäismäärätToimivat() {
        this.hahmojako.setTuloksiaEnintaanLaskentaaKohden(20);
        this.hahmojako.setTuloksiaEnintaanYhteensa(30);
        int erotus = this.hahmojako.getTuloksiaEnintaanYhteensa() - this.hahmojako.getTuloksiaEnintaanLaskentaaKohden();
        assertEquals(10, erotus);
    }
    
    @Test
    public void enimmaisvariaatioastenAsetusToimii() {
        this.hahmojako.setEnimmaisvariaatioaste(4);
        assertEquals(4, this.hahmojako.getEnimmaisvariaatioaste());
    }
    
    @Test
    public void tulostenEnimmaismaaranAsetusToimii() {
        this.hahmojako.setTulostenEnimmaismaara(200);
        assertEquals(200, this.hahmojako.getTulostenEnimmaismaara());
    }
    
    @Test
    public void laskennanAikakatkaisunAsetusToimii() {
        this.hahmojako.setLaskennanAikakatkaisu(180);
        assertEquals(180, this.hahmojako.getLaskennanAikakatkaisu());
    }
    
    @Test
    public void diagnostiikkatilanAsetusToimii() {
        this.hahmojako.setDiagnostiikkatila(true);
        assertEquals(true, this.hahmojako.getDiagnostiikkatila());
    }
    
    @Test
    public void hahmojakoMahdottomillaEhdoillaEiToimi() {
        this.hahmojako.setKaytettavaAlgoritmi("galeShapleyHahmoKosii");
        this.hahmojako.setMinimisopivuus(90);
        assertEquals(-1, this.hahmojako.teeHahmojako());
    }
    
    
    
}
