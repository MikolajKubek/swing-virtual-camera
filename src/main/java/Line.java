public class Line {
    public Point3D a;
    public Point3D b;

    public Line(int x1, int y1, int z1, int x2, int y2, int z2){
        this.a = new Point3D(x1, y1, z1);
        this.b = new Point3D(x2, y2, z2);
    }

    public Line(Point3D a, Point3D b){
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString() {
        return "Line{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}
