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
    static Color fillColor = Color.BLACK;
    static Color strokeColor = Color.BLACK;
    static float strokeWidth;
    static int currShape = 0;

    static JButton fillColorBtn;
    static JButton strokeColorBtn;

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

        for (int i = 0; i < 4; i++) {
            JToggleButton shapeButton = new JToggleButton(icons.get(i));
            if (i == 0)
                shapeButton.setSelected(true);
            shapesButtons.add(shapeButton);
            int finalI = i;
            shapeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    currShape = finalI;
                    System.out.println(currShape);
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
                strokeWidth = ((Double)strokeWidthSp.getValue()).floatValue();
            }
        });
        strokeWidth = ((Double)strokeWidthSp.getValue()).floatValue();
        optionsBar.add(strokeWidthSp);

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
                Shape preview;
                switch (currShape) {
                    case 0 -> preview = new Line(x, y, x, y);
                    case 1 -> preview = new Rectangle(x, y, x, y);
                    case 2 -> preview = new Ellipse(x, y, x, y);
                    case 3 -> preview = new Polygon(x, y, x, y, 5);
                    default -> throw new IllegalStateException("Unexpected value: " + currShape);
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
                super.mouseMoved(e);
                mouseCoordsLbl.setText(
                        "  x: " + e.getPoint().x +
                                "  y: " + e.getPoint().y
                );
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                DrawShapes.shapes.get(DrawShapes.shapes.size() - 1).x1 = e.getPoint().x;
                DrawShapes.shapes.get(DrawShapes.shapes.size() - 1).y1 = e.getPoint().y;
                mouseCoordsLbl.setText(
                        "  x: " + e.getPoint().x +
                                "  y: " + e.getPoint().y
                );
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
