/**
 * NOTE: In this game, ranks are numbered starting from 0 at the top to 7 at the bottom
 */

import javax.swing.*;

public class Game {

    public static final String STARTING_FEN_RECORD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static String CURRENT_FEN_RECORD = STARTING_FEN_RECORD;

    /**
     * Specifies who's turn is it. The constants from Piece.java determine it.
     */
    public static int turn = Piece.WHITE;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Board();
            }
        });
    }

    public static void switchTurn() {
        if(turn == Piece.WHITE) {
            turn = Piece.BLACK;
        } else {
            turn = Piece.WHITE;
        }
    }
    
}