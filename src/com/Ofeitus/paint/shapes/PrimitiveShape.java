package com.Ofeitus.paint.shapes;

public abstract class PrimitiveShape extends Shape {
    public int x, y, x1, y1;

    public PrimitiveShape(int x, int y, int x1, int y1) {
        super();
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
    }
}
