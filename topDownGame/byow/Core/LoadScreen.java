package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class LoadScreen {

    /** Creates a new loading screen with all of the set parameters in place **/
    int width;
    int height;
    String seed;
    public LoadScreen(int width, int hieght) {
        this.width = width;
        this.height = hieght;

        StdDraw.setCanvasSize(width * 16, hieght * 16);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, hieght);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);

        drawStartScreen();
    }

    public void drawStartScreen() {
        int halfWidth = width / 2;
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(halfWidth, height * .8, "CS 61B: The Game");
        Font fontSmall = new Font("Monaco", Font.PLAIN, 20);
        StdDraw.setFont(fontSmall);
        StdDraw.text(halfWidth, height * .4, "New Game (N)");
        StdDraw.text(halfWidth, height * .3, "Load Game (L)");
        StdDraw.text(halfWidth, height * .2, "Quit (Q)");
        StdDraw.show();
    }

    public Long enterSeed() {
        drawFrame("Enter Seed: ");
        seed = "";
        while(true) {
            if(StdDraw.hasNextKeyTyped()) {
                Character c = StdDraw.nextKeyTyped();
                seed += c;
                drawFrame(seed);
                if(c == 's' || c == 'S') {
                    break;
                }
            }
        }
        Long seedNum = Long.parseLong(seed.substring(0, seed.length() - 1));
        System.out.println(seedNum);
        return seedNum;
    }

    public String getSeed() {
        return seed;
    }

    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(this.width / 2, this.height / 2, s);
        StdDraw.show();
    }

}
