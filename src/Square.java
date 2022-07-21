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
    private int row;
    private int col;

    public Square(Color squareColor, int color, int rank, int row, int col, String boardCode) {
        this.boardCode = boardCode;
        this.row = row;
        this.col = col;

        setBackground(squareColor);
        setOpaque(true);
        setPreferredSize(new Dimension(100,100));

        setLayout(new GridBagLayout());
        add(new Piece(color, rank));

    }

    public void setMovableSpace(Square key, Boolean value) {
        if(!movableSpaces.containsKey(key)) return;
        movableSpaces.replace(key, value);
    }

    public void setAllMovableSpaces(HashMap<Square, Boolean> list) {
        movableSpaces = list;
    }

    public void removeMovableSpace(Square space) {
        if(!movableSpaces.containsKey(space)) return;
        movableSpaces.remove(space);
    }

    public void removeAllMovableSpaces() {
        movableSpaces.clear();
    }

    public HashMap<Square, Boolean> getMovableSquares() {
        return movableSpaces;
    }

    public Piece getPiece() {
        return (Piece) getComponent(0);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String toString() {
        return boardCode;
    }
}
