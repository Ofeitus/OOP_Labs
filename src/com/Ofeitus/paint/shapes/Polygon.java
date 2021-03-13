package com.Ofeitus.paint.shapes;

import java.awt.*;
import java.util.ArrayList;

public class Polygon extends DynamicShape {

    public Polygon() {
        points = new ArrayList<>();
    }

    public void addPoint(int x, int y) {
        points.add( new Point(x, y));
    }

    public void draw(Graphics2D g2) {
        java.awt.Polygon polygon = new java.awt.Polygon();
        for (Point point: points) {
            polygon.addPoint(point.x, point.y);
        }

        g2.setStroke(stroke);
        g2.setColor(fillColor);
        g2.fillPolygon(polygon);
        g2.setColor(strokeColor);
        g2.drawPolygon(polygon);
    }
}
