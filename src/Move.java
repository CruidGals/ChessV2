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
                Square square = Board.board[row][col];

                switch(square.getPiece().getRank()) {
                    case Piece.KING: square.setAllMovableSpaces(kingPieceMoves(row, col));
                                     break;
                    case Piece.PAWN: square.setAllMovableSpaces(pawnPieceMoves(row, col));
                                     break;
                    case Piece.BISHOP: square.setAllMovableSpaces(bishopPieceMoves(row, col));
                                       break;
                    case Piece.KNIGHT: square.setAllMovableSpaces(knightPieceMoves(row, col));
                                       break;
                    case Piece.ROOK: square.setAllMovableSpaces(rookPieceMoves(row, col));
                                     break;
                    case Piece.QUEEN: square.setAllMovableSpaces(queenPieceMoves(row, col));
                                      break;
                    default: break;
                }
            }
        }
    }

    /* --------------------- Piece Move Checkers ------------------------------- */

    public static HashMap<Square, Boolean> kingPieceMoves(int row, int col) {
        HashMap<Square, Boolean> possibleMoves = new HashMap<Square, Boolean>();

        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(!Board.withinBoard(row + i, col + j) || (i == 0 && j == 0)) continue;

                if(Board.pieces[row][col].getColor() != Board.pieces[row + i][col + j].getColor() || Board.pieces[row + i][col + j].getRank() == Piece.NO_PIECE) {
                    //Implement check/checkmate feature later
                    possibleMoves.put(Board.board[row + i][col + j], Board.pieces[row][col].getColor() != Board.pieces[row + i][col + j].getColor());                   
                }
            }
        }

        putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
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

        putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
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

        putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
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
        
        putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
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

        putMovesOntoDirectory(possibleMoves.keySet(), Board.board[row][col]);
        return possibleMoves;
    }

    public static HashMap<Square, Boolean> queenPieceMoves(int row, int col) {
        HashMap<Square, Boolean> possibleMoves = new HashMap<Square, Boolean>();

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
        for(Square square : selectedSquare.getMovableSquares().keySet()) {
            allPossibleMoves.get(square).remove(selectedSquare);
        }
        selectedSquare.removeAllMovableSpaces();
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
            case Piece.KING: targetSquare.setAllMovableSpaces(kingPieceMoves(row, col));
                             break;
            case Piece.PAWN: targetSquare.setAllMovableSpaces(pawnPieceMoves(row, col));
                             break;
            case Piece.BISHOP: targetSquare.setAllMovableSpaces(bishopPieceMoves(row, col));
                               break;
            case Piece.KNIGHT: targetSquare.setAllMovableSpaces(knightPieceMoves(row, col));
                               break;
            case Piece.ROOK: targetSquare.setAllMovableSpaces(rookPieceMoves(row, col));
                             break;
            case Piece.QUEEN: targetSquare.setAllMovableSpaces(queenPieceMoves(row, col));
                              break;
            default: break;
        }
    }
}
