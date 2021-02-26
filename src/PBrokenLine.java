import java.awt.*;
import java.awt.Color;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class PBrokenLine extends PShape {
    Color color;
    ArrayList<Point> points = new ArrayList<>();
    float stroke;

    public PBrokenLine() {
        super();
        color = Color.black;
        stroke = 2.0f;
    }

    public void addPoint(int x, int y) {
        points.add(new Point(x, y));
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(stroke));
        for (int i = 0; i < points.size() - 1; i++) {
            g2.draw(new Line2D.Double(points.get(i).x, points.get(i).y, points.get(i+1).x, points.get(i+1).y));
        }
    }
}
