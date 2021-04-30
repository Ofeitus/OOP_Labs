package com.plugin;

import com.Ofeitus.paint.shapeFactories.ShapeFactory;
import com.Ofeitus.paint.shapes.Shape;

public class TrapezoidFactory extends ShapeFactory {

    @Override
    public Shape factoryMethod(int x, int y, int x1, int y1, int option) {
        return new Trapezoid(x, y, x1, y1);
    }
}
