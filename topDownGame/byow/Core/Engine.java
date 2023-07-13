package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Locale;

public class Engine {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    BuildWorld world;
    LoadScreen ls = new LoadScreen(WIDTH, HEIGHT);
    String seedSaver;
    String moveSaver;

    Boolean isLoad;

    String tileName;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() throws IOException {
        while(true){
            if(StdDraw.hasNextKeyTyped()) {
                Character c = StdDraw.nextKeyTyped();
                if(c != null) {
                    seedSaver += c;
                }
                createLoadScreen(c);
            }
        }
    }

    private void createLoadScreen(Character c) throws IOException {
        if (c == 'n' || c == 'N') {
            isLoad = false;
            createNewWorld();
        }
        else if (c == 'l' || c == 'L') {
            isLoad = true;
            loadWorld();
            runPlayThrough();
            gameLoop();
        }
        else if (c == 'q' || c == 'Q') {
            System.exit(0);
        }
    }

    public void runPlayThrough(){
        ArrayList<Character> moves = NumberFileConcatenator.getMoves("byow/Core/move-file.txt");
        //System.out.println(moves);
        for(Character c : moves) {
            world.moveCharacter(c);
            StdDraw.pause(50);
        }
    }

    public TETile[][] loadWorld(){
        String str = NumberFileConcatenator.getConcatenatedNumbers("byow/Core/seed-file.txt");
        long oldSeed = 0;
        if(!str.equalsIgnoreCase("")) {
            oldSeed = Long.parseLong(str);
        }
        //System.out.println("oldSeed: " + oldSeed);
        world = new BuildWorld(WIDTH, HEIGHT, oldSeed);
        return world.getTiles();
    }

    private void createNewWorld() {
        long seed = ls.enterSeed();
        seedSaver += ls.getSeed();
        world = new BuildWorld(WIDTH, HEIGHT, seed);
        gameLoop();
    }

    public void gameLoop() {
        while(true) {
            if(!world.mouseInRange()) {
                tileName = "HUD";
            } else {
                tileName = world.getTiles()[(int) StdDraw.mouseX()][(int) StdDraw.mouseY()].description();
            }
            world.drawHUD(tileName);

            if(StdDraw.hasNextKeyTyped()) {
                Character c = StdDraw.nextKeyTyped();
                moveSaver += c;
                world.moveCharacter(c);
                if(moveSaver.length() >= 2 && moveSaver.substring(moveSaver.length() - 2, moveSaver.length()).equalsIgnoreCase(":Q")){
                    saveFile();
                    System.exit(0);
                    break;
                }
            }
        }
    }

    private void saveFile() {
        //get rid of the null in the beginning
        seedSaver = nullRemover(seedSaver);
        //System.out.println(seedSaver);
        //System.out.println("MoveSaver: " + moveSaver);
        try {
            FileWriter seedWriter = new FileWriter("byow/Core/seed-file.txt", isLoad);
            seedWriter.write(seedSaver);
            seedWriter.close();

            FileWriter moveWriter = new FileWriter("byow/Core/move-file.txt", isLoad);
            moveWriter.write(moveSaver);
            moveWriter.close();
        } catch (IOException e) {
            System.out.println("error occured");
            e.printStackTrace();
        }
    }

    private void moveFileSaver() {
        try {
            FileWriter moveWriter = new FileWriter("byow/Core/move-file.txt", isLoad);
            moveWriter.write(moveSaver);
            moveWriter.close();
        } catch (IOException e) {
            System.out.println("error occured");
            e.printStackTrace();
        }
    }

    private String nullRemover(String str) {
        if(str.length() >= 4 && str.substring(0, "null".length()).equalsIgnoreCase("null")){
            str = seedSaver.substring("null".length());
        }
        return str;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input){
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        this.isLoad = false;
        boolean quit = false;
        if(input != "" && input.indexOf("l") == 0){
            this.isLoad = true;
            moveSaver += input;
            moveFileSaver();
        } else {
            int index = input.toLowerCase(Locale.US).indexOf('s') + 1;
            seedSaver = input.substring(0, index);
            moveSaver = input.substring(index);
            saveFile();
        }
        loadWorld();
        runPlayThrough();
        return world.getTiles();
    }

    public static void main(String[] args){
        // Change these parameters as necessary
        Engine test = new Engine();
        TETile[][] world = test.interactWithInputString("laddw");
        test.gameLoop();
    }
}