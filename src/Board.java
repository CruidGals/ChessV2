import javax.swing.*;
import java.awt.*;

public class Board extends JFrame {
    public static Square[][] board = new Square[8][8];
    public static JPanel boardPanel;

    public Board() {
        setSize(800,800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        initBoard();
        boardPanel = createBoardGraphicPanel();

        add(boardPanel);
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

    public static JPanel createBoardGraphicPanel() {
        JPanel boardPanel = new JPanel(new GridLayout(8,8));

        for(Square[] row : board) {
            for(Square col : row) {
                boardPanel.add(col);
            }
        }

        return boardPanel;
    }
}
