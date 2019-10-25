# Toteutusdokumentti: LARPCaster-työkalu
Tämä dokumentti esittelee LARPCaster-työkalun teknistä toteutusta, rakennetta ja toimintaa ja pyrkii avaamaan syitä erilaisten suunnittelu ja toteutusratkaisujen taustalla.

## Ohjelman yleisrakenne
LarpCaster-työkalu jakautuu perusrakenteeltaan kolmeen erillisinä paketteina toteutettuun osaan: ohjelman käynnistämisestä, tekstimuotoisesta käyttöliittymästä, tietojen syöttämisestä ja tulostamisesta huolehtivasta ui-paketista, ohjelman tietorakenteet ja toimintalogiikan sisältävästä domain-paketista ja varsinaiset laskenta-algoritmit sisältävästä algorithms-paketista. 

![LARPCaster-työkalun looginen rakenne](https://github.com/vmarttil/larp-caster/blob/master/dokumentaatio/kuvat/LARPCaster_sovelluslogiikka_2.png)

Näistä ensimmäinen sisältää sovelluksen käynnistävän Main-luokan lisäksi luokan TextUI, joka toteuttaa käyttöliittymän yhteensopivuustietojen lataamiseen ja tarkasteluun, hahmojakoalgoritmien käyttämien parametrien ja reunaehtojen määrittelyyn ja algoritmien ajamiseen sekä tulosten tarkasteluun. Yhteensopivuustietojen tuonti työkaluun on toistaiseksi toteutettu hyvin yksinkertaisesti ja vailla minkäänlaista syötteen oikeellisuuden tarkistusta tai syötetiedostojen hallinnointia siitä syystä, että työkalu on suunniteltu osaksi laajempaa työkalukokonaisuutta jonka muiden osien kanssa kommunikointiin käytettäviä rajapintoja ei ole vielä määritelty valmiiksi. Osittain samasta syystä työkalu ei vielä sisäläl toiminnallisuutta tulosten viemiseen tiedostoon tai tietokantaan (osin tämä johtuu myös käytettävissä olevan työajan priorisoimisesta työkalun ydintoimintojen hiomiseen).

Toinen paketti koostuu viidestä luokasta, joista Hahmojako muodostaa ohjelman ytimen ja ohjaa sekä tietorakenteiden että algoritmien toimintaa ja koordinoi niiden välistä kommunikaatiota. Kuhunkin Hahmojako-olioon liittyy yksi Sopivuusmatriisi-olio joka määrittelee algoritmien käyttämien syötetietojen säilyttämiseen ja käsittelyyn liittyvän toiminnallisuuden ja yksi Tulosluettelo-olio, joka huolehtii algoritmien laskmien tulosten hallinnoinnista. Sopivuusmatriisi sisältää myös kokoelman pelaaja- ja hahmokohtaisia Ehdokasluettelo-olioita, jotka sisältävät järjestetyn luettelon tietyn hahmon tai pelaajan kanssa yhteensopivista hahmoista tai pelaajista ja joita käytetään suoraan hahmojakojen laskentaan kahdessa kolmesta ratkaisualgoritmista. Kukin Tulosluettelo-olio – joita voi liittyä useita samaan Hahmojako-olioon – puolestaan toimii säilytyspaikkana kaikille yhden algoritmin laskentakerran laskemille tuloksille ja niihin liittyvälle metadatalle, jotka tallennetaan Tulos-olioon. Yksi Tulos-olio sisältää aina yhden lasketun hahmojaon (sekä siihen liittyvät metatiedot), joita saattaa yhden algoritmin ajon yhteydessä syntyä kymmeniä tuhansia, mutta joista Tulosluetteloon tallennetaan käytännöllisyyssyistä vain rajattu määrä.

Kolmas paketti sisältää kolmen työkalun käyttämän laskenta-algoritmin toiminnallisuuden, joista kukin on toteutettu omana luokkanaan. Kukin näistä algoritmeista toimii ja käyttää Sopivuusmatriisin sisältämiä tietoja hieman eri tavalla, mutta kaikki algoritmit palauttavat tuloksensa vakiomuotoisena Tulosluettelo-olioon tallennettuna Tulos-olioiden joukkona.

### Luokkakaavio
Alla oleva luokkakaavio sisältää täydellisen luettelon sovelluksen kaikkien luokkien kaikista luokkamuuttujista ja metodeista, dokumentaation kattavuuden vuoksi ja hahmottamisen helpottamiseksi mukaan lukien myös yksinkertaiset set- ja get-metodit. Luokkien väliset viivat kuvaavat niiden välistä tiedonkulkua.

![LARPCaster-työkalun luokkakaavio](https://github.com/vmarttil/larp-caster/blob/master/dokumentaatio/kuvat/LARPCaster_luokkakaavio_2.png)

## Algoritmien toiminta







## Puutteet ja parannusehdotukset


## Lähteet
