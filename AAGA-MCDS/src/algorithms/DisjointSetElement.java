package algorithms;
public class DisjointSetElement<T> {
	private static int genindex = 0;
	private T data;
	private DisjointSetElement<T> parent;
	private int rank;
	public int index;

	public DisjointSetElement(T elem) {
		this.data = elem;
		this.parent = this;
		this.rank = 0;
		this.index = genindex++;
	}
	
	public T getData() {
		return data;
	}
	
	public void incRank() {
		rank++;
	}
	
	public void setParent(DisjointSetElement<T> newp) {
		this.parent = newp;
	}

	public DisjointSetElement<T> find() {
		if(this.parent != this) {
			this.parent = this.parent.find();
		}
		return this.parent;
	}
	
	public void union(DisjointSetElement<T> el2) {
		DisjointSetElement<T> xroot = this.find();
		DisjointSetElement<T> yroot = el2.find();
		
		if(xroot == yroot) return;
		
		if(xroot.rank < yroot.rank)
			xroot.parent = yroot;
		else if(xroot.rank > yroot.rank)
			yroot.parent = xroot;
		else {
			yroot.parent = xroot;
			xroot.rank++;
		}
	}
	
	
	public static void unionRoot(DisjointSetElement xroot, DisjointSetElement yroot) {
		if(xroot == yroot) return;
		
		if(xroot.rank < yroot.rank)
			xroot.parent = yroot;
		else if(xroot.rank > yroot.rank)
			yroot.parent = xroot;
		else {
			yroot.parent = xroot;
			xroot.rank++;
		}
	}
}