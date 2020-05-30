import javax.swing.*;
import java.awt.*;

public class Canvas extends JComponent {
    private Scene scene;
    private Color primaryColor = new Color(248, 23, 255);
    private Color secondaryColor = new Color(13, 1, 64);

    public Canvas(){
        super();
        this.scene = new Scene();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    private void drawEdges(Graphics g){
        g.setColor(this.secondaryColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(this.primaryColor);
        this.scene.sortScene();
        for (Polygon polygon : this.scene.getPolygons()) {
            Polygon.AwtPolygonData awtPolygonData = polygon.getAwtPolygonData(0, 0);
            g.setColor(polygon.getColor());
            System.out.println(polygon.getColor());
            g.drawPolygon(awtPolygonData.getXs(), awtPolygonData.getYs(), awtPolygonData.getSize());
        }
    }

    @Override
    public synchronized void paint(Graphics graphics) {
        drawEdges(graphics);
    }
}
