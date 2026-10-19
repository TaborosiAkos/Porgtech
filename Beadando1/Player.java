package Beadando1;

public abstract class Player {
    private int money;
    private String name;
    private int position;

    public Player(String name){
        this.name = name;
        this.money = 10000;
        this.position = 0;
    }

    public int getMoney(){
        return money;
    }

    public int getPosition(){
        return position;
    }

    public void move(int steps, int boardSize){
        position = (steps + position) % boardSize;
    }

    public String getName(){
        return name;
    }

    public void addMoney(int amount){
        money += amount;
    }

    public void pay(int amount){
        money -= amount;
    }

    public boolean canPay(int amount){
        return money >= amount;
    }

    public abstract boolean shouldBuy();
}
