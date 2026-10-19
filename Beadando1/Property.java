package Beadando1;

public class Property extends BoardCard{
    private String ownerName;
    private boolean hasHouse;

    public Property(String type, int position){
        super(type, 1000, position);
        ownerName = "bank";
        hasHouse = false;
    }

    public String getOwnerName(){
        return ownerName;
    }

    public void setOwnerName(String name){
        ownerName = name;
    }

    public boolean getHouse(){
        return hasHouse;
    }

    public void setHouse(){
        hasHouse = true;
    }

    public boolean hasOwner(){
        return !"bank".equals(getOwnerName());
    }
}
