/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import fi.kapsi.vmarttil.larpcaster.domain.Hahmojako;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
public class IntegraatioTest {
    
    Hahmojako hahmojako;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    
    public IntegraatioTest() {
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
    
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    
    @After
    public void tearDown() {
    }
    
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    
    @Test
    public void hahmojakoGaleShapleyHahmoSyntDatallaToimiiOikein() {
        this.hahmojako.setKaytettavaAlgoritmi("galeShapleyHahmoKosii");
        this.hahmojako.setMinimisopivuus(50);
        this.hahmojako.setDiagnostiikkatila(true);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmojenPelaajat()[5];
        assertEquals(5, pelaaja);
    }
    
    @Test
    public void hahmojakoGaleShapleyPelaajaSyntDatallaToimiiOikein() {
        this.hahmojako.setKaytettavaAlgoritmi("galeShapleyPelaajaKosii");
        this.hahmojako.setMinimisopivuus(70);
        this.hahmojako.setDiagnostiikkatila(true);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmojenPelaajat()[3];
        assertEquals(3, pelaaja);
    }
    
    @Test
    public void hahmojakoGaleShapleyHahmoOdyDatallaToimiiOikein() {
        this.hahmojako = new Hahmojako();
        try {
            hahmojako.lataaYhteensopivuustiedot("LARPCaster_odytest_1_26-52.xml");
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            System.out.println("");
            System.out.println("VIRHE: Yhteensopivuustietojen lataus ei onnistunut.");
            System.out.println("");
            e1.printStackTrace();
        }
        this.hahmojako.setKaytettavaAlgoritmi("galeShapleyHahmoKosii");
        this.hahmojako.setMinimisopivuus(50);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmojenPelaajat()[15];
        assertEquals(18, pelaaja);
    }
    
    @Test
    public void hahmojakoGaleShapleyPelaajaOdyDatallaToimiiOikein() {
        this.hahmojako = new Hahmojako();
        try {
            hahmojako.lataaYhteensopivuustiedot("LARPCaster_odytest_1_26-52.xml");
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            System.out.println("");
            System.out.println("VIRHE: Yhteensopivuustietojen lataus ei onnistunut.");
            System.out.println("");
            e1.printStackTrace();
        }
        this.hahmojako.setKaytettavaAlgoritmi("galeShapleyPelaajaKosii");
        this.hahmojako.setMinimisopivuus(50);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmojenPelaajat()[15];
        assertEquals(18, pelaaja);
    }
    
    @Test
    public void hahmojakoGaleShapleyHahmoParittomallaOdyDatallaToimiiOikein() {
        this.hahmojako = new Hahmojako();
        try {
            hahmojako.lataaYhteensopivuustiedot("LARPCaster_odytest_1_13-26.xml");
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            System.out.println("");
            System.out.println("VIRHE: Yhteensopivuustietojen lataus ei onnistunut.");
            System.out.println("");
            e1.printStackTrace();
        }
        this.hahmojako.setKaytettavaAlgoritmi("galeShapleyHahmoKosii");
        this.hahmojako.setMinimisopivuus(0);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmojenPelaajat()[5];
        assertEquals(16, pelaaja);
    }
    
    @Test
    public void hahmojakoGaleShapleyHahmoSuurellaOdyDatallaToimiiOikein() {
        this.hahmojako = new Hahmojako();
        try {
            hahmojako.lataaYhteensopivuustiedot("LARPCaster_odytest_1_52-104.xml");
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            System.out.println("");
            System.out.println("VIRHE: Yhteensopivuustietojen lataus ei onnistunut.");
            System.out.println("");
            e1.printStackTrace();
        }
        this.hahmojako.setKaytettavaAlgoritmi("galeShapleyPelaajaKosii");
        this.hahmojako.setMinimisopivuus(40);
        this.hahmojako.setTulostenEnimmaismaara(100);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmojenPelaajat()[5];
        assertEquals(99, pelaaja);
    }
    
    @Test
    public void hahmojakoGaleShapleyPelaajaSuurellaOdyDatallaToimiiOikein() {
        this.hahmojako = new Hahmojako();
        try {
            hahmojako.lataaYhteensopivuustiedot("LARPCaster_odytest_1_52-104.xml");
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            System.out.println("");
            System.out.println("VIRHE: Yhteensopivuustietojen lataus ei onnistunut.");
            System.out.println("");
            e1.printStackTrace();
        }
        this.hahmojako.setKaytettavaAlgoritmi("galeShapleyPelaajaKosii");
        this.hahmojako.setMinimisopivuus(30);
        this.hahmojako.setTulostenEnimmaismaara(100);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmojenPelaajat()[5];
        assertEquals(99, pelaaja);
    }
    
    @Test
    public void hahmojakoPeruuttavaSyntDatalla50ToimiiOikein() {
        this.hahmojako.setKaytettavaAlgoritmi("peruuttava");
        this.hahmojako.setMinimisopivuus(50);
        this.hahmojako.setDiagnostiikkatila(true);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmojenPelaajat()[3];
        assertEquals(3, pelaaja);
    }
    
    @Test
    public void hahmojakoPeruuttavaSyntDatalla70ToimiiOikein() {
        this.hahmojako.setKaytettavaAlgoritmi("peruuttava");
        this.hahmojako.setMinimisopivuus(70);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmojenPelaajat()[5];
        assertEquals(5, pelaaja);
    }
    
    @Test
    public void hahmojakoPeruuttavaOdyDatallaToimiiOikein() {
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
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmojenPelaajat()[5];
        assertEquals(18, pelaaja);
    }
    
    @Test
    public void hahmojakoUnkarilainenSyntDatalla50ToimiiOikein() {
        this.hahmojako.setKaytettavaAlgoritmi("unkarilainen");
        this.hahmojako.setMinimisopivuus(50);
        this.hahmojako.setDiagnostiikkatila(true);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmojenPelaajat()[8];
        assertEquals(8, pelaaja);
    }
    
    @Test
    public void hahmojakoUnkarilainenSyntDatalla80ToimiiOikein() {
        this.hahmojako.setKaytettavaAlgoritmi("unkarilainen");
        this.hahmojako.setMinimisopivuus(70);
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmojenPelaajat()[1];
        assertEquals(1, pelaaja);
    }
    
    @Test
    public void hahmojakoUnkarilanenSuurellaOdyDatallaToimiiOikein() {
        this.hahmojako = new Hahmojako();
        try {
            hahmojako.lataaYhteensopivuustiedot("LARPCaster_odytest_1_52-104.xml");
        } catch (SAXException | ParserConfigurationException | IOException e1) {
            System.out.println("");
            System.out.println("VIRHE: Yhteensopivuustietojen lataus ei onnistunut.");
            System.out.println("");
            e1.printStackTrace();
        }
        this.hahmojako.setKaytettavaAlgoritmi("unkarilainen");
        this.hahmojako.teeHahmojako();
        int pelaaja = this.hahmojako.getTulokset()[0].hae(0).getHahmojenPelaajat()[20];
        assertEquals(40, pelaaja);
    }
    
    
}
