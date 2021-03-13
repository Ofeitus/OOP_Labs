package com.Ofeitus.paint.shapes;

import java.awt.*;
import java.util.ArrayList;

public abstract class DynamicShape extends Shape {
    private boolean active;
    protected ArrayList<Point> points;

    public DynamicShape() {
        active = true;
    }

    public abstract void addPoint(int x, int y);

    public int pointsCount() {
        return points.size();
    }

    public Point lastPoint() {
        return points.get(points.size() - 1);
    }

    public void removePoint(int index) {
        points.remove(index);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean value) {
        active = value;
    }
}
