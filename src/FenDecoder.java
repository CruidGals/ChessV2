import java.util.Arrays;

public class FenDecoder {

    /**
     * Starting from the very top row (index 0), lists the piece arrangement for each row
     */
    public static String[] pieceRowCodes = new String[8];
    public static String turn = "";
    public static String castlingStatus = "";
    public static String enPassantTargetSquare = "";
    public static int halfMoveCount = 0;
    public static int fullMoveCount = 0;

    public static void decodeFenRecord(String fenRecord) {
        String[] fields = fenRecord.split(" ");

        turn = fields[1];
        castlingStatus = fields[2];
        enPassantTargetSquare = fields[3];
        halfMoveCount = Integer.parseInt(fields[4]);
        fullMoveCount = Integer.parseInt(fields[5]);

        pieceRowCodes = fields[0].split("/");
    }

    /* -------------------- Individual field decoders ------------------------ */

    public static void decodePieceRowCodes() {
        for(int row = 0; row < 8; row ++) {
            int colIndex = 0;
            
            String[] currentCode = pieceRowCodes[row].split("");

            for(String pieceCode : currentCode) {
                int color;
                int rank;

                color = Character.isUpperCase(pieceCode.charAt(0)) ? Piece.WHITE : Piece.BLACK;

                pieceCode = pieceCode.toLowerCase();

                //This code decides what rank the piece is based on it's letter in the code
                if(pieceCode.equals("r")) rank = Piece.ROOK;
                else if(pieceCode.equals("n")) rank = Piece.KNIGHT;
                else if(pieceCode.equals("b")) rank = Piece.BISHOP;
                else if(pieceCode.equals("q")) rank = Piece.QUEEN;
                else if(pieceCode.equals("k")) rank = Piece.KING;
                else if(pieceCode.equals("p")) rank = Piece.PAWN;
                else rank = Piece.NO_PIECE;

                if(isNumeric(pieceCode)) {
                    for(int i = 0; i < Integer.parseInt(pieceCode); i++) {
                        Board.pieces[row][colIndex] = new Piece(Piece.NO_COLOR, rank);
                        colIndex++;
                    }
                } else {
                    Board.pieces[row][colIndex] = new Piece(color, rank);
                    colIndex++;
                }

            }
        }

        for(Piece[] row : Board.pieces) {
            System.out.println(Arrays.asList(row));
        }
    }

    //Helper methods
    public static boolean isNumeric(String string) {
        if(string == null || string.equals("")) return false;

        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
