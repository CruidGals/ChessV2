import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
public class Piece extends JLabel {
    //Constants
    public static final int NO_PIECE = 0;
    public static final int KING = 1;
    public static final int PAWN = 2;
    public static final int BISHOP = 3;
    public static final int KNIGHT = 4;
    public static final int ROOK = 5;
    public static final int QUEEN = 6;

    public static final int WHITE = 8;
    public static final int BLACK = 16;
    public static final int NO_COLOR = 24;

    //Instance vars
    private int rank;
    private int color;

    public Piece(int color, int rank) {
        this.color = color;
        this.rank = rank;
        setOpaque(false);
        setPreferredSize(new Dimension(100,100));

        updatePieceUI();
    }

    /*------------------- Getter and Setter Methods --------------------- */
    public int getRank() {
        return rank;
    }

    public int getColor() {
        return color;
    }
    /*------------------------------------------------------------------- */

    public void updatePieceUI() {
        String rankName = "", colorName = "";

        if(rank == NO_PIECE) return;

        switch(rank) {
            case KING: rankName = "King";
                    break;
            case PAWN: rankName = "Pawn";
                    break;
            case BISHOP: rankName = "Bishop";
                    break;
            case KNIGHT: rankName = "Knight";
                    break;
            case ROOK: rankName = "Rook";
                    break;
            case QUEEN: rankName = "Queen";
                    break;
        }

        if(color == WHITE) {
            colorName = "White";
        } else if (color == BLACK) {
            colorName = "Black";
        }
        
        ImageIcon pieceIcon = new ImageIcon(getClass().getResource("resources/" + colorName + rankName + ".png")); // load the image to a imageIcon
        Image image = pieceIcon.getImage(); // transform it 
        Image newimg = image.getScaledInstance(100, 100,  Image.SCALE_SMOOTH); // scale it the smooth way  
        pieceIcon = new ImageIcon(newimg);  // transform it back

        setIcon(pieceIcon);
        revalidate();
    }

    public String toString() {
        String str = "";
        if(rank == 0) return "";
        else if(rank == KING) str = "K";
        else if(rank == PAWN) str = "P";
        else if(rank == BISHOP) str = "B";
        else if(rank == KNIGHT) str = "N";
        else if(rank == ROOK) str = "R";
        else if(rank == QUEEN) str = "Q";

        if(color == BLACK) str = str.toLowerCase();

        return str;
    }
}
