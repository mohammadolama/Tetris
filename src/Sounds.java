import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class Sounds {
    private static final File music = new File("Sounds\\music.mp3");
    private static final File line = new File("Sounds\\Line.mp3");
    private static final File failure = new File("Sounds\\Failure.mp3");
    private static final File newpiece = new File("Sounds\\NewPiece.mp3");


    static void MainTrack() throws FileNotFoundException, JavaLayerException {
        FileInputStream fs = new FileInputStream(music);
        BufferedInputStream bi = new BufferedInputStream(fs);
        Player player = new Player(bi);
        player.play();
    }
    static void LineTrack() throws FileNotFoundException, JavaLayerException {
        FileInputStream fs = new FileInputStream(line);
        BufferedInputStream bi = new BufferedInputStream(fs);
        Player player = new Player(bi);
        player.play();
    }
    static void FailureTrack() throws FileNotFoundException, JavaLayerException {
        FileInputStream fs = new FileInputStream(failure);
        BufferedInputStream bi = new BufferedInputStream(fs);
        Player player = new Player(bi);
        player.play();
    }
    static void NewPieceTrack() throws FileNotFoundException, JavaLayerException {
        FileInputStream fs = new FileInputStream(newpiece);
        BufferedInputStream bi = new BufferedInputStream(fs);
        Player player = new Player(bi);
        player.play();
    }

}
