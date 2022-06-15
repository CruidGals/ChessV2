import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JFrame {
    public static Square[][] board = new Square[8][8];

    private static JLayeredPane layeredPanel = new JLayeredPane();
    private static JPanel boardPanel;

    public Board() {
        setSize(816,816);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        initBoard();
        boardPanel = createBoardGraphicPanel();

        layeredPanel.setOpaque(false);
        layeredPanel.setLayout(new OverlayLayout(layeredPanel));
        layeredPanel.add(boardPanel, JLayeredPane.DEFAULT_LAYER);

        add(layeredPanel);
        setVisible(true);

        addMouseListener(new PieceClickListener());
        addMouseMotionListener(new PieceDragListener());
    }

    public static void initBoard() {
        for(int rank = 0; rank < 8; rank++) {
            for(int file = 0; file < 8; file++) {
                boolean isLight = (rank + file) % 2 == 0;
                
                Color squareColor = isLight ? Square.LIGHT_SQUARE_COLOR : Square.DARK_SQUARE_COLOR;
                int pRank, pColor = 0;

                if(rank == 0 && file == 0) {
                    pRank = Piece.QUEEN;
                    pColor = Piece.WHITE;
                } else {
                    pRank = Piece.NO_PIECE;
                    pColor = Piece.WHITE;
                }
                board[rank][file] = new Square(squareColor, pColor, pRank);
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

    private class PieceClickListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            Component c = boardPanel.findComponentAt(e.getX(), e.getY());
            System.out.println(c);
        }
    }

    private class PieceDragListener extends MouseMotionAdapter {

    }
}
