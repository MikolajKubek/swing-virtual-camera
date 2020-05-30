import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class View extends JFrame implements KeyListener {
    public Hashtable<Integer, Boolean> pressed = new Hashtable<>();

    public View(String title, int width, int height){
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(width, height);
        this.setTitle(title);
        addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
            this.pressed.put(keyEvent.getKeyCode(), true);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        this.pressed.remove(keyEvent.getKeyCode());
    }

    private static List<Polygon> readConfig(String configFile){
        HashMap<Integer, Point3D> map = new HashMap<>();
        List<Polygon> polygons = new ArrayList<>();
        Color color = new Color(0, 0 ,0);
        int index = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile))){
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] splitLine = line.split(" ");
                switch (splitLine[0]){
                    case "c":
                        int r = Integer.parseInt(splitLine[1]);
                        int g = Integer.parseInt(splitLine[2]);
                        int b = Integer.parseInt(splitLine[3]);
                        color = new Color(r, g, b);
                        break;
                    case "p":
                        int x = Integer.parseInt(splitLine[1]);
                        int y = Integer.parseInt(splitLine[2]);
                        int z = Integer.parseInt(splitLine[3]);
                        map.put(index++, new Point3D(x, y, z));
                        break;
                    case "i":
                        Point3D[] points = new Point3D[splitLine.length - 1];
                        for (int i = 0; i < splitLine.length - 1; i++){
                            int key = Integer.parseInt(splitLine[i + 1]);
                            if (map.containsKey(key)) {
                                points[i] = map.get(key);
                            }
                            else {
                                System.out.println("Index " + key + " not found");
                            }
                        }
                        polygons.add(new Polygon(color, points));
                        break;
                }
            }
        }
        catch (IOException e){
            System.out.println("File not found");
        }

        return polygons;
    }

    public static void main(String[] args){
        int movementSpeed = 5;
        int rotationSpeed = 1;
        List<Polygon> polygons = readConfig(args[0]);
        Canvas canvas = new Canvas();
        Scene scene = new Scene();
        scene.setPolygons(polygons);
        Camera camera = new Camera(scene);
        View view = new View("grak", 1000, 700);

        Timer timer = new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                for (Integer code: view.pressed.keySet()){
                    switch (code){
                        case KeyEvent.VK_CONTROL:
                            camera.move(0, -movementSpeed, 0);
                            break;
                        case KeyEvent.VK_SPACE:
                            camera.move(0, movementSpeed, 0);
                            break;
                        case KeyEvent.VK_A:
                            camera.move(movementSpeed, 0, 0);
                            break;
                        case KeyEvent.VK_D:
                            camera.move(-movementSpeed, 0, 0);
                            break;
                        case KeyEvent.VK_S:
                            camera.move(0, 0, movementSpeed);
                            break;
                        case KeyEvent.VK_W:
                            camera.move(0, 0, -movementSpeed);
                            break;
                        case KeyEvent.VK_DOWN:
                            camera.rotate(rotationSpeed, 0, 0);
                            break;
                        case KeyEvent.VK_UP:
                            camera.rotate(-rotationSpeed, 0, 0);
                            break;
                        case KeyEvent.VK_LEFT:
                            camera.rotate(0, rotationSpeed, 0);
                            break;
                        case KeyEvent.VK_RIGHT:
                            camera.rotate(0, -rotationSpeed, 0);
                            break;
                        case KeyEvent.VK_Q:
                            camera.rotate(0, 0, rotationSpeed);
                            break;
                        case KeyEvent.VK_E:
                            camera.rotate(0, 0, -rotationSpeed);
                            break;
                        case KeyEvent.VK_1:
                            camera.increaseFocal(10, 10);
                            break;
                        case KeyEvent.VK_2:
                            camera.increaseFocal(-10, -10);
                            break;
                    }
                }
                if (view.pressed.size() > 0) {
                    canvas.setScene(camera.projectScene(canvas.getWidth() / 2, canvas.getHeight() / 2));
                    canvas.repaint();
                }
            }
        });
        view.getContentPane().add(canvas);
        canvas.setScene(camera.projectScene(canvas.getWidth() / 2, canvas.getHeight() / 2));
        canvas.paint(canvas.getGraphics());
        canvas.setScene(camera.projectScene(canvas.getWidth() / 2, canvas.getHeight() / 2));
        canvas.repaint();

        timer.start();
    }
}
