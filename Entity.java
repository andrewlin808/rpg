public abstract class Entity extends Mappable {
    //Instances
    private String type;
    private String name;
    private int lv;
    private int HP;
    private int maxHP;
    private int st;
    private int df;

    /**
     * Class Entity Constructor -> "new Player"
     * <p>
     * Call to super initializes the row and col locations in Class Mappable based on given param r and c.
     * <p>
     * Additionally initializesk hit point, max hit point, strength, defense, and symbol.
     * 
     * @param type classification of an Entity, either as Subclasses Character, Spirit, Monster, or NPC
     * @param name Entity's name
     * @param level Entity's level
     * @param r Entity's row location in Class Mappable
     * @param c Entity's col location in Class Mappable
     */
    public Entity(String type, String name, int level, int r, int c) {
        super(r,c);
        if (type.equals("Character")) {
            //Character
            //MP: 5/10 (on even levels)
            HP = (int)(Math.random() * 6) + 10 ;//10~15
            st = (int)(Math.random() * 5) + 6;//6~10
            df = (int)(Math.random() * 6) + 5;//5~10
        } else if (type.equals("Spirit")) {
            //Spirit
            HP = 10 * (level - 1) + 30;//10(x - 1) + 30
            st = 10 * (level - 1) + 15;//5(x - 1) + 30
            df = 5 * (level - 1) + 30;//5(x - 1) + 30
        } else if (type.equals("Monster")) {
            //Monster
            HP = 6 * level + level / 2 + 5;//HP: 6x + x/2 + 5
            st = 5 * level + level / 3 + 4;//st: 5x + x/3 + 4
            df = 4 * level + level / 4 + 3;//df: 4x + x/2 + 3
        } else if (type.equals("NPC")) {
            //NPC
            HP = 1;
        }
        this.type = type;
        this.name = name;
        lv = level;
        maxHP = HP;
    }

    /**
     * Class Entity Constructor -> when loading saved Player
     * <p>
     * Call to super initializes the row and col locations in Class Mappable as (3,3) - "spawnpoint" in Map "villageloop"
     * <p>
     * Type is initialized to Character, and symbol accordingly.
     * Additionally initializes name, level, hit point, max hit point, strength, defense.
     * 
     * @param name Character's name
     * @param level Character's level
     * @param HP Character's hit point
     * @param maxHP Character's max hit point
     * @param st Character's strengh
     * @param df Character's defense
     */
    public Entity(String name, int level, int HP, int maxHP, int st, int df) {
        super(3,3);
        type = "Character";
        this.name = name;
        lv = level;
        this.HP = HP;
        this.maxHP = maxHP;
        this.st = st;
        this.df = df;
    }

    //overriden by Subclass Monster | Subclass Spirit
    public void attack(Player player) {
    }

    public String getEntitySaveStats() {
        return type + ":" + name + ":" + lv + ":" + HP + ":" + maxHP + ":" + st + ":" + df + ":";
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getLv() {
        return lv;
    }

    public int getHP() {
        return HP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public String getHPs() {
        return HP + "/" + maxHP;
    }

    public int getSt() {
        return st;
    }

    public int getDf() {
        return df;
    }

    public void setName(String n) {
        name = n;
    }

    public void setLv(int lvs) {
        lv = lvs;
    }

    public void setHP(int val) {
        if (val < 0) {
            HP = 0;
        } else {
            HP = val;
            if (HP > maxHP) {
                HP = maxHP;
            }
        }
    }

    public void setMaxHP(int val) {
        if (HP > val) {
            HP = val;
        }
        maxHP = val;
    }

    public void restore() {
        HP = maxHP;
    }

    public void setSt(int val) {
        st = val;
    }

    public void setDf(int val) {
        df = val;
    }

    //overriden by NPC
    public void printConversation(Map floor, Player player) {}
    //overriden by NPC
    public boolean hasConversation() {return false;}

    @Override
    public abstract int interactableID();

    @Override
    public abstract String getSymbol();

    @Override
    public abstract String toString();
}