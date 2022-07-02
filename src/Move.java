import java.util.ArrayList;
public class Move {
    
    public static void initMoves() {
        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                Square square = Board.board[row][col];

                switch(square.getPiece().getRank()) {
                    case Piece.KING: square.setMovableSpaces(kingPieceMoves(row, col));
                                     System.out.println(square.getMovableSquares());
                                     break;
                    default: break;
                }
            }
        }
    }

    /* --------------------- Piece Move Checkers ------------------------------- */

    public static ArrayList<Square> kingPieceMoves(int row, int col) {
        ArrayList<Square> possibleMoves = new ArrayList<Square>();

        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(!Board.withinBoard(row + i, col + j) || (i == 0 && j == 0)) continue;

                if(Board.pieces[row][col].getColor() != Board.pieces[row + i][col + j].getColor() || Board.pieces[row + i][col + j].getRank() == Piece.NO_PIECE) {
                    //Implement check/checkmate feature later
                    possibleMoves.add(Board.board[row + i][col + j]);                   
                }
            }
        }

        return possibleMoves;
    }
    
}
