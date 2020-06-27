import java.util.ArrayList;
import java.util.List;

public class BspTree {
    private Polygon polygon;
    private BspTree leftChild;
    private BspTree rightChild;

    public BspTree(Polygon polygon){
        this.polygon = polygon;
        this.leftChild = null;
        this.rightChild = null;
    }

    public List<Polygon> getValuesFromLeft(){
        List<Polygon> values = new ArrayList<>();
        if (this.leftChild != null){
            values.addAll(this.leftChild.getValuesFromLeft());
        }
        if (this.polygon != null) {
            values.add(this.polygon);
        }
        if (this.rightChild != null){
            values.addAll(this.rightChild.getValuesFromLeft());
        }

        return values;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setLeftChild(BspTree leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(BspTree rightChild) {
        this.rightChild = rightChild;
    }
}
