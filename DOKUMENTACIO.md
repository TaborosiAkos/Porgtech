# Porgtech – projekt-dokumentáció

## 1. A feladat leírása

A program egy egyszerű, körökre osztott társasjátékot valósít meg. A játékosok egy előre megadott mezőkből álló táblán haladnak végig. A lépések számát a \`gameData.txt\` fájlban megadott dobássorozat határozza meg.

A táblán háromféle mező található:

- **Ingatlan (\`Property\`)** – megvásárolható. Ha egy másik játékos tulajdonában van, a rá lépő játékos bérleti díjat fizet.
- **Szolgáltatás (\`Service\`)** – a mező értékének megfelelő összeget kell fizetni.
- **Szerencsemező (\`Lucky\`)** – a mező értékének megfelelő összeget kapja a játékos.

Minden játékos 10 000 egység pénzzel és a tábla 0. pozíciójáról indul. A játékosok különböző stratégiát követnek az ingatlanok megvásárlásakor:

- **Greedy:** minden vásárlási lehetőségnél vásárolni szeretne.
- **Careful:** kezdetben a pénze felét külön keretként kezeli, és legfeljebb 1000 egységenként csökkenti ezt a keretet vásárláskor.
- **Tactical:** a belső számláló alapján minden második vásárlási lehetőséget választaná.

A játék addig fut, amíg legalább két játékos kiesik. Kiesés történhet akkor, ha a játékos nem tudja kifizetni a rá kiszabott díjat, vagy a pénze nullára vagy az alá csökken.

A játékosok, a mezők és a dobások adatait a \`Beadando1/gameData.txt\` fájl tartalmazza. A fájl szakaszait a \`--\` elválasztó sorok választják el.

## 2. Osztálydiagram

~~~mermaid
classDiagram
    class BoardCard {
        -String type
        -int value
        -int position
        +BoardCard(String type, int value, int position)
        +getType() String
        +getPosition() int
        +getValue() int
        +setValue(int amount) void
    }
    class Property {
        -String ownerName
        -boolean hasHouse
        +Property(String type, int position)
        +getOwnerName() String
        +setOwnerName(String name) void
        +getHouse() boolean
        +setHouse() void
        +hasOwner() boolean
    }
    class Service {
        +Service(String type, int value, int position)
    }
    class Lucky {
        +Lucky(String type, int value, int position)
    }
    class Player {
        <<abstract>>
        -int money
        -String name
        -int position
        +Player(String name)
        +getMoney() int
        +getPosition() int
        +move(int steps, int boardSize) void
        +getName() String
        +addMoney(int amount) void
        +pay(int amount) void
        +canPay(int amount) boolean
        +shouldBuy()* boolean
    }
    class Careful {
        -int budget
        +Careful(String name)
        +shouldBuy() boolean
        +setBudeget(int amount) void
    }
    class Greedy {
        +Greedy(String name)
        +shouldBuy() boolean
    }
    class Tactical {
        -int boughtThisRoundCounter
        +Tactical(String name)
        +setCounter() void
        +shouldBuy() boolean
    }
    class Game {
        {static} int boardLength
        {static} Player[] players
        {static} ArrayList~Integer~ diceRole
        +main(String[] args) void
        +GameSetup() BoardCard[]
        +PlayerSetup(BufferedReader br, String line) void
        +DiceRole(BufferedReader br, String line) void
    }
    BoardCard <|-- Property
    BoardCard <|-- Service
    BoardCard <|-- Lucky
    Player <|-- Careful
    Player <|-- Greedy
    Player <|-- Tactical
    Game --> Player : játékosokat kezel
    Game --> BoardCard : játéktáblát használ
~~~

A \`Game\` osztály a játék vezérléséért felel. A játékosokat és a táblamezőket tömbökben tárolja, ezért a diagramon ezek használati/kapcsolati viszonyként szerepelnek, nem öröklésként.

## 3. Az osztályok és metódusaik rövid leírása

### 3.1. BoardCard

A \`BoardCard\` a játéktábla mezőinek közös ősosztálya. Tárolja a mező típusát, értékét és pozícióját.

| Metódus | Leírás |
|---|---|
| \`BoardCard(type, value, position)\` | Létrehoz egy mezőt a megadott adatokkal. |
| \`getType()\` | Visszaadja a mező típusát. |
| \`getPosition()\` | Visszaadja a mező pozícióját. |
| \`getValue()\` | Visszaadja a mező értékét. |
| \`setValue(amount)\` | Beállítja a mező értékét. |

### 3.2. Property

A \`BoardCard\` leszármazottja, amely egy megvásárolható ingatlant reprezentál. Alapértelmezett értéke 1000, tulajdonosa kezdetben a bank, ház pedig kezdetben nincs rajta.

| Metódus | Leírás |
|---|---|
| \`Property(type, position)\` | Létrehoz egy ingatlant 1000-es értékkel. |
| \`getOwnerName()\` | Visszaadja a tulajdonos nevét. |
| \`setOwnerName(name)\` | Beállítja a tulajdonos nevét. |
| \`getHouse()\` | Megadja, hogy van-e ház az ingatlanon. |
| \`setHouse()\` | Beállítja, hogy az ingatlanon van ház. |
| \`hasOwner()\` | Igaz értéket ad, ha az ingatlan tulajdonosa nem a bank. |

### 3.3. Service

A \`BoardCard\` leszármazottja. Olyan mezőt jelöl, amelyre lépve a játékosnak a mező értékét kell kifizetnie. Saját metódust nem definiál, a konstruktoron keresztül adja át az adatokat az ősosztálynak.

### 3.4. Lucky

A \`BoardCard\` leszármazottja. Szerencsemezőt reprezentál, amelyre lépve a játékos a mező értékének megfelelő pénzt kap. Saját metódust nem definiál.

### 3.5. Player

Absztrakt ősosztály, amely a játékosok közös adatait és műveleteit tartalmazza. A játékos neve, pénze és aktuális pozíciója tartozik hozzá. Kezdő pénzösszege 10 000, kezdő pozíciója 0.

| Metódus | Leírás |
|---|---|
| \`Player(name)\` | Létrehoz egy játékost a megadott névvel és alapértékekkel. |
| \`getMoney()\` | Visszaadja a játékos pénzét. |
| \`getPosition()\` | Visszaadja a játékos pozícióját. |
| \`move(steps, boardSize)\` | A játékost a megadott lépésszámmal továbbmozgatja; a tábla végén körbeér. |
| \`getName()\` | Visszaadja a játékos nevét. |
| \`addMoney(amount)\` | Növeli a játékos pénzét. |
| \`pay(amount)\` | Csökkenti a játékos pénzét. |
| \`canPay(amount)\` | Megvizsgálja, hogy a játékosnak van-e elegendő pénze a fizetéshez. |
| \`shouldBuy()\` | Absztrakt metódus; a leszármazott osztályok határozzák meg a vásárlási stratégiát. |

### 3.6. Careful

A \`Player\` leszármazottja. Óvatos vásárlási stratégiát valósít meg. A konstruktorban a kezdő pénz felét elkülönített keretként tárolja.

| Metódus | Leírás |
|---|---|
| \`Careful(name)\` | Létrehozza a játékost, és beállítja az 5000 egységnyi kezdő keretet. |
| \`shouldBuy()\` | Akkor engedélyezi a vásárlást, ha a keretből levonható 1000 egység. |
| \`setBudeget(amount)\` | Csökkenti a belső keretet a megadott összeggel. |

### 3.7. Greedy

A \`Player\` leszármazottja. A \`shouldBuy()\` metódusa minden esetben \`true\` értéket ad vissza, tehát mindig szeretne vásárolni.

### 3.8. Tactical

A \`Player\` leszármazottja. A \`boughtThisRoundCounter\` számláló alapján határozza meg a vásárlási szándékot.

| Metódus | Leírás |
|---|---|
| \`Tactical(name)\` | Létrehozza a játékost, a számlálót nullára állítja. |
| \`setCounter()\` | Eggyel növeli a számlálót. |
| \`shouldBuy()\` | Páratlan számláló esetén igaz, páros esetén hamis értéket ad. |

### 3.9. Game

A \`Game\` osztály tartalmazza a játék fő vezérlési logikáját. Beolvassa a konfigurációt, létrehozza a játékosokat és a táblát, majd végrehajtja a köröket, a pénzmozgásokat és a kiesések kezelését.

| Metódus | Leírás |
|---|---|
| \`main(args)\` | Elindítja és vezérli a játékot. Feldolgozza a lépéseket, a mezőkre lépés következményeit és a kieséseket. |
| \`GameSetup()\` | Beolvassa a fájlból a tábla mezőit, majd meghívja a játékosok és a dobások beolvasását. |
| \`PlayerSetup(br, line)\` | Beolvassa a játékosok számát, nevét és stratégiáját, majd létrehozza őket. |
| \`DiceRole(br, line)\` | Beolvassa a dobások sorozatát. |

## 4. Tesztelés

### 4.1. A tesztelés célja és módja

A tesztelés célja az osztályok önálló működésének ellenőrzése. A projekt jelenlegi állapotában nincs külön tesztkeretrendszer vagy build-konfiguráció, ezért a mellékelt \`ProjectTests.java\` egyszerű, külső könyvtár nélküli tesztprogramot használ. A tesztek ellenőrzéseket végeznek, és hiba esetén kivételt dobnak.

A tesztek lefedik a mezők adatainak kezelését, a játékosok pénzkezelését és mozgását, az ingatlan tulajdonságait, valamint a három vásárlási stratégia működését.

### 4.2. Tesztesetek

| Azonosító | Vizsgált működés | Bemenet / előfeltétel | Elvárt eredmény |
|---|---|---|---|
| T01 | Mező létrehozása | \`BoardCard("service", 250, 3)\` | A típus, érték és pozíció rendre service, 250 és 3. |
| T02 | Mező értékének módosítása | Érték 250-ről 500-ra | A \`getValue()\` 500-at ad vissza. |
| T03 | Játékos alapállapota | Új \`Greedy("Teszt")\` | Pénz: 10000, pozíció: 0, név: Teszt. |
| T04 | Játékos mozgatása | 4 lépés, 7 mezős tábla | A pozíció 4. |
| T05 | Körbeérés | 5. pozícióról 4 lépés, 7 mezős tábla | A pozíció 2. |
| T06 | Pénz hozzáadása | 10000 + 500 | Az egyenleg 10500. |
| T07 | Fizetés | 10000-ből 1500 kifizetése | Az egyenleg 8500. |
| T08 | Fizetőképesség | 10000 pénz, 10000-es és 10001-es díj | Az első fizethető, a második nem. |
| T09 | Ingatlan alapállapota | Új \`Property\` | Tulajdonos bank, érték 1000, ház nincs, nincs tulajdonosa. |
| T10 | Ingatlan tulajdonosának beállítása | Tulajdonos: Teszt | A tulajdonos neve Teszt, a \`hasOwner()\` igaz. |
| T11 | Ház beállítása | \`setHouse()\` meghívása | A \`getHouse()\` igaz. |
| T12 | Szolgáltatás mező | \`Service("service", 300, 2)\` | Az érték 300, a pozíció 2. |
| T13 | Szerencsemező | \`Lucky("lucky", 200, 4)\` | Az érték 200, a pozíció 4. |
| T14 | Greedy stratégia | Új Greedy játékos | A \`shouldBuy()\` igaz. |
| T15 | Careful stratégia | Új Careful játékos | Az első öt hívásból négy igaz, az ötödik hamis. |
| T16 | Tactical stratégia | Új Tactical játékos, majd számlálónövelés | Kezdetben hamis, egyszeri növelés után igaz, újabb növelés után hamis. |

### 4.3. Tesztek futtatása

A projekt gyökérkönyvtárából a következő parancsokkal fordíthatók és futtathatók a tesztek:

~~~bash
javac Beadando1/*.java
java Beadando1.ProjectTests
~~~

Sikeres futtatás esetén a program a tesztek számát és a sikeres tesztek számát írja ki. Bármely sikertelen ellenőrzés kivételt okoz, így a futás hibával áll le.

### 4.4. Integrációs tesztek és ismert korlátok

Az egységtesztek az osztályok önálló metódusait ellenőrzik. A teljes játékfolyamatot külön, a \`gameData.txt\` fájl használatával kell ellenőrizni. Javasolt integrációs ellenőrzések:

1. A program a megadott fájlból beolvassa a táblát, a játékosokat és a dobásokat.
2. A játékos a megfelelő mezőre lép a dobássorozat alapján.
3. A szabad ingatlan megvásárlása levonja az árát és beállítja a tulajdonost.
4. Más játékos ingatlanára lépve a bérleti díj a tulajdonoshoz kerül.
5. A szolgáltatásmező levonja a díjat, a szerencsemező pedig jóváírja a jutalmat.
6. A fizetésre képtelen játékos kiesik, és a játék a második kieső után befejeződik.

A forráskód vizsgálata során az alábbi, teszteléskor figyelembe veendő és javításra érdemes pontok láthatók:

- A \`GameSetup()\` beolvassa az ingatlan értékét, de a \`Property\` konstruktora jelenleg mindig 1000-es értéket állít be. Emiatt a fájlban megadott ingatlanár nem érvényesül.
- A \`Tactical\` számlálóját a \`Game.main()\` jelenlegi kódja nem növeli, ezért a játékban a stratégia alapállapotban mindig a vásárlás elutasítását adja vissza.
- A \`Careful\` külön kerete a vásárlási szándék vizsgálatakor csökken, de a játékos tényleges pénzegyenlegét a vásárláskor a \`Game\` kezeli. A két érték így egymástól függetlenül változik.
- A \`GameSetup()\` fájlbeolvasási útvonala relatív, ezért a programot a projekt gyökérkönyvtárából célszerű futtatni.
- A \`diceRole\` statikus lista újabb játékindításkor nem ürül ki automatikusan.

Ezek nem a tesztek hibái, hanem a jelenlegi implementációból következő viselkedések, amelyeket a későbbi fejlesztés során érdemes kezelni.
