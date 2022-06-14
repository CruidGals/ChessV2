import java.awt.*;
import javax.swing.*;

public class Square extends JLabel {
    public static final Color LIGHT_SQUARE_COLOR = new Color(255,255,255);
    public static final Color DARK_SQUARE_COLOR = new Color(118,150,86);

    public Square(Color color) {
        setBackground(color);
        setOpaque(true);
        setPreferredSize(new Dimension(100,100));
    }
}
