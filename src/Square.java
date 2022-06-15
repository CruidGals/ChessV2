import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Square extends JLabel {
    public static final Color LIGHT_SQUARE_COLOR = new Color(255,255,255);
    public static final Color DARK_SQUARE_COLOR = new Color(118,150,86);

    private Piece piece;

    public Square(Color squareColor, int color, int rank) {
        setBackground(squareColor);
        setOpaque(true);
        setPreferredSize(new Dimension(100,100));

        piece = new Piece(color, rank);
        setLayout(new GridBagLayout());
        add(piece);

        //addMouseListener(new MouseClickListener());
    }

    private class MouseClickListener extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {

            //DEBUG CODE
            if(e.getButton() == MouseEvent.BUTTON3) {
                String tempRank = JOptionPane.showInputDialog(null, "Enter rank of this piece. (0 = None, 1 = King, 2 = Pawn, 3 = Bishop, 4 = Knight, 5 = Rook, 6 = Queen)");
                String tempColor = JOptionPane.showInputDialog(null, "Enter color of this piece. (8 = White, 16 = Black) ");

                if(tempRank == null || tempColor == null) return;

                piece.setPiece(Integer.parseInt(tempColor), Integer.parseInt(tempRank));
            }

        }
    }
}
