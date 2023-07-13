package byow.Core;

import org.junit.jupiter.api.Test;

public class Coordinate implements Comparable{
    int x;
    int y;

    public Coordinate (int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    @Override
    public int compareTo(Object o) {
        Coordinate second = (Coordinate) o;
        if (second.getX() > getX()) {
            return -1;
        } else if (getX() > second.getX()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object obj) {
        Coordinate c = (Coordinate) obj;
        return this.getX() == c.getX() && this.getY() == c.getY();
    }

    @Override
    public String toString() {
        return "( " + getX() + " , " + getY() + " )";
    }

    @Override
    public int hashCode() {
        return 199 * this.getX() + 659 * this.getY();
    }

}

