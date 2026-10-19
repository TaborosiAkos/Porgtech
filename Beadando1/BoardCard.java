package Beadando1;

public class BoardCard {
    private String type;
    private int value;
    private int position;

    public BoardCard(String type, int value, int position){
        this.type = type;
        this.value = value;
        this.position = position;
    }

    public String getType(){
        return type;
    }

    public int getPosition(){
        return position;
    }

    public int getValue(){
        return value;
    }

    public void setValue(int amount){
        value = amount;
    }
}
