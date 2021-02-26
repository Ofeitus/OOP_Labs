import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DrawShapes extends JComponent {
    static ArrayList<PShape> shapes = new ArrayList<>();

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for (PShape shape : shapes) {
            shape.draw(g2);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Draw Shapes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new DrawShapes());

        // Линия
        PLine line = new PLine(100, 100, 160, 210);
        line.color = Color.GREEN;
        line.stroke = 4.0f;

        DrawShapes.shapes.add(line);

        // Эллипс
        PEllipse ellipse = new PEllipse(600, 100, 150, 50);
        ellipse.color = Color.YELLOW;
        ellipse.stroke = 4.0f;

        DrawShapes.shapes.add(ellipse);

        // Прямоугольник
        PRectangle rectangle = new PRectangle(350, 100, 100, 150);
        rectangle.color = Color.BLUE;
        rectangle.stroke = 4.0f;

        DrawShapes.shapes.add(rectangle);

        // Многоугольник
        PPolygon polygon = new PPolygon(200, 400, 7, 50);
        polygon.color = Color.RED;
        polygon.stroke = 4.0f;

        DrawShapes.shapes.add(polygon);

        // Ломанная
        PBrokenLine brokenLine = new PBrokenLine();
        brokenLine.color = Color.MAGENTA;
        brokenLine.stroke = 4.0f;
        brokenLine.addPoint(600, 300);
        brokenLine.addPoint(650, 350);
        brokenLine.addPoint(520, 450);
        brokenLine.addPoint(530, 360);
        brokenLine.addPoint(580, 480);
        brokenLine.addPoint(640, 380);

        DrawShapes.shapes.add(brokenLine);

        frame.pack();
        frame.setSize(new Dimension(800, 600));
        frame.setVisible(true);
    }
}