import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Move {
    public static HashMap<Square, ArrayList<Square>> pieceMoves = new HashMap<Square, ArrayList<Square>>();

    public static void initMoves() {

        for(int rank = 0; rank < 8; rank++) { //Starts from top rank to bottom rank
            for(char file = 0; file < 8; file++) { //Starts from top file a to file h

                Square[] selectedPieceMoves = new Square[0];
                Piece selectedPiece = Board.pieces[rank][file];

                if(selectedPiece.getRank() == Piece.NO_PIECE) continue;

                switch(selectedPiece.getRank()) {
                    case Piece.KING: selectedPieceMoves = kingPieceMoves(rank, file);
                                     break;
                    default: break;
                }

                putMovesOntoSquare(Board.board[rank][file], selectedPieceMoves);
            }
        }

        System.out.println(pieceMoves);
    }

    public static void putMovesOntoSquare(Square square, Square[] moves) {
        if(square.getPiece().getColor() == Piece.WHITE) {
            if(!pieceMoves.containsKey(square)) {
                pieceMoves.put(square, new ArrayList<Square>());
            }

            for(Square move : moves) {
                pieceMoves.get(square).add(move);
            }
        }
    }

    /* --------------------- Piece Move Checkers ------------------------------- */

    public static Square[] kingPieceMoves(int rank, int file) {
        ArrayList<Square> possibleMoves = new ArrayList<Square>();

        for(int i = -1; i <= 1; i++) {
            for(int j = -1; j <= 1; j++) {
                if(!Board.withinBoard(rank + i, file + j) && (i != 0 && j != 0)) break;

                if(Board.pieces[rank][file].getColor() != Board.pieces[rank + i][file + j].getColor()) {
                    //Implement check/checkmate feature later
                    possibleMoves.add(Board.board[rank][file]);
                }
            }
        }

        Square[] temp = new Square[possibleMoves.size()];
        for(int i = 0; i < temp.length; i++) {
            temp[i] = possibleMoves.get(i);
        }

        return temp;
    }

    
}
