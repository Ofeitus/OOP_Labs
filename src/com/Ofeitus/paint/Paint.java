package com.Ofeitus.paint;

import com.Ofeitus.paint.shapeFactories.*;
import com.Ofeitus.paint.shapes.PolyLine;
import com.Ofeitus.paint.shapes.Polygon;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
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

    private static class ButtonClickListener implements ActionListener{
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

        //
        DrawShapes drawShapes = new DrawShapes();

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
        icons.add( new ImageIcon("resources\\ToolLine_s0.png"));
        icons.add( new ImageIcon("resources\\ToolRect_s0.png"));
        icons.add( new ImageIcon("resources\\ToolEllipse_s0.png"));
        icons.add( new ImageIcon("resources\\ToolPolygon_s0.png"));
        icons.add( new ImageIcon("resources\\ToolLassoPoly_s0.png"));
        icons.add( new ImageIcon("resources\\ToolCustomShape_s0.png"));

        for (int i = 0; i < 6 ; i++) {
            JToggleButton shapeButton = new JToggleButton(icons.get(i));
            if (i == 0)
                shapeButton.setSelected(true);
            shapesButtons.add(shapeButton);
            int finalI = i;
            shapeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currShape = finalI;
                    if (currShape == 3) {
                        edgesLabel.setVisible(true);
                        edgesCountSp.setVisible(true);
                    }
                    else {
                        edgesLabel.setVisible(false);
                        edgesCountSp.setVisible(false);
                    }
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
        strokeWidthSp.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                strokeWidth = ((Double) strokeWidthSp.getValue()).floatValue();
            }
        });
        strokeWidth = ((Double) strokeWidthSp.getValue()).floatValue();
        optionsBar.add(strokeWidthSp);

            // Кол-во сторон правильного многоугольника
        edgesLabel = new JLabel("  Стороны: ");
        edgesLabel.setVisible(false);
        optionsBar.add(edgesLabel);
        model = new SpinnerNumberModel(5, 3, 100.0, 1);
        edgesCountSp = new JSpinner(model);
        edgesCountSp.setMaximumSize( new Dimension(70, 70));
        edgesCountSp.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                edgesCount = ((Double) edgesCountSp.getValue()).intValue();
            }
        });
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

                // Число сторон правильного многоугольника
                int option = 0;
                if (shapeFactories.get(currShape) instanceof RegularPolygonFactory)
                    option = edgesCount;

                // Создание фигуры через фабрику
                Shape preview = shapeFactories.get(currShape).factoryMethod(x, y, x, y, option);

                // Если есть активная ломаная или полигон
                if (DrawShapes.shapes.size() > 0) {
                    Shape shape = DrawShapes.shapes.get(DrawShapes.shapes.size() - 1);
                    if (shape instanceof PolyLine || shape instanceof Polygon) {
                        if (!shape.finished) {
                            if (e.getButton() == MouseEvent.BUTTON3) {
                                // Закончить рисование ломанной или полигона (правая кнопка мыши)
                                shape.points.remove(shape.points.size() - 1);
                                shape.finished = true;
                            } else
                                // Добавить точку
                                shape.addPoint(x, y);
                            frame.repaint();
                            return;
                        }
                    }
                }

                preview.fillColor = fillColor;
                preview.strokeColor = strokeColor;
                preview.stroke = strokeWidth;
                DrawShapes.shapes.add(preview);
                frame.repaint();
            }
        });

        drawField.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseDragged(e);
                int x = e.getPoint().x;
                int y = e.getPoint().y;

                // Предпросмотр рисования ломанной линии
                if (DrawShapes.shapes.size() > 0) {
                    Shape shape = DrawShapes.shapes.get(DrawShapes.shapes.size() - 1);
                    if (shape instanceof PolyLine || shape instanceof Polygon) {
                        if (!shape.finished) {
                            shape.points.get(shape.points.size() - 1).x = x;
                            shape.points.get(shape.points.size() - 1).y = y;
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
                if (DrawShapes.shapes.size() > 0) {
                    Shape shape = DrawShapes.shapes.get(DrawShapes.shapes.size() - 1);
                    if (!(shape instanceof PolyLine || shape instanceof Polygon)) {
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
