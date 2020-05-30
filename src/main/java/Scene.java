import java.util.ArrayList;
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

    public void sortScene(){
        for (Polygon polygon : this.polygons){
            polygon.calculateWeight();
        }

        polygons.sort(new Comparator<Polygon>() {
            @Override
            public int compare(Polygon polygon, Polygon t1) {
                if (t1.weight == polygon.weight) return 0;
                else if (t1.weight > polygon.weight) return 1;
                else return -1;
            }
        });
    }

    public void setPolygons(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }
}
