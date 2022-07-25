import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;

public class Square extends JLabel {
    public static final Color LIGHT_SQUARE_COLOR = new Color(255,255,255);
    public static final Color DARK_SQUARE_COLOR = new Color(118,150,86);

    /**
     * HashMap that stores the squares that it can move or it is blocked by another piece. First boolean value depicts whether it is blocked or not,
     * second boolean value depicts whether its an attackable square.
     */
    private HashMap<Square, boolean[]> interactableSpaces = new HashMap<Square, boolean[]>();

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

    /*--------------------- Set Methods ----------------------- */

    public void setMovableSpace(Square key, Boolean value) {
        if(!interactableSpaces.containsKey(key)) return;

        boolean[] temp = {true, value};
        interactableSpaces.replace(key, temp);
    }

    public void setBlockedSpace(Square key) {
        if(!interactableSpaces.containsKey(key)) return;

        boolean[] temp = {false, false};
        interactableSpaces.replace(key, temp);
    }

    public void setAllInteractableSpaces(HashMap<Square, boolean[]> list) {
        interactableSpaces = list;
    }

    /*--------------------- Remove Methods ----------------------- */

    public void removeInteractableSpace(Square space) {
        if(!interactableSpaces.containsKey(space)) return;
        interactableSpaces.remove(space);
    }

    public void removeAllInteractableSpaces() {
        interactableSpaces.clear();
    }

    /*--------------------- Get Methods ----------------------- */

    public HashMap<Square, Boolean> getMovableSpaces() {
        HashMap<Square, Boolean> allMovableSpaces = new HashMap<Square, Boolean>();
        Square[] allSquares = (Square[]) interactableSpaces.keySet().toArray();
        
        for(int i = 0; i < allSquares.length; i++) {
            Square currentSquare = allSquares[i];

            if(interactableSpaces.get(currentSquare)[0]) {
                allMovableSpaces.put(currentSquare, interactableSpaces.get(currentSquare)[1]);
            }
        }

        return allMovableSpaces;
    }

    public ArrayList<Square> getBlockedSpaces() {
        ArrayList<Square> allBlockedSpaces = new ArrayList<Square>();
        Square[] allSquares = (Square[]) interactableSpaces.keySet().toArray();
        
        for(int i = 0; i < allSquares.length; i++) {
            Square currentSquare = allSquares[i];

            if(!interactableSpaces.get(currentSquare)[0]) {
                allBlockedSpaces.add(currentSquare);
            }
        }

        return allBlockedSpaces;
    }

    public HashMap<Square, boolean[]> getAllInteractableSpaces() {
        return interactableSpaces;
    }
    
    /*--------------------- Other Get Methods ----------------------- */

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
