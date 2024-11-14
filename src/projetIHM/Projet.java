package projetIHM;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

class MainFrame extends JFrame {
    private ArrayList<CustomShape> shapes = new ArrayList<>();
    private String selectedShape = null;
    private Point mouseClickPoint = null;

    public MainFrame() {
        setTitle("IHM Project figures géométriques");
        setSize(1200, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel controlPanel = new JPanel();

        JButton triangleButton = createStyledButton("TRIANGLE");
        JButton circleButton = createStyledButton("CERCLE");
        JButton rectangleButton = createStyledButton("RECTANGLE");
        JButton squareButton = createStyledButton("CARRE");
        JButton clearButton = createStyledButton("EFFACER");
        clearButton.addActionListener(e -> clearShapes());

        controlPanel.add(triangleButton);
        controlPanel.add(circleButton);
        controlPanel.add(rectangleButton);
        controlPanel.add(squareButton);
        controlPanel.add(clearButton);

        add(controlPanel, BorderLayout.NORTH);
        add(new DrawingPanel(), BorderLayout.CENTER);
    }

    private void clearShapes() {
        shapes.clear();
        repaint();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);

        button.setForeground(Color.BLACK);
        button.setBackground(new Color(50, 100, 200)); // Utilize your preferred color
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setContentAreaFilled(true);
            }

            public void mouseExited(MouseEvent evt) {
                button.setContentAreaFilled(false);
            }
        });

        button.addActionListener(e -> selectedShape = text);

        return button;
    }

    public class CustomShape {
        private Shape shape;
        private Color color;

        public CustomShape(Shape shape, Color color) {
            this.shape = shape;
            this.color = color;
        }

        public Shape getShape() {
            return shape;
        }

        public Color getColor() {
            return color;
        }

        public void moveBy(int dx, int dy) {
            if (shape instanceof Rectangle) {
                Rectangle rectangle = (Rectangle) shape;
                rectangle.setLocation(rectangle.x + dx, rectangle.y + dy);
            } else if (shape instanceof Ellipse2D) {
                Ellipse2D ellipse = (Ellipse2D) shape;
                ellipse.setFrame(ellipse.getX() + dx, ellipse.getY() + dy, ellipse.getWidth(), ellipse.getHeight());
            }
        }
    }

    private class DrawingPanel extends JPanel {
        public DrawingPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Color shapeColor = JColorChooser.showDialog(null, "Choisissez une couleur", Color.BLACK);

                    if (selectedShape.equals("CARRE")) {
                        int sideLength = Integer.parseInt(
                                JOptionPane.showInputDialog("Veuillez saisir une dimension pour les côtés :"));
                        shapes.add(
                                new CustomShape(new Rectangle(e.getX(), e.getY(), sideLength, sideLength), shapeColor));
                    } else if (selectedShape.equals("RECTANGLE")) {
                        int width = Integer.parseInt(JOptionPane.showInputDialog("Veuillez saisir la largeur :"));
                        int height = Integer.parseInt(JOptionPane.showInputDialog("Veuillez saisir la longueur :"));
                        shapes.add(new CustomShape(new Rectangle(e.getX(), e.getY(), width, height), shapeColor));
                    } else if (selectedShape.equals("CERCLE")) {
                        int radius = Integer.parseInt(JOptionPane.showInputDialog("Veuillez saisir le rayon :"));
                        shapes.add(new CustomShape(
                                new Ellipse2D.Double(e.getX() - radius, e.getY() - radius, 2 * radius, 2 * radius),
                                shapeColor));
                    } else if (selectedShape.equals("TRIANGLE")) {
                        int x1 = Integer.parseInt(JOptionPane
                                .showInputDialog("Veuillez renseigner le coordonnée x du premier sommet :"));
                        int y1 = Integer.parseInt(JOptionPane
                                .showInputDialog("Veuillez renseigner le coordonnée y du premier sommet :"));
                        int x2 = Integer.parseInt(JOptionPane
                                .showInputDialog("Veuillez renseigner le coordonnée x du deuxième sommet :"));
                        int y2 = Integer.parseInt(JOptionPane
                                .showInputDialog("Veuillez renseigner le coordonnée y du deuxième sommet :"));
                        int x3 = Integer.parseInt(JOptionPane
                                .showInputDialog("Veuillez renseigner le coordonnée x du troisième sommet :"));
                        int y3 = Integer.parseInt(JOptionPane
                                .showInputDialog("Veuillez renseigner le coordonnée y du troisième sommet :"));

                        int[] xPoints = { x1, x2, x3 };
                        int[] yPoints = { y1, y2, y3 };

                        shapes.add(new CustomShape(new Polygon(xPoints, yPoints, 3), shapeColor));
                    }
                    repaint();
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                  
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (selectedShape != null) {
                        if (mouseClickPoint != null) {
                            int dx = e.getX() - mouseClickPoint.x;
                            int dy = e.getY() - mouseClickPoint.y;
                            if (selectedShape != null) {
                                for (CustomShape customShape : shapes) {
                                    if (customShape.getShape().contains(mouseClickPoint)) {
                                        customShape.moveBy(dx, dy);
                                        break;
                                    }
                                }
                                mouseClickPoint = e.getPoint();
                                repaint();
                            }
                        }
                    }
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    mouseClickPoint = e.getPoint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    mouseClickPoint = null;
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

         
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            for (CustomShape customShape : shapes) {
                g2d.setColor(customShape.getColor());
                g2d.draw(customShape.getShape());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
