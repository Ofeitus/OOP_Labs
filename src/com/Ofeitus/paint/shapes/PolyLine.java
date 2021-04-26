package com.Ofeitus.paint.shapes;

import java.awt.*;
import java.util.ArrayList;

public class PolyLine extends DynamicShape {

    public PolyLine() {
        points = new ArrayList<>();
    }

    public void addPoint(int x, int y) {
        points.add( new Point(x, y));
    }

    public void draw(Graphics2D g2) {
        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];
        int i = 0;
        for (Point point: points) {
            xPoints[i] = point.x;
            yPoints[i] = point.y;
            i++;
        }
        g2.setStroke(new BasicStroke(strokeWidth));
        g2.setColor(strokeColor);
        g2.drawPolyline(xPoints, yPoints, points.size());
    }
}
