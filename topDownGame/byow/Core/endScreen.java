package byow.Core;

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class endScreen {
    /** Creates a new loading screen with all of the set parameters in place **/
    int width;
    int height;
    String seed;
    public endScreen(int width, int hieght) {
        this.width = width;
        this.height = hieght;

        StdDraw.setCanvasSize(width * 16, hieght * 16);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, hieght);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);

        drawEndScreen();
    }

    public void drawEndScreen() {
        int halfWidth = width / 2;
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(halfWidth, height * .8, "You Win!!!");
        StdDraw.show();
    }
}
