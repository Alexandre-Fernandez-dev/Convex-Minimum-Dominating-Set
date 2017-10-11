
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class VertexDS {
	
	public Point p;
	public ArrayList<VertexDS> neighbors = new ArrayList<VertexDS>();
	public int id;
	public Color color = Color.gray;
	
	public DisjointSetElement<VertexDS> dse;

	public VertexDS(Point p, int id) {
		super();
		this.p = p;
		this.id = id;
	}

	public void makeDSE() {
		this.dse = new DisjointSetElement<VertexDS>(this);
	}
	
	public int degree() {
		return neighbors.size();
	}

}
