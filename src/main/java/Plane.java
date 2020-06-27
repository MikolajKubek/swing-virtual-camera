public class Plane {
    public double a;
    public double b;
    public double c;
    public double d;
    public Point3D point;

    public Plane(Point3D pointA, Point3D pointB, Point3D pointC){
        if (pointA.containsIllegalValues() || pointB.containsIllegalValues() || pointC.containsIllegalValues()){
            throw new IllegalArgumentException();
        }
        Point3D v1 = pointB.subtract(pointA);
        Point3D v2 = pointC.subtract(pointA);

        Point3D normalVector = v1.crossProduct(v2);

        this.a = normalVector.x;
        this.b = normalVector.y;
        this.c = normalVector.z;
        this.d = calculateD(pointA);
        this.point = pointA;

        this.reduce();
    }

    public double calculateD(Point3D point){
        return -1 * this.a * point.x - this.b * point.y - this.c * point.z;
    }

    private void reduce(){
        double denominator = (this.a + this.b + this.c + this.d) / 4;
        if (denominator != 0){
            this.a /= denominator;
            this.b /= denominator;
            this.c /= denominator;
            this.d /= denominator;
        }
    }

    @Override
    public String toString() {
        return "Plane{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                '}';
    }

    public double getPointRelation(double x, double y, double z){
        return a*x + b*y + c*z + d;
    }

    public double getPointRelation(Point3D point3D) {
        return getPointRelation(point3D.x, point3D.y, point3D.z);
    }
}
