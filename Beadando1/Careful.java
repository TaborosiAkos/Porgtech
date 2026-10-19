package Beadando1;

public class Careful extends Player {

    private int budget;

    public Careful(String name){
        super(name);
        this.budget = (getMoney() / 2);
    }

    @Override
    public boolean shouldBuy(){
        if(budget - 1000 >= 0){
            setBudeget(1000);
            return true;
        }else{
            return false;
        }
    }

    public void setBudeget(int amount){
        budget -= amount;
    }

}
