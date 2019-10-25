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
Seuraavissa käsitellään lyhyesti kunkin algoritmin toimintalogiikka ja niihin tehdyt optimoinnit.

### Galen-Shapleyn algoritmi
Galen-Shapleyn algoritmi on ajassa O(n²) toimiva ratkaisualgoritmi ns. "vakaiden avioliittojen ongelmalle" (Stable Marriage Problem), jossa pyritään löytämään _vakaat parit_ kahden yhtä suuren joukon (alkuperäisessä ongelmassa miesten ja naisten) jäsenten välille näiden esittämien preferenssien perusteella. Vakailla pareilla tarkoitetaan tämän ongelman yhteydessä paria, jonka kummallekin osapuolelle ei löydy toista mieluisampaa kumppania (pari voi olla epäsymmetrinen siten, että toiselle osapuolelle on olemassa jokin toinen mieluisampi kumppani, mutta toiselle ei). Koska hahmojaossa on kyse aivan samoin kahden erillisen ryhmän (pelaajat ja hahmot) jäsenten yhdistämisestä siten, että kukin pari on mahdollisimman sopiva toisilleen, on hahmonjako-ongelma muotoiltavissa vakaiden avioliittojen ongelmaksi. 

Pääpiirteissään algoritmi toimii siten, että se käy läpi kunkin aktiivisen eli kosivan osapuolen jäsenen ehdokaslistja järjestyksessä. Kunkin aktiivisen osapuolen jäsenen kohdalla se valitsee laskevaan sopivuusjärjestykseen järjestetystä ehdokaslistasta ensimmäisen ehdokkaan, jota kyseinen hahmo tai pelaaja ei vielä ole kosinut ja kosii tätä, eli tarkistaa onko ehdokas paritettu jo jollekin muulle hahmolle tai pelaajalle. Jos ei ole, algoritmi parittaa kosineen ja kositun hahmon ja pelaajan toisilleen. Jos ehdokas on jo varattu, tarkistetaan onko uusi kosija sopivampi kuin vanha pari; jos on, aiempi paritus puretaan ja ehdokas paritetaan uudelle kosijalle. Kun tätä toistetaan riittän monta kierrosta, päädytään lopulta tasapainotilaan, jossa kaikki paritetut hahmot ja pelaajat ovat mahdollisimman yhteensopivia.  

Koska Galen-Shapleyn algoritmi löytää lähtökohtaisesti yhden useasta mahdollisesta vakaasta pariutuksesta ja hahmojaon tapauksessa on hyödyllistä jos käytössä on useita vaihtoehtoja (lopullisia päätöksiä tehdessä huomioon on otettava myös vaikeasti operationalisoitavia seikkoja joita algoritmi ei kykene huomioimaan), on algoritmiin tässä toteutuksessa lisätty joitakin optimointeja jotka pyrkivät tuottamaan järkevän määrän (20-100) mahdollisimman optimaalista ratkaisua. Koska Galen-Shapleyn algoritmi on epäsymmetrinen "kosivan" ja "kosittavan" osapuolen suhteen – algoritmi tuottaa ratkaisun, joka on optimaalinen nimenomaan kosivan osapuolen suhteen – ensimmäinen luonnollinen ratkaisu oli toteuttaa algoritmi erikseen molempiin suuntiin, mikä on tässä toteutuksessa tehty erikseen ajettavina algoritmeina. Toinen optimointikeino useiden vaihtoehtoisten mutta mahdollisimman hyvin yhteensopivien ratkaisujen löytämiseksi on hyödyntää sitä seikkaa, että koska yhteensopivuusprosentit ilmoitetaan kokonaisluvun tarkkudella, usein tietyn hahmon tai pelaajan ehdokaslista sisältää useampia yhtä sopivia ehdokkaita joiden keskinäinen järjestys listalla on sattuman tulosta. Tätä seikkaa on hyödynnetty siten, että algoritmi lasketaan useaan kertaan, siten että välissä poistetaan aiemmassa laskennassa käytetty ehdokas jolla on yhtä sopiva kilpailija. Koska tästä seuraavien ehdokaslistavarianttien yhdistelmien määrä nousee eksponentiaalisesti, johtaen aikavaativuuden radikaaliin kasvuun tapauksissa joissa rinnakkaisuutta on paljon, on laskettavia variaatioita rajoitettu ensinnäkin siten, että useat samanarvoiset ehdokkaat otetaan huomioon vain silloin kuin ne ovat hahmon tai pelaajan ehdokaslistan kärjessä ja toiseksi siten, että käyttäjä voi määrittää rajan sille, kuinka "syvälle" näitä variaatioita lasketaan, eli käytännössä kuinka monesta ehdokaslistasta kerrallaan voi olla käytössä vaihtoehtoinen versio. Näin aikavaativuus saadan pudotettua luokasta O(2^n) jossa n on pelaajien määrä, luokkaan O(2^k), jossa k on valittu enimmäissyvyys (usein välillä 2-5). Tästä huolimatta variaatioiden laskeminen tuottaa usein epäkäytännöllisen suuren määrän vaihtoehtoisia tuloksia, joten tähän algoritmiin on lisäksi toteutettu ykäyttäjän määritettävissä oleva yläraja laskettavien variaatioiden määrälle (oletusarvo 50 000). Tämä tarkoittae, että hiemankaan suurempien aineistojen kanssa algoritmi ei välttämätä löydä kaikkia optimaalisimpia ratkaisuja.   



## Puutteet ja parannusehdotukset
Galen-Shapleyn algoritmista voisi olla joitain tilanteita varten hyvä lisätä myös versio, joka laskee tulokset molempiin suuntiin saman laskennan sisällä ja antaa yhdistetyn vastausjoukon.

Peruuttavasta hausta voisi olla järkevää sisällyttää myös optimoimaton versio, jossa haun voi toteuttaa suoraan halutulla minimisopivuustasolla ilman että se yrittää laskea ensin korkeamman yhteensopivuuden tuloksia, sillä tilanteessa jossa ratkaisuja tiedetään olevan löydettävissä vain matalammilla yhteensopivuustasoilla, korkean yhteensopivuuden ratkaisujen yrittäminen ensin hukkaa vain aikaa.



## Lähteet

"Assignment problem". 2019. Wikipedia-artikkeli. Saatavissa osoitteesta: https://en.wikipedia.org/wiki/Assignment_problem. 

Dickerson, John P. "Stable Matching". Luentomateriaali. Carnegie Mellon University. Saatavissa osoitteesta: http://www.cs.cmu.edu/~arielpro/15896s16/slides/896s16-16.pdf.

Gale D. & Shapley L.S. 1962. "College Admissions and the Stability of Marriage". Artikkeli. American Mathematical Monthly, Vol. 69., s. 9–15. Saatavissa osoitteesta: http://www.eecs.harvard.edu/cs286r/courses/fall09/papers/galeshapley.pdf.

Osipenko, Alexander.2019. "Gale–Shapley algorithm simply explained". Artikkeli. Towards Data Science. Saatavissa osoitteesta: https://towardsdatascience.com/gale-shapley-algorithm-simply-explained-caa344e643c2.

Srinivasan, G. 2009. "Lec-16 Assignment Problem - Hungarian Algorithm". Opetusvideo. Saatavissa osoitteesta: https://www.youtube.com/watch?v=BUGIhEecipE&t=1521s.

"Stable marriage problem". 2019. Wikipedia-artikkeli. Saatavissa osoitteesta: https://en.wikipedia.org/wiki/Stable_marriage_problem.
