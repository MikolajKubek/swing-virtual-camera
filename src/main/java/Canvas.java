import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Canvas extends JComponent {
    private List<Line> lines;
    private Color primaryColor = new Color(248, 23, 255);
    private Color secondaryColor = new Color(13, 1, 64);

    public Canvas(){
        super();
        this.lines = new ArrayList<>();
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public void paint(Graphics g){
        g.setColor(this.secondaryColor);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(this.primaryColor);
        int halfWidth = this.getWidth() / 2;
        int halfHeight = this.getHeight() / 2;
        for (Line line: this.lines) {
            g.drawLine((int) line.a.x + halfWidth, (int) line.a.y + halfHeight, (int) line.b.x + halfWidth, (int) line.b.y + halfHeight);
        }
    }
}
