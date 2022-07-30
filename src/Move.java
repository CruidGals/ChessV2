import java.util.*;
import java.util.concurrent.*;
public class Move {

    static int test = 0;
    /**
     * The Keys defines the end squares, while the Values store which squares can move to the specified Key
     */
    public static Map<Square, CopyOnWriteArrayList<Square>> allPossibleMoves = new HashMap<Square, CopyOnWriteArrayList<Square>>(64);
    
    public static void initMoves() {
        for(Square[] row : Board.board) {
            for(Square col : row) {
                allPossibleMoves.put(col, new CopyOnWriteArrayList<Square>());
            }
        }

        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                setMovableSpacesFromRank(Board.board[row][col]);
            }
        }
    }

    /* --------------------- Piece Move Checkers ------------------------------- */

    public static HashMap<Square, Boolean> kingPieceMoves(int row, int col) {
        HashMap<Square, Boolean> possibleMoves = new HashMap<Square, Boolean>();

        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if((!Board.withinBoard(row + i, col + j) || (i == 0 && j == 0)) || Board.pieces[row + i][col + j].getColor() == Board.pieces[row][col].getColor()) continue;

                if(Board.pieces[row + i][col + j].getColor() == Piece.NO_COLOR) {
                    possibleMoves.put(Board.board[row + i][col + j], false);
                } else {
                    possibleMoves.put(Board.board[row + i][col + j], true);
                }

            }
        }

        if(Board.pieces[row][col].getRank() != Piece.NO_PIECE) putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
        return possibleMoves;
    }
    
    public static HashMap<Square, Boolean> pawnPieceMoves(int row, int col) {
        HashMap<Square, Boolean> possibleMoves = new HashMap<Square, Boolean>();

        boolean canDoubleMove;

        if((Board.pieces[row][col].getColor() == Piece.WHITE && row == 6) || (Board.pieces[row][col].getColor() == Piece.BLACK && row == 1)) {
            canDoubleMove = true;
        } else {
            canDoubleMove = false;
        }

        //Indicates the direction the pawn is going on the board (negative is going up, positive is going down)
        int direction = Board.pieces[row][col].getColor() == Piece.WHITE ? -1 : 1;

        //This if statement checks if a regular pawn move is possible
        if(Board.withinBoard(row  + direction * 1, col) && Board.pieces[row + direction * 1][col].getRank() == Piece.NO_PIECE) {
            possibleMoves.put(Board.board[row + direction * 1][col], false);

            if(canDoubleMove && (Board.withinBoard(row  + direction * 2, col) && Board.pieces[row + direction * 2][col].getRank() == Piece.NO_PIECE)) {
                possibleMoves.put(Board.board[row + direction * 2][col], false);
            }
        }

        //This if statement checks if an attack is possible (Checks space to the right first then space to the left)
        if(Board.withinBoard(row  + direction * 1, col + 1) && (Board.pieces[row + direction * 1][col + 1].getRank() != Piece.NO_PIECE && Board.pieces[row + direction * 1][col + 1].getColor() != Board.pieces[row][col].getColor())) {
            possibleMoves.put(Board.board[row + direction * 1][col + 1], true);
        }
        if(Board.withinBoard(row  + direction * 1, col - 1) && (Board.pieces[row + direction * 1][col - 1].getRank() != Piece.NO_PIECE && Board.pieces[row + direction * 1][col - 1].getColor() != Board.pieces[row][col].getColor())) {
            possibleMoves.put(Board.board[row + direction * 1][col - 1], true);
        }

        if(Board.pieces[row][col].getRank() != Piece.NO_PIECE) putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
        return possibleMoves;
    }

    public static HashMap<Square, Boolean> bishopPieceMoves(int row, int col) {
        HashMap<Square, Boolean> possibleMoves = new HashMap<Square, Boolean>();

        for(int i = -1; i <= 1; i += 2) { //Checks up diagonals first, then down diagonals
            for(int j = -1; j <= 1; j += 2) { //Checks left then right
                int increment = 1;
                while(Board.withinBoard(row + i * increment, col + j * increment) &&
                      Board.pieces[row + i * increment][col + j * increment].getColor() != Board.pieces[row][col].getColor()) {
                        
                    possibleMoves.put(Board.board[row + i * increment][col + j * increment], false);

                    if(Board.pieces[row + i * increment][col + j * increment].getColor() != Piece.NO_COLOR) {
                        possibleMoves.replace(Board.board[row + i * increment][col + j * increment], true);
                        break;
                    }

                    increment++;
                }
            }
        }

        if(Board.pieces[row][col].getRank() != Piece.NO_PIECE) putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
        return possibleMoves;
    }

    public static HashMap<Square, Boolean> knightPieceMoves(int row, int col) {
        HashMap<Square, Boolean> possibleMoves = new HashMap<Square, Boolean>();
        System.out.println(row + " " + col);

        /* Difficult to understand
         * - The first for loop is used as a switch to go from checking spaces horizontally from the knight
         *   to checking spaces vertically from the knight
         * - The second for loop is used solely to set the number of spaces the target squares will be
         *   (A knight always moves 2 spaces in one direction and one space in another direction. The 2 spaces in one direction
         *    is set by using the 2nd for loop)
         *   This for loop decides the specfic direction it will check (up or down / left or right)
         * - Later on in the method, it will check one space on the opposite axis of direction (remember a knight moves
         *   2 spaces in one direction and one space in another direction. This part determines the "one space in another direction")
         */

        for(int i = 0; i <= 1; i++) {
            for(int displacement = -2; displacement <= 2; displacement += 4) {
                if(i == 0) {
                    if(Board.withinBoard(row + displacement, col)) {
                        for(int direction = -1; direction <= 1; direction += 2) {
                            if(Board.withinBoard(row + displacement, col + direction) && Board.pieces[row + displacement][col + direction].getColor() != Board.pieces[row][col].getColor()) { //Checks for space below piece
                                if(Board.pieces[row + displacement][col + direction].getColor() != Piece.NO_COLOR) {
                                    possibleMoves.put(Board.board[row + displacement][col + direction], true);
                                } else {
                                    possibleMoves.put(Board.board[row + displacement][col + direction], false);
                                }
                            }
                        }
                    }
                } else {
                    if(Board.withinBoard(row, col + displacement)) {
                        for(int direction = -1; direction <= 1; direction += 2) {
                            if(Board.withinBoard(row + direction, col + displacement) && Board.pieces[row + direction][col + displacement].getColor() != Board.pieces[row][col].getColor()) { //Checks for space below piece
                                if(Board.pieces[row + direction][col + displacement].getColor() != Piece.NO_COLOR) {
                                    possibleMoves.put(Board.board[row + direction][col + displacement], true);
                                } else {
                                    possibleMoves.put(Board.board[row + direction][col + displacement], false);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if(Board.pieces[row][col].getRank() != Piece.NO_PIECE) putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
        return possibleMoves;
    }

    public static HashMap<Square, Boolean> rookPieceMoves(int row, int col) {
        HashMap<Square, Boolean> possibleMoves = new HashMap<Square, Boolean>();

        for(int i = -1; i <= 1; i += 2) { //Checks up first, then down
            int increment = 1;
            while(Board.withinBoard(row + i * increment, col) &&
                  Board.pieces[row + i * increment][col].getColor() != Board.pieces[row][col].getColor()) {
                
                possibleMoves.put(Board.board[row + i * increment][col], false);

                if(Board.pieces[row + i * increment][col].getColor() != Piece.NO_COLOR) {
                    possibleMoves.replace(Board.board[row + i * increment][col], true);
                    break;
                }

                increment++;
            }
        }
        for(int j = -1; j <= 1; j += 2) { //Checks left then right
            int increment = 1;

            while(Board.withinBoard(row, col + j * increment) &&
                  Board.pieces[row][col + j * increment].getColor() != Board.pieces[row][col].getColor()) {
                
                possibleMoves.put(Board.board[row][col + j * increment], false);

                if(Board.pieces[row][col + j * increment].getColor() != Piece.NO_COLOR) {
                    possibleMoves.replace(Board.board[row][col + j * increment], true);
                    break;
                }

                increment++;
            }
        }

        if(Board.pieces[row][col].getRank() != Piece.NO_PIECE) putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
        return possibleMoves;
    }

    public static HashMap<Square, Boolean> queenPieceMoves(int row, int col) {
        HashMap<Square, Boolean> possibleMoves = new HashMap<Square, Boolean>();

        //A queen is basically a rook + bishop
        possibleMoves.putAll(bishopPieceMoves(row, col));
        possibleMoves.putAll(rookPieceMoves(row, col));

        return possibleMoves;
    }

    /* -------------------- Other helper methods ------------------------------- */

    private static void putMovesOntoDirectory(Set<Square> keys, Square selectedSquare) {
        for(Square targetSquare : keys) {
            allPossibleMoves.get(targetSquare).add(selectedSquare);
        }
    }

    public static void removeMovesFromSquare(Square selectedSquare) {
        for(Square square : selectedSquare.getMovableSpaces().keySet()) {
            allPossibleMoves.get(square).remove(selectedSquare);
        }
        selectedSquare.removeAllMovableSpaces();
    }

    public static void updatePossibleMoves(Square selectedSquare, Square targetSquare) {
        
        /*-------------------- This part affects the targetedSquare ---------------------------- */
        for(Square square : allPossibleMoves.get(targetSquare)) {
            removeMovesFromSquare(square);
            setMovableSpacesFromRank(square);
        }

        for(int row = targetSquare.getRow() - 1; row <= targetSquare.getRow() + 1; row += 2) {
            for(int col = targetSquare.getCol() - 1; col <= targetSquare.getCol() + 1; col += 2) {
                if(Board.withinBoard(row, col) && (Board.pieces[row][col].getRank() == Piece.PAWN)) {
                    removeMovesFromSquare(Board.board[row][col]);
                    setMovableSpacesFromRank(Board.board[row][col]);
                }
            }
        }
        
        setMovableSpacesFromRank(targetSquare);

        /*-------------------- This part affects the selectedSquare ---------------------------- */
        int row = selectedSquare.getRow();
        int col = selectedSquare.getCol();

        for(Square square : knightPieceMoves(row, col).keySet()) {
            if(square.getPiece().getRank() != Piece.KNIGHT) continue;

            removeMovesFromSquare(square);
            square.setAllMovableSpaces(knightPieceMoves(square.getRow(), square.getCol()));
        }

        for(Square square : queenPieceMoves(row, col).keySet()) {
            removeMovesFromSquare(square);
            setMovableSpacesFromRank(square);
        }
    }

    private static void setMovableSpacesFromRank(Square square) {
        switch(square.getPiece().getRank()) {
            case Piece.KING: square.setAllMovableSpaces(kingPieceMoves(square.getRow(), square.getCol()));
                             break;
            case Piece.PAWN: square.setAllMovableSpaces(pawnPieceMoves(square.getRow(), square.getCol()));
                             break;
            case Piece.BISHOP: square.setAllMovableSpaces(bishopPieceMoves(square.getRow(), square.getCol()));
                               break;
            case Piece.KNIGHT: square.setAllMovableSpaces(knightPieceMoves(square.getRow(), square.getCol()));
                               break;
            case Piece.ROOK: square.setAllMovableSpaces(rookPieceMoves(square.getRow(), square.getCol()));
                             break;
            case Piece.QUEEN: square.setAllMovableSpaces(queenPieceMoves(square.getRow(), square.getCol()));
                              break;
            default: break;
        }
    }
}