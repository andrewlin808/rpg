import java.util.ArrayList;

public class Map {
    //Instances
    private Mappable[][] map;
    private String name;
    private int rows;
    private int cols;
    private ArrayList<Monster> monsters;
    private ArrayList<Spirit> spirits;
    private ArrayList<NPC> NPCs;

    /**
     * Class Map Constructor -> loaded from .txt files
     * <p>
     * Initialize name to be param floor, which is also used by .txt file, 
     * a 2D map according to .txt file, and their Integer representations.
     * <p>
     * Entities and portals are stored in their respective ArrayLists, 
     * and are accessed with Map.getInteractableID()
     * 
     * @param floor the name of the .txt file
     */
    public Map(String floor) {
        name = floor;
        MediaFile.setInputFile(floor);
        //loadMap
        rows = Integer.valueOf(MediaFile.readString());
        cols = Integer.valueOf(MediaFile.readString());
        map = new Mappable[rows][cols];
        for (int r = 0; r < map.length; r++) {
            String[] line = MediaFile.readString().split(" ");
            for (int c = 0; c < map[r].length; c++) {
                if (Integer.valueOf(line[c]) == 0) {
                    //Path
                    map[r][c] = new Path(r,c);
                } else if (Integer.valueOf(line[c]) == 1) {
                    //Wall
                    map[r][c] = new Wall(r,c);
                } else if (Integer.valueOf(line[c]) == 2) {
                    //Character
                } else if (Integer.valueOf(line[c]) == 3) {
                    //Monster
                } else if (Integer.valueOf(line[c]) == 4) {
                    //Spirit
                } else if (Integer.valueOf(line[c]) == 5) {
                    //NPC
                } else if (Integer.valueOf(line[c]) == 6) {
                    //Portal
                }
            }
        }
        //loadEntities
        monsters = new ArrayList<Monster>();
        spirits = new ArrayList<Spirit>();
        NPCs = new ArrayList<NPC>();
        String read = MediaFile.readString();
        if (read != null) {
            String[] types = read.split(":");
            String[] data;
            //Monster (name:level:r:c:)
            for (int m = 0; m < Integer.valueOf(types[0]); m++) {
                data = MediaFile.readString().split(":");
                monsters.add(new Monster(data[0],Integer.valueOf(data[1]),Integer.valueOf(data[2]),Integer.valueOf(data[3])));
                placeObject(monsters.get(m));
            }
            //Spirit (name:level:r:c:)
            for (int s = 0; s < Integer.valueOf(types[1]); s++) {
                data = MediaFile.readString().split(":");
                spirits.add(new Spirit(data[0],Integer.valueOf(data[1]),Integer.valueOf(data[2]),Integer.valueOf(data[3])));
                placeObject(spirits.get(s));
            }
            //NPC (name:r:c:conversations)
            for (int n = 0; n < Integer.valueOf(types[2]); n++) {
                data = MediaFile.readString().split(":");
                NPCs.add(new NPC(data[0],Integer.valueOf(data[1]),Integer.valueOf(data[2])));
                //addConversation
                for (int i = 0; i < Integer.valueOf(data[3]); i++) {
                    NPCs.get(n).addConversation(MediaFile.readString());
                }
                placeObject(NPCs.get(n));
            }
            //Portal
            for (int p = 0; p < Integer.valueOf(types[3]); p++) {
                data = MediaFile.readString().split(":");
                placeObject(new Portal(Integer.valueOf(data[0]),Integer.valueOf(data[1]),data[2],Integer.valueOf(data[3]),Integer.valueOf(data[4])));
            }
        }
    }

    /**
     * Print a visual representation of Mappable[][] map
     */
    public void printMap() {
        System.out.println();
        System.out.println(name);
        System.out.println();
        for (int r = 0; r < map.length; r++) {
            for (int c = 0; c < map[r].length; c++) {
                System.out.print(" " + map[r][c].getSymbol());
            }
            System.out.println();
        }
    }

    /**
     * Place a Mappable on Mappable[][] map
     * 
     * @param toPlace the Mappable Object to remove
     */
    public void placeObject(Mappable toPlace) {
        map[toPlace.getRow()][toPlace.getCol()] = toPlace;
    }

    /**
     * Remove a Mappable on Mappabe[][] map
     * <p>
     * The location of on the map is substituded with a Subclass Path
     * 
     * @param toRemove the Mappable Object ot remove
     */
    public void removeObject(Mappable toRemove) {
        map[toRemove.getRow()][toRemove.getCol()] = new Path(toRemove.getRow(),toRemove.getCol());
    }

    /**
     * Checks whether Character can move at a certain location in Mappable[][] map
     * 
     * @param row the row of map
     * @param col the column of map
     * @return true if Character can move to that location (it being a Subclass Path), false if not
     */
    public boolean canMove(int row, int col) {
        if (map[row][col].getSymbol().equals(" ")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks whether there's an interactable Mappable near Character
     * 
     * @param character the Character to check with
     * @return true if there is an interactable Mappable, false if not
     */
    public boolean interactableInRange(Character character) {
        int r = character.getRow();
        int c = character.getCol();
        //player is guranteed to not be in border Walls
        if (map[r - 1][c].interactableID() > 0 || map[r + 1][c].interactableID() > 0 || map[r][c - 1].interactableID() > 0 || map[r][c + 1].interactableID() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the interactableID, type, and index of an interactable Mappable
     * <p>
     * interactable[0] contains the interactableID.
     * <p>
     * interactable[1] contains the type of the interactable Mappable.
     * <p>
     * interactable[2] contains the index of the interactable Mappable.
     * 
     * @param character the Character to check with
     * @return an int[] containing the ID, type, and index of the interactable Mappable
     */
    public int[] getInteractableID(Character character) {
        int r = character.getRow();
        int c = character.getCol();
        int[] interactable = new int[3];//[interactableID,type of Entity,index of ArrayList]
        Mappable[] locations = {map[r - 1][c],map[r + 1][c],map[r][c - 1],map[r][c + 1]};
        for (Mappable loc : locations) {
            if (loc.getSymbol().equals("M")) {//Monster
                interactable[0] = loc.interactableID();
                interactable[1] = 1;
                for (int i = 0; i < monsters.size(); i++) {
                    if (loc.getRow() == monsters.get(i).getRow() && loc.getCol() == monsters.get(i).getCol()) {
                        interactable[2] = i;
                        break;
                    }   
                }
                break;
            } else if (loc.getSymbol().equals("S")) {//Spirit
                interactable[0] = loc.interactableID();
                interactable[1] = 2;
                for (int i = 0; i < spirits.size(); i++) {
                    if (loc.getRow() == spirits.get(i).getRow() && loc.getCol() == spirits.get(i).getCol()) {
                        interactable[2] = i;
                        break;
                    }
                }
                break;
            } else if (loc.getSymbol().equals("P")) {//NPC
                interactable[0] = loc.interactableID();
                interactable[1] = 3;
                for (int i = 0; i < NPCs.size(); i++) {
                    if (loc.getRow() == NPCs.get(i).getRow() && loc.getCol() == NPCs.get(i).getCol()) {
                        interactable[2] = i;
                        break;
                    }
                }
                break;
            } else if (loc.getSymbol().equals("Ø")) {//Portal //[ID,r,c]
                interactable[0] = loc.interactableID();
                interactable[1] = loc.getRow();
                interactable[2] = loc.getCol();
                break;
            }
        }
        return interactable;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public ArrayList<Spirit> getSpirits() {
        return spirits;
    }

    public ArrayList<NPC> getNPCs() {
        return NPCs;
    }

    public Mappable getMappable(int r, int c) {
        return map[r][c];
    }

    public String getName() {
        return name;
    }
}