package com.plugin;

import com.Ofeitus.paint.shapes.PrimitiveShape;

import java.awt.*;

import static java.lang.Math.abs;

public class Heart extends PrimitiveShape {

    public Heart(int x, int y, int x1, int y1) {
        super(x, y, x1, y1);
    }

    public void draw(Graphics2D g2) {
        g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        Polygon polygon = new Polygon();

        int width = abs(x - x1);
        int height = abs(y - y1);

        for (float t = 0; t < 2 * Math.PI; t += 0.01) {
            int fx = (int) Math.round(width * Math.pow(Math.sin(t), 3.0));
            int fy = (int) Math.round(height * (13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t)) / 13);
            if (y > y1)
                polygon.addPoint(x + fx, y + fy);
            else
                polygon.addPoint(x + fx, y - fy);
        }

        g2.setColor(fillColor);
        g2.fillPolygon(polygon);
        g2.setColor(strokeColor);
        g2.drawPolygon(polygon);
    }
}