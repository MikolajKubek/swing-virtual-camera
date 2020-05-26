import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


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

    private static List<Line> readConfig(String configFile){
        HashMap<Integer, Point3D> map = new HashMap<>();
        List<Line> lines = new ArrayList<>();
        int index = 0;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile))){
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] splitLine = line.split(" ");
                switch (splitLine.length){
                    case 3:
                        int x = Integer.parseInt(splitLine[0]);
                        int y = Integer.parseInt(splitLine[1]);
                        int z = Integer.parseInt(splitLine[2]);
                        map.put(index++, new Point3D(x, y, z));
                        break;
                    case 2:
                        int indexA = Integer.parseInt(splitLine[0]);
                        int indexB = Integer.parseInt(splitLine[1]);
                        if (map.containsKey(indexA) && map.containsKey(indexB)){
                            lines.add(new Line(map.get(indexA), map.get(indexB)));
                        }
                        else {
                            System.out.println("One of indices " + indexA + " " + indexB + " not found");
                        }
                        break;
                }
            }
        }
        catch (IOException e){
            System.out.println("File not found");
        }

        return lines;
    }

    public static void main(String[] args){
        int movementSpeed = 5;
        int rotationSpeed = 1;
        List<Line> lines = readConfig(args[0]);
        Canvas canvas = new Canvas();
        Scene scene = new Scene();
        scene.setLines(lines);
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
                    canvas.setLines(camera.projectScene(canvas.getWidth(), canvas.getHeight()));
                    canvas.repaint();
                }
            }
        });
        view.getContentPane().add(canvas);
        canvas.setLines(camera.projectScene(canvas.getWidth(), canvas.getHeight()));
        canvas.paint(canvas.getGraphics());

        timer.start();
    }
}
