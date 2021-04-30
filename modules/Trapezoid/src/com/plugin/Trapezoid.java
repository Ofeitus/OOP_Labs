package com.plugin;

import com.Ofeitus.paint.shapes.PrimitiveShape;

import java.awt.*;

import static java.lang.Math.min;
import static java.lang.Math.abs;

public class Trapezoid extends PrimitiveShape {

    public Trapezoid(int x, int y, int x1, int y1) {
        super(x, y, x1, y1);
    }

    public void draw(Graphics2D g2) {
        g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        java.awt.Polygon polygon = new java.awt.Polygon();

        int x0 = min(x, x1);
        int y0 = min(y, y1);
        int width = abs(x - x1);
        int height = abs(y - y1);

        if (y1 > y) {
            polygon.addPoint(x0 + width / 4, y0);
            polygon.addPoint(x0 + width / 4 * 3, y0);
            polygon.addPoint(x0 + width, y0 + height);
            polygon.addPoint(x0, y0 + height);
        } else {
            polygon.addPoint(x0 + width / 4, y0 + height);
            polygon.addPoint(x0 + width / 4 * 3, y0 + height);
            polygon.addPoint(x0 + width, y0);
            polygon.addPoint(x0, y0);
        }

        g2.setColor(fillColor);
        g2.fillPolygon(polygon);
        g2.setColor(strokeColor);
        g2.drawPolygon(polygon);
    }
}