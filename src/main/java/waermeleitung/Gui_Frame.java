package waermeleitung;

import javax.swing.JFrame;
import java.io.FileNotFoundException;

public class Gui_Frame extends JFrame{
    private final JFrame frame;

    public Gui_Frame(int width, int height) throws FileNotFoundException {
        frame = new JFrame("Bewegung");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        addJPanel();
    }

    public void addJPanel() {
        My_JPanel paint = new My_JPanel();
        frame.add(paint);
    }

    public void repaint(int width, int height) {
        frame.repaint(0,0, width,  height);
    }
}
