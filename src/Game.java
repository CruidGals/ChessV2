/**
 * NOTE: In this game, ranks are numbered starting from 0 at the top to 7 at the bottom
 */

import javax.swing.*;

public class Game {

    public static final String STARTING_FEN_RECORD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static String CURRENT_FEN_RECORD = STARTING_FEN_RECORD;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Board();
            }
        });
    }

    
}