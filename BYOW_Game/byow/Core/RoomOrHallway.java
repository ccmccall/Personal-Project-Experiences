package byow.Core;

import java.io.Serializable;

public class ROH implements Serializable {
    /*will probably only need the anchor corner point, and the length and width. */
    Pos position;
    int roomLength; /* length means Y*/
    int roomWidth; /* width means X*/

    public ROH(Pos position, int length, int width) {
        this.position = position;
        this.roomLength = length;
        this.roomWidth = width;
    }

    public static class Pos implements Serializable {
        int x;
        int y;

        Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
