import java.awt.*;
import java.awt.Color;
import java.awt.geom.Line2D;

public class PPolygon extends PShape {
    Color color;
    int x, y, n, r;
    float stroke;
    double a, b, z;
    double angle;

    public PPolygon(int x, int y, int n, int r) {
        super();
        this.x = x;
        this.y = y;
        this.n = n;
        this.r = r;
        color = Color.black;
        stroke = 2.0f;
        angle = 360.0 / n;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(stroke));
        int x1, x2, y1, y2;
        x1 = x + r;
        y1 = y;
        z = 0;
        for(int i = 0; i <= n; i++) {
            y2 = y1;
            x2 = x1;
            a = Math.cos(z / 180 * Math.PI);
            b = Math.sin(z / 180 * Math.PI);
            x1 = x + (int)(Math.round(a * r));
            y1 = y + (int)(Math.round(b * r));
            g2.draw(new Line2D.Double(x1, y1, x2, y2));
            z = z - angle;
        }
    }
}
