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

        return possibleMoves;
    }

    public static HashMap<Square, Boolean> rookPieceMoves(int row, int col) {
        HashMap<Square, Boolean> possibleMoves = new HashMap<Square, Boolean>();

        return possibleMoves;
    }

    public static HashMap<Square, Boolean> queenPieceMoves(int row, int col) {
        HashMap<Square, Boolean> possibleMoves = new HashMap<Square, Boolean>();

        return possibleMoves;
    }
}
