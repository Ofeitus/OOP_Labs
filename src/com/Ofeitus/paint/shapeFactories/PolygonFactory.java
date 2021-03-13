package com.Ofeitus.paint.shapeFactories;

import com.Ofeitus.paint.shapes.Shape;
import com.Ofeitus.paint.shapes.Polygon;

public class PolygonFactory extends ShapeFactory {

    @Override
    public Shape factoryMethod(int x, int y, int x1, int y1, int option) {
        return new Polygon();
    }
}