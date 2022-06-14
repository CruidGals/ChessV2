import javax.swing.*;
import java.awt.*;

public class Board extends JFrame {
    public static Square[][] board = new Square[8][8];
    public static Piece[][] pieces = new Piece[8][8];

    private static JLayeredPane layeredPanel = new JLayeredPane();
    private static JPanel boardPanel;
    private static JLayeredPane piecePanel;

    public Board() {
        setSize(816,816);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        initBoard();
        initPieces();
        boardPanel = createBoardGraphicPanel();
        piecePanel = createPieceGraphicPanel();
        piecePanel.setOpaque(false);

        layeredPanel.setOpaque(false);
        layeredPanel.setLayout(new OverlayLayout(layeredPanel));
        layeredPanel.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPanel.add(piecePanel, JLayeredPane.MODAL_LAYER);

        add(layeredPanel);
        setVisible(true);
    }

    public static void initBoard() {
        for(int rank = 0; rank < 8; rank++) {
            for(int file = 0; file < 8; file++) {
                boolean isLight = (rank + file) % 2 == 0;
                
                Square square = isLight ? new Square(Square.LIGHT_SQUARE_COLOR) : new Square(Square.DARK_SQUARE_COLOR);
                board[rank][file] = square;
            }
        }
    }

    public static void initPieces() {
        for(int rank = 0; rank < 8; rank++) {
            for(int file = 0; file < 8; file++) {
                //Do Fen Notation shit here
                if(rank == 0 && file == 0) {
                    pieces[rank][file] = new Piece(Piece.WHITE, Piece.QUEEN);
                } else {
                    pieces[rank][file] = new Piece(Piece.WHITE, Piece.NO_PIECE);
                }
            }
        }
    }

    /*------------ GUI Implementation -------------- */

    public static JPanel createBoardGraphicPanel() {
        JPanel boardPanel = new JPanel(new GridLayout(8,8));

        for(Square[] row : board) {
            for(Square col : row) {
                boardPanel.add(col);
            }
        }

        return boardPanel;
    }

    public static JLayeredPane createPieceGraphicPanel() {
        JLayeredPane piecePanel = new JLayeredPane();
        piecePanel.setLayout(new GridLayout(8,8));

        for(Piece[] row : pieces) {
            for(Piece col : row) {
                piecePanel.add(col, JLayeredPane.DEFAULT_LAYER);
            }
        }

        return piecePanel;
    }

    /**
     * Precondition: piece must be a part of pieces[][]
     * @param piece
     */
    public static void pieceDraggedAction(Piece piece) {
        piecePanel.add(piece, JLayeredPane.DRAG_LAYER);
    }
}
