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
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Paint {

    static DrawShapes drawShapes;
    static JFileChooser fileChooser = new JFileChooser();
    static String selectedFile = "";

    static JFrame frame = new JFrame("OOPaint!");
    static Color fillColor = new Color(255, 102, 102);
    static Color strokeColor = new Color(255, 51, 51);
    static float strokeWidth = 8;
    static int edgesCount;
    static int currShape = 0;

    static JButton fillColorBtn;
    static JButton strokeColorBtn;
    static JLabel edgesLabel;
    static JSpinner edgesCountSp;

    public static URL[] getFileUrls(String directoryPath) throws MalformedURLException {

        File dir = new File(directoryPath);

        Collection<URL> files  = new ArrayList<>();

        if(dir.isDirectory()){
            File[] listFiles = dir.listFiles();

            assert listFiles != null;
            for(File file : listFiles){
                if(file.isFile()) {
                    files.add(file.toURL());
                }
            }
        }

        return files.toArray(new URL[0]);
    }

    public static ArrayList<String> getClassNames(File givenFile) throws IOException {
        ArrayList<String> classNames = new ArrayList<>();
        try (JarFile jarFile = new JarFile(givenFile)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    String className = jarEntry.getName()
                            .replace("/", ".")
                            .replace(".class", "");
                    classNames.add(className);
                }
            }
            return classNames;
        }
    }

    public static String getIconName(File givenFile) throws IOException {
        try (JarFile jarFile = new JarFile(givenFile)) {
            Enumeration<JarEntry> e = jarFile.entries();
            while (e.hasMoreElements()) {
                JarEntry jarEntry = e.nextElement();
                if (jarEntry.getName().toLowerCase().contains("icon")) {
                    return jarEntry.getName();
                }
            }
            return "";
        }
    }

    public static String getFactoryClassName(ArrayList<String> names) {
        for (String name : names) {
            if (name.toLowerCase().contains("factory"))
                return name;
        }
        return "";
    }

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

    private static class MyActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            switch (command) {
                case "chooseFillColor" -> {
                    Color color = JColorChooser.showDialog(null, "Choose fill color", fillColor);
                    if (color != null)
                        fillColor = color;
                    fillColorBtn.setBackground(fillColor);
                }
                case "chooseStrokeColor" -> {
                    Color color = JColorChooser.showDialog(null, "Choose stroke color", strokeColor);
                    if (color != null)
                        strokeColor = color;
                    strokeColorBtn.setBackground(strokeColor);
                }
                case "newFile" -> {
                    drawShapes.clearShapes();
                    frame.repaint();
                    selectedFile = "";
                }
                case "openFile" -> {
                    if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        selectedFile = fileChooser.getSelectedFile().getPath();
                        drawShapes.loadContent(selectedFile);
                        frame.repaint();
                    }
                }
                case "saveFile" -> {
                    if (!selectedFile.equals("")) {
                        drawShapes.saveContent(selectedFile);
                    }
                    else if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        selectedFile = fileChooser.getSelectedFile().getPath();
                        drawShapes.saveContent(selectedFile);
                    }
                }
                case "saveFileAs" -> {
                    if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        selectedFile = fileChooser.getSelectedFile().getPath();
                        drawShapes.saveContent(selectedFile);
                    }
                }
                case "exit" -> System.exit(0);
                case "undo" -> {
                    drawShapes.undo();
                    frame.repaint();
                }
                case "redo" -> {
                    drawShapes.redo();
                    frame.repaint();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
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

        MyActionListener actionLister = new MyActionListener();

        drawShapes = new DrawShapes();

        // Список фабрик фигур
        ArrayList<ShapeFactory> shapeFactories = new ArrayList<>();
        shapeFactories.add(new LineFactory());
        shapeFactories.add(new RectangleFactory());
        shapeFactories.add(new EllipseFactory());
        shapeFactories.add(new RegularPolygonFactory());
        shapeFactories.add(new PolyLineFactory());
        shapeFactories.add(new PolygonFactory());

        // Иконки фигур
        ArrayList<ImageIcon> icons = new ArrayList<>();
        icons.add( new ImageIcon("resources/ToolLine_s0.png"));
        icons.add( new ImageIcon("resources/ToolRect_s0.png"));
        icons.add( new ImageIcon("resources/ToolEllipse_s0.png"));
        icons.add( new ImageIcon("resources/ToolPolygon_s0.png"));
        icons.add( new ImageIcon("resources/ToolLassoPoly_s0.png"));
        icons.add( new ImageIcon("resources/ToolCustomShape_s0.png"));

        // Модули
        URL[] classLoaderUrls = getFileUrls("C:/Users/ofeitus/IdeaProjects/OOPaint/plugins/");
        for (URL url : classLoaderUrls) {
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
            // Фабрика фигуры из плагина
            ArrayList<String> classNames = getClassNames(new File(url.getFile()));
            Class<?> pluginClass = urlClassLoader.loadClass(getFactoryClassName(classNames));
            Constructor<?> constructor = pluginClass.getConstructor();
            Object beanObj = constructor.newInstance();
            shapeFactories.add((ShapeFactory) beanObj);
            // Иконка фигуры
            icons.add( new ImageIcon(urlClassLoader.getResource(getIconName(new File(url.getFile())))));
        }

        // Строка меню
        JMenuBar menubar = new JMenuBar();

        JMenu menu = new JMenu("File");

        JMenuItem itm = new JMenuItem("New");
        itm.setActionCommand("newFile");
        itm.addActionListener(actionLister);
        menu.add(itm);

        itm = new JMenuItem("Open...");
        itm.setActionCommand("openFile");
        itm.addActionListener(actionLister);
        menu.add(itm);

        itm = new JMenuItem("Save");
        itm.setActionCommand("saveFile");
        itm.setAccelerator(KeyStroke.getKeyStroke("ctrl pressed S"));
        itm.addActionListener(actionLister);
        menu.add(itm);

        itm = new JMenuItem("Save as");
        itm.setActionCommand("saveFileAs");
        itm.setAccelerator(KeyStroke.getKeyStroke("ctrl shift pressed S"));
        itm.addActionListener(actionLister);
        menu.add(itm);

        menu.add(new JSeparator());

        itm = new JMenuItem("Exit");
        itm.setActionCommand("exit");
        itm.addActionListener(actionLister);
        menu.add(itm);

        menubar.add(menu);

        menu = new JMenu("Edit");

        itm = new JMenuItem("Undo");
        itm.setActionCommand("undo");
        itm.setAccelerator(KeyStroke.getKeyStroke("ctrl pressed Z"));
        itm.addActionListener(actionLister);
        menu.add(itm);

        itm = new JMenuItem("Redo");
        itm.setActionCommand("redo");
        itm.setAccelerator(KeyStroke.getKeyStroke("ctrl shift pressed Z"));
        itm.addActionListener(actionLister);
        menu.add(itm);

        menubar.add(menu);

        frame.setJMenuBar(menubar);

        // Панель фигур
        JPanel shapesBar = new JPanel();
        shapesBar.setLayout( new BoxLayout(shapesBar, BoxLayout.LINE_AXIS));
        ButtonGroup shapesButtons = new ButtonGroup();

        for (int i = 0; i < shapeFactories.size() ; i++) {
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
            if (i == 5) {
                shapesBar.add(new JLabel("  Plugins: "));
            }
        }
        shapesBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Панель свойств
        JPanel optionsBar = new JPanel();
        optionsBar.setLayout( new BoxLayout(optionsBar, BoxLayout.LINE_AXIS));

            // Цвет заливки
        optionsBar.add( new JLabel("  Fill color: "));
        fillColorBtn = new JButton("    ");
        fillColorBtn.setBackground(fillColor);
        fillColorBtn.setActionCommand("chooseFillColor");
        fillColorBtn.addActionListener(actionLister);
        optionsBar.add(fillColorBtn);

            // Цвет обводки
        optionsBar.add( new JLabel("  Stroke color: "));
        strokeColorBtn = new JButton("    ");
        strokeColorBtn.setBackground(strokeColor);
        strokeColorBtn.setActionCommand("chooseStrokeColor");
        strokeColorBtn.addActionListener(actionLister);
        optionsBar.add(strokeColorBtn);

            // Толщина обводки
        SpinnerNumberModel model = new SpinnerNumberModel(8.0, 0.0, 100.0, 0.1);
        JSpinner strokeWidthSp = new JSpinner(model);
        strokeWidthSp.setMaximumSize( new Dimension(70, 70));
        strokeWidthSp.addChangeListener(e -> strokeWidth = ((Double) strokeWidthSp.getValue()).floatValue());
        strokeWidth = ((Double) strokeWidthSp.getValue()).floatValue();
        optionsBar.add(strokeWidthSp);

            // Кол-во сторон правильного многоугольника
        edgesLabel = new JLabel("  Edges: ");
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

        // Рисование фигур
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
                        if (e.isShiftDown()) {
                            if (Math.abs(x - shape.x) > Math.abs(y - shape.y)) {
                                if (y > shape.y)
                                    shape.y1 = shape.y + Math.abs(x - shape.x);
                                else
                                    shape.y1 = shape.y - Math.abs(x - shape.x);
                            } else {
                                if (x > shape.x)
                                    shape.x1 = shape.x + Math.abs(y - shape.y);
                                else
                                    shape.x1 = shape.x - Math.abs(y - shape.y);
                            }
                        }
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
