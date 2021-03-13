package com.Ofeitus.paint.shapeFactories;

import com.Ofeitus.paint.shapes.Shape;

public abstract class ShapeFactory {
    public abstract Shape factoryMethod(int x, int y, int x1, int y1, int option);
}
