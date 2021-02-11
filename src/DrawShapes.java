import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;

public class DrawShapes extends JComponent {
    static int n = 7; // Число углов
    static int R = 50;
    static int X = 200;
    static int Y = 400;
    static double a, b, z;
    static double angle = 360.0 / n;

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4));

        // Отрезок
        g2.setColor(Color.GREEN);
        g2.draw(new Line2D.Double(100, 100, 160, 210));

        // Прямоугольник
        g2.setColor(Color.BLUE);
        g2.draw(new Rectangle2D.Double(350, 100, 100, 150));

        // Эллипс
        g2.setColor(Color.YELLOW);
        g2.draw(new Ellipse2D.Double(600, 100, 150, 50));

        // Многоугольник
        g2.setColor(Color.RED);
        int x1, x2, y1, y2;
        x1 = X + R;
        y1 = Y;
        z = 0;
        for(int i = 0; i <= n; i++) {
            y2 = y1;
            x2 = x1;
            a = Math.cos(z / 180 * Math.PI);
            b = Math.sin(z / 180 * Math.PI);
            x1 = X + (int)(Math.round(a * R));
            y1 = Y + (int)(Math.round(b * R));
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
            z = z - angle;
        }

        // Ломаная
        g2.setColor(Color.MAGENTA);
        g2.draw(new Line2D.Double(600, 300, 650, 350));
        g2.draw(new Line2D.Double(650, 350, 520, 450));
        g2.draw(new Line2D.Double(520, 450, 530, 360));
        g2.draw(new Line2D.Double(530, 360, 580, 480));
        g2.draw(new Line2D.Double(580, 480, 640, 380));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Draw Shapes");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new DrawShapes());
        frame.pack();
        frame.setSize(new Dimension(800, 600));
        frame.setVisible(true);
    }
}