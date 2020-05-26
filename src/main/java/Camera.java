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
        List<Line> transformedLines = new ArrayList<>();
        RealMatrix translationMatrix = this.getTranslationMatrix(x, y, z);
        for (Line line: this.scene.getLines()) {
            Point3D newA = new Point3D(translationMatrix.multiply(line.a.toRealMatrix()));
            Point3D newB = new Point3D(translationMatrix.multiply(line.b.toRealMatrix()));
            transformedLines.add(new Line(newA, newB));
        }
        this.scene.setLines(transformedLines);
    }

    public void rotate(int rotationX, int rotationY, int rotationZ){
        List<Line> transformedLines = new ArrayList<>();
        RealMatrix rotationMatrix = this.getXRotationMatrix(rotationX).multiply(this.getYRotationMatrix(rotationY)).multiply(this.getZRotationMatrix(rotationZ));
        for (Line line: this.scene.getLines()) {
            Point3D newA = new Point3D(rotationMatrix.multiply(line.a.toRealMatrix()));
            Point3D newB = new Point3D(rotationMatrix.multiply(line.b.toRealMatrix()));
            transformedLines.add(new Line(newA, newB));
        }
        this.scene.setLines(transformedLines);
    }

    public List<Line> projectScene(int screenWidth, int screenHeight){
        List<Line> transformedLines = new ArrayList<>();
        RealMatrix focalMatrix = new Array2DRowRealMatrix(new double[][]{
                {this.focalX, 0, 0, 0},
                {0, this.focalY, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        for (Line line: this.scene.getLines()) {
            RealMatrix aResult = focalMatrix.multiply(line.a.toRealMatrix());
            RealMatrix bResult = focalMatrix.multiply(line.b.toRealMatrix());
            double aScale = aResult.getEntry(2, 0);
            double bScale = bResult.getEntry(2, 0);
            if (aScale <= 0) {
                aScale = 1;
            }
            if (bScale <= 0) {
                bScale = 1;
            }
            Line newLine = new Line(
                    new Point3D(
                            aResult.getEntry(0, 0) / aScale,
                            aResult.getEntry(1, 0) / aScale,
                            0
                    ),
                    new Point3D(
                            bResult.getEntry(0, 0) / bScale,
                            bResult.getEntry(1, 0) / bScale,
                            0
                    ));
            transformedLines.add(newLine);
        }
        return transformedLines;
    }


        @Override
    public String toString() {
        return "Camera{}";
    }
}
