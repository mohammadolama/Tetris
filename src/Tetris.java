import javazoom.jl.decoder.JavaLayerException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class Tetris extends JFrame   {

    private static JFrame jFrame1 = new JFrame();


    private Tetris() throws IOException {
        Font font = new Font("Serif", Font.PLAIN, 18);
        setSize(600, 800);

        JPanel j1 = new JPanel();
        j1.setLayout(new BorderLayout());

        /////////////////////////////////////////////
        JPanel j21 = new JPanel();
        JLabel statusBar = new JLabel("Total Score : 0", JLabel.CENTER);
        JLabel numLinesRemoved = new JLabel("Lines removed : 0", JLabel.CENTER);


        j21.setLayout(new BoxLayout(j21, BoxLayout.LINE_AXIS));
        GridLayout gridLayout=new GridLayout(4,1);
        j21.setLayout(gridLayout);
        statusBar.setFont(font);
        numLinesRemoved.setFont(font);
        j21.setPreferredSize(new Dimension(250, 150));
        j21.setBackground(Color.pink);
        j1.setSize(500, 500);
        j1.add(j21, BorderLayout.NORTH);

        //////////////////////////////////////////////
        JPanel j22 = new JPanel();
        j22.setPreferredSize(new Dimension(250, 250));
        j22.setBackground(Color.LIGHT_GRAY);
        j1.add(j22, BorderLayout.SOUTH);

        ///////////////////////////////////
        JPanel j23 = new JPanel();
        j23.setLayout(new FlowLayout());
        j23.setBackground(Color.ORANGE);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FileReader fileReader = new FileReader("Scores.json");
            ArrayList sc = objectMapper.readValue(fileReader, new TypeReference<ArrayList>() {
            });
            String[][] data = new String[15][2];
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 2; j++) {
                    if (j == 0) {
                        data[i][j] = (i+1) + "";
                    } else {
                        data[i][j] = sc.get(14 - i) + "";
                    }
                }
            }
            String[] Column = {"RANK", "Score"};

            JTable jTable = new JTable(data, Column);
            jTable.setFont(new Font("Serif" , Font.BOLD, 17));
            jTable.setRowHeight(18);
            jTable.setFocusable(false);
            jTable.setPreferredSize(new Dimension(250,300));
            JScrollPane jScrollPane = new JScrollPane(jTable);
            jScrollPane.setWheelScrollingEnabled(false);
            jScrollPane.setPreferredSize(new Dimension(250,310));
            j23.add(jScrollPane);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ////////////////////////////////////////////

        j1.add(j23, BorderLayout.CENTER);


        add(j1, BorderLayout.EAST);
        setResizable(false);
//
        Board board = new Board(statusBar, numLinesRemoved, j22);
        add(board, BorderLayout.CENTER);
        System.out.println(board.getPreferredSize());
        JButton exit=new JButton("EXIT");
        exit.setFocusable(false);
        exit.addActionListener(e -> {
            try{
                if (board.isStarted()){
                ObjectMapper objectMapper = new ObjectMapper();
                FileReader fileReader = new FileReader("Scores.json");
                ArrayList<Integer> sc = objectMapper.readValue(fileReader, new TypeReference<ArrayList<Integer>>() {
                });
                sc.add(board.getScore());
                Collections.sort(sc);
                sc.remove(0);
                FileWriter fileWriter = new FileWriter("Scores.json");
                objectMapper.writeValue(fileWriter, sc);}
                jFrame1.dispose();
                System.exit(0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }


        });
        JButton restart=new JButton("Restart");
        restart.setFocusable(false);
        restart.addActionListener(e -> board.restart());
        j21.add(exit);
        j21.add(restart);
        j21.add(statusBar);
        j21.add(numLinesRemoved);


        board.start();


//

        setTitle("My Tetris");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private static void setSize()   {
        jFrame1.setLayout(new BorderLayout());
        jFrame1.setLocationRelativeTo(null);
        jFrame1.setSize(400, 200);
        JLabel jLabel = new JLabel("Choose the size of the game", JLabel.CENTER);
        JPanel jPanel = new JPanel();
        JButton j1 = new JButton("Small");
        JButton j2 = new JButton("Meduim");
        JButton j3 = new JButton("Large");
        j1.addActionListener(e -> {
            Board.setBoardWidth(10);
            Board.setBoardHeight(22);
            jFrame1.dispose();
            Tetris myTetris = null;
            try {
                myTetris = new Tetris();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            assert myTetris != null;
            myTetris.setLocationRelativeTo(null);
            myTetris.setVisible(true);
        });
        j2.addActionListener(e -> {
            Board.setBoardWidth(14);
            Board.setBoardHeight(30);
            jFrame1.dispose();
            Tetris myTetris = null;
            try {
                myTetris = new Tetris();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            assert myTetris != null;
            myTetris.setLocationRelativeTo(null);
            myTetris.setVisible(true);
        });
        j3.addActionListener(e -> {
            Board.setBoardWidth(20);
            Board.setBoardHeight(44);
            jFrame1.dispose();
            Tetris myTetris = null;
            try {
                myTetris = new Tetris();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            assert myTetris != null;
            myTetris.setLocationRelativeTo(null);
            myTetris.setVisible(true);
        });
        jPanel.setLayout(new FlowLayout());
        jPanel.add(j1);
        jPanel.add(j2);
        jPanel.add(j3);
        jFrame1.add(jLabel, BorderLayout.NORTH);
        jFrame1.add(jPanel);
        jFrame1.setVisible(true);
        jFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    public static void main(String[] args) {
        setSize();
        new Thread(()->{
            try {
                while (true) {
                    Sounds.MainTrack();
                }
            }catch (FileNotFoundException | JavaLayerException e) {
                e.printStackTrace();
            }
        }).start();

    }



}