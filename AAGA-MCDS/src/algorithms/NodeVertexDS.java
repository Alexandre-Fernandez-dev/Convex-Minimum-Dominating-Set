package algorithms;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.GrayFilter;

/**
 * This class represents a vertex with his neighbors in a graph
 * AND a node of a spanning tree built upon the graph structure.
 * 'neighbors' contains the neighbors of this vertex in the graph.
 * 
 * Some of his field holds a Tree Data Structure for the MIS algorithm using a spanning tree (MIS 2).
 * 'neigborsTree' are the neighbors of this vertex in the graph that are his children or his parent in the tree,
 * 'neigborTree' is split between 'parent' and 'children' in the process
 * @author alexandre
 *
 */
public class NodeVertexDS {
	
	public static int count = 0;//TESTS
	
	public ArrayList<NodeVertexDS> neighbors = new ArrayList<NodeVertexDS>();
	public Point p;
	public int id;
	public Color color = Color.gray;
	public DisjointSetElement<NodeVertexDS> disjointSetElem;
	
	//UTILISES POUR LA VERSION 2 DU MIS (STRUCTURE D'ARBRE)
	public int rank = Integer.MAX_VALUE;
	public ArrayList<NodeVertexDS> neighborsTree = new ArrayList<NodeVertexDS>();
	public ArrayList<NodeVertexDS> children = new ArrayList<NodeVertexDS>();
	public NodeVertexDS parent = null;
	
	//UTILISES POUR LA VERSION 1 DU MIS
	public Color misColor = Color.white;
	public boolean isActive = false;
	
	
	public NodeVertexDS(Point p, int id) {
		this.p = p;
		this.id = id;
		this.color = Color.WHITE;
	}
	
	public void makeDSElement() {
		this.disjointSetElem = new DisjointSetElement<NodeVertexDS>(this);
	}

	public int degree() {
		return neighbors.size();
	}

	public void initiateRank(int i) {
		this.rank = i;
		for(NodeVertexDS vn : neighborsTree) {
			if(vn.rank==Integer.MAX_VALUE) {
				vn.initiateRank(i+1);
				children.add(vn);
			} else {
				if(vn.rank < this.rank) {
					parent = vn;
				}
			}
		}
	}
	
	//UTILISE POUR LA VERSION 1 DU MIS
	public int getEffDeg() {
	    int i = 0;
	    for (NodeVertexDS v : this.neighbors)
		if (v.misColor == color.white)
		    i++;
	    return i;
	}
	
	
	//TESTS MIS V2
	public void checkRoot() {
		this.count = 0;
		assert(this.parent == null);
		assert(this.children.size() == this.neighborsTree.size());
		count++;
		for(NodeVertexDS ch : this.children) {
			ch.checkTree();
		}
	}
	
	public void checkTree() {
		assert(this.parent != null);
		assert(this.children.size() + 1 == this.neighborsTree.size());
		count++;
		for(NodeVertexDS ch : this.children) {
			ch.checkTree();
		}
	}

}
