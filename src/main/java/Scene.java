import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<Line> lines;

    public Scene(){
        this.lines = new ArrayList<>();
        lines.add(new Line(0, 0, 0, 100, 100, 0));
        lines.add(new Line(10, 0, 0, 110, 100, 0));
        lines.add(new Line(20, 0, 0, 120, 100, 0));
        lines.add(new Line(30, 0, 0, 130, 100, 0));
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }
}
