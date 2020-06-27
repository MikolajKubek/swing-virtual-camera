import java.util.*;

public class Scene {
    private List<Polygon> polygons;
    public int cameraX = 0;
    public int cameraY = 0;
    public int cameraZ = 0;

    public Scene(){
        this.polygons = new ArrayList<>();
    }

    public Scene(List<Polygon> polygons){
        this.polygons = polygons;
    }

    public void setCamera(int x, int y, int z){
        this.cameraX = x;
        this.cameraY = y;
        this.cameraZ = z;
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

    private Point3D getLinePlaneIntersection(Plane plane, Point3D pointA, Point3D pointB) {
        Point3D normalVector = new Point3D(plane.a, plane.b, plane.c);
        Point3D lineDirection = pointB.subtract(pointA);
        double d = plane.point.subtract(pointA).dotProduct(normalVector) / lineDirection.dotProduct(normalVector);
        Point3D intersection = new Point3D(pointA.x + d * lineDirection.x, pointA.y + d * lineDirection.y, pointA.z + d * lineDirection.z);
        if (intersection.containsIllegalValues()){
            return pointA;
        }
        return intersection;
    }

    private PolygonPair splitPolygon(Plane plane, Polygon polygon, List<Point3D> cameraSidePoints, List<Point3D> oppositeSidePoints) {
        Set<Point3D> newCameraSidePoints = new HashSet<>();
        Set<Point3D> newOppositeSidePoints = new HashSet<>();

        for (int i = 0; i < polygon.getPointsSize(); i++){
            Point3D pointA = polygon.getPoint(i);
            Point3D pointB = polygon.getPoint(i+1);

            if (cameraSidePoints.contains(pointA) && cameraSidePoints.contains(pointB)){
                newCameraSidePoints.add(pointA);
                newCameraSidePoints.add(pointB);
            }
            else if (oppositeSidePoints.contains(pointA) && oppositeSidePoints.contains(pointB)){
                newOppositeSidePoints.add(pointA);
                newOppositeSidePoints.add(pointB);
            }
            else if (cameraSidePoints.contains(pointA) && !cameraSidePoints.contains(pointB)){
                Point3D intersection = getLinePlaneIntersection(plane, pointA, pointB);
                newCameraSidePoints.add(pointA);
                newCameraSidePoints.add(intersection);
                newOppositeSidePoints.add(intersection);
                newOppositeSidePoints.add(pointB);
            }
            else{
                Point3D intersection = getLinePlaneIntersection(plane, pointA, pointB);
                newOppositeSidePoints.add(pointA);
                newOppositeSidePoints.add(intersection);
                newCameraSidePoints.add(intersection);
                newCameraSidePoints.add(pointB);
            }
        }

        return new PolygonPair(new Polygon(newCameraSidePoints.toArray(new Point3D[0])), new Polygon(newOppositeSidePoints.toArray(new Point3D[0])));
    }

    private BspTree buildTree(List<Polygon> polygons){
        if (polygons.size() == 0){
            return new BspTree(null);
        }

        Polygon polygon = polygons.get(0);
        BspTree tree = new BspTree(polygon);
        Plane plane = null;
        try {
            plane = polygon.getPlane();
        }
        catch (IllegalArgumentException e){
            return buildTree(polygons.subList(1, polygons.size()));
        }

        List<Polygon> cameraSidePolygons = new ArrayList<>();
        List<Polygon> oppositeSidePolygons = new ArrayList<>();

        for (int i = 1; i < polygons.size(); i++){
            polygon = polygons.get(i);

            List<Point3D> cameraSidePoints = new ArrayList<>();
            List<Point3D> oppositeSidePoints = new ArrayList<>();

            for (Point3D point3D: polygon.getPoints()){
                if (point3D.containsIllegalValues()){
                    continue;
                }
                double cameraPointRelation = plane.getPointRelation(cameraX, cameraY, cameraZ);
                double pointRelation = plane.getPointRelation(point3D);

                if (pointRelation * cameraPointRelation >= 0){
                    cameraSidePoints.add(point3D);
                }
                else {
                    oppositeSidePoints.add(point3D);
                }
            }
            if (oppositeSidePoints.size() == 0){
                cameraSidePolygons.add(polygon);
            }
            else if (cameraSidePoints.size() == 0){
                oppositeSidePolygons.add(polygon);
            }
            else {
//                PolygonPair pair = splitPolygon(plane, polygon, cameraSidePoints, oppositeSidePoints);
//                cameraSidePolygons.add(pair.cameraSidePolygon);
//                oppositeSidePolygons.add(pair.oppositeSidePolygon);
                if (polygon.weight <= tree.getPolygon().weight) {
                    cameraSidePolygons.add(polygon);
                }
                else {
                    oppositeSidePolygons.add(polygon);
                }
            }

//            cameraSidePolygons.add(polygon);
        }

        tree.setLeftChild(buildTree(oppositeSidePolygons));
        tree.setRightChild(buildTree(cameraSidePolygons));

        return tree;
    }

    public synchronized List<Polygon> getSortedPolygons(){
//        this.sortScene();
        BspTree tree = this.buildTree(this.polygons);
        return tree.getValuesFromLeft();
    }

    public synchronized void setPolygons(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    public synchronized List<Polygon> getPolygons() {
        return polygons;
    }

    private class PolygonPair{
        public Polygon cameraSidePolygon;
        public Polygon oppositeSidePolygon;

        public PolygonPair(Polygon cameraSidePolygon, Polygon oppositeSidePolygon) {
            this.cameraSidePolygon = cameraSidePolygon;
            this.oppositeSidePolygon = oppositeSidePolygon;
        }
    }
}
