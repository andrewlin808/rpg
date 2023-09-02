public class Monster extends Entity {

    /**
     * Class Monster Constructor -> loaded from .txt files
     * <p>
     * Call to super initializes type to Monster, name, level, row and col of Class mappable.
     * 
     * @param name the name of Monster
     * @param level the level of Monster
     * @param r the row of Monster
     * @param c the column of Monster
     */
    public Monster(String name, int level, int r, int c) {
        super("Monster",name,level,r,c);
    }

    /**
     * Monster attacks against a Character
     * <p>
     * Values of Monster's attack/Character's defense are randomized 
     * from half of Monster's strength/Character's defense up to full values, respectively.
     * Deal damage and subtract Character's hit point accordingly.
     * <p>
     * With Monster's attack being less than Character's defense, Monster's level being 5 higher than
     * Character's level && value of the difference in damage is less or equal to 5, 1 damage is dealt
     * to Character as recoil damage.
     * <P>
     * Equipments' durabilities are decreased according Player.equipmentUsed().
     * 
     * @param opp Character to attack against
     */
    @Override
    public void attack(Player player) {
        Character character = player.getCharacter();
        //randomized MOnster's attack
        System.out.println();
        int att = (int)(Math.random() * (getSt() / 2 + 1)) + getSt() / 2;
        if (getSt() % 2 == 1) {
            att++;
        }
        //randomized Character's defense
        int df = (int)(Math.random() * (character.getDf() / 2 + 1)) + character.getDf() / 2;
        if (character.getDf() % 2 == 1) {
            df++;
        }
        GameRunner.slowPrint("\t" + getName() + " attacked " + character.getName(),100);
        GameRunner.slowPrint("\tatt: " + att + "   df: " + df,100);
        int dmg = att - df;
        if (dmg > 0) {
            System.out.print("\t" + character.getName() + "'s HP: " + character.getHPs() + "  -" + dmg);
            //substract hit point
            character.setHP(character.getHP() - dmg);
        } else {
            //recoil damage
            if (getLv() - character.getLv() >= 5 && -dmg <= 5) {
                GameRunner.slowPrint("\t" + character.getName() + " endured " + getName() + "'s attack",100);
                System.out.print("\t" + character.getName() + "'s HP: " + character.getHPs() + "  -1");
                character.setHP(character.getHP() - 1);
            } else {
                System.out.print("\t" + getName() + "'s attack was blocked... -0");
            }
        }
        GameRunner.slowPrint(" -> " + character.getHPs() + "\n",200);
        //Decrease Equipment durability
        if (dmg >= character.getLv()) {
            player.equipmentUsed(new int[]{1,2,3,4,5});
        }
        GameRunner.confirm();
    }

    @Override
    public String getSymbol() {
        return "M";
    }

    @Override
    public int interactableID() {
        return 1;
    }

    public String toString() {
        return "\n\t" + getName() + "  Lv" + getLv() +
        "\n\tHP: " + getHPs() + "  St: " + getSt() + "  Df: " + getDf() + "\n";
    }
}