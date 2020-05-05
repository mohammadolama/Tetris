
import javazoom.jl.decoder.JavaLayerException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Board extends JPanel implements ActionListener {
    private static int BOARD_WIDTH = 14;
    private static int BOARD_HEIGHT = 30;
    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isStarted = false;
    private boolean isPaused = false;
    private int score = 0;
    private int linesRemoved = 0;
    private int currentX = 0;
    private int currentY = 0;
    private JLabel statusBar;
    private JLabel numLinesRemoved;
    private Shape currentPiece=new Shape();
    private Shape nextPiece1=new Shape();
    private Shape nextPiece2;
    private Pieces[][] board;
    private NextPiecePanel nextPiecePanel;

    private BufferedImage bf = ImageIO.read(new File("back2.jpg"));


    Board(JLabel statusBar, JLabel numLinesRemoed, JPanel nextpiece) throws IOException {
        setBackground(Color.LIGHT_GRAY);
        setFocusable(true);
        nextPiece1.setShape(Pieces.LineShape);
        nextPiece2 = Shape.RandomShape();
        nextPiecePanel = new NextPiecePanel(nextPiece1);
        nextPiecePanel.setPreferredSize(new Dimension(250, 250));
        nextpiece.add(nextPiecePanel);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        setBorder(border);
        timer = new Timer(1000, this); // timer for lines down
        this.statusBar = statusBar;
        this.numLinesRemoved = numLinesRemoed;
        board = new Pieces[BOARD_HEIGHT][BOARD_WIDTH];
        clearBoard();
        addKeyListener(new MyTetrisAdapter());
    }


    static void setBoardHeight(int boardHeight) {
        BOARD_HEIGHT = boardHeight;
    }

    static void setBoardWidth(int boardWidth) {
        BOARD_WIDTH = boardWidth;
    }


    private Pieces shapeAt(int x, int y) {
        return board[y][x];
    }


    private int squareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    private int squareWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }


    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                board[i][j] = Pieces.NOTHING;
            }
        }
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = currentX + currentPiece.x(i);
            int y = currentY - currentPiece.y(i);
            board[y][x] = currentPiece.getShape();
        }

        removeFullLines();

        if (!isFallingFinished) {
            newPiece();

        }
    }

    boolean isStarted() {
        return isStarted;
    }

    private void undo() {
        currentX = BOARD_WIDTH / 2;
        currentY = BOARD_HEIGHT + currentPiece.minY();
        tryMove(currentPiece, currentX, currentY);
    }

    private void  newPiece() {
        new Thread(() -> {
            try {
                Sounds.NewPieceTrack();
            } catch (FileNotFoundException | JavaLayerException e) {
                e.printStackTrace();
            }

        }).start();


        currentPiece = nextPiece1;
        currentX = BOARD_WIDTH / 2;
        currentY = BOARD_HEIGHT + currentPiece.minY()-1;
        nextPiece1= Shape.TransShape(nextPiece2);
        nextPiece2 = Shape.RandomShape();
        nextPiecePanel.Update(nextPiece1 , nextPiece2);
        undo();

        if (!tryMove(currentPiece, currentX, currentY - 1)) {
            currentPiece.setShape(Pieces.NOTHING);
            timer.stop();
            isStarted = false;
            repaint();
            new Thread(() -> {
                try {
                    Sounds.FailureTrack();
                } catch (FileNotFoundException | JavaLayerException e) {
                    e.printStackTrace();
                }

            }).start();


            try {
                ObjectMapper objectMapper = new ObjectMapper();
                FileReader fileReader = new FileReader("Scores.json");
                ArrayList<Integer> sc = objectMapper.readValue(fileReader, new TypeReference<ArrayList<Integer>>() {
                });
                sc.add(score);
                Collections.sort(sc);
                sc.remove(0);
                FileWriter fileWriter = new FileWriter("Scores.json");
                objectMapper.writeValue(fileWriter, sc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void oneLineDown() {
        if (!tryMove(currentPiece, currentX, currentY - 1))
            pieceDropped();
    }

    int getScore() {
        return score;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();

        } else {
            oneLineDown();
        }
    }

    private void griding(Graphics g) {
        Color color = Color.DARK_GRAY;
        g.setColor(color);
        for (int i = 0; i < getSize().getWidth() - 10; i += squareWidth()) {
            for (int j = (int) getSize().getHeight(); j >= 0; j -= squareHeight()) {
                g.drawRect(i, j, squareWidth(), squareHeight());
            }
        }
    }


    private void drawSquare(Graphics g, int x, int y, Pieces shape) {
        Color color = shape.color;
        g.setColor(color);
        g.fill3DRect(x + 1, y + 1, squareWidth() - 1, squareHeight() - 1, true);
    }

    private void drawGameIsPaused(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        String prompt = "Paused ";
        Font font = new Font("Serif", Font.BOLD, 50);
        graphics2D.setFont(font);
        graphics2D.setColor(Color.yellow);
        graphics2D.drawString(prompt, (int) (getSize().getWidth()) / 2-70,(int) (getSize().getHeight()) / 2);
    }
    private void drawGameOver(Graphics2D graphics2D) {
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        String prompt = "Game Over :( ";
        Font font = new Font("Serif", Font.BOLD, 50);
        graphics2D.setFont(font);
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawString(prompt, (int) (getSize().getWidth()) / 2-150,(int) (getSize().getHeight()) / 2);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.drawImage(bf, 0, 0, null);


        if (isPaused) {
            drawGameIsPaused((Graphics2D) g);
        }  else {

            Dimension size = getSize();
            int boardTop = (int) size.getHeight() - BOARD_HEIGHT * squareHeight();

            griding(g);


            //// Draw previous squares /////

            for (int i = 0; i < BOARD_HEIGHT; i++) {
                for (int j = 0; j < BOARD_WIDTH; ++j) {
                    Pieces shape = shapeAt(j, BOARD_HEIGHT - i - 1);

                    if (shape != Pieces.NOTHING) {
                        drawSquare(g, j * squareWidth(), boardTop + i * squareHeight(), shape);
                    }
                }
            }

            ////// Draw current Piece //////

            if (currentPiece.getShape() != Pieces.NOTHING) {
                for (int i = 0; i < 4; ++i) {
                    int x = currentX + currentPiece.x(i);
                    int y = currentY - currentPiece.y(i);
                    drawSquare(g, x * squareWidth(), boardTop + (BOARD_HEIGHT - y - 1) * squareHeight(), currentPiece.getShape());
                }
            }
            if (!isStarted) {
                drawGameOver((Graphics2D) g);
            }
        }
    }

    void start() {
        if (isPaused)
            return;
        setFocusable(true);
        isStarted = true;
        isFallingFinished = false;
        score = 0;
        linesRemoved=0;
        statusBar.setText("Total Score : " + score);
        numLinesRemoved.setText("Lines Removed : " + linesRemoved);
        clearBoard();
        newPiece();
        timer.start();
    }

    private void pause() {
        if (!isStarted)
            return;

        isPaused = !isPaused;

        if (isPaused) {
            timer.stop();
        } else {
            timer.start();
        }

        repaint();
    }

    private boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);

            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT)
                return false;

            if (shapeAt(x, y) != Pieces.NOTHING)
                return false;
        }

        currentPiece = newPiece;
        currentX = newX;
        currentY = newY;
        repaint();

        return true;
    }

    private void removeFullLines() {
        int numFullLines = 0;


        /// check if the line is full ////
        for (int i = BOARD_HEIGHT - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < BOARD_WIDTH; ++j) {
                if (shapeAt(j, i) == Pieces.NOTHING) {
                    lineIsFull = false;
                    break;
                }
            }


            /// remove full lines ////
            if (lineIsFull) {
                ++numFullLines;

                for (int k = i; k < BOARD_HEIGHT - 1; ++k) {
                    for (int j = 0; j < BOARD_WIDTH; ++j) {
                        board[k][j] = shapeAt(j, k + 1);
                    }
                }
            }

            if (numFullLines > 0) {
                new Thread(() -> {
                    try {
                        Sounds.LineTrack();
                    } catch (FileNotFoundException | JavaLayerException e) {
                        e.printStackTrace();
                    }

                }).start();


                score += (numFullLines * 10);
                linesRemoved += numFullLines;
                statusBar.setText("Total Score : " + score);
                numLinesRemoved.setText("Lines Removed : " + linesRemoved);
                isFallingFinished = true;
                currentPiece.setShape(Pieces.NOTHING);
                numFullLines = 0;
                repaint();
            }
        }
        score += 1;
        statusBar.setText("Total Score : " + score);
    }

    private void dropDown() {
        int newY = currentY;

        while (newY > 0) {
            if (!tryMove(currentPiece, currentX, newY - 1))
                break;
            --newY;
        }

        pieceDropped();
    }

    class MyTetrisAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent ke) {
            int keyCode = ke.getKeyCode();
            if (keyCode == KeyEvent.VK_R)
                restart();
            if ((!isStarted )|| currentPiece.getShape() == Pieces.NOTHING)
                return;


            if (keyCode == KeyEvent.VK_P)
                pause();

            if (isPaused)
                return;

            switch (keyCode) {
                case KeyEvent.VK_LEFT:
                    tryMove(currentPiece, currentX - 1, currentY);
                    break;
                case KeyEvent.VK_RIGHT:
                    tryMove(currentPiece, currentX + 1, currentY);
                    break;
                case KeyEvent.VK_U:
                    undo();
                    break;
                case KeyEvent.VK_DOWN:
                    tryMove(currentPiece.rotateRight(), currentX, currentY);
                    break;
                case KeyEvent.VK_UP:
                    tryMove(currentPiece.rotateLeft(), currentX, currentY);
                    break;
                case KeyEvent.VK_SPACE:
                    dropDown();
                    break;
                case KeyEvent.VK_D:
                    oneLineDown();
                    break;
                case KeyEvent.VK_R :
                    restart();
                    break;
            }

        }
    }

    void restart(){
        start();
    }


}