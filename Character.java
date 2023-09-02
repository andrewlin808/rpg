import java.util.ArrayList;

public class Character extends Entity {
    //Instances
    private int XP;
    private int[] stats;

    private ArrayList<String> conversation;

    /**
     * Subclass Character Constructor -> "new Character"
     * <p>
     * Call to super initializes instances in Class Entity
     * <p>
     * Additionally initializes experience and stats.
     * 
     * @param name Character's name
     */
    public Character(String name) {
        super("Character",name,1,1,1);//(1,1) default location at 1-1
        XP = 0;
        stats = new int[3];
    }

    /** 
     * Subclass Character Constructor -> when loading saved Player
     * <p>
     * Call to super initializes instances in Class Entity
     * <p>
     * Additionally initializes experience and stats
     * 
     * @param data the array containing exact param name, level, hit point, max hit point, strength, defense of Character
     */
    public Character(String[] data) {
        super(data[1],Integer.valueOf(data[2]),Integer.valueOf(data[3]),Integer.valueOf(data[4]),Integer.valueOf(data[5]),Integer.valueOf(data[6]));
        XP = Integer.valueOf(data[7]);
        stats = new int[3];
    }

    /**
     * Add experience to Character and level up if necessary
     * <p>
     * If given Entity: increase experience by 1 && if Entity's level is greater than
     * Character, add additional experience based how many levels Entity is greather than
     * Character + addtional 1 value per 5 of Entity's level, which is then squared.
     * <p>
     * If given just param val: increase experience of Character by param val
     * <p>
     * While Character's experience is greater than the power of 2 of the Character's next level, 
     * level up the Character: hit point += 3~5 | strength += 2~4 | defense += 2~4 (per level).
     * "Reset" the prior experience value based on the value of Character's current (new) level squared.
     */
    public void addXP(Entity opp, int val) {
        System.out.println('\u000c');
        if (opp == null) {
            //add XP based on val
            GameRunner.slowPrint(getName() + "'s experience increased by " + val,200);
            System.out.print("XP: " + getXPs() + " -> ");
            XP += val;
            GameRunner.slowPrint(getXPs(),200);
        } else {
            //add XP after defeating an Entity
            //add 1 XP
            GameRunner.slowPrint(getName() + "'s experience increased by 1",200);
            System.out.print("XP: " + getXPs() + " -> ");
            XP++;
            GameRunner.slowPrint(getXPs(),200);
            //bonus XP
            if (opp.getLv() > getLv()) {
                int additionalXP = (int)(Math.pow(opp.getLv() - getLv() + opp.getLv() / 5,2));
                GameRunner.slowPrint("Additional " + additionalXP + " XP gained!",300);
                System.out.print("XP: " + getXPs() + " -> ");
                XP += additionalXP;
                GameRunner.slowPrint(getXPs(),300);
            }
        }
        //check if level up
        while (XP >= (int)(Math.pow(getLv() + 1, 2) * 2)) {
            GameRunner.slowPrint(getName() + " leveled up!" + "\nlv" + getLv() + " -> lv" + (getLv() + 1),200);;
            setLv(getLv() + 1);
            int hp = (int)Math.random() * 3 + 3;//3~5
            int s = (int)Math.random() * 3 + 2;//2~4
            int d = (int)Math.random() * 3 + 2;//2~4
            //addStats
            setMaxHP(getMaxHP() + hp);
            setSt(getSt() + s);
            setDf(getDf() + d);
            GameRunner.slowPrint("HP: " + (getMaxHP() - stats[0] - hp) + " -> " + (getMaxHP() - stats[0]) + "\t+" + hp +
                "\nSt: " + (getSt() - stats[1] - s) + " -> " + (getSt() - stats[1]) + "\t+" + s +
                "\nDf: " + (getDf() - stats[2] - d) + " -> " + (getDf() - stats[2]) + "\t+" + d,500);
            //check remaining XP
            XP -= (int)(Math.pow(getLv(), 2)) * 2;
            restore();
            GameRunner.slowPrint("Health has been restored",150);
            System.out.println(this);
            GameRunner.confirm();
        }

    }

    /**
     * Moves Character in a specified direction 1 space on a given Map and print the Map; see: Map.canMove()
     * <p>
     * Precondition: Character's location will not be on the edges of the Map
     * <p>
     * Character can only move to a Mappable Path location and uses w/a/s/d to move
     * 
     * @param direction the direction to move to
     */
    public void move(String direction, Map room) {
        System.out.println('\u000c');
        room.removeObject(this);
        if (direction.equals("w") && room.canMove(super.getRow() - 1,super.getCol())) {//up
            super.setRow(super.getRow() - 1);
        } else if (direction.equals("a") && room.canMove(super.getRow(), super.getCol() - 1)) {//left
            super.setCol(super.getCol() - 1);
        } else if (direction.equals("s") && room.canMove(super.getRow() + 1, super.getCol()))  {//down
            super.setRow(super.getRow() + 1);
        } else if (direction.equals("d") && room.canMove(super.getRow(), super.getCol() + 1)) {//right
            super.setCol(super.getCol() + 1);
        }
        room.placeObject(this);
        room.printMap();
    }

    /**
     * Add stats to Character's max hit point, strength, and defense based on an Equipment's stats; See also: Player.equip()
     *
     * @param add the stats of the Equipment to add 
     */
    public void addStats(int[] add) {
        stats[0] += add[0];
        stats[1] += add[1];
        stats[2] += add[2];
        setMaxHP(getMaxHP() + add[0]);
        setSt(getSt() + add[1]);
        setDf(getDf() + add[2]);
    }

    /**
     * Substract stats to Character's max hit point, strength, and defense based on an Equipment's stats; See also: Player.unequip()
     *
     * @param minus the stats of the Equipment to subtract 
     */
    public void removeStats(int[] minus) {
        stats[0] -= minus[0];
        stats[1] -= minus[1];
        stats[2] -= minus[2];
        setMaxHP(getMaxHP() - minus[0]);
        setSt(getSt() - minus[1]);
        setDf(getDf() - minus[2]);
    }

    public int getXP() {
        return XP;
    }

    public String getXPs() {
        return XP + "/" + (int)(Math.pow(getLv() + 1,2)) * 2;
    }

    @Override
    public int interactableID() {
        return 2;
    }

    @Override
    public String getSymbol() {
        return "¡";
    }

    @Override
    public String toString() {
        return "\n" + getName() + "  Lv" + getLv() + "   XP: " + getXPs() + 
        "\nHP: " + getHPs() + "(" + stats[0] + ")" + 
        "\nSt: " + (getSt() - stats[1]) + "(+" + stats[1] + ")" +
        "\nDf: " + (getDf() - stats[2]) + "(+" + stats[2] + ")\n";

    }

}