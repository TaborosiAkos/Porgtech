package Beadando1;

/**
 * Egyszerű, külső tesztkönyvtár nélkül futtatható ellenőrzések.
 * Futtatás: javac Beadando1/*.java && java Beadando1.ProjectTests
 */
public class ProjectTests {
    private static int passed = 0;

    private static void check(boolean condition, String testName) {
        if (!condition) {
            throw new AssertionError("Sikertelen teszt: " + testName);
        }
        passed++;
        System.out.println("[OK] " + testName);
    }

    public static void main(String[] args) {
        testBoardCard();
        testPlayer();
        testProperty();
        testOtherCards();
        testStrategies();

        System.out.println();
        System.out.println("Sikeres tesztek: " + passed);
    }

    private static void testBoardCard() {
        BoardCard card = new BoardCard("service", 250, 3);
        check("service".equals(card.getType()), "T01 – mező típusa");
        check(card.getValue() == 250, "T01 – mező értéke");
        check(card.getPosition() == 3, "T01 – mező pozíciója");

        card.setValue(500);
        check(card.getValue() == 500, "T02 – mező értékének módosítása");
    }

    private static void testPlayer() {
        Greedy player = new Greedy("Teszt");
        check("Teszt".equals(player.getName()), "T03 – játékos neve");
        check(player.getMoney() == 10000, "T03 – kezdő pénz");
        check(player.getPosition() == 0, "T03 – kezdő pozíció");

        player.move(4, 7);
        check(player.getPosition() == 4, "T04 – normál lépés");

        player.move(5, 7);
        check(player.getPosition() == 2, "T05 – körbeérés");

        player.addMoney(500);
        check(player.getMoney() == 10500, "T06 – pénz hozzáadása");

        player.pay(1500);
        check(player.getMoney() == 9000, "T07 – fizetés");

        check(player.canPay(9000), "T08 – pontos összeg kifizethető");
        check(!player.canPay(9001), "T08 – nagyobb összeg nem fizethető ki");
    }

    private static void testProperty() {
        Property property = new Property("property", 1);
        check("bank".equals(property.getOwnerName()), "T09 – alapértelmezett tulajdonos");
        check(property.getValue() == 1000, "T09 – alapértelmezett ingatlanérték");
        check(!property.getHouse(), "T09 – kezdetben nincs ház");
        check(!property.hasOwner(), "T09 – kezdetben nincs játékos tulajdonos");

        property.setOwnerName("Teszt");
        check("Teszt".equals(property.getOwnerName()), "T10 – tulajdonos beállítása");
        check(property.hasOwner(), "T10 – tulajdonos ellenőrzése");

        property.setHouse();
        check(property.getHouse(), "T11 – ház beállítása");
    }

    private static void testOtherCards() {
        Service service = new Service("service", 300, 2);
        check(service.getValue() == 300, "T12 – szolgáltatás értéke");
        check(service.getPosition() == 2, "T12 – szolgáltatás pozíciója");

        Lucky lucky = new Lucky("lucky", 200, 4);
        check(lucky.getValue() == 200, "T13 – szerencsemező értéke");
        check(lucky.getPosition() == 4, "T13 – szerencsemező pozíciója");
    }

    private static void testStrategies() {
        Greedy greedy = new Greedy("Mohó");
        check(greedy.shouldBuy(), "T14 – Greedy mindig vásárolni szeretne");

        Careful careful = new Careful("Óvatos");
        check(careful.shouldBuy(), "T15 – Careful első vásárlása");
        check(careful.shouldBuy(), "T15 – Careful második vásárlása");
        check(careful.shouldBuy(), "T15 – Careful harmadik vásárlása");
        check(careful.shouldBuy(), "T15 – Careful negyedik vásárlása");
        check(!careful.shouldBuy(), "T15 – Careful ötödik vásárlása már nem engedélyezett");

        Tactical tactical = new Tactical("Taktikus");
        check(!tactical.shouldBuy(), "T16 – Tactical kezdeti állapota");
        tactical.setCounter();
        check(tactical.shouldBuy(), "T16 – Tactical páratlan számláló");
        tactical.setCounter();
        check(!tactical.shouldBuy(), "T16 – Tactical páros számláló");
    }
}
