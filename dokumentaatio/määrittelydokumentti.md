# Määrittelydokumentti: LARPCaster-työkalu

## Taustaa
LARPCaster-työkalu on tarkoitettu auttamaan liveroolipelien järjestäjiä eli pelinjohtajia pelaajavalintojen ja hahmojaon tekemisessä. Suomalaisissa liveroolipeleissä on yleensä tapana, että pelaajat ilmoittautuvat peliin täyttämällä lomakkeen, jossa kysytään henkilötietojen lisäksi myös lukuisia kysymyksiä pelityyliin ja hahmoon liittyvistä mieltymyksistä ja toiveista, jotta kullekin pelaajalle voidaan valita mahdollisimman hyvin tälle sopiva hahmo. Perinteisesti pelinjohto on lukenut ilmoittautumislomakkeet läpi manuaalisesti ja yrittäneet jakaa valmiiksi kirjoitetut hahmot pelaajille siten, että kukin pelaaja saisi mahdollisimman hyvin toiveitaan vastaavan hahmon. Tämä on kuitenkin hyvin hankalaa ja työlästä, ja edellyttää että hahmojakoon osallistuvat pelinjohtajat tuntevat myös hahmot hyvin, mikä saattaa aiheuttaa ongelmia suuremmissa peleissä joissa hahmoja on ollut kirjoittamassa suuri määrä ihmisiä ja hyvin harvalla on käsitystä muista kuin itse kirjoittamistaan hahmoista.

Vaikka suuri osa ilmoittautumislomakkeista perustuu edelleen kysymyksiin joihin pelaajat vastaavat vapaamuotoisesti kirjoittamalla, ovat myös erilaiset monivalintakysymykset ja muut yksiselitteistä dataa tuottavat kysymykset yleistyneet viime vuosina. Tällaisten kysymysten käyttö avaa mahdollisuuden myös vastausten laskennalliselle analyysille ja parhaimmillaan hyvin valitut ja muotoillut monivalinta-, kyllä/ei- ja arvoasteikkokysymykset tarjoavat pelinjohdolle hyvin täsmällistä tietoa siitä, mitkä pelin hahmoista mahdollisesti sopivat kullekin pelaajalle. Kun näihin vastauksiin yhdistetään hahmojen kirjoittajien kustakin hahmosta täyttämän vastaavan mutta hahmon ominaisuuksia ja pelisisältöä kuvaavan kyselyn data, tulee mahdolliseksi vertailla pelaajan toiveita ja hahmon piirteitä matemaattisesti. Näiden mahdollisuuksien hyödyntämiseksi olen viimeisten kahden vuoden aikana kehittänyt järjestelmän, joka perustuu (toistaiseksi Google Forms -muotoisten lomakkeiden XML-muotoon muunnetun mutta tulevaisuudessa tätä varten erikseen kehitetyn ilmoittautumisjärjestelmän tuottaman) vastausdatan puoliautomaattiseen analyysiin ja laskee annettujen parametrien perusteella yhteensopivuusprosentin kullekin hahmon ja pelaajan yhdistelmälle. 

Menetelmää ja sen tuottamaa tietoa on toistaiseksi käytetty kolmessa pelissä (Juhannustanssit (2017), Avalon: Murroksen aika (2018) ja Odysseus (2019)) perinteisen pelinjohdon suorittaman hahmojaon tukena. Yhteensopivuusprosenttien avulla on ollut mahdollista karsia automaattisesti pois pelaajan toiveisiin täysin sopimattomat hahmot ja keskittää huomio parhaiten sopiviin hahmoihin, mikä on helpottanut pelinjohdon työtä huomattavasti, etenkin Odysseuksen kaltaisissa suuremmissa peleissä. Ongelmana on edelleen kuitenkin se, että kyseessä on monimutkainen ja erilaisia riippuvuussuhteita sisältävä järjestelmä jonka kokonaisuuden hahmottaminen ja optimoiminen manuaalisesti on hankalaa. Tämän harjoitustyön tarkoituksena on kehittää hahmojaon tueksi työkalu, joka tuottaa annettujen parametrien perusteella ehdotuksia yhteensopivuusprosenttien perusteella optimoiduiksi hahmojaoiksi, käyttäen hyväksi soveltuvia matemaattisia malleja ja niihin tehtyjä aineiston luonteeseen perustuvia optimointeja.

# Ongelman mallintaminen
Hahmojaossa kyse on kahteen erilliseen ryhmään (hahmot ja pelaajat) kuuluvien yksilöiden kohdistaminen toisiinsa niiden keskinäisiä suhteita kuvaavien attribuuttien perusteella. Tilannetta voidaan mallintaa suuntaamattomalla kaksijakoisella verkolla, jossa jaon toisen puolen solmut edustavat hahmoja ja toisen puolen solmut pelaajia. Näiden välisiä suhteita voidaan kuvata yhteensopivuusprosentilla painotetuilla kaarilla jotka yhdistävät jokaisen hahmon jokaiseen pelaajaan. Hahmojen ja pelaajien määrän ei ole pakko olla yhtä suuri, mutta käytännössä pelaajien määrä on aina suurempi kuin hahmojen määrä.

Koska kyseessä on käytännössä kaksiulotteinen matriisi, kaarten määrä on *p* × *h*, jossa *p* on pelaajien ja *h* hahmojen määrä, mikä tarkoittaa, että esimerkiksi 100 hahmon ja 150 ilmoittautuneen pelaajan tapauksessa kaarten määrä nousee helposti kohtuullisen suureksi algoritmeille, joiden aikavaativuus on O(n^2) tai korkeampi (kuten monissa tämäntapaisten ongelmien perinteisissä ratkaisuissa joita käsitellään alempana). Aineiston luonteesta johtuen verkkoa on kuitenkin mahdollista yksinkertaistaa jo sen syöttövaiheessa. Johtuen negatiivisille toiveille annetusta absoluuttisesta painotuksesta (eli pelaajalle ei haluta antaa hahmoa joka sisältää elementtejä joita tämä ei ehdottomasti halua pelata) aineisto sisältää runsaasti hahmo-pelaajapareja, joiden välinen yhteensopivuus on 0, mikä tarkoittaa, että näiden solmujen välille ei ole tarpeen luoda kaarta. Tästä johtuen luonnollisin esitysmuoto syötteelle on kohdesolmut ja näihin johtavien kaarien painot sisältävä vieruslista, jossa kunkin solmun naapurit on järjestetty niihin johtavan kaaren painon mukaan.

Aineiston pohjalta muodostetun kaksijakoisen verkon avulla annettu ongelma voidaan pyrkiä ratkaisemaan laskemalla eri algoritmeja käyttäen maksimiparitus, jossa jokainen hahmo yhdistyy yhteen pelaajaan ja jonka kokonaisvirtaus on mahdollisimman suuri. 

# Syöte ja käytettävät tietorakenteet
Työkalu saa syötteekseen yhteensopivuuslaskentaprosessin tuottaman TEI XML -muotoisen dokumentin, joka sisältää: 

1. *hahmolistan* jossa kunkin hahmon kohdalla on listattu kaikki pelaajat joiden yhteensopivuusprosentti on >0 yhteensopivuusprosentteineen , ja 
2. *pelaajalistan*, jossa on vastavuoroisesti listattu kunkin pelaajan kohdalla hahmot, joiden yhteensopivuusprosentti on >0 yhteensopivuusprosentteineen.

Hahmojen ja pelaajien tunnukset tallennetaan erillisiin taulukoihin, joiden indeksejä käytetään ohjelman sisällä niiden tunnuksena. Hahmojen ja pelaajien välisiä yhteensopivuuksia kuvaavat vieruslistat tallennetaan samoja indeksejä käyttäen taulukoihin, jonka kukin alkio sisältää 

# Käytettävät algoritmit



# Aika- ja tilavaativuus



# Lähteet

