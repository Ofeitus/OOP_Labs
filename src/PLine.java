import java.awt.*;
import java.awt.Color;
import java.awt.geom.Line2D;

public class PLine extends PShape {
    Color color;
    int x1, y1, x2, y2;
    float stroke;

    public PLine(int x1, int y1, int x2, int y2) {
        super();
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        color = Color.black;
        stroke = 2.0f;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(stroke));
        g2.draw(new Line2D.Double(x1, y1, x2, y2));
    }
}
