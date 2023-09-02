public class Item {
    private String name;
    private int quantity;

    public Item(String name) {
        this.name = name;
        quantity = 1;
    }

    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }     

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int val) {
        if (val < 0) {
            quantity = 0;
        } else {
            quantity = val;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    //overloaded by Equipment
    public String getRarity() {
        return "";
    }

    //overloaded by Equipment
    public int getStars() {
        return 0;
    }

    //overloaded by Equipment
    public int getDurability() {
        return 0;
    }

    //overloaded by Equipment
    public int[] getLocation() {
        return new int[]{-1,-1,-1};
    }   

    //overloaded by Equipment
    public int[] getStats() {

        return new int[]{-1,-1,-1};
    }

    public String toString() {
        return "\n" + name + "\nQuantity: " + quantity + "\n";
    }

}