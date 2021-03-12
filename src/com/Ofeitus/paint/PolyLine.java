package com.Ofeitus.paint;

import java.awt.*;
import java.util.ArrayList;

public class PolyLine extends Shape {
    public ArrayList<Point> points = new ArrayList<>();
    boolean finished = false;
    int mode;

    public PolyLine(int x, int y, int x1, int y1, int mode) {
        super(x, y, x1, y1);
        this.mode = mode;
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
        if (mode == 0) {
            g2.setColor(strokeColor);
            g2.drawPolyline(xPoints, yPoints, points.size());
        }
        else if (mode == 1) {
            g2.setColor(fillColor);
            g2.fillPolygon(xPoints, yPoints, points.size());
            g2.setColor(strokeColor);
            g2.drawPolygon(xPoints, yPoints, points.size());
        }
    }
}
