# Testausdokumentti: LARPCaster-työkalu

Työkalun toiminnallisuuden säilyttämistä varten varten projektiin on kirjoitettu joukko automaattisia JUnit-yksikkötestejä, jotka testaavat työkalun perustoiminnallisuutta sekä yksittäisten metodikutsujen että laajempien integraatiotestien avulla käyttäen sekä yksinkertaista synteettistä testidataa että todellisesta käyttötapauksesta kerättyä esimerkkidataa. Lisäksi ohjelman sisältämiä algoritmeja – erityisesti niiden tuottamien tulosten laatua, mutta myös niiden suorituskykyä – testataan sekä pienellä synteettisellä testdatalla että realistisen kokoisella käyttödatajoukolla. Tämä auttaa sekä optimoimaan ja kehittämään algoritmeja tuotantoprosessin aikana, että mahdollistaa lopullisten algoritmien suorituskykyvertailun ja siihen perustuvien käyttösuositusten laatimisen. 

## Testidata
Sekä automaattisissa yksikkötesteissä että suorituskykytestauksessa käytetään testidatana sekä pientä ja ennustettavasti käyttäytyvää synteettistä ja tarkoitusta varten laadittua datajoukkoa (LARPCaster_synthtest_1.xml: 10 hahmoa, 10 pelaajaa, tunnettu sopivuusjärjestys), että aikaisempien liveroolipelien hahmonjakoprosesseissa käytettyä todellisia hahmojen ja pelaajien välisiä yhteensopivuusmatriiseja vaihtelevan kokoisiksi datajoukoiksi rajattuna. Suurimmat käytettävät datajoukot (310 pelaajaa, 104 hahmoa) edustavat tosielämän tapausten yläpäätä, jota suuremmat datajoukot ovat epätodennäköisiä, joten käytettävyyden kannalta riittää, jos työkalun algoritmit toimivat järkevästi tämän kokoisilla datajoukoilla. Tyypillisempää käyttötapausta on mallinnettu keskikokoisella (104 pelaajaa, 52 hahmoa) datajoukolla. Testien toistettavuuden vuoksi kaikki tehdyt manuaaliset testit on kuvailtu alla. Koska ohjelman käyttämät algoritmit toimivat hyvin eri periaatteilla, ne käyttäytyvät hyvin eri tavalla erilaisilla testidatoilla – mikä on myös syy sille, että työkalu sisältää ne kaikki – joten mekaaniset suorituskykyvertailut eivät ole kovin mielekkäitä, vaan niiden sijaan testaus on toteutettu analysoimalla niiden käyttäytymistä erilaisissa käyttötapauksissa.

## Automaattiset testit
Perustoiminnallisuutta testaavat JUnit-yksikkötestit pyrkivät testaamaan sekä yksittäisiä metodeja, että useiden metodien ja tietorakenteiden yhteistoimintaa sekä yksittäisten luokkien sisällä että luokkien välissä, mukaillen ohjelman todellista työnkulkua. Automaattisissa testeissä käytetään joko synteettistä testidataa (LARPCaster_synthtest_1.xml) tai eri kokoisia todellisen datan alajoukkoja, jotta saadaan mallinnettua työkalun metodien käyttäytyminen eri olosuhteissa. Kaikkia työkalun tiloja ei kuitenkaan ollut mahdollista saavuttaa käytettävissä olevalla testidatalla, joten etenkin suoritushaarojen ja osin myös komentojen osalta testikattavuus ei ole kaikkien luokkien osalta täydellinen vaan jää 90–95 prosenttiin. Alla on esitetty automattisten testien kattavuutta kuvaavat jacoco-työkalun raportit (käyttöliittymän metodeja ei testattu automaattisesti eivätkä ne näin ollen sisälly testikattavuusraporttiin):






## Manuaalinen tulosanalyysi
Toistaiseksi tämä osio sisältää suuntaa antavia ja algoritmien optimoinnissa auttavia alustavia havaintoja algoritmien käyttäytymisestä ja suorituskyvystä. Lopullinen testiraportti tulee sisältämään kattavan analyysin eri algoritmien heikkouksista ja vahvuuksista sekä niiden suorituskyvyn vertailua erilaisilla datajoukoilla.

### Galen-Shapleyn algoritmi
Galen-Shapleyn algoritmi pyrkii muodostamaan vakaan parituksen osapuolten (tässä tapauksessa hahmot ja pelaajat) jäsenten välille siten, että yhdessäkään parissa molemmilla osapuolilla ei ole paremmin sopivaa kumppania parin ulkopuolella. Tämä algoritmi on hyvin tehokas, mutta tämänhetkisessä toteutuksessaan tuottaa vain yhden vakaan parituksen; käytännön tilanteen kannalta olisi hyödyllisempää jos algoritmi tuottaisi kaikki halutun minimiyhteensopivuuden ylittävät vakaat paritukset, koska tämä antaa enemmän liikkumavaraa laskennan ulkopuolisten rajoitteiden ja preferenssien huomioon ottamiselle.

Teoreettinen aikavaativuus on O(n²), jossa n on pelaajien määrä; hahmojen määrä m voi olla tätä pienempi, mutta koska täydellisen parituksen aikaansaamiseksi algoritmia varten luodaan n-m keinotekoista hahmoa joilla on minimiyhteensopivuus kaikkiin pelaajiin, aikavaativuus määräytyy pelaajien määrän mukaan. Koska käytettävät datajoukot ovat tosielämässä suhteellisen pieniä (<200 pelaajaa), pysyy suoritusaika hyvin pienenä tällä aikavaativuudella, jopa siinä määärin, että suurimmallakin datajoukolla laskenta-aika pyöristyy 0,0 sekuntiin, mikä tekee vertailusta mahdotonta tällä tarkkuudella. Tässä toteutuksessa suurimman pullonkaulan algoritmin tehokkuudelle muodostaakin algoritmin suorittaminen potentiaalisesti hyvin suurella (enimmillään n!) joukolla ehdokaslistavariantteja.



### Peruuttava haku
Verrattuna Galen-Shapleyn-algoritmiin peruuttavaan hakuun perustuva algoritmi - joka perustuu raakaan laskentatehoon - on valtavan epätehokas, koska se laskee kaikki sallitut hahmojaot, joita hiemankaan suuremmalla datajoukolla ja väljillä minimivaatimuksilla kertyy helposti miljoonia. Algoritmin mukaan ottamisen tarkoitus on testata, onko tällaista algoritmia mahdollista optimoida datan ominaispiirteitä ja rajoitteita hyödyntäen siten, että se toimisi riittävän tehokkaasti vaadittavan kokoisilla syötteillä. Toistaiseksi algoritmia on optimoitu ainoastaan siten, että se käyttää mahdollisuuksien läpikäynnissä kullekin hahmolle laskettua mahdollisten pelaajien joukkoa, joka on (hieman minimisopivuusasetuksesta riippuen) selvästi kaikkien pelaajien määrää pienempi joukko, ja siten, että algoritmi käy vaihtoehdot läpi sopivimmasta alkaen. Tässä muodossaan algoritmi on käytössä olevista datajoukoista käyttökelpoinen ainoastaan synteettisen testidatan (LARPCaster_synthtest_1.xml) sekä pienimmän todellisen datan alijoukon LARPCaster_odytest_1_13_13.xml tapauksessa, joissa se tuottaa seuraavanlaiset tulokset:

~~~~
  Datajoukko: LARPCaster_synthtest_1.xml (10 hahmoa, 10 pelaajaa)
  Minimisopivuus: 50%
  Ratkaisuja yhteensä: 22 830
  Laskennan kesto: 2 s

50 parasta laskettua ratkaisua ja niiden laskentajärjestys:
  1 - (1. laskettu) - ka. sop. 86.0%
  2 - (3. laskettu) - ka. sop. 84.0%
  3 - (18467. laskettu) - ka. sop. 83.5%
  4 - (11334. laskettu) - ka. sop. 83.5%
  5 - (5019. laskettu) - ka. sop. 83.5%
  6 - (252. laskettu) - ka. sop. 83.5%
  7 - (191. laskettu) - ka. sop. 83.5%
  8 - (25. laskettu) - ka. sop. 83.5%
  9 - (17. laskettu) - ka. sop. 83.5%
  10 - (12. laskettu) - ka. sop. 83.5%
  11 - (6. laskettu) - ka. sop. 83.5%
  12 - (4783. laskettu) - ka. sop. 83.0%
  13 - (157. laskettu) - ka. sop. 83.0%
  14 - (27. laskettu) - ka. sop. 83.0%
  15 - (5027. laskettu) - ka. sop. 82.5%
  16 - (3376. laskettu) - ka. sop. 82.5%
  17 - (1028. laskettu) - ka. sop. 82.5%
  18 - (364. laskettu) - ka. sop. 82.5%
  19 - (199. laskettu) - ka. sop. 82.5%
  20 - (127. laskettu) - ka. sop. 82.5%
  21 - (90. laskettu) - ka. sop. 82.5%
  22 - (29. laskettu) - ka. sop. 82.5%
  23 - (18528. laskettu) - ka. sop. 82.0%
  24 - (18480. laskettu) - ka. sop. 82.0%
  25 - (15281. laskettu) - ka. sop. 82.0%
  26 - (11268. laskettu) - ka. sop. 82.0%
  27 - (5029. laskettu) - ka. sop. 82.0%
  28 - (1309. laskettu) - ka. sop. 82.0%
  29 - (1047. laskettu) - ka. sop. 82.0%
  30 - (1030. laskettu) - ka. sop. 82.0%
  31 - (985. laskettu) - ka. sop. 82.0%
  32 - (760. laskettu) - ka. sop. 82.0%
  33 - (530. laskettu) - ka. sop. 82.0%
  34 - (201. laskettu) - ka. sop. 82.0%
  35 - (46. laskettu) - ka. sop. 82.0%
  36 - (9. laskettu) - ka. sop. 82.0%
  37 - (7. laskettu) - ka. sop. 82.0%
  38 - (2. laskettu) - ka. sop. 82.0%
  39 - (18469. laskettu) - ka. sop. 81.5%
  40 - (18359. laskettu) - ka. sop. 81.5%
  41 - (16431. laskettu) - ka. sop. 81.5%
  42 - (11454. laskettu) - ka. sop. 81.5%
  43 - (11408. laskettu) - ka. sop. 81.5%
  44 - (11336. laskettu) - ka. sop. 81.5%
  45 - (5089. laskettu) - ka. sop. 81.5%
  46 - (5031. laskettu) - ka. sop. 81.5%
  47 - (5025. laskettu) - ka. sop. 81.5%
  48 - (5021. laskettu) - ka. sop. 81.5%
  49 - (4791. laskettu) - ka. sop. 81.5%
  50 - (4788. laskettu) - ka. sop. 81.5% 
~~~~
~~~~
  Datajoukko: LARPCaster_odytest_1_13_26.xml (13 hahmoa, 26 pelaajaa)
  Minimisopivuus: 50%
  Ratkaisuja yhteensä: 635 923 (katkaisu kun peräkkäisten ratkaisujen löytämiseen menee yli 2 s)
  Laskennan kesto: 42 s
  
50 parasta laskettua ratkaisua ja niiden laskentajärjestys:
  1 - (5127. laskettu) - ka. sop. 76.69%
  2 - (308209. laskettu) - ka. sop. 76.61%
  3 - (5105. laskettu) - ka. sop. 76.46%
  4 - (308187. laskettu) - ka. sop. 76.38%
  5 - (6029. laskettu) - ka. sop. 76.38%
  6 - (309462. laskettu) - ka. sop. 76.3%
  7 - (7084. laskettu) - ka. sop. 76.3%
  8 - (5192. laskettu) - ka. sop. 76.3%
  9 - (310868. laskettu) - ka. sop. 76.23%
  10 - (308274. laskettu) - ka. sop. 76.23%
  11 - (6054. laskettu) - ka. sop. 76.23%
  12 - (309487. laskettu) - ka. sop. 76.15%
  13 - (6931. laskettu) - ka. sop. 76.15%
  14 - (6007. laskettu) - ka. sop. 76.15%
  15 - (5280. laskettu) - ka. sop. 76.15%
  16 - (310715. laskettu) - ka. sop. 76.07%
  17 - (309440. laskettu) - ka. sop. 76.07%
  18 - (308362. laskettu) - ka. sop. 76.07%
  19 - (7062. laskettu) - ka. sop. 76.07%
  20 - (5130. laskettu) - ka. sop. 76.07%
  21 - (310846. laskettu) - ka. sop. 76.0%
  22 - (308212. laskettu) - ka. sop. 76.0%
  23 - (6956. laskettu) - ka. sop. 76.0%
  24 - (6182. laskettu) - ka. sop. 76.0%
  25 - (6094. laskettu) - ka. sop. 76.0%
  26 - (5107. laskettu) - ka. sop. 76.0%
  27 - (1079. laskettu) - ka. sop. 76.0%
  28 - (310740. laskettu) - ka. sop. 75.92%
  29 - (309615. laskettu) - ka. sop. 75.92%
  30 - (309527. laskettu) - ka. sop. 75.92%
  31 - (308189. laskettu) - ka. sop. 75.92%
  32 - (302512. laskettu) - ka. sop. 75.92%
  33 - (27147. laskettu) - ka. sop. 75.92%
  34 - (7149. laskettu) - ka. sop. 75.92%
  35 - (6909. laskettu) - ka. sop. 75.92%
  36 - (5258. laskettu) - ka. sop. 75.92%
  37 - (24. laskettu) - ka. sop. 75.92%
  38 - (338960. laskettu) - ka. sop. 75.84%
  39 - (310933. laskettu) - ka. sop. 75.84%
  40 - (310693. laskettu) - ka. sop. 75.84%
  41 - (308340. laskettu) - ka. sop. 75.84%
  42 - (301106. laskettu) - ka. sop. 75.84%
  43 - (71800. laskettu) - ka. sop. 75.84%
  44 - (6119. laskettu) - ka. sop. 75.84%
  45 - (6019. laskettu) - ka. sop. 75.84%
  46 - (400612. laskettu) - ka. sop. 75.76%
  47 - (309552. laskettu) - ka. sop. 75.76%
  48 - (309452. laskettu) - ka. sop. 75.76%
  49 - (15354. laskettu) - ka. sop. 75.76%
  50 - (6996. laskettu) - ka. sop. 75.76% 
~~~~
Suuremmilla testidatajoukoilla suoritusaika kasvaa niin suureksi, ettei algoritmi ole tämänhetkisessä tilassaan käyttökelpoinen. Tuloksia tutkittaessa ja laskentaprosessia väliaikatulosteiden avulla seuratessa käy kuitenkin selväksi, että ratkaisujen laskenta tapahtuu huomattavan nopeasti tiettyyn pisteeseen saakka (useimmilla syötteillä n. 1 min), jonka jälkeen helpot ratkaisut on käyty läpi ja yksittäisen ratkaisun laskuaika nousee radikaalisti. Jos laskenta keskeytetään tässä vaiheessa ja asetetaan lasketut ratkaisut sopivuusjärjestykseen, huomataan, että jo tässä vaiheessa algoritmi on löytänyt huomattavan määrän (10-100) ratkaisuja, joiden sopivuus selvästi lähestyy maksimia (ja on usein hieman suurempi kuin Gale-Shapleyn algoritmilla lasketun ratkaisun sopivuus. Näin ollen saattaa olla järkevää luopua kaikkien mahdollisten tulosten laskentayrityksestä ja laskea sen sijaan vain "helpot" eli kohtuullisessa ajassa laskettavat ratkaisut, sillä jo ne tuottavat niin suuren määrän sopivuudeltaan lähellä maksimia olevia ratkaisuja, ettei suuremmasta määrästä ole juurikaan käytännön hyötyä. Tämä tarjoaa yhden mahdollisuuden algoritmin optimointiin ja asettaa luonnolliseksi seuraavaksi askeleeksi sen selvittämisen, miten algoritmi saataisiin laskemaan parhaat ratkaisut laskennan alkupäässä siten, että ratkaisujen sopivuus laskisi laskennan edetessä jolloin riittää x:n ensimmäisen ratkaisun laskeminen. 

### Vertailua

Tässä vaiheessa tulosten mielekäs suorituskykyvertailu ei vielä ole mahdollista, koska Galen-Shapleyn algoritmi tuottaa toistaiseksi yhden ratkaisun siinä, missä peruuttavaan hakuun perustuva algoritmi tuottaa parhaimmillaan kymmeniä miljoonia ratkaisuja.

Algoritmien tuottamien tulosten suhteen voidaan kuitenkin tehdä vertailua jo tässä vaiheessa, joten alla on esitetty peruuttavaan hakuun perustuvan algoritmin sopivin ratkaisu ja Galen-Shapleyn algoritmin tuottama ratkaisu molemmissa yllä kuvatuista tapauksista:

#### LARPCaster_synthtest_1.xml
~~~~
  Peruuttava haku
  Hahmo:                  Pelaaja:                Sopivuus:
  h1                      p1                      90 %
  h2                      p2                      80 %
  h3                      p3                      75 %
  h4                      p4                      85 %
  h5                      p5                      90 %
  h6                      p6                      90 %
  h7                      p7                      85 %
  h8                      p8                      95 %
  h9                      p9                      90 %
  h10                     p10                     80 %
~~~~
~~~~
  Gale-Shapley
  Hahmo:                  Pelaaja:                Sopivuus:
  h1                      p1                      90 %
  h2                      p2                      80 %
  h3                      p3                      75 %
  h4                      p4                      85 %
  h5                      p5                      90 %
  h6                      p6                      90 %
  h7                      p7                      85 %
  h8                      p8                      95 %
  h9                      p9                      90 %
  h10                     p10                     80 %
~~~~
#### LARPCaster_odytest_1_13_26.xml
~~~~
  Peruuttava haku
  Hahmo:                  Pelaaja:                Sopivuus:
  jin_komatsu             22                      77 %
  dallan_jordan           26                      83 %
  yera_romero             3                       78 %
  kai_rogers              24                      79 %
  cal_allen               18                      53 %
  idris_kalashnik         12                      77 %
  skye_duran              8                       87 %
  tyler_carrillo          19                      82 %
  xavier_blake            13                      64 %
  alia_swanson            15                      83 %
  zyra_lee                4                       81 %
  malak_kovalenko         10                      75 %
  the_guardian            5                       78 %
~~~~
~~~~
  Gale-Shapley
  Hahmo:                  Pelaaja:                Sopivuus:
  jin_komatsu             22                      77 %
  dallan_jordan           26                      83 %
  yera_romero             3                       78 %
  kai_rogers              24                      79 %
  cal_allen               -                       -
  idris_kalashnik         12                      77 %
  skye_duran              8                       87 %
  tyler_carrillo          19                      82 %
  xavier_blake            13                      64 %
  alia_swanson            15                      83 %
  zyra_lee                4                       81 %
  malak_kovalenko         10                      75 %
  the_guardian            18                      84 %
~~~~
