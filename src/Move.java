import java.util.HashMap;
public class Move {
    
    public static void initMoves() {
        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                Square square = Board.board[row][col];

                switch(square.getPiece().getRank()) {
                    case Piece.KING: square.setMovableSpaces(kingPieceMoves(row, col));
                                     break;
                    case Piece.PAWN: square.setMovableSpaces(pawnPieceMoves(row, col));
                                     break;
                    case Piece.BISHOP: square.setMovableSpaces(bishopPieceMoves(row, col));
                                       break;
                    case Piece.KNIGHT: square.setMovableSpaces(knightPieceMoves(row, col));
                                       break;
                    case Piece.ROOK: square.setMovableSpaces(rookPieceMoves(row, col));
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

        return possibleMoves;
    }

    public static HashMap<Square, Boolean> knightPieceMoves(int row, int col) {
        HashMap<Square, Boolean> possibleMoves = new HashMap<Square, Boolean>();

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

                int endRow = row, endCol = col;
                if(i == 0) {
                    endRow += displacement;
                } else {
                    endCol += displacement;
                }
                
                if(Board.withinBoard(endRow, endCol)) {
                    if(i == 0) {
                        for(int direction = -1; direction <= 1; direction += 2) {
                            if(Board.withinBoard(endRow, endCol + direction) && Board.pieces[endRow][endCol + direction].getColor() != Board.pieces[row][col].getColor()) { //Checks for space below piece
                                if(Board.pieces[endRow][endCol + direction].getColor() != Piece.NO_COLOR) {
                                    possibleMoves.put(Board.board[endRow][endCol + direction], true);
                                } else {
                                    possibleMoves.put(Board.board[endRow][endCol + direction], false);
                                }
                            }
                        }
                    } else {
                        for(int direction = -1; direction <= 1; direction += 2) {
                            if(Board.withinBoard(endRow + direction, endCol) && Board.pieces[endRow + direction][endCol].getColor() != Board.pieces[row][col].getColor()) { //Checks for space below piece
                                if(Board.pieces[endRow + direction][endCol].getColor() != Piece.NO_COLOR) {
                                    possibleMoves.put(Board.board[endRow + direction][endCol], true);
                                } else {
                                    possibleMoves.put(Board.board[endRow + direction][endCol], false);
                                }
                            }
                        }
                    }
                }
            }
        }

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

        return possibleMoves;
    }

    public static HashMap<Square, Boolean> queenPieceMoves(int row, int col) {
        HashMap<Square, Boolean> possibleMoves = new HashMap<Square, Boolean>();

        return possibleMoves;
    }
}
