package Beadando1;

public class Tactical extends Player {
    private int boughtThisRoundCounter;

    public Tactical(String name){
        super(name);
        boughtThisRoundCounter = 0;
    }

    public void setCounter(){
        boughtThisRoundCounter++;
    }

    @Override
    public boolean shouldBuy(){
        if(boughtThisRoundCounter % 2 == 0){
            return false;
        }else{
            return true;
        }
    }

}
