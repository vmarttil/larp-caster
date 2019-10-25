# Testausdokumentti: LARPCaster-työkalu

Työkalun toiminnallisuuden säilyttämistä varten varten projektiin on kirjoitettu joukko automaattisia JUnit-yksikkötestejä, jotka testaavat työkalun perustoiminnallisuutta sekä yksittäisten metodikutsujen että laajempien integraatiotestien avulla käyttäen sekä yksinkertaista synteettistä testidataa että todellisesta käyttötapauksesta kerättyä esimerkkidataa. Lisäksi ohjelman sisältämiä algoritmeja – erityisesti niiden tuottamien tulosten laatua, mutta myös niiden suorituskykyä – testataan sekä pienellä synteettisellä testdatalla että realistisen kokoisella käyttödatajoukolla. Tämä auttaa sekä optimoimaan ja kehittämään algoritmeja tuotantoprosessin aikana, että mahdollistaa lopullisten algoritmien suorituskykyvertailun ja siihen perustuvien käyttösuositusten laatimisen. 

## Testidata
Sekä automaattisissa yksikkötesteissä että suorituskykytestauksessa käytetään testidatana sekä pientä ja ennustettavasti käyttäytyvää synteettistä ja tarkoitusta varten laadittua datajoukkoa (LARPCaster_synthtest_1.xml: 10 hahmoa, 10 pelaajaa, tunnettu sopivuusjärjestys), että aikaisempien liveroolipelien hahmonjakoprosesseissa käytettyä todellisia hahmojen ja pelaajien välisiä yhteensopivuusmatriiseja vaihtelevan kokoisiksi datajoukoiksi rajattuna. Suurimmat käytettävät datajoukot (310 pelaajaa, 104 hahmoa) edustavat tosielämän tapausten yläpäätä, jota suuremmat datajoukot ovat epätodennäköisiä, joten käytettävyyden kannalta riittää, jos työkalun algoritmit toimivat järkevästi tämän kokoisilla datajoukoilla. Tyypillisempää käyttötapausta on mallinnettu keskikokoisella (104 pelaajaa, 52 hahmoa) datajoukolla. Testien toistettavuuden vuoksi kaikki tehdyt manuaaliset testit on kuvailtu alla. Koska ohjelman käyttämät algoritmit toimivat hyvin eri periaatteilla, ne käyttäytyvät hyvin eri tavalla erilaisilla testidatoilla – mikä on myös syy sille, että työkalu sisältää ne kaikki – joten mekaaniset suorituskykyvertailut eivät ole kovin mielekkäitä, vaan niiden sijaan testaus on toteutettu analysoimalla niiden käyttäytymistä erilaisissa käyttötapauksissa.

## Automaattiset testit
Perustoiminnallisuutta testaavat JUnit-yksikkötestit pyrkivät testaamaan sekä yksittäisiä metodeja, että useiden metodien ja tietorakenteiden yhteistoimintaa sekä yksittäisten luokkien sisällä että luokkien välissä, mukaillen ohjelman todellista työnkulkua. Automaattisissa testeissä käytetään joko synteettistä testidataa (LARPCaster_synthtest_1.xml) tai eri kokoisia todellisen datan alajoukkoja, jotta saadaan mallinnettua työkalun metodien käyttäytyminen eri olosuhteissa. Kaikkia työkalun tiloja ei kuitenkaan ollut mahdollista saavuttaa käytettävissä olevalla testidatalla, joten etenkin suoritushaarojen ja osin myös komentojen osalta testikattavuus ei ole kaikkien luokkien osalta täydellinen vaan jää 90–95 prosenttiin. Alla on esitetty automattisten testien kattavuutta kuvaavat jacoco-työkalun raportit (käyttöliittymän metodeja ei testattu automaattisesti eivätkä ne näin ollen sisälly testikattavuusraporttiin):

![Testikattavuus: larp-caster](https://github.com/vmarttil/larp-caster/blob/master/dokumentaatio/kuvat/testit_larp-caster.png)

![Testikattavuus: algorithms](https://github.com/vmarttil/larp-caster/blob/master/dokumentaatio/kuvat/testit_algorithms.png)

![Testikattavuus: Unkarilainen](https://github.com/vmarttil/larp-caster/blob/master/dokumentaatio/kuvat/testit_unkarilainen.png)
![Testikattavuus: Galeshapley](https://github.com/vmarttil/larp-caster/blob/master/dokumentaatio/kuvat/testit_galeshapley.png)
![Testikattavuus: Peruuttava](https://github.com/vmarttil/larp-caster/blob/master/dokumentaatio/kuvat/testit_peruuttava.png)

![Testikattavuus: domain](https://github.com/vmarttil/larp-caster/blob/master/dokumentaatio/kuvat/testit_domain.png)

![Testikattavuus: Hahmojako](https://github.com/vmarttil/larp-caster/blob/master/dokumentaatio/kuvat/testit_hahmojako.png)
![Testikattavuus: Ehdokaslista](https://github.com/vmarttil/larp-caster/blob/master/dokumentaatio/kuvat/testit_ehdokaslista.png)
![Testikattavuus: Tulos](https://github.com/vmarttil/larp-caster/blob/master/dokumentaatio/kuvat/testit_tulos.png)
![Testikattavuus: Tulosluettelo](https://github.com/vmarttil/larp-caster/blob/master/dokumentaatio/kuvat/testit_tulosluettelo.png)
![Testikattavuus: Sopivuusmatriisi](https://github.com/vmarttil/larp-caster/blob/master/dokumentaatio/kuvat/testit_sopivuusmatriisi.png)

## Manuaalinen tulosanalyysi
Työkalun eri algoritmien käyttäytymistä eri tilanteissa on testattu ajamalla niitä eri kokoisilla datajoukoilla ja eri parametreilla ja vertailemalla niiden käyttäytymistä. Eri algoritmien välistä suoraa suorituskykyvertailua ei ole mielekästä tehdä, koska esimerkiksi niiden tuottamat tulokset vaikuttavat voimakkaasti niiden vaatimaan laskenta-aikaan, joten niiden käyttäytyminen on hyvin epälineaarista suhteessa datajoukon kokoon ja riipuu useimmissa tapauksissa enemmän datajoukon luonteesta kuin sen koosta sekä käytetyistä parametreista. Tämän vuoksi suoran vertailun sijaan seuraavissa testituloksissa keskitytään enemmän kunkin algoritmin käyttäytymisen ja ominaispiirteiden tarkasteluun.

### Galen-Shapleyn algoritmi
Galen-Shapleyn algoritmi pyrkii muodostamaan vakaan parituksen osapuolten (tässä tapauksessa hahmot ja pelaajat) jäsenten välille siten, että yhdessäkään parissa molemmilla osapuolilla ei ole paremmin sopivaa kumppania parin ulkopuolella. Tämä algoritmi on hyvin tehokas, mutta tämänhetkisessä toteutuksessaan tuottaa vain yhden vakaan parituksen; käytännön tilanteen kannalta olisi hyödyllisempää jos algoritmi tuottaisi kaikki halutun minimiyhteensopivuuden ylittävät vakaat paritukset, koska tämä antaa enemmän liikkumavaraa laskennan ulkopuolisten rajoitteiden ja preferenssien huomioon ottamiselle.

Teoreettinen aikavaativuus on O(n²), jossa n on pelaajien määrä; hahmojen määrä m voi olla tätä pienempi, mutta koska täydellisen parituksen aikaansaamiseksi algoritmia varten luodaan n-m keinotekoista hahmoa joilla on minimiyhteensopivuus kaikkiin pelaajiin, aikavaativuus määräytyy pelaajien määrän mukaan. Koska käytettävät datajoukot ovat tosielämässä suhteellisen pieniä (<200 pelaajaa), pysyy suoritusaika hyvin pienenä tällä aikavaativuudella, jopa siinä määärin, että suurimmallakin datajoukolla laskenta-aika pyöristyy 0,0 sekuntiin, mikä tekee vertailusta mahdotonta tällä tarkkuudella. Tässä toteutuksessa suurimman pullonkaulan algoritmin tehokkuudelle muodostaakin algoritmin suorittaminen potentiaalisesti hyvin suurella (enimmillään n!) joukolla ehdokaslistavariantteja.

Käytännössä Galen-Shapleyn algoritmi on itsessään hyvin nopea ja suoriutuu suuristakin syötteistä sekunnin murto-osissa, mutta tuottaa vain yhden hahmojaon. Kun siihen yhdistetään ehdokaslistavariaatioiden laskenta ja läpikäynti, saattaa tämä tietynkaltaisella aineistolla (joka sisältää paljon potentiaalisia variantteja joista harva johtaa kelvolliseen hahmojakoon) aiheuttaa aikavaativuuden huomattavan kasvun, mutta tämä ei suoraan riipu käytettävän syöteaineiston koosta. Esimerkiksi kolmella eri kokoisella aineistolla Galen-Shapleyn algoritmi käytti seuraavasti aikaa ja tuotti seuraavat tulokset:

~~~~
Aineisto:                                                   Algoritmi:   Variaatioita:  Hahmojakoja:  Max.Yht.sop.:  Aika:

LARPCaster_synthtest_1.xml (10 hahmoa, 10 pelaajaa)         G-S/pelaaja      1424            1          86,0 %       1 ms
LARPCaster_synthtest_1.xml (10 hahmoa, 10 pelaajaa)         G-S/hahmo        148             2          86,0 %       13 ms

LARPCaster_odytest_1_52-104.xml (52 hahmoa, 104 pelaajaa)   G-S/pelaaja     50 022*          1          80,0 %       351 ms
LARPCaster_odytest_1_52-104.xml (52 hahmoa, 104 pelaajaa)   G-S/hahmo       50 017*          1          80,21 %      338 ms

LARPCaster_odytest_all.xml (104 hahmoa, 310 pelaajaa)       G-S/pelaaja     50 052*          1          83,73 %      892 ms
LARPCaster_odytest_all.xml (104 hahmoa, 310 pelaajaa)       G-S/hahmo       50 027*          1          83,74 %      814 ms
LARPCaster_odytest_all.xml (104 hahmoa, 310 pelaajaa)       G-S/pelaaja    100 051*          1          83,73 %     1770 ms
LARPCaster_odytest_all.xml (104 hahmoa, 310 pelaajaa)       G-S/hahmo      100 027*          1          83,74 %     1467 ms

~~~~

Näistä mittaustuloksista voidaan todeta, että vaikka Galen-Shapleyn algoritmin itsensä aikavaativuus on teoreettisesti O(n²), ja sen oheen lisätyn varianttien laskennan pitäisi nostaa aikavaativuutta entisestään, vaikuttaa käytetty aika kasvavan suhteellisen lineaarisesti käytettävän aineiston koon ja laskettujen varianttien suhteen, tulosten pysyessa paljolti ennallaan. Tämä tekee Galen-Shapleyn algoritmista hyvin tehokkaan tilanteissa, joissa ei tarvita montaa hahmojakoa. 

### Peruuttava haku
Kontrastina voidaan tarkastella optimoitua ja laskevaan sopivuusrajaan perustuvaa peruuttavaa hakua, joka on raakaan laskentatehoon perustuvana hyvin epätehokas verrattuna Galen-Shapleyn-algoritmiin. Sen hyvä puoli kuitenkin on, että se antaa *hyvin* suuren määrän vaihtehtoisia hahmojakoja varsin pienillä yhteensopivuuseroilla, mikäli aineisto sellaiset mahdollistaa. Tämän vuoksi sen käytölle on tietyissä tilanteissa perusteensa, ja rajoittamalla laskettavien varianttien määrää ja pyrkimällä laskemaan ensin kaikkein yhteensopivimat versiot siitä voidaan kuitenkin saada käyttökelpoinen.

~~~~
Aineisto:                                                   Algoritmi:    Tulosraja:   Hahmojakoja:  Max.Yht.sop.:     Aika:

LARPCaster_synthtest_1.xml (10 hahmoa, 10 pelaajaa)         Peruuttava      50 000       22 830       86,00 %         5 226 ms
 
LARPCaster_odytest_1_52-104.xml (52 hahmoa, 104 pelaajaa)   Peruuttava      10 000       10 001       79,94 %        91 469 ms
LARPCaster_odytest_1_52-104.xml (52 hahmoa, 104 pelaajaa)   Peruuttava      50 000       50 001       80,21 %       102 656 ms
LARPCaster_odytest_1_52-104.xml (52 hahmoa, 104 pelaajaa)   Peruuttava     100 000      100 001       80,11 %       162 300 ms
~~~~

Yllä olevien tulosten perusteella voidaan todeta, että kohtuullisen kokoisila syöteaineistoilla  kuvatulla tavalla optimoitu peruuttava haku pääsee melko optimaalisiin hahmojakoihin jo varsin pienellä (10 000) laskettujen ratkaisujen määrällä eikä laskettujen ratkaisujen määrän lisääminen tuo enää kovinkaan suurta hyötyä. Diagnostiikkatuloksista voidaan kuitenkin nähdä, että laskeva sopivuusraja ei enää tämänkokoisilla syötteillä varmista absoluuttisten optimijakojen mahtumista mukaan, sillä ensimmäinen sopivuusraja, jolla algoritmi löytää tuloksia, tuottaa jo yli 100 000 tulosta, mikä tarkoittaa, ettei niistä saada laskettua kaikkia. Pienen synteettisen aineiston diagnostiikkaa tarkastelemalla voidaan kuitenkin nähdä periaatteen toiminta - jos haluttaisiin esimerkiksi 100 parasta tulosta, voitaisiin laskenta tässä tapauksessa lopettaa jo sopivuusrajan 80 % jälkeen: 

~~~~
0 uniikkia tulosta laskettu sopivuusrajalla >100 3 millisekunnissa.
0 uniikkia tulosta laskettu sopivuusrajalla >95 3 millisekunnissa.
2 uniikkia tulosta laskettu sopivuusrajalla >90 4 millisekunnissa.
36 uniikkia tulosta laskettu sopivuusrajalla >85 14 millisekunnissa.
590 uniikkia tulosta laskettu sopivuusrajalla >80 30 millisekunnissa.
3484 uniikkia tulosta laskettu sopivuusrajalla >75 75 millisekunnissa.
10647 uniikkia tulosta laskettu sopivuusrajalla >70 332 millisekunnissa.
22830 uniikkia tulosta laskettu sopivuusrajalla >65 1527 millisekunnissa.
22830 uniikkia tulosta laskettu sopivuusrajalla >60 2746 millisekunnissa.
22830 uniikkia tulosta laskettu sopivuusrajalla >55 3965 millisekunnissa.
22830 uniikkia tulosta laskettu sopivuusrajalla >50 5226 millisekunnissa.
~~~~

Testattaessa peruuttavaa hakua suurella ja vaativalla (vähän kelvollisia hahmojakoja suhteessa pelaajien ja hahmojen määrään) aineistolla LARPCaster_odytest_all.xml (104 hahmoa, 310 pelaajaa), ei se edes laskevan sopivuusrajan avulla löytänyt yhtään kelvollista hahmojakoa järkevien aikakatkaisurajojen (600 s) puitteissa, mikä johtuu siitä, että tämänkaltaisilla aineistoilla laskettavien vaihtoehtojen määrä kasvaa valtavaksi kun kelvollisten hahmojakojen määrä puolestaan laskee (koska täytettävänä on enemmän ehtoja) ja todennäköisyys, että järkevässä ajassa laskettujen vaihtoehtojen joukkoon osuu kelvollinen hahmojako, on olemattoman pieni.

(Valitettavasti loppujen tehtyjen testien dokumentointiin ei enää riittänyt aikaa...)
