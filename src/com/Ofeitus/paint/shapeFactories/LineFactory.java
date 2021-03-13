package com.Ofeitus.paint.shapeFactories;

import com.Ofeitus.paint.shapes.Shape;
import com.Ofeitus.paint.shapes.Line;

public class LineFactory extends ShapeFactory {

    @Override
    public Shape factoryMethod(int x, int y, int x1, int y1, int option) {
        return new Line(x, y, x1, y1);
    }
}
