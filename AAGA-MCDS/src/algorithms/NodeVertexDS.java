package algorithms;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.GrayFilter;

public class NodeVertexDS extends VertexDS {
	
	public static int count = 0;//REMOVE TESTS
	
	public ArrayList<NodeVertexDS> neighborsTree = new ArrayList<NodeVertexDS>();
	public ArrayList<NodeVertexDS> children = new ArrayList<NodeVertexDS>();
	public NodeVertexDS parent = null;
	
	public ArrayList<NodeVertexDS> neighbors = new ArrayList<NodeVertexDS>();
	private int rank = Integer.MAX_VALUE;
	
	public NodeVertexDS(Point p, int id) {
		super(p, id);
		this.color = Color.WHITE;
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
	
	public static void buildMIS(NodeVertexDS root) {
		root.color = Color.BLACK;
		ArrayList<NodeVertexDS> fifoNodes = new ArrayList<NodeVertexDS>();
		fifoNodes.add(root);
		while(!fifoNodes.isEmpty()) {
			NodeVertexDS node = fifoNodes.remove(0);
			
			boolean countedOneBlack = false;
			for(NodeVertexDS n : node.neighbors) {
				if(n.color == Color.BLACK) countedOneBlack = true;
			}
			if(countedOneBlack) {
				node.color = Color.GRAY;
				fifoNodes.addAll(node.children);
			} else {
				boolean allParentGray = true;
				for(NodeVertexDS n : node.neighbors) {
					if(n.rank < node.rank && !n.color.equals(Color.GRAY)) allParentGray = false;
				}
				if(allParentGray) {
					if(node.color.equals(Color.WHITE))
						node.color = Color.BLACK;
					fifoNodes.addAll(node.children);
				}
			}
		}
	}
	
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
