package com.Ofeitus.paint;

import com.Ofeitus.paint.shapeFactories.*;
import com.Ofeitus.paint.shapes.DynamicShape;
import com.Ofeitus.paint.shapes.PrimitiveShape;
import com.Ofeitus.paint.shapes.Shape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Paint {

    static JFrame frame = new JFrame("OOPaint!");
    static Color fillColor = Color.GRAY;
    static Color strokeColor = Color.BLACK;
    static float strokeWidth;
    static int edgesCount;
    static int currShape = 0;

    static JButton fillColorBtn;
    static JButton strokeColorBtn;
    static JLabel edgesLabel;
    static JSpinner edgesCountSp;

    private static void configureComponents(final Container c) {
        Component[] comps = c.getComponents();
        for (Component comp : comps) {
            if (comp instanceof JToggleButton)
                ((JToggleButton) comp).setFocusPainted(false);
            if (comp instanceof JButton)
                ((JButton) comp).setFocusPainted(false);
            if (comp instanceof Container)
                configureComponents((Container) comp);
        }
    }

    private static class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if(command.equals("chooseFillColor")) {
                Color color = JColorChooser.showDialog(null, "Выберите цвет заливки", fillColor);
                if (color != null)
                    fillColor = color;
                fillColorBtn.setBackground(fillColor);
            } else if(command.equals("chooseStrokeColor")) {
                Color color = JColorChooser.showDialog(null, "Выберите цвет обводки", strokeColor);
                if (color != null)
                    strokeColor = color;
                strokeColorBtn.setBackground(strokeColor);
            }
        }
    }

    public static void main(String[] args) {
        // Стиль интерфейса
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {

            }
        }

        DrawShapes drawShapes = new DrawShapes();

        InputMap input = drawShapes.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        input.put(KeyStroke.getKeyStroke("ctrl pressed Z"), "undo");
        drawShapes.getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawShapes.undo();
                frame.repaint();
            }
        });

        input.put(KeyStroke.getKeyStroke("ctrl shift pressed Z"), "redo");
        drawShapes.getActionMap().put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawShapes.redo();
                frame.repaint();
            }
        });

        input.put(KeyStroke.getKeyStroke("ctrl pressed S"), "save");
        drawShapes.getActionMap().put("save", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawShapes.saveContent();
            }
        });

        input.put(KeyStroke.getKeyStroke("ctrl pressed L"), "load");
        drawShapes.getActionMap().put("load", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawShapes.loadContent();
                frame.repaint();
            }
        });

        // Список фабрик фигур
        ArrayList<ShapeFactory> shapeFactories = new ArrayList<>();
        shapeFactories.add(new LineFactory());
        shapeFactories.add(new RectangleFactory());
        shapeFactories.add(new EllipseFactory());
        shapeFactories.add(new RegularPolygonFactory());
        shapeFactories.add(new PolyLineFactory());
        shapeFactories.add(new PolygonFactory());

        // Панель фигур
        JPanel shapesBar = new JPanel();
        shapesBar.setLayout( new BoxLayout(shapesBar, BoxLayout.LINE_AXIS));
        ButtonGroup shapesButtons = new ButtonGroup();
        ArrayList<ImageIcon> icons = new ArrayList<>();
        icons.add( new ImageIcon("resources/ToolLine_s0.png"));
        icons.add( new ImageIcon("resources/ToolRect_s0.png"));
        icons.add( new ImageIcon("resources/ToolEllipse_s0.png"));
        icons.add( new ImageIcon("resources/ToolPolygon_s0.png"));
        icons.add( new ImageIcon("resources/ToolLassoPoly_s0.png"));
        icons.add( new ImageIcon("resources/ToolCustomShape_s0.png"));

        for (int i = 0; i < 6 ; i++) {
            JToggleButton shapeButton = new JToggleButton(icons.get(i));
            if (i == 0)
                shapeButton.setSelected(true);
            shapesButtons.add(shapeButton);
            int finalI = i;
            shapeButton.addActionListener(e -> {
                currShape = finalI;
                if (currShape == 3) {
                    edgesLabel.setVisible(true);
                    edgesCountSp.setVisible(true);
                }
                else {
                    edgesLabel.setVisible(false);
                    edgesCountSp.setVisible(false);
                }
            });
            shapesBar.add(shapeButton);
        }
        shapesBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Панель свойств
        JPanel optionsBar = new JPanel();
        optionsBar.setLayout( new BoxLayout(optionsBar, BoxLayout.LINE_AXIS));

            // Цвет заливки
        optionsBar.add( new JLabel("  Заливка: "));
        fillColorBtn = new JButton("    ");
        fillColorBtn.setBackground(fillColor);
        fillColorBtn.setActionCommand("chooseFillColor");
        fillColorBtn.addActionListener( new ButtonClickListener());
        optionsBar.add(fillColorBtn);

            // Цвет обводки
        optionsBar.add( new JLabel("  Обводка: "));
        strokeColorBtn = new JButton("    ");
        strokeColorBtn.setBackground(strokeColor);
        strokeColorBtn.setActionCommand("chooseStrokeColor");
        strokeColorBtn.addActionListener( new ButtonClickListener());
        optionsBar.add(strokeColorBtn);

            // Толщина обводки
        SpinnerNumberModel model = new SpinnerNumberModel(4.0, 0.0, 100.0, 0.1);
        JSpinner strokeWidthSp = new JSpinner(model);
        strokeWidthSp.setMaximumSize( new Dimension(70, 70));
        strokeWidthSp.addChangeListener(e -> strokeWidth = ((Double) strokeWidthSp.getValue()).floatValue());
        strokeWidth = ((Double) strokeWidthSp.getValue()).floatValue();
        optionsBar.add(strokeWidthSp);

            // Кол-во сторон правильного многоугольника
        edgesLabel = new JLabel("  Стороны: ");
        edgesLabel.setVisible(false);
        optionsBar.add(edgesLabel);
        model = new SpinnerNumberModel(5, 3, 100.0, 1);
        edgesCountSp = new JSpinner(model);
        edgesCountSp.setMaximumSize( new Dimension(70, 70));
        edgesCountSp.addChangeListener(e -> edgesCount = ((Double) edgesCountSp.getValue()).intValue());
        edgesCount = ((Double) edgesCountSp.getValue()).intValue();
        optionsBar.add(edgesCountSp);
        edgesCountSp.setVisible(false);

        optionsBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Панель инструментов (фигуры и свойства)
        JPanel toolbar = new JPanel();
        toolbar.setLayout( new BoxLayout(toolbar, BoxLayout.PAGE_AXIS));
        toolbar.add(shapesBar);
        toolbar.add(optionsBar);

        // Панель состояния
        JPanel statusbar = new JPanel();
        statusbar.setLayout( new BoxLayout(statusbar, BoxLayout.LINE_AXIS));
        JLabel mouseCoordsLbl = new JLabel("  x:   y:");
        statusbar.add(mouseCoordsLbl);

        // Область рисования
        JPanel drawField = new JPanel();
        drawField.setLayout( new BoxLayout(drawField, BoxLayout.PAGE_AXIS));
        drawField.setBackground(Color.WHITE);
        drawField.add(drawShapes);
        drawField.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                int x = e.getPoint().x;
                int y = e.getPoint().y;

                // Если есть активная динамическая фигура
                if (drawShapes.shapesCount() > 0) {
                    if (drawShapes.lastShape() instanceof DynamicShape) {
                        DynamicShape shape = (DynamicShape) drawShapes.lastShape();
                        if (shape.isActive()) {
                            if (e.getButton() == MouseEvent.BUTTON3) {
                                // Закончить рисование ломанной или полигона (правая кнопка мыши)
                                shape.removePoint(shape.pointsCount() - 1);
                                shape.setActive(false);
                            } else
                                // Добавить точку
                                shape.addPoint(x, y);
                            frame.repaint();
                            return;
                        }
                    }
                }

                drawShapes.clearUndo();

                // Число сторон правильного многоугольника
                int option = 0;
                if (shapeFactories.get(currShape) instanceof RegularPolygonFactory)
                    option = edgesCount;

                // Создание фигуры через фабрику
                Shape shape = shapeFactories.get(currShape).factoryMethod(x, y, x, y, option);

                if (shape instanceof DynamicShape) {
                    ((DynamicShape) shape).addPoint(x, y);
                    ((DynamicShape) shape).addPoint(x, y);
                }

                shape.fillColor = fillColor;
                shape.strokeColor = strokeColor;
                shape.strokeWidth = strokeWidth;
                drawShapes.addShape(shape);
                frame.repaint();
            }
        });

        drawField.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseDragged(e);
                int x = e.getPoint().x;
                int y = e.getPoint().y;

                // Предпросмотр рисования динамических фигур
                if (drawShapes.shapesCount() > 0) {
                    if (drawShapes.lastShape() instanceof DynamicShape) {
                        DynamicShape shape = (DynamicShape) drawShapes.lastShape();
                        if (shape.isActive()) {
                            shape.lastPoint().x = x;
                            shape.lastPoint().y = y;
                            frame.repaint();
                        }
                    }
                }
                mouseCoordsLbl.setText("  x: " + x + "  y: " + y);
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int x = e.getPoint().x;
                int y = e.getPoint().y;

                // Предпросморт рисования фигур
                if (drawShapes.shapesCount() > 0) {
                    if (drawShapes.lastShape() instanceof PrimitiveShape) {
                        PrimitiveShape shape = (PrimitiveShape) drawShapes.lastShape();
                        shape.x1 = x;
                        shape.y1 = y;
                    }
                }
                mouseCoordsLbl.setText("  x: " + x + "  y: " + y);
                frame.repaint();
            }
        });

        // Главная панель
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(toolbar, BorderLayout.NORTH);
        mainPanel.add(drawField, BorderLayout.CENTER);
        mainPanel.add(statusbar, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.add(mainPanel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        configureComponents(frame);
    }
}
