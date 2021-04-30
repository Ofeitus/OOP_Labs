package com.plugin;

import com.Ofeitus.paint.shapeFactories.ShapeFactory;
import com.Ofeitus.paint.shapes.Shape;

public class HeartFactory extends ShapeFactory {

    @Override
    public Shape factoryMethod(int x, int y, int x1, int y1, int option) {
        return new Heart(x, y, x1, y1);
    }
}
