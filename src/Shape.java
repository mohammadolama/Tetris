import java.util.Random;

class Shape {

    private Pieces pieceShape;
    private int[][] coords;


    Shape() {
        coords = new int[4][2];
        setShape(Pieces.NOTHING);
    }

    void setShape(Pieces shape) {
        for (int i = 0; i < 4; i++) {
            System.arraycopy(shape.raas[i], 0, coords[i], 0, 2);
        }

        pieceShape = shape;
    }

    private void setX(int index, int x) {
        coords[index][0] = x;
    }

    private void setY(int index, int y) {
        coords[index][1] = y;
    }

    int x(int index) {
        return coords[index][0];
    }

    int y(int index) {
        return coords[index][1];
    }

    Pieces getShape() {
        return pieceShape;
    }



    private void setRandomShape() {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Pieces[] values = Pieces.values();
        setShape(values[x]);
    }

    static Shape RandomShape(){
        Shape shape=new Shape();
        shape.setRandomShape();
        return shape;
    }

    static Shape TransShape(Shape shape){
        Shape shape1=shape;
        return shape1;
    }

    int minY() {
        int m = coords[0][1];

        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }

        return m;
    }

    Shape rotateLeft() {
        if (pieceShape == Pieces.WINDOW)
            return this;

        Shape result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; i++) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }

        return result;
    }

    Shape rotateRight() {
        if (pieceShape == Pieces.WINDOW)
            return this;

        Shape result = new Shape();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; i++) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }

        return result;
    }
}
