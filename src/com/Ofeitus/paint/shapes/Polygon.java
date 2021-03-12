package com.Ofeitus.paint.shapes;

import com.Ofeitus.paint.Shape;

import java.awt.*;
import java.util.ArrayList;

public class Polygon extends Shape {

    public Polygon(int x, int y, int x1, int y1) {
        super(x, y, x1, y1);
        points = new ArrayList<>();
        addPoint(x, y);
        addPoint(x, y);
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
        g2.setStroke(new BasicStroke(stroke));
        g2.setColor(fillColor);
        g2.fillPolygon(xPoints, yPoints, points.size());
        g2.setColor(strokeColor);
        g2.drawPolygon(xPoints, yPoints, points.size());
    }
}
