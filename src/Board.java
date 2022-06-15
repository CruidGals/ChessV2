import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class Board extends JFrame {
    public static Square[][] board = new Square[8][8];

    private static JLayeredPane layeredPanel = new JLayeredPane();
    private static JPanel boardPanel;

    // Vars for dragging
    private static Piece selectedPiece;

    public Board() {
        setSize(816, 839);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initBoard();
        boardPanel = createBoardGraphicPanel();
        layeredPanel.setBounds(0, 0, 800, 800);
        layeredPanel.setOpaque(false);
        layeredPanel.add(boardPanel, JLayeredPane.DEFAULT_LAYER);
        boardPanel.setBounds(0, 0, 800, 800);

        add(layeredPanel);
        setVisible(true);

        layeredPanel.addMouseListener(new PieceClickListener());
        layeredPanel.addMouseMotionListener(new PieceDragListener());
    }

    public static void initBoard() {
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                boolean isLight = (rank + file) % 2 == 0;

                Color squareColor = isLight ? Square.LIGHT_SQUARE_COLOR : Square.DARK_SQUARE_COLOR;
                int pRank, pColor = 0;

                if (rank == 0 && file == 0) {
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
        JPanel boardPanel = new JPanel(new GridLayout(8, 8));

        for (Square[] row : board) {
            for (Square col : row) {
                boardPanel.add(col);
            }
        }

        return boardPanel;
    }

    private class PieceClickListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            Component c = boardPanel.findComponentAt(e.getX(), e.getY());

            if (e.getButton() == MouseEvent.BUTTON1) {
                selectedPiece = null;

                if (c instanceof Square)
                    return;

                selectedPiece = (Piece) c;

                layeredPanel.add(selectedPiece, JLayeredPane.DRAG_LAYER);
                selectedPiece.setLocation(e.getX() - 50, e.getY() - 50);
            }

            //DEBUG CODE
            if(e.getButton() == MouseEvent.BUTTON3) {
                if(c instanceof Piece)
                    c = c.getParent();

                String tempRank = JOptionPane.showInputDialog(null, "Enter rank of this piece. (0 = None, 1 = King, 2 = Pawn, 3 = Bishop, 4 = Knight, 5 = Rook, 6 = Queen)");
                String tempColor = JOptionPane.showInputDialog(null, "Enter color of this piece. (8 = White, 16 = Black) ");

                if(tempRank == null || tempColor == null) return;

                ((Square) c).getPiece().setPiece(Integer.parseInt(tempColor), Integer.parseInt(tempRank));
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Component c = boardPanel.findComponentAt(e.getX(), e.getY());
            System.out.println(c);
        }

    }

    private class PieceDragListener extends MouseMotionAdapter {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (selectedPiece == null)
                return;

            Point currPoint = e.getPoint();
            int deltaX = (currPoint.x - 50) - selectedPiece.getX();
            int deltaY = (currPoint.y - 50) - selectedPiece.getY();

            Point currPiecePoint = selectedPiece.getLocation();
            currPiecePoint.translate(deltaX, deltaY);
            selectedPiece.setLocation(currPiecePoint);
        }
    }
}
