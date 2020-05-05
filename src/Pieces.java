import java.awt.*;

public enum Pieces {

    NOTHING(new int[][] { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } }, new Color(0, 0, 0)),
    ZShape(new int[][] { { -1, 0 }, { 0, 0 }, { 0, 1 }, { 1, 1 } }, new Color(218, 0, 15)),
    SShape(new int[][] { { 1, 0 }, { 0, 0 }, { 0, 1 }, { -1, 1 } }, new Color(204, 116, 38)),
    LineShape(new int[][] { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 2, 0 } }, new Color(204, 201, 0)),
    TShape(new int[][] { { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } }, new Color(0, 204, 4)),
    WINDOW(new int[][] { { 0, 0 }, { 1, 0 }, { 1, 1 }, { 0, 1 } }, new Color(0, 32, 204)),
    LShape(new int[][] {  { -1, -1 },{ -1, 0 }, { 0, 0 }, { 1, 0 } }, new Color(202, 145, 204)),
    MirroredLShape(new int[][] { { -1, 0 }, { 0, 0 }, { 1, 0 }, { -1, 1 } }, new Color(204, 0, 193));

    public
    int[][] raas;
    public Color color;


    Pieces(int[][] raas, Color c) {
        this.raas = raas;
        color = c;
    }
}
