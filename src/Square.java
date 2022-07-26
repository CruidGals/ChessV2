import java.awt.*;
import java.util.HashMap;

import javax.swing.*;

public class Square extends JLabel {
    public static final Color LIGHT_SQUARE_COLOR = new Color(255,255,255);
    public static final Color DARK_SQUARE_COLOR = new Color(118,150,86);
    public static final Color LIGHT_MOVABLE_SQUARE_COLOR = new Color(181, 181, 181);
    public static final Color DARK_MOVABLE_SQUARE_COLOR = new Color(132, 140, 125);
    public static final Color SELECTED_SQUARE_COLOR = new Color(226, 237, 66);
    public static final Color ATTACKABLE_SQUARE_COLOR = new Color(255, 99, 99);

    /**
     * HashMap that stores the squares that it can move along if it is an Attackable Square
     */
    private HashMap<Square, Boolean> movableSpaces = new HashMap<Square, Boolean>();

    private Color squareColor;
    private String boardCode;
    private int row;
    private int col;
    private boolean showingMoveOptions = false;

    public Square(Color squareColor, int color, int rank, int row, int col, String boardCode) {
        this.squareColor = squareColor;
        this.boardCode = boardCode;
        this.row = row;
        this.col = col;

        setBackground(squareColor);
        setOpaque(true);
        setPreferredSize(new Dimension(100,100));

        setLayout(new GridBagLayout());
        add(new Piece(color, rank));

    }

    public void toggleMoveOptions() {
        if(showingMoveOptions) {
            setBackground(squareColor);

            for(Square square : movableSpaces.keySet()) {
                square.setBackground(square.getSquareColor());
            }

            showingMoveOptions = false;
        } else {
            setBackground(SELECTED_SQUARE_COLOR);

            for(Square square : movableSpaces.keySet()) {
                if(movableSpaces.get(square)) {
                    square.setBackground(ATTACKABLE_SQUARE_COLOR);
                } else {
                    if(square.getSquareColor() == LIGHT_SQUARE_COLOR) {
                        square.setBackground(LIGHT_MOVABLE_SQUARE_COLOR);
                    } else {
                        square.setBackground(DARK_MOVABLE_SQUARE_COLOR);
                    }
                }
            }

            showingMoveOptions = true;
        }
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

    public HashMap<Square, Boolean> getMovableSpaces() {
        return movableSpaces;
    }

    public Color getSquareColor() {
        return squareColor;
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