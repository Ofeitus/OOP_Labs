package com.Ofeitus.paint;

import com.Ofeitus.paint.shapes.Shape;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DrawShapes extends JComponent {
    private ArrayList<Shape> shapesList;
    private ArrayList<Shape> undoList;

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for (Shape shape : shapesList) {
            shape.draw(g2);
        }
    }

    public DrawShapes() {
        shapesList = new ArrayList<>();
        undoList = new ArrayList<>();
    }

    public int shapesCount() {
        return shapesList.size();
    }

    public Shape getShape(int index) {
        return shapesList.get(index);
    }

    public Shape lastShape() {
        return shapesList.get(shapesList.size() - 1);
    }

    public void addShape(Shape shape) {
        shapesList.add(shape);
    }

    public void undo() {
        if (shapesList.size() > 0) {
            undoList.add(shapesList.get(shapesList.size() - 1));
            shapesList.remove(shapesList.size() - 1);
        }
    }

    public void redo() {
        if (undoList.size() > 0) {
            shapesList.add(undoList.get(undoList.size() - 1));
            undoList.remove(undoList.size() - 1);
        }
    }

    public void clearUndo() {
        undoList.clear();
    }

    public void saveContent() {
        try (FileOutputStream outFile = new FileOutputStream("file.pnt");
             ObjectOutputStream object = new ObjectOutputStream(outFile)){
            object.writeObject(shapesList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loadContent() {
        try (FileInputStream inFile = new FileInputStream("file.pnt");
             ObjectInputStream objectIn = new ObjectInputStream(inFile)){
            shapesList = (ArrayList<Shape>) objectIn.readObject();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        undoList.clear();
    }
}