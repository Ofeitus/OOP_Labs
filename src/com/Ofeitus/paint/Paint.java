package com.Ofeitus.paint;

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

        DrawShapes drawShapes = new DrawShapes();

        // Shapes bar
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

        // Options bar
        JPanel optionsBar = new JPanel();
        optionsBar.setLayout( new BoxLayout(optionsBar, BoxLayout.LINE_AXIS));
            // Fill color
        optionsBar.add( new JLabel("  Заливка: "));
        fillColorBtn = new JButton("    ");
        fillColorBtn.setBackground(fillColor);
        fillColorBtn.setActionCommand("chooseFillColor");
        fillColorBtn.addActionListener( new ButtonClickListener());
        optionsBar.add(fillColorBtn);
            // Stroke color
        optionsBar.add( new JLabel("  Обводка: "));
        strokeColorBtn = new JButton("    ");
        strokeColorBtn.setBackground(strokeColor);
        strokeColorBtn.setActionCommand("chooseStrokeColor");
        strokeColorBtn.addActionListener( new ButtonClickListener());
        optionsBar.add(strokeColorBtn);
            // Width spinner
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
            // Edges count spinner
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

        // Toolbar
        JPanel toolbar = new JPanel();
        toolbar.setLayout( new BoxLayout(toolbar, BoxLayout.PAGE_AXIS));
        toolbar.add(shapesBar);
        toolbar.add(optionsBar);

        // Status bar
        JPanel statusbar = new JPanel();
        statusbar.setLayout( new BoxLayout(statusbar, BoxLayout.LINE_AXIS));
        JLabel mouseCoordsLbl = new JLabel("  x:   y:");
        statusbar.add(mouseCoordsLbl);

        // Drawing area
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
                Shape preview = null;

                // Handle simple shapes
                if (currShape == 0) preview = new Line(x, y, x, y);
                else if (currShape == 1) preview = new Rectangle(x, y, x, y);
                else if (currShape == 2) preview = new Ellipse(x, y, x, y);
                else if (currShape == 3) preview = new Polygon(x, y, x, y, edgesCount);

                // Handle polyline
                else if (currShape == 4 || currShape == 5) {
                    if (DrawShapes.shapes.size() > 0) {
                        Shape shape = DrawShapes.shapes.get(DrawShapes.shapes.size() - 1);
                        if (shape instanceof PolyLine) {
                            if (!((PolyLine) shape).finished) {
                                if (e.getButton() == MouseEvent.BUTTON3) {
                                    ((PolyLine) shape).points.remove(((PolyLine) shape).points.size() - 1);
                                    ((PolyLine) shape).finished = true;
                                } else
                                    ((PolyLine) shape).addPoint(x, y);
                                frame.repaint();
                                return;
                            }
                        }
                    }
                    int mode;
                    if (currShape == 4)
                        mode = 0;
                    else
                        mode = 1;
                    preview = new PolyLine(0, 0, 0, 0, mode);
                    ((PolyLine) preview).addPoint(x, y);
                    ((PolyLine) preview).addPoint(x, y);
                }

                if (preview != null) {
                    preview.fillColor = fillColor;
                    preview.strokeColor = strokeColor;
                    preview.stroke = strokeWidth;
                    DrawShapes.shapes.add(preview);
                    frame.repaint();
                }
            }
        });

        drawField.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseDragged(e);
                int x = e.getPoint().x;
                int y = e.getPoint().y;
                if (DrawShapes.shapes.size() > 0) {
                    Shape shape = DrawShapes.shapes.get(DrawShapes.shapes.size() - 1);
                    if (shape instanceof PolyLine) {
                        if (!((PolyLine) shape).finished) {
                            ((PolyLine) shape).points.get(((PolyLine) shape).points.size() - 1).x = x;
                            ((PolyLine) shape).points.get(((PolyLine) shape).points.size() - 1).y = y;
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
                if (DrawShapes.shapes.size() > 0) {
                    Shape shape = DrawShapes.shapes.get(DrawShapes.shapes.size() - 1);
                    if (!(shape instanceof PolyLine)) {
                        shape.x1 = x;
                        shape.y1 = y;
                    }
                }
                mouseCoordsLbl.setText("  x: " + x + "  y: " + y);
                frame.repaint();
            }
        });

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
