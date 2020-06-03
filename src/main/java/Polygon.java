import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Polygon {
    public double weight = 0;
    private List<Point3D> points;
    private Color color;

    public Polygon(Color color, Point3D... points){
        this(points);
        this.color = color;
    }

    public Polygon(Point3D... points){
        this.points = new ArrayList<>();
        for (Point3D point: points){
            if (point != null) {
                this.points.add(point);
            }
        }
    }

    public List<Point3D> getPoints() {
        return points;
    }

    public Color getColor() {
        return this.color;
    }

    public void calculateWeight() {
        double weight = 0;
        for (Point3D point: this.points){
            weight += Math.sqrt(point.x * point.x + point.y * point.y + point.z * point.z);
        }
        this.weight = weight / this.points.size();
    }

    public int getPointsSize(){
        return this.points.size();
    }

    public Point3D getPoint(int index){
        return this.points.get(index % this.points.size());
    }

    public AwtPolygonData getAwtPolygonData(int offsetX, int offsetY){
        int pointsSize = this.getPointsSize();
        int[] xs = new int[pointsSize];
        int[] ys = new int[pointsSize];
        int i = 0;
        for (Point3D point: this.points){
            xs[i] = (int)point.x + offsetX;
            ys[i++] = (int)point.y + offsetY;
        }
        return new AwtPolygonData(xs, ys, pointsSize);
    }

    @Override
    public String toString() {
        return "Polygon{}";
    }

    public class AwtPolygonData{
        private int[] xs;
        private int[] ys;
        private int size;

        public AwtPolygonData(int[] xs, int[] ys, int size){
            this.xs = xs;
            this.ys = ys;
            this.size = size;
        }

        public int[] getXs() {
            return xs;
        }

        public int[] getYs() {
            return ys;
        }

        public int getSize() {
            return size;
        }
    }
}
