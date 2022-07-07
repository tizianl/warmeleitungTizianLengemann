package waermeleitung;

import javax.swing.*;
import java.awt.*;


public class My_JPanel extends JPanel {

    private static final double[][][] threeDimArray = Cuboid.getArray();
    private final double highestValue = Cuboid.getHighestValue();
    private final double lowestValue = Cuboid.getLowestValue();
    private static final int xLength = Cuboid.getXValue();
    private static final int zHalf = Cuboid.getZValue() / 2;
    private static final int yLength = Cuboid.getYValue();
    private final double sectionNumber = (highestValue - lowestValue) / numberOfColours;
    private static final int numberOfColours = 10;

    // Specific Colors
    private final Color blackPurple = new Color(153, 51, 153);
    private final Color pink = new Color(255,0,255);
    private final Color purple = new Color(132, 0 , 255);



    private final Color darkBlue = new Color(0,0,128);

    private final Color lightBlue = new Color(0,191 ,255);

    private final Color darkGreen = new Color(0,153,51);
    private final Color lightGreen = new Color(72,255,0);

    private final Color lightYellow = new Color(255,255,210);

    private final Color lightBrown = new Color(188,143,143);

    private final Color Red = new Color(255,0,0);
    private final Color magenta = new Color(255,0,255);












    public My_JPanel() {}

    public void paint(Graphics g) {
        for(int i = 1; i < xLength - 1; i++) {
            for(int t = 1 ; t < yLength - 1; t++) {
                if (threeDimArray[i][zHalf][t] < (lowestValue + sectionNumber * 1)) {
                    g.setColor(darkBlue);
                }
                else if (threeDimArray[i][zHalf][t]  < (lowestValue + sectionNumber * 2)) {
                    g.setColor(lightBlue);
                }
                else if (threeDimArray[i][zHalf][t]  < (lowestValue + sectionNumber * 3)) {
                    g.setColor(darkGreen);
                }
                else if (threeDimArray[i][zHalf][t]  < lowestValue + sectionNumber * 4) {
                    g.setColor(lightGreen);
                }
                else if (threeDimArray[i][zHalf][t] < lowestValue + sectionNumber * 5) {
                    g.setColor(lightYellow);
                }
                else if (threeDimArray[i][zHalf][t] < lowestValue + sectionNumber * 6) {
                    g.setColor(lightBrown);
                }
                else if (threeDimArray[i][zHalf][t] < lowestValue + sectionNumber * 7) {
                    g.setColor(Color.YELLOW);
                }
                else if (threeDimArray[i][zHalf][t] < lowestValue + sectionNumber * 8) {
                    g.setColor(Color.ORANGE);
                }
                else if (threeDimArray[i][zHalf][t] < lowestValue + sectionNumber * 9) {
                    g.setColor(Red);
                } else {
                    g.setColor(magenta);
                }
                g.fillRect(i*6-6,t*6-6,5,  5);
            }
        }
    }
}

