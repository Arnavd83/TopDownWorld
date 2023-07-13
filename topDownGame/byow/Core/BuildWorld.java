package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;
import javassist.compiler.ast.Pair;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang3.math.IEEE754rUtils.min;

public class BuildWorld {
    private TETile[][] tiles;
    int width;
    int height;
    long seed;
    Random rand;
    ArrayList<Coordinate> room_points;
    HashSet<Coordinate> room_set;

    TERenderer ter = new TERenderer();
    Coordinate characterPosition, endPos;
    TETile endTile;
    String tileName = "";
    endScreen endScreen;

    /** Sets the value of each of the constructor variables **/
    public BuildWorld (int width, int height, long seed) {
        //Create World
        this.width = width;
        this.height = height;
        WorldCreation world = new WorldCreation(width, height, seed);
        tiles = world.getTiles();

        //Get RoomPoints and Initial Character Position
        room_points = world.getRoomPoints();
        room_set = new HashSet<>(room_points);

        characterPosition = world.getCharacterPos();
        endPos = world.endPosition();
        endTile = Tileset.LOCKED_DOOR;

        room_set.remove(characterPosition);
        room_set.remove(endPos);
        System.out.println(room_set);

        // Render World
        ter.initialize(width, height + 3);
        renderWorld();
        StdDraw.show();
    }

    public void renderWorld() {
        StdDraw.clear();
        tiles[characterPosition.getX()][characterPosition.getY()] = Tileset.AVATAR;
        tiles[endPos.getX()][endPos.getY()] = endTile;
        ter.renderFrame(tiles);
        StdDraw.show();
    }

    /** Returns the tiles associated with Build World **/
    public TETile[][] getTiles() {
        return tiles;
    }

    /** Iterates through roomPoints and puts it in a set **/


    public boolean canMove(Character c) {
        int newX = characterPosition.getX();
        int newY = characterPosition.getY();
        if(c == 'a') {
            newX --;
        } else if(c == 'd') {
            newX ++;
        } else if(c == 'w') {
            newY ++;
        } else if(c == 's') {
            newY --;
        } else if(c == 'g') {
            System.out.println(room_set);
            System.out.println("room_set is empty: " + room_set.isEmpty());
        } else {
           return false;
        }

        if (newX < width && newX > 0 && newY < height && newY > 0) {
            TETile nextTile = tiles[newX][newY];
            if (nextTile != Tileset.TREE || nextTile == Tileset.FLOWER) {
                tiles[characterPosition.getX()][characterPosition.getY()] = Tileset.FLOOR;
                characterPosition = new Coordinate(newX, newY);
                if (room_set.contains(characterPosition)) {
                    room_set.remove(characterPosition);
                }
                if (room_set.isEmpty()) {
                    System.out.println("entered if statement");
                    endTile = Tileset.UNLOCKED_DOOR;
                }
                return true;
            }
        }
        return false;
    }
    public void moveCharacter(char c) {
        if(tiles[characterPosition.getX()][characterPosition.getY()] == Tileset.UNLOCKED_DOOR) {
            endScreen = new endScreen(width, height);
        }
        else if(canMove(c)) {
            renderWorld();
        }
    }

    public void drawHUD(String s) {
        mouseShits(s);
    }


    public boolean mouseInRange() {
        return !(StdDraw.mouseX() >= width
                || StdDraw.mouseX() < 0
                || StdDraw.mouseY() >= height
                || StdDraw.mouseY() < 0);
    }

    /** Creates the HUD for the game and adds a black rectangle to cover the coordiantes **/
    public void mouseShits(String tileName) {
        StdDraw.setPenColor(Color.black);
        StdDraw.filledRectangle(width / 2, height + 1.5, 10, 1.5);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 14);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height + 1.5, tileName);
        dateTime();
        StdDraw.show();
    }

    public void dateTime() {
        StdDraw.setPenColor(Color.black);
        StdDraw.filledRectangle(8, height + 1.5, 10, 1.5);
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd, yyyy  hh:mm:ss a");
        StdDraw.setPenColor(Color.WHITE);
        Date date = new Date();
        String dateString = formatter.format(date);
        StdDraw.text(8, height + 1.5, dateString);
    }
}
