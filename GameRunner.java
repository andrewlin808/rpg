
import java.util.Scanner;
import java.util.ArrayList;

public class GameRunner {
    private static Player player;
    public static Scanner read = new Scanner(System.in);
    private static String input = "";
    private static boolean saved = false;
    private static boolean close = false;
    private static boolean portal = false;
    private static boolean died = false;

    public static void main(String[] args) {
        player = new Player("???");
        slowPrint("Use w/a/s/d to move around || numbers (e.g. 1 or 2 or 3) to respond",500);
        confirm();
        if (!saved) {
            runMap("1-1",player);
        } else {
            runMap("villageLoop",player);
        }
    }

    private static void runMap(String floor, Player player) {
        Character character = player.getCharacter();
        Map room = new Map(floor);
        System.out.println("Loading " + floor);
        printRepeat(".",5,500);
        System.out.println('\u000c');
        Mappable portalNext = new Portal(0,0,"n/a",0,0);
        while (!died && !close && !portal) {
            character.move(input,room);
            if (input.equals("1")) {
                player.openInterface();
                input = "skip";
            } else {
                input = "";
            }
            System.out.println("1) Check Player");
            //checkInteractable
            while (room.interactableInRange(character) && !close && !died && !portal) {
                int[] interactableLocation = room.getInteractableID(character);
                //fix NPC print statements (to see conversation and not clear console)
                if (interactableLocation[0] != 2 || input.equals("skip")) {
                    System.out.println('\u000c');
                    room.printMap();
                    System.out.println("1) Check Player");
                } else if (!input.equals("")) {
                    System.out.println("1) Check Player");
                }
                //printAvailableActions
                String[] actions = {"Battle against ","Talk to ","Portal to "};
                System.out.print("2) " + actions[interactableLocation[0] - 1]);
                //selectAction
                Entity entity;
                if (interactableLocation[0] == 1) {
                    //battle
                    if (interactableLocation[1] == 1) {
                        entity = room.getMonsters().get(interactableLocation[2]);
                    } else {
                        entity = room.getSpirits().get(interactableLocation[2]);
                    }
                    System.out.println(entity.getName());
                    if (entity.getSymbol().equals("S")) {
                        input = read.next();
                        if (input.equals("2")) {
                            slowPrint("You dare challenge me, mortal?",200);
                            System.out.println("1) Check Player");
                            System.out.println("2) Challenge " + entity.getName());
                            input = read.next();
                        }
                    } else {
                        input = read.next();
                    }
                    if (input.equals("2")) {
                        if (entity.getSymbol().equals("S")) {
                            slowPrint(entity.getName() + ": \n\t You have been warned...",100);
                            confirm();
                        }
                        System.out.println("Battle against " + entity.getName() + " has started! \nLoading");
                        printRepeat(".",5,250);
                        while (!died) {
                            //battle
                            System.out.println('\u000c');
                            System.out.println(character + "" + entity);
                            System.out.println("1) Attack \n2) Check Player");
                            input = read.next();
                            if (input.equals("1")) {
                                slow(500);
                                player.attack(entity);
                                if (entity.getHP() > 0) {
                                    //Entity's attack
                                    slow(500);
                                    entity.attack(player);
                                    if (character.getHP() == 0) {
                                        //break when died
                                        died = true;
                                        room.removeObject(character);
                                        break;
                                    }
                                } else {
                                    slowPrint(character.getName() + " has defeated " + entity.getName() + "!",500);
                                    character.addXP(entity,-1);
                                    player.addMoney(entity,0);
                                    room.removeObject(entity);
                                    if (interactableLocation[1] == 1) {
                                        room.getMonsters().remove(interactableLocation[2]);
                                    } else {
                                        room.getSpirits().remove(interactableLocation[2]);
                                    }
                                    confirm();
                                    System.out.println("Leaving battle");
                                    printRepeat(".",5,250);
                                    System.out.println('\u000c');
                                    room.printMap();
                                    input = "skip";
                                    break;
                                }
                            } else if (input.equals("2")) {
                                player.openInterface();
                            }
                        }
                    } else if (input.equals("1")) {
                        player.openInterface();
                        input = "skip";
                    }
                    break;
                } else if (interactableLocation[0] == 2) {
                    entity = room.getNPCs().get(interactableLocation[2]);
                    System.out.println(entity.getName());
                    input = read.next();
                    if (input.equals("2")) {
                        entity.printConversation(room,player);
                    } else if (input.equals("1")) {
                        player.openInterface();
                        input = "skip";
                    } else {
                        break;
                    }
                } else if (interactableLocation[0] == 3) {
                    portalNext = room.getMappable(interactableLocation[1],interactableLocation[2]);
                    System.out.println(portalNext.getMap());
                    input = read.next();
                    if (input.equals("2")) {
                        if (room.getName().equals("1-1")) {
                            if (room.getNPCs().size() == 0 && portalNext.getMap().equals("village")) {
                                portal = true;
                            } else {
                                System.out.println("The portal is not opened yet!");
                            }
                        } else if (room.getName().equals("village")) {
                            System.out.println("The portal is not opened yet!");
                        } else if (portalNext.getMap().equals("n/a")) {
                            System.out.println("Under construction");
                        } else {
                            portal = true;
                        }
                    } else if (input.equals("1")) {
                        player.openInterface();
                        input = "skip";
                    }
                    break;
                }
            }
            if (died || close || portal) {
                break;
            } else if (!input.equals("w") && !input.equals("a") && !input.equals("s") && !input.equals("d") && !input.equals("skip")) {
                input = read.next();
            }
        }
        if (died) {
            System.out.println("You have died");
        } else if (close) {
            System.out.println("Game closed");
        } else if (portal) {
            portal = false;
            System.out.println("You entered the portal");
            if (room.getName().equals("1-1") && room.getNPCs().size() == 0 && portalNext.getMap().equals("village")) {
                character.setLocation(portalNext.getPlayerRow(),portalNext.getPlayerCol());
                runMap(portalNext.getMap(),player);
            } else {
                character.setLocation(portalNext.getPlayerRow(),portalNext.getPlayerCol());
                runMap(portalNext.getMap(),player);   
            }
        }
    }

    public static void setClose(boolean val) {
        close = val;
    }

    public static void setSaved(boolean val) {
        saved = val;
    }

    public static void slow(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Slow down the S.o.p of a given String
     * 
     * @param text the String to print
     * @param val the value to slow down S.o.p
     */
    public static void slowPrint(String text,int val) {
        slow(val);
        System.out.println(text);
    }

    /**
     * Simple user confirmation
     */
    public static void confirm() {
        slowPrint("Press anything to continue",300);
        read.next();
    }

    /**
     * Prints a slowPrint String for a certain amount of times
     * 
     * @param text the String to print
     * @param repeat the number of times to print param text
     * @param val the value to slow down S.o.p
     */
    public static void printRepeat(String text, int repeat, int val) {
        for (int i = 0; i < repeat; i++) {
            slowPrint(text,val);
        }
    }

    /**
     * Check if user input is within an inclusive range of numbers
     * <p>
     * Used to prevent InputMisMatchException.
     * 
     * @param min the mininum inclusive range
     * @param max the maximum inclusive range
     * @return the Integer value of user's input, return -1 if out of range
     */
    public static int StringInputInRange(int min, int max) {
        String input = read.next();
        for (int i = min; i <= max; i++) {
            if (input.equals(String.valueOf(i))) {
                return i;
            }
        }
        return -1;
    }

    /** 
     * Simulate Character battling against a Monster
     * <p>
     * Statistics to print: total battles, wins, losses, 
     * rounds (two-way attack, if possible), average round/battle, and win percentage.
     * <p>
     * Entities will not gain experience through battles; Equipments will lose durability accordingly
     * (see: Player.equipmentUsed(); HP are only restored if Entity faints
     * 
     * @param battles the number of battles to simulation (a battle lasts until an Entity faints)
     * @param mylevel the level to set Character at
     * @param oppLevel the level to set Entity at
     * @param equip true to fullEquip() Player, else false
     * @param rarity the rarity to set equipments at (1~7 inclusive)
     */
    public static void simulation(int totalBattles, int myLevel, int oppLevel, boolean equip, int rarity) {
        Player player = new Player();
        Character character = player.getCharacter();
        //levelUp
        for (int i = 1; i < myLevel; i++) {
            character.addXP(null,(int)(Math.pow(character.getLv() + 1,2) * 2));
        }
        //FullEquip
        if (equip) {
            player.fullEquip(rarity);
        }
        int rounds = 0;
        int wins = 0;
        int losses = 0;
        Monster opp = new Monster("Monster",oppLevel,0,0);
        System.out.println(opp);
        confirm();
        for (int i = 0; i < totalBattles; i++) {
            while (opp.getHP() > 0 && character.getHP() > 0) {
                player.attack(opp);
                if (opp.getHP() > 0) {
                    opp.attack(player);
                }
                rounds++;
                if (opp.getHP() == 0) {
                    System.out.println(opp.getName() + " has fainted");
                    wins++;
                    opp.restore();
                    break;
                } else if (character.getHP() == 0) {
                    System.out.println(character.getName() + " has fainted");
                    losses++;
                    character.restore();
                    break;
                }
            }
        }
        System.out.println("\n–––––––––––––Result–––––––––––––");
        System.out.println("Total Battles: " + totalBattles);
        System.out.println("Wins: " + wins);
        System.out.println("Losses: " + losses);
        System.out.println("Total Rounds: " + rounds + "\nAverage Round/Battle: " + (rounds / totalBattles));
        System.out.println("Win Percentage: " + ((int)(wins / (double)totalBattles) * 100) + "%");
        System.out.println("––––––––––––––––––––––––––––––––");
        System.out.println(character);
        System.out.println(opp);
    }
}