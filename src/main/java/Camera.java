import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class Camera {
    private Scene scene;
    private int focalX = 250;
    private int focalY = 250;

    public Camera(Scene scene) {
        this.scene = scene;
    }

    public void increaseFocal(int x, int y){
        this.focalX += x;
        this.focalY += y;

        if (this.focalX <= 0){
            this.focalX = 1;
        }
        if (this.focalY <= 0){
            this.focalY = 1;
        }
    }

    public static RealMatrix getTranslationMatrix(int x, int y, int z){
        return new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0, x},
                {0, 1, 0, y},
                {0, 0, 1, z},
                {0, 0, 0, 1}
        });
    }

    public static RealMatrix getXRotationMatrix(int angle){
        double sin = Math.sin(angle * Math.PI / 180);
        double cos = Math.cos(angle * Math.PI / 180);

        return new Array2DRowRealMatrix(new double[][]{
                {1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}
        });
    }

    public static RealMatrix getYRotationMatrix(int angle){
        double sin = Math.sin(angle * Math.PI / 180);
        double cos = Math.cos(angle * Math.PI / 180);

        return new Array2DRowRealMatrix(new double[][]{
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}
        });
    }

    public static RealMatrix getZRotationMatrix(int angle){
        double sin = Math.sin(angle * Math.PI / 180);
        double cos = Math.cos(angle * Math.PI / 180);

        return new Array2DRowRealMatrix(new double[][]{
                {cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
    }

    public void move(int x, int y, int z){
        List<Polygon> transformedPolygons = new ArrayList<>();
        RealMatrix translationMatrix = this.getTranslationMatrix(x, y, z);
        for (Polygon polygon : this.scene.getPolygons()) {
            int pointsSize = polygon.getPointsSize();
            Point3D[] points = new Point3D[pointsSize];
            for (int i = 0; i < pointsSize; i++){
                points[i] = new Point3D(translationMatrix.multiply(polygon.getPoint(i).toRealMatrix()));
            }
            transformedPolygons.add(new Polygon(polygon.getColor(), points));
        }
        this.scene.setPolygons(transformedPolygons);
    }

    public void rotate(int rotationX, int rotationY, int rotationZ){
        List<Polygon> transformedPolygons = new ArrayList<>();
        RealMatrix rotationMatrix = this.getXRotationMatrix(rotationX).multiply(this.getYRotationMatrix(rotationY)).multiply(this.getZRotationMatrix(rotationZ));
        for (Polygon polygon : this.scene.getPolygons()) {
            int pointsSize = polygon.getPointsSize();
            Point3D[] points = new Point3D[pointsSize];
            for (int i = 0; i < pointsSize; i++){
                points[i] = new Point3D(rotationMatrix.multiply(polygon.getPoint(i).toRealMatrix()));
            }
            transformedPolygons.add(new Polygon(polygon.getColor(), points));
        }
        this.scene.setPolygons(transformedPolygons);
    }

    public Scene projectScene(int offsetX, int offsetY){
        List<Polygon> transformedPolygons = new ArrayList<>();
        RealMatrix focalMatrix = new Array2DRowRealMatrix(new double[][]{
                {this.focalX, 0, 0, 0},
                {0, this.focalY, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        for (Polygon polygon : this.scene.getPolygons()) {
            int pointsSize = polygon.getPointsSize();
            Point3D[] points = new Point3D[pointsSize];
            for (int i = 0; i < pointsSize; i++){
                RealMatrix result = focalMatrix.multiply(polygon.getPoint(i).toRealMatrix());
                double scale = result.getEntry(2, 0);
                scale = scale <= 0 ? 1 : scale;
                points[i] = new Point3D(
                        (result.getEntry(0, 0) / scale) + offsetX,
                        (result.getEntry(1, 0) / scale) + offsetY,
                        0
                );
            }
            transformedPolygons.add(new Polygon(polygon.getColor(), points));
        }
        return new Scene(transformedPolygons);
    }


        @Override
    public String toString() {
        return "Camera{}";
    }
}
