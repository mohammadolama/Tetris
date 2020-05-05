
import javax.swing.*;
import java.awt.*;

public class NextPiecePanel extends JPanel {
    private Shape nextPiece1;
    private Shape nextPiece2 = new Shape();
    private int BOARD_HEIGHT = 10;
    private int curX;
    private int curY;
    private int squareHeight = 25;
    private int squareWidth = 25;


    NextPiecePanel(Shape pieces) {
        System.out.println("hello");
        this.nextPiece1 = pieces;
        curX = 3;
        curY = BOARD_HEIGHT + nextPiece1.minY() + 1;
        setSize(new Dimension(250, 250));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    private void drawSquare(Graphics g, int x, int y, Pieces shape) {
        Color color = shape.color;
        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth - 2, squareHeight - 2);
        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight - 1, x, y);
        g.drawLine(x, y, x + squareWidth - 1, y);
        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight - 1, x + squareWidth - 1, y + squareHeight - 1);
        g.drawLine(x + squareWidth - 1, y + squareHeight - 1, x + squareWidth - 1, y + 1);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        var g2d = (Graphics2D) g;
        var gp2 = new GradientPaint(10, 10,
                Color.PINK, 20, 20, Color.CYAN, true);

        g2d.setPaint(gp2);
        g2d.fillRect(0, 0, 250, 250);


        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * 12;
//
        if (nextPiece1.getShape() != Pieces.NOTHING) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + nextPiece1.x(i);
                int y = curY - nextPiece1.y(i);
                drawSquare(g, x * squareWidth+15, boardTop + (BOARD_HEIGHT - y - 1) * squareHeight-30, nextPiece1.getShape());
            }
        }
        if (nextPiece2.getShape() != Pieces.NOTHING) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + nextPiece2.x(i);
                int y = curY - nextPiece2.y(i);
                drawSquare(g, x * squareWidth+15, boardTop + (BOARD_HEIGHT - y - 1) * squareHeight+70, nextPiece2.getShape());
            }
        }
    }

    void Update(Shape shape1, Shape shape2) {
        nextPiece1 = shape1;
        nextPiece2 = shape2;
        repaint();
    }
}
