package com.Ofeitus.paint.shapes;

import java.awt.*;
import java.io.Serializable;

public abstract class Shape implements Serializable {
    public float strokeWidth;
    public Color fillColor;
    public Color strokeColor;

    public abstract void draw(Graphics2D g2);
}
