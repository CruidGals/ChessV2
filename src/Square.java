import java.awt.*;
import java.util.HashMap;

import javax.swing.*;

public class Square extends JLabel {
    public static final Color LIGHT_SQUARE_COLOR = new Color(255,255,255);
    public static final Color DARK_SQUARE_COLOR = new Color(118,150,86);

    /**
     * HashMap that stores the squares that it can move along if it is an Attackable Square
     */
    private HashMap<Square, Boolean> movableSpaces = new HashMap<Square, Boolean>();

    private String boardCode;

    public Square(Color squareColor, int color, int rank, String boardCode) {
        this.boardCode = boardCode;

        setBackground(squareColor);
        setOpaque(true);
        setPreferredSize(new Dimension(100,100));

        setLayout(new GridBagLayout());
        add(new Piece(color, rank));

    }

    public void setMovableSpaces(HashMap<Square, Boolean> list) {
        movableSpaces = list;
    }

    public HashMap<Square, Boolean> getMovableSquares() {
        return movableSpaces;
    }

    public Piece getPiece() {
        return (Piece) getComponent(0);
    }

    public String toString() {
        return boardCode;
    }
}
