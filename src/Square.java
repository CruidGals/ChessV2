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

    }

    public Piece getPiece() {
        return piece;
    }

    
}
