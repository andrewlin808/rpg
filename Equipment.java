import java.util.ArrayList;

public class Equipment extends Item {
    //instances
    public static final String[][][] EQUIPMENTS = 
        {
            {//body armor //x
                {"WoodenSword","WoodenShield","WoodenHelmet","WoodenChestplate","WoodenLeggings","WoodenBoots"},//50
                {"BoneSword","BoneShield","BoneHelmet","BoneChestplate","BoneLeggings","BoneBoots"},//100
                {"StoneSword","StoneShield","StoneHelmet","StoneChestplate","StoneLeggings","StoneBoots"},//150
                {"CopperSword","CopperShield","CopperHelmet","CopperChestplate","CopperLeggings","CopperBoots"},//200
                {"BronzeSword","BronzeShield","BronzeHelmet","BronzeChestplate","BronzeLeggings","BronzeBoots"},//250
                {"IronSword","IronShield","IronHelmet","IronChestplate","IronLeggings","IronBoots"},//300
                {"SteelSword","SteelShield","SteelHelmet","SteelChestplate","SteelLeggings","SteelBoots"},//400
                {"MithrilSword","MithrilShield","MithrilHelmet","MithrilChestplate","MithrilLeggings","MithrilBoots"},//500
                {"TungstenSword","TungstenShield","TungstenHelmet","TungstenChestplate","TungstenLeggings","TungstenBoots"},//600
                {"TitaniumSword","TitaniumShield","TitaniumHelmet","TitaniumChestplate","TitaniumLeggings","TitaniumBoots"},//700
                {"PlatinumSword","PlatinumShield","PlatinumHelmet","PlatinumChestplate","PlatinumLeggings","PlatinumBoots"},//800
                {"AdamantineSword","AdamantineShield","AdamantineHelmet","AdamantineChestplate","AdamantineLeggings","AdamantineBoots"}//1000
            },
            {//Accessories
                {"GoldBelt","GoldNecklace","GoldBracelet","GoldRing"},
                {"RubyBelt","RubyNecklace","RubyBracelet","RubyRing"},
                {"SapphireBelt","SapphireNecklace","SapphireBracelet","SapphireRing"},
                {"EmeraldBelt","EmeraldNecklace","EmeraldBracelet","EmeraldRing"},
                {"DiamondBelt","DiamondNecklace","DiamondBracelet","DiamondRing"}
            }
        };
    //[HP/st/df/dura]
    private final int[][][][] EQUIPMENT_STATS =
        {
            {   //Sword,Shield,Helment,Chestplate,Leggings,Boots
                {{0,5,0,50},{2,0,2,50},{0,0,1,50},{0,0,2,50},{0,0,2,50},{0,0,1,50}},//Wooden
                {{0,10,0,100},{5,0,4,100},{0,0,2,100},{0,0,4,100},{0,0,4,100},{0,0,2,100}},//Bone
                {{0,15,0,150},{10,0,8,150},{0,0,5,150},{0,0,5,150},{0,0,2,150},{0,0,4,150}},//Stone
                {{0,30,0,200},{13,0,14,200},{0,0,8,200},{0,0,7,200},{0,0,9,200},{0,0,9,200}},//Copper
                {{0,40,0,250},{25,0,20,250},{0,0,13,250},{0,0,15,250},{0,0,14,250},{0,0,12,250}},//Bronze
                {{0,50,0,300},{30,0,27,300},{0,0,15,300},{0,0,18,300},{0,0,18,300},{0,0,15,300}},//Iron
                {{0,75,0,400},{40,0,46,400},{0,0,20,400},{0,0,23,400},{0,0,22,400},{0,0,19,400}},//Steel
                {{0,100,0,500},{60,0,60,500},{0,0,30,500},{0,0,35,500},{0,0,32,500},{0,0,30,500}},//Mithril
                {{0,125,0,600},{70,0,70,600},{0,0,35,600},{0,0,40,600},{0,0,37,600},{0,0,35,600}},//Tungsten
                {{0,150,0,700},{80,0,80,700},{0,0,40,700},{0,0,45,700},{0,0,40,700},{0,0,40,700}},//Titanium
                {{0,170,0,800},{90,0,90,800},{0,0,45,800},{0,0,50,800},{0,0,48,800},{0,0,45,800}},//Platinum
                {{0,200,0,1000},{100,0,100,1000},{0,0,50,1000},{0,0,70,1000},{0,0,60,1000},{0,0,50,1000}}//Adamantine
            },
            {
                {{5,5,5,20},{5,5,5,20},{5,5,0,20},{5,5,0,20}},//1~9
                {{10,10,10,40},{10,10,10,40},{10,10,0,40},{10,10,0,40}},//10~19
                {{15,15,15,50},{15,15,15,50},{15,15,0,50},{15,15,0,50}},//20~29
                {{30,30,30,70},{30,30,30,70},{30,30,0,70},{30,30,0,70}},//30~39
                {{50,50,50,100},{50,50,50,100},{50,50,0,100},{50,50,0,100}}//40~60
            }
        };

    private int stars;//1~7
    private String rarity;//1~7 || Common~Unique
    private int[] stats = new int[5];//HP/st/df/dura/maxDura
    private int[] loc = new int[3];//[x,y,z]

    /**
     * Class Equipment Constructor -> for random generated Equipment; see: loadRarity() / loadStats()
     * <p>
     * Call to super initializes a "" name and a quantity of 1.
     * <p>
     * Additionally initializes the loc in String[][][] equipments, name based on its location,
     * a random rarity, stars based on the rarity, and stats based on the loc in int[][][][] equipmentStats.
     * 
     * @param type 0 for body armor ; 1 for accessories
     * @param lv Character's level
     */
    public Equipment(int type, int lv) {//randomGeneration
        super("");
        loc[0] = type;
        loc[1] = lv / (type * 5 + 5);
        loc[2] = (int)(Math.random() * EQUIPMENTS[loc[0]][loc[1]].length);
        setName(EQUIPMENTS[loc[0]][loc[1]][loc[2]]);
        loadRarity(-1);
        //stars
        stars = Integer.valueOf(rarity.substring(0,1));
        rarity = rarity.substring(1);
        loadStats();
    }

    /**
     * Class Equipment Constructor -> when specifying which Equipment to construct; see: loadRarity() / loadStats()
     * <p>
     * Call to super initializes name based on param name
     * <p>
     * Search through the String[][][] equipments based on param name -> initializes loc based on its location 
     * -> rarity based on param rar, stars based on the rarity, and stats based on the loc in int[][][][] equipmentStats.
     * 
     * @param name the name of the Equipment (Item)
     * @param rar the rarity/stars to give to Equipment
     */
    public Equipment(String name,int rar) {
        super(name);
        for (int x = 0; x < EQUIPMENTS.length; x++) {
            for (int y = 0 ; y < EQUIPMENTS[x].length; y++) {
                for (int z = 0; z < EQUIPMENTS[x][y].length; z++) {
                    if (name.equalsIgnoreCase(EQUIPMENTS[x][y][z])) {
                        loc[0] = x;
                        loc[1] = y;
                        loc[2] = z;
                        break;
                    }
                }
            }
        }
        loadRarity(rar);
        stars = Integer.valueOf(rarity.substring(0,1)); 
        rarity = rarity.substring(1);
        loadStats();
    }

    /**
     * Class Equipment Constructor -> for converting an Item to an Equipment; used: Player.openInterface()
     * <p>
     * call to super initializes name of Equipment
     * <p>
     * Additionally initializes stars, rarity, stats, and loc based on param toConvert's instances
     * 
     * @param toConvert Item to convert to Equipment
     */
    public Equipment(Item toConvert) {
        super(toConvert.getName());
        stars = toConvert.getStars();
        rarity = toConvert.getRarity();
        stats = toConvert.getStats();
        loc = toConvert.getLocation();
    }

    /**
     * Initializes int[] stats based on loc
     * <p>
     * Additional stats is added based on rarity (stars): 
     * loc[1] (y value of String[][][] equipments) * stars + (stars - 1)
     */
    public void loadStats() {
        //giveBaseStats 
        stats[0] = EQUIPMENT_STATS[loc[0]][loc[1]][loc[2]][0];
        stats[1] = EQUIPMENT_STATS[loc[0]][loc[1]][loc[2]][1];
        stats[2] = EQUIPMENT_STATS[loc[0]][loc[1]][loc[2]][2];
        stats[3] = EQUIPMENT_STATS[loc[0]][loc[1]][loc[2]][3];
        stats[4] = stats[3];
        //addRarityStats
        int val = loc[1] * stars + (stars - 1);
        stats[0] += val;
        stats[1] += val;
        stats[2] += val;
        stats[3] += val;
        stats[4] += val;
    }

    /**
     * Initializes String rarity based on param rar
     * <p>
     * 1) If param rar == -1, randomize rarity: Unique(7): 1% | Archaic(6): 2% | 
     * Legendary(5): 3% | Epic(4): 5% | Rare(3): 10% | Uncommon(2): 19% | Common(1): 60%
     * <p>
     * 2) if param rar given: initializes rarity accordingly
     * 
     * @param rar the rarity of Equipment; -1 to randomize rarity
     */
    public void loadRarity(int rar) {
        int chance = 10000;
        if (rar == -1) {
            //rollRarity
            chance = (int)(Math.random() * 1000) + 1;
        }
        //selectRarity
        if (rar == 7 || chance >= 1 && chance <= 10) {
            rarity = "7Unique";
        } else if (rar == 6 || chance <= 30) {
            rarity = "6Archaic";
        } else if (rar == 5 || chance <= 60) {
            rarity = "5Legendary";
        } else if (rar == 4 || chance <= 110) {
            rarity = "4Epic";
        } else if (rar == 3 || chance <= 210) {
            rarity = "3Rare";
        } else if (rar == 2 || chance <= 400) {
            rarity = "2Uncommon";
        } else if (rar == 1 || chance <= 1000) {
            rarity = "1Common";
        }
    }

    /**
     * Get the cost of an Equipment based on Character's level. 
     * <p>
     * Cost of Equipment is Character's level * 50 * rarity (stars)
     * 
     * @param lv Character's level
     * @return the cost of an Equipment
     */
    public int getCost(int lv) {
        return lv * 50 * stars;
    }

    public String getDurabilities() {
        return stats[3] + "/" + stats[4];
    }

    public String getRarity() {
        return rarity;
    }

    public void setDurability(int val) {
        stats[3] = val;
    }

    //overload Item
    public int getStars() {
        return stars;
    }

    //overload Item
    public int getDurability() {
        return stats[3];
    }

    //overload Item
    public int[] getLocation() {
        return loc;
    }

    //overload Item
    public int[] getStats() {
        return stats;
    } 

    public String toString() {
        String s = "";
        for (int i = 0; i < stars; i++) {
            s += "✯";
        }
        return "\n" + s + "\n" + rarity + " " + getName() +
        "\nDurability: " + getDurabilities() +
        "\n+" + stats[0] + " HP" + 
        "\n+" + stats[1] + " St" +
        "\n+" + stats[2] + " Df\n";
    }
}