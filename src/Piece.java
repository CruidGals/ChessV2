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

    //Instance vars
    private int rank;
    private int color;

    public Piece(int color, int rank) {
        this.color = color;
        this.rank = rank;

        updatePieceUI();

        addMouseListener(new MouseClickListener());
    }

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

    public void setPiece(int rank) {
        this.rank = rank;
        updatePieceUI();
    }

    public void setPiece(int color, int rank) {
        this.rank = rank;
        this.color = color;
        updatePieceUI();
    }

    private class MouseClickListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            if(e.getButton() == MouseEvent.BUTTON3) {
                String tempRank = JOptionPane.showInputDialog(null, "Enter rank of this piece. (0 = None, 1 = King, 2 = Pawn, 3 = Bishop, 4 = Knight, 5 = Rook, 6 = Queen)");
                String tempColor = JOptionPane.showInputDialog(null, "Enter color of this piece. (8 = White, 16 = Black) ");

                if(tempRank == null || tempColor == null) return;

                setPiece(Integer.parseInt(tempColor), Integer.parseInt(tempRank));
            }
        }
    }
}
