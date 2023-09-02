import java.util.ArrayList;

public class NPC extends Entity {
    //instances
    private ArrayList<String> conversation;

    /**
     * Class NPC Constructor -> when loading from .txt files
     * <p>
     * Call to super initializes type to NPC, name, row and col of Class Mappable
     * <p>
     * Additionally initializes conversation
     */
    public NPC(String name, int r, int c) {
        super("NPC",name,1,r,c);
        conversation = new ArrayList<String>();
    }    

    /**
     * Adds a line of conversation to an NPC
     * 
     * @param text the line to add
     */
    public void addConversation(String text) {
        conversation.add(text);
    }

    public void printConversation(Map floor,Player player) {
        System.out.println('\u000c');
        Character character = player.getCharacter();
        if (floor.getName().equals("village") && !floor.getNPCs().get(1).hasConversation()) {
            floor.getNPCs().get(0).conversation = new ArrayList<String>();
            floor.getNPCs().get(2).conversation = new ArrayList<String>();
        }
        if (hasConversation()) {
            if (action("Give equipments",true)) {
                //Merchant Wang: give equipments after conversation
                //WoodenSword
                player.equip(new Equipment("WoodenSword",3));
                //WoodenShield
                player.equip(new Equipment("WoodenShield",3));
                System.out.println('\u000c');
            } else if (action("Give back equipments",true)) {
                //village: return equipments to Merchant Wang
                player.unequip(0);
                player.unequip(1);
                player.useItemFromBag(null,0);
                player.useItemFromBag(null,0);
                System.out.println('\u000c');
            } else if (action("Shop",true)) {
                player.useShop();
            } else if (action("Repeat:Shop",false)) {
                player.useShop();
            } else if (action("Repeat:Save",false)) {
                print("1) Save and close the game?",false);
                System.out.println("\t2) Exit");
                if (GameRunner.read.next().equals("1")) {
                    player.saveAndClose();
                }
                System.out.println('\u000c');
            } else if (action("Repeat:BuyPotion",false)) {
                player.buyPotion();
            } else if (action("Repeat",false)) {
                if (floor.getName().equals("village") && onRepeat(floor,1) && getName().equals("Merchant Wang") && onRepeat(floor,0) && conversation.size() > 2) {
                    conversation.remove(0);
                    print(conversation.get(0),true);//Hi again!
                } else if (floor.getName().equals("village") && onRepeat(floor,2) && getName().equals("Chief Darius") && onRepeat(floor,1)) {
                    conversation.remove(0);
                    print(conversation.get(0),true);//Before you embark on your adventure, I'll save your progress so far.
                } else {
                    print(conversation.get(0).substring(conversation.get(0).indexOf(":") + 1),false);//Repeat:
                }
            } else if (action("Wait",false)) {
                if (getName().equals("Chief Darius") && onRepeat(floor,0)) {
                    conversation.remove(0);
                    print(conversation.get(0),true);//Hello, brave adventurer!
                } else if (getName().equals("Merchant Wang")) {
                    if (floor.getMonsters().size() > 0) {
                        //1-1
                        //while there's Monsters: print
                        print("Go defeat the \"M\" symboled slimes and come back to me",false);
                        floor.placeObject(new Path(2,1));
                    } else {
                        conversation.remove(0);
                        print(conversation.get(0),true);//Are the slimes gone now?
                    }
                } else {
                    print("...",false);
                }
            } else if (action("Name",true)) {//Chief Darius
                System.out.println("Enter your name: ");
                String n = GameRunner.read.next();
                character.setName(n);
                print("I see. You are " + n,false);
            } else if (action("GiveMoney",false)) {//Chief Darius
                player.addMoney(null,Integer.valueOf(conversation.get(0).substring(conversation.get(0).indexOf(":") + 1)));
                conversation.remove(0);
            } else if (getName().equals("Sorcerer Apprentice Melina")) {
                if (floor.getName().equals("village")) {
                    if (floor.getNPCs().get(0).conversation.size() == 1) {
                        if (action("GivePotion",true)) {
                            System.out.println("You have acquired: ");
                            Item toAdd = new Item("HP Potion(S)",3);
                            player.addItemToBag(toAdd);
                            System.out.println(toAdd);
                        } else {
                            print(conversation.get(0),true);//Hello, I am a proud sorcerer apprentice, Melina.
                        }
                    } else {
                        print("...",false);
                    }
                } else {//villageLoop
                    player.buyPotion();
                }
            } else if (action("Save",true)) {//Chief Darius
                player.saveAndClose();//villageLoop
            } else {
                print(conversation.get(0),true);
            }
        } else if (floor.getName().equals("1-1")) {
            //Merchant Wang leaving 1-1
            int[] locs = {2,1,3,1,4,1,5,1,6,1,6,2,6,3,5,3,4,3,3,3,2,3,2,4};
            //Cool walking animation
            for (int i = 0; i < locs.length; i += 2) {
                GameRunner.slow(250); 
                System.out.println('\u000c');
                floor.removeObject(this);
                this.setLocation(locs[i],locs[i + 1]);
                floor.placeObject(this);
                floor.printMap();
            }
            System.out.println('\u000c');
            floor.removeObject(this);
            floor.getNPCs().remove(0);
        } else {
            print("...",false);
        }
        floor.printMap();
    }

    /**
     * Check if NPC's line is a Repeat String
     * 
     * @param floor the Class Map in which the NPC is in
     * @param index the index of the NPC
     * @return true if NPC's line is repeat, false if not
     */
    public boolean onRepeat(Map floor, int index) {
        if (floor.getNPCs().get(index).conversation.get(0).indexOf("Repeat") == 0) {
            return true;
        }
        return false;
    }

    /**
     * Check for a specific String that is an action to perform in NPC.printConversation()
     * 
     * @param text the String to check
     * @param remove true to remove the current line, false if not
     * @return if current line is removed
     */
    public boolean action(String text, boolean remove) {
        if (conversation.get(0).indexOf(text) == 0) {
            if (remove) {
                conversation.remove(0);
            }
            return true;
        }
        return false;
    }

    /**
     * Prints a line of an NPC's conversation as defined in .txt files
     * 
     * @param text the conversation of NPC to print word-by-word
     * @param remove true to move on to next line, false to repeat
     */
    public void print(String text, boolean remove) {
        System.out.print(getName() + ": \n\t");
        for (int i = 0; i < text.length(); i++) {
            GameRunner.slow(20);
            System.out.print(text.substring(i,i + 1));
        }
        GameRunner.slow(400);
        System.out.println();
        if (remove) {
            conversation.remove(0);
        }
    }

    /**
     * Check whether an NPC has conversations
     * 
     * @return true if NPC has conversations, false if not
     */
    public boolean hasConversation() {
        if (conversation.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getSymbol() {
        return "P";
    }

    @Override
    public int interactableID() {
        return 2;
    }

    @Override
    public String toString() {
        return "\n" + getName();
    }
}