import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Scene {
    private List<Polygon> polygons;

    public Scene(){
        this.polygons = new ArrayList<>();
    }

    public Scene(List<Polygon> polygons){
        this.polygons = polygons;
    }

    public void calculateWeights(){
        for (Polygon polygon : this.polygons){
            polygon.calculateWeight();
        }
    }

    public void sortScene(){
        polygons.sort(new Comparator<Polygon>() {
            @Override
            public int compare(Polygon polygon, Polygon t1) {
                if (t1.weight == polygon.weight) return 0;
                else if (t1.weight > polygon.weight) return 1;
                else return -1;
            }
        });
    }

    public synchronized void setPolygons(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    public synchronized List<Polygon> getPolygons() {
        return polygons;
    }
}
