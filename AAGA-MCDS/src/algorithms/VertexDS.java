package algorithms;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class VertexDS {
	
	public Point p;
	public ArrayList<VertexDS> neighbors = new ArrayList<VertexDS>();
	public int id;
	public Color color = Color.gray;
	
	public Color misColor = Color.white;
	public boolean isActive = false;
	
	public DisjointSetElement<VertexDS> bbcomp;

	public VertexDS(Point p, int id) {
		super();
		this.p = p;
		this.id = id;
	}

	public void makeBBComp() {
		this.bbcomp = new DisjointSetElement<VertexDS>(this);
	}
	
	public int degree() {
		return neighbors.size();
	}
	
	public int getEffDeg() {
	    int i = 0;
	    for (VertexDS v : this.neighbors)
		if (v.misColor == color.white)
		    i++;
	    return i;
	}
	

}
