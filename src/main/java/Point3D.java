import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

public class Point3D {
    public double x;
    public double y;
    public double z;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D(RealMatrix matrix) {
        if (matrix.getColumnDimension() == 1 && matrix.getRowDimension() >= 3){
            this.x = matrix.getEntry(0, 0);
            this.y = matrix.getEntry(1, 0);
            this.z = matrix.getEntry(2, 0);
        }
        else {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }
    }

    public RealMatrix toRealMatrix(){
        return new Array2DRowRealMatrix(new double[][]{
                {this.x},
                {this.y},
                {this.z},
                {1}
        });
    }

    public boolean containsInfinity(){
        return this.x == Double.NEGATIVE_INFINITY || this.x == Double.POSITIVE_INFINITY || this.y == Double.NEGATIVE_INFINITY || this.y == Double.POSITIVE_INFINITY || this.z == Double.NEGATIVE_INFINITY || this.z == Double.POSITIVE_INFINITY;
    }

    @Override
    public String toString() {
        return "Point3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
