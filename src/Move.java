import java.util.*;
import java.util.concurrent.*;
public class Move {

    /**
     * The Keys defines the end squares, while the Values store which squares can move to the specified Key
     */
    public static Map<Square, CopyOnWriteArrayList<Square>> allPossibleMoves = new HashMap<>(64);

    public static final boolean[] MOVABLE_ATTACKABLE = {true, true};
    public static final boolean[] MOVABLE_UNATTACKABLE = {true, false};
    public static final boolean[] BLOCKED = {false, false};
    
    public static void initMoves() {
        for(Square[] row : Board.board) {
            for(Square col : row) {
                allPossibleMoves.put(col, new CopyOnWriteArrayList<Square>());
            }
        }

        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                Square square = Board.board[row][col];

                switch(square.getPiece().getRank()) {
                    case Piece.KING: square.setAllInteractableSpaces(kingPieceMoves(row, col));
                                     break;
                    case Piece.PAWN: square.setAllInteractableSpaces(pawnPieceMoves(row, col));
                                     break;
                    case Piece.BISHOP: square.setAllInteractableSpaces(bishopPieceMoves(row, col));
                                       break;
                    case Piece.KNIGHT: square.setAllInteractableSpaces(knightPieceMoves(row, col));
                                       break;
                    case Piece.ROOK: square.setAllInteractableSpaces(rookPieceMoves(row, col));
                                     break;
                    case Piece.QUEEN: square.setAllInteractableSpaces(queenPieceMoves(row, col));
                                      break;
                    default: break;
                }
            }
        }
    }

    /* --------------------- Piece Move Checkers ------------------------------- */

    public static HashMap<Square, boolean[]> kingPieceMoves(int row, int col) {
        HashMap<Square, boolean[]> possibleMoves = new HashMap<>();

        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(!Board.withinBoard(row + i, col + j) || (i == 0 && j == 0)) continue;

                if(Board.pieces[row][col].getColor() != Board.pieces[row + i][col + j].getColor()) {
                    //Implement check/checkmate feature later
                    if(Board.pieces[row + i][col + j].getRank() == Piece.NO_PIECE) {
                        possibleMoves.put(Board.board[row + i][col + j], MOVABLE_UNATTACKABLE);   
                    } else {
                        possibleMoves.put(Board.board[row + i][col + j], MOVABLE_ATTACKABLE);  
                    }             
                } else {
                    possibleMoves.put(Board.board[row + i][col + j], BLOCKED);
                }
            }
        }

        putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
        return possibleMoves;
    }
    
    public static HashMap<Square, boolean[]> pawnPieceMoves(int row, int col) {
        HashMap<Square, boolean[]> possibleMoves = new HashMap<>();

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
            possibleMoves.put(Board.board[row + direction * 1][col], MOVABLE_UNATTACKABLE);

            if(canDoubleMove && (Board.withinBoard(row  + direction * 2, col) && Board.pieces[row + direction * 2][col].getRank() == Piece.NO_PIECE)) {
                possibleMoves.put(Board.board[row + direction * 2][col], MOVABLE_UNATTACKABLE);
            }
        }

        //This if statement checks if an attack is possible (Checks space to the right first then space to the left)
        if(Board.withinBoard(row  + direction * 1, col + 1) && (Board.pieces[row + direction * 1][col + 1].getRank() != Piece.NO_PIECE && Board.pieces[row + direction * 1][col + 1].getColor() != Board.pieces[row][col].getColor())) {
            possibleMoves.put(Board.board[row + direction * 1][col + 1], MOVABLE_ATTACKABLE);
        } else {
            possibleMoves.put(Board.board[row + direction * 1][col + 1], BLOCKED);
        }
        if(Board.withinBoard(row  + direction * 1, col - 1) && (Board.pieces[row + direction * 1][col - 1].getRank() != Piece.NO_PIECE && Board.pieces[row + direction * 1][col - 1].getColor() != Board.pieces[row][col].getColor())) {
            possibleMoves.put(Board.board[row + direction * 1][col - 1], MOVABLE_ATTACKABLE);
        } else {
            possibleMoves.put(Board.board[row + direction * 1][col + 1], BLOCKED);
        }

        putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
        return possibleMoves;
    }

    public static HashMap<Square, boolean[]> bishopPieceMoves(int row, int col) {
        HashMap<Square, boolean[]> possibleMoves = new HashMap<>();

        for(int i = -1; i <= 1; i += 2) { //Checks up diagonals first, then down diagonals
            for(int j = -1; j <= 1; j += 2) { //Checks left then right
                int increment = 1;
                while(Board.withinBoard(row + i * increment, col + j * increment) &&
                      Board.pieces[row + i * increment][col + j * increment].getColor() != Board.pieces[row][col].getColor()) {
                        
                    possibleMoves.put(Board.board[row + i * increment][col + j * increment], MOVABLE_UNATTACKABLE);

                    if(Board.pieces[row + i * increment][col + j * increment].getColor() != Piece.NO_COLOR) {
                        possibleMoves.replace(Board.board[row + i * increment][col + j * increment], MOVABLE_ATTACKABLE);
                        break;
                    }

                    increment++;
                }

                if(Board.pieces[row + i * increment][col + j * increment].getColor() == Board.pieces[row][col].getColor()) {
                    possibleMoves.put(Board.board[row + i * increment][col + j * increment], BLOCKED);
                }
            }
        }

        putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
        return possibleMoves;
    }

    public static HashMap<Square, boolean[]> knightPieceMoves(int row, int col) {
        HashMap<Square, boolean[]> possibleMoves = new HashMap<>();
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
                            if(Board.withinBoard(row + displacement, col + direction)) { //Checks for space below piece
                                if(Board.pieces[row + displacement][col + direction].getColor() == Board.pieces[row][col].getColor()) {
                                    possibleMoves.put(Board.board[row + displacement][col + direction], BLOCKED);
                                } else if(Board.pieces[row + displacement][col + direction].getColor() != Piece.NO_COLOR) {
                                    possibleMoves.put(Board.board[row + displacement][col + direction], MOVABLE_ATTACKABLE);
                                } else {
                                    possibleMoves.put(Board.board[row + displacement][col + direction], MOVABLE_UNATTACKABLE);
                                }
                            }
                        }
                    }
                } else {
                    if(Board.withinBoard(row, col + displacement)) {
                        for(int direction = -1; direction <= 1; direction += 2) {
                            if(Board.withinBoard(row + direction, col + displacement)) { //Checks for space below piece
                                if(Board.pieces[row + direction][col + displacement].getColor() != Board.pieces[row][col].getColor()) {
                                    possibleMoves.put(Board.board[row + direction][col + displacement], BLOCKED);
                                } else if(Board.pieces[row + direction][col + displacement].getColor() != Piece.NO_COLOR) {
                                    possibleMoves.put(Board.board[row + direction][col + displacement], MOVABLE_ATTACKABLE);
                                } else {
                                    possibleMoves.put(Board.board[row + direction][col + displacement], MOVABLE_UNATTACKABLE);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
        return possibleMoves;
    }

    public static HashMap<Square, boolean[]> rookPieceMoves(int row, int col) {
        HashMap<Square, boolean[]> possibleMoves = new HashMap<>();

        for(int i = -1; i <= 1; i += 2) { //Checks up first, then down
            int increment = 1;
            while(Board.withinBoard(row + i * increment, col) &&
                  Board.pieces[row + i * increment][col].getColor() != Board.pieces[row][col].getColor()) {
                
                possibleMoves.put(Board.board[row + i * increment][col], MOVABLE_UNATTACKABLE);

                if(Board.pieces[row + i * increment][col].getColor() != Piece.NO_COLOR) {
                    possibleMoves.replace(Board.board[row + i * increment][col], MOVABLE_UNATTACKABLE);
                    break;
                }

                increment++;
            }

            if(Board.pieces[row + i * increment][col].getColor() == Board.pieces[row][col].getColor()) {
                possibleMoves.put(Board.board[row + i * increment][col], BLOCKED);
            }

        }
        
        for(int j = -1; j <= 1; j += 2) { //Checks left then right
            int increment = 1;

            while(Board.withinBoard(row, col + j * increment) &&
                  Board.pieces[row][col + j * increment].getColor() != Board.pieces[row][col].getColor()) {
                
                possibleMoves.put(Board.board[row][col + j * increment], MOVABLE_UNATTACKABLE);

                if(Board.pieces[row][col + j * increment].getColor() != Piece.NO_COLOR) {
                    possibleMoves.replace(Board.board[row][col + j * increment], MOVABLE_ATTACKABLE);
                    break;
                }

                increment++;
            }

            if(Board.pieces[row][col + j * increment].getColor() == Board.pieces[row][col].getColor()) {
                possibleMoves.put(Board.board[row][col + j * increment], BLOCKED);
            }
        }

        putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
        return possibleMoves;
    }

    public static HashMap<Square, boolean[]> queenPieceMoves(int row, int col) {
        HashMap<Square, boolean[]> possibleMoves = new HashMap<>();

        //A queen is basically a rook + bishop
        possibleMoves.putAll(bishopPieceMoves(row, col));
        possibleMoves.putAll(rookPieceMoves(row, col));

        putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
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
        selectedSquare.removeAllInteractableSpaces();
    }

    public static void updatePossibleMoves(Square targetSquare) {
        for(Square square : allPossibleMoves.get(targetSquare)) {
            if(square.getPiece().getColor() == targetSquare.getPiece().getColor()) {
                allPossibleMoves.get(targetSquare).remove(square);
            } else {
                if(square.getPiece().getColor() != Piece.NO_COLOR) {
                    square.setMovableSpace(targetSquare, true);
                } else {
                    square.setMovableSpace(targetSquare, false);
                }
            }
        }

        int row = targetSquare.getRow(), col = targetSquare.getCol();

        switch(targetSquare.getPiece().getRank()) {
            case Piece.KING: targetSquare.setAllInteractableSpaces(kingPieceMoves(row, col));
                             break;
            case Piece.PAWN: targetSquare.setAllInteractableSpaces(pawnPieceMoves(row, col));
                             break;
            case Piece.BISHOP: targetSquare.setAllInteractableSpaces(bishopPieceMoves(row, col));
                               break;
            case Piece.KNIGHT: targetSquare.setAllInteractableSpaces(knightPieceMoves(row, col));
                               break;
            case Piece.ROOK: targetSquare.setAllInteractableSpaces(rookPieceMoves(row, col));
                             break;
            case Piece.QUEEN: targetSquare.setAllInteractableSpaces(queenPieceMoves(row, col));
                              break;
            default: break;
        }
    }
}
