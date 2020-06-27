import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.Math;

public class Camera {
    private Scene scene;
    private int focalX = 250;
    private int focalY = 250;

    public int cameraX = 0;
    public int cameraY = 0;
    public int cameraZ = 0;

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

    public synchronized void move(int x, int y, int z){
        this.cameraX += x;
        this.cameraY += y;
        this.cameraZ += z;
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

    public synchronized void rotate(int rotationX, int rotationY, int rotationZ){
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

    public synchronized Scene projectScene(int offsetX, int offsetY){
        List<Polygon> transformedPolygons = new ArrayList<>();
        RealMatrix focalMatrix = new Array2DRowRealMatrix(new double[][]{
                {this.focalX, 0, 0, 0},
                {0, this.focalY, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        for (Polygon polygon : this.scene.getSortedPolygons()) {
            polygon.calculateWeight();
            double weight = polygon.weight;
            int pointsSize = polygon.getPointsSize();
            Point3D[] points = new Point3D[pointsSize];
            for (int i = 0; i < pointsSize; i++){
                RealMatrix result = focalMatrix.multiply(polygon.getPoint(i).toRealMatrix());
                double z = result.getEntry(2, 0);
                double scale = z <= 0 ? Double.MIN_VALUE : z;
                if(z > -170) {
                    points[i] = new Point3D(
                            (result.getEntry(0, 0) / scale) + offsetX,
                            (result.getEntry(1, 0) / scale) + offsetY,
                            z
                    );
                }
            }
            Polygon polygon1 = new Polygon(polygon.getColor(), points);
            polygon1.weight = weight;
            transformedPolygons.add(polygon1);
        }
        Scene newScene = new Scene(transformedPolygons);
        newScene.setCamera(this.cameraX, this.cameraY, this.cameraZ);
        return newScene;
    }


        @Override
    public String toString() {
        return "Camera{}";
    }
}
