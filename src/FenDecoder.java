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
        
    }
}
