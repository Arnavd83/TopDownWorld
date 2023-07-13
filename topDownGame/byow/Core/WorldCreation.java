package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static org.apache.commons.lang3.math.IEEE754rUtils.min;

public class WorldCreation {

    private TETile[][] tiles;
    int GAME_WIDTH;
    int GAME_HEIGHT;
    int rooms;
    final int MIN_ROOM_SIZE = 3;
    final int MAX_ROOM_HEIGHT = 7;
    final int MAX_ROOM_WIDTH = 7;
    final int MIN_ROOMS = 13;
    final int MAX_ROOMS = 17;
    long seed;
    Random rand;
    ArrayList<Coordinate> room_points;

    TERenderer ter = new TERenderer();
    Coordinate characterPosition;
    String tileName = "";

    public WorldCreation(int width, int height, long seed) {
        //Initialize each variable
        this.GAME_WIDTH = width;
        this.GAME_HEIGHT = height;
        this.seed = seed;
        tiles = new TETile[this.GAME_WIDTH][this.GAME_HEIGHT];
        room_points = new ArrayList<Coordinate>();

        //Generates random amount of rooms between min and max rooms
        this.rand = new Random(seed);
        this.rooms = rand.nextInt((MAX_ROOMS - MIN_ROOMS) + 1) + MIN_ROOMS;

        //Creates World
        createWorld();
    }

    public ArrayList<Coordinate> getRoomPoints(){
        return room_points;
    }

    public Coordinate getCharacterPos() {
        System.out.println(characterPosition);
        return characterPosition;
    }

    /** Creates the world **/
    public void createWorld() {
        fillWorld();
        generateRooms();
        generateFlowers();
        buildHallways();
        createWalls();
        addCharacter();
    }

    /** fills the world with wall Tiles**/
    private void fillWorld() {
        for (int i = 0; i < GAME_WIDTH; i++) {
            for (int j = 0; j < GAME_HEIGHT; j++) {
                tiles[i][j] = Tileset.WATER;
            }
        }
    }

    /** Generates Rooms **/
    public void generateRooms() {
        for(int i = 0; i < rooms; i++) {
            buildRectRoom(rand.nextInt(GAME_WIDTH - 1), rand.nextInt(GAME_HEIGHT - 1));
        }
        Collections.sort(room_points);
    }

    /** Generates a Flower at every point in the roomPoints array **/
    public void generateFlowers() {
        for(Coordinate c : room_points) {
            if (c.getX() < GAME_WIDTH && c.getY() < GAME_HEIGHT) {
                tiles[c.getX()][c.getY()] = Tileset.FLOWER;
            }
        }
    }

    /** Generate the Walls with tiles next to floor tiles **/
    public void createWalls() {
        for (int i = 1; i < GAME_WIDTH; i++) {
            for (int j = 1; j < GAME_HEIGHT; j++) {
                if (tiles[i][j] == Tileset.CUSTOM_FLOOR || tiles[i][j] == Tileset.FLOWER) {
                    wallHelperMethod(i, j);
                }
            }
        }
    }

    /** generate walls around a point of floor **/
    public void wallHelperMethod(int i, int j) {
        for (int x = Math.max(0, i - 1); x <= Math.min(i + 1, tiles.length - 1); x++) {
            for (int y = Math.max(0, j - 1); y <= Math.min(j + 1, tiles[x].length - 1); y++) {
                if (tiles[x][y] == Tileset.WATER) {
                    tiles[x][y] = Tileset.TREE;
                }
            }
        }
    }

    /** Create a rectangular room of random size **/
    public void buildRectRoom(int x, int y) {
        //Randomize room size dimensions
        int roomHeight = rand.nextInt((MAX_ROOM_HEIGHT - MIN_ROOM_SIZE) + 1) + MIN_ROOM_SIZE;
        int roomWidth = rand.nextInt((MAX_ROOM_WIDTH - MIN_ROOM_SIZE) + 1) + MIN_ROOM_SIZE;

        //Fills room
        for (int i = x; i < min(x + roomWidth, GAME_WIDTH); i++) {
            for (int j = y; j < min(y + roomHeight, GAME_HEIGHT); j++) {
                tiles[i][j] = Tileset.CUSTOM_FLOOR;
            }
        }

        // Add a random point inside the room to connect with hallways
        Coordinate newCoord = new Coordinate(x + roomWidth/2, y + roomHeight/2);
        if(newCoord.getX() < GAME_WIDTH && newCoord.getY() < GAME_HEIGHT) {
            room_points.add(newCoord);
        } else {
            room_points.add(new Coordinate(x, y));
        }
    }

    /** Generates Hallways that connect rooms to each other **/
    public void buildHallways() {
        for(int i = 0; i < room_points.size() - 1; i++) {
            connectPoints(room_points.get(i), room_points.get(i + 1));
        }
    }

    /** Builds connect two rooms with hallways **/
    public void connectPoints(Coordinate one, Coordinate two) {
        int x = one.getX();
        int y = one.getY();
        while(x != two.getX()) {
            x = moveCloser(x, two.getX());
            if(tiles[x][y] != Tileset.FLOWER) {
                tiles[x][y] = Tileset.CUSTOM_FLOOR;
            }
        }
        while(y != two.getY()) {
            if(tiles[x][y] != Tileset.FLOWER) {
                tiles[x][y] = Tileset.CUSTOM_FLOOR;
            }
            y = moveCloser(y, two.getY());
        }
    }

    /** Moves X closer to Y (helper method for connect rooms **/
    public int moveCloser(int x, int y) {
        if(x > y) {
            return x - 1;
        } else if (x < y) {
            return x + 1;
        } else {
            return x;
        }
    }

    /** Returns the tiles associated with Build World **/
    public TETile[][] getTiles() {
        return tiles;
    }

    /** Places Charact3r in starting spot **/
    public void addCharacter() {
        characterPosition = room_points.get(0);
    }

    public Coordinate endPosition() {
        return room_points.get(room_points.size() - 1);
    }

}
