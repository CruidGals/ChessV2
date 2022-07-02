import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

public class Square extends JLabel {
    public static final Color LIGHT_SQUARE_COLOR = new Color(255,255,255);
    public static final Color DARK_SQUARE_COLOR = new Color(118,150,86);

    private ArrayList<Square> movableSpaces = new ArrayList<Square>();

    private String boardCode;

    public Square(Color squareColor, int color, int rank, String boardCode) {
        this.boardCode = boardCode;

        setBackground(squareColor);
        setOpaque(true);
        setPreferredSize(new Dimension(100,100));

        setLayout(new GridBagLayout());
        add(new Piece(color, rank));

    }

    public void setMovableSpaces(ArrayList<Square> list) {
        movableSpaces = list;
    }

    public ArrayList<Square> getMovableSquares() {
        return movableSpaces;
    }

    public Piece getPiece() {
        return (Piece) getComponent(0);
    }

    public String toString() {
        return boardCode;
    }
}
