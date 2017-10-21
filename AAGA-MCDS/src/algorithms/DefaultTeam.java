package algorithms;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

public class DefaultTeam {
	public static int useMIS = 1;

	public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeTreshold) {
		ArrayList<NodeVertexDS> graph = new ArrayList<NodeVertexDS>();

		//BUILD THE GRAPH STRUCTURE
		for (int i = 0; i < points.size(); i++) {
			graph.add(new NodeVertexDS(points.get(i), i));
		}
		for (NodeVertexDS v : graph) {
			for (NodeVertexDS v2 : graph) {
				if (v == v2)
					continue;
				if (v.p.distance(v2.p) < edgeTreshold) {
					v.neighbors.add(v2);
				}
			}
		}
		// START

		// CALCULATE MIS
		ArrayList<NodeVertexDS> MIS = new ArrayList<NodeVertexDS>();
		
		if(DefaultTeam.useMIS  == 1)
			MIS = distributedMIS1(graph);
		else if(DefaultTeam.useMIS == 2)
			MIS = distributedMIS2(graph);
		
		// AT FIRST ALL ELEMENTS OF THE MIS ARE BLACK AND FORM DISJOINT BLACK-BLUE COMPONENTS
		// MAKE A DISJOINT SET ELEMENT REPRESENTING A BLACK-BLUE COMPONENT (BB COMPONENTS)
		for (NodeVertexDS vi : MIS) {
			vi.color = Color.BLACK;
			vi.makeDSElement();
		}

		// GRAY NODES
		ArrayList<NodeVertexDS> grayNodes = (ArrayList<NodeVertexDS>) graph.clone();
		grayNodes.removeAll(MIS);

		for (int i = 5; i >= 2; i--) {
			boolean cont = true;
			while (cont) { //WHILE THERE IS GRAY NODES WITH AT LEAST i BLACK NEIGHBORS IN DIFFERENT BB COMPONENTS
				int newblueId = -1;
				for (int j = 0; j < grayNodes.size(); j++) {
					NodeVertexDS vgray = grayNodes.get(j);
					if (vgray.degree() < i)
						continue;
					
					//COUNT BLACK NEIGBORS
					ArrayList<NodeVertexDS> blackneighbors = new ArrayList<NodeVertexDS>();
					for (NodeVertexDS vgraynei : vgray.neighbors)
						if (vgraynei.color == Color.BLACK)
							blackneighbors.add(vgraynei);
					if (blackneighbors.size() < i)
						continue;
					
					//FIND ROOT NODE INDISJOINT SET OF ALL BLACK NEIGHBORS
					ArrayList<DisjointSetElement<NodeVertexDS>> neiBBcompsroots = new ArrayList<DisjointSetElement<NodeVertexDS>>();

					for (NodeVertexDS blackn : blackneighbors) {
						neiBBcompsroots.add(blackn.disjointSetElem.find());
					}

					//COUNT DIFFERENT BLACK BLUE COMPONENTS ( == number of black neighbors that are in different disjoint set)
					HashSet<Integer> idsbbcomps = new HashSet<Integer>();
					for (DisjointSetElement<NodeVertexDS> rootneibbcomp : neiBBcompsroots) {
						idsbbcomps.add(rootneibbcomp.index);
					}
					if (idsbbcomps.size() < i)
						continue;
					
					vgray.color = Color.BLUE;
					newblueId = j;
					// IF THE NODE HAS BEED TURNED TO BLUE, ALL BLACK BLUE COMPONENTS INDUCED
					// BY ITS BLACK NEIGBORS ARE MERGED
					for (int id = 1; id < neiBBcompsroots.size(); id++) {
						blackneighbors.get(0).disjointSetElem.union(blackneighbors.get(id).disjointSetElem);
					}
					break;
				}
				if (newblueId != -1)
					grayNodes.remove(newblueId);
				else //NEXT FOR LOOP : DECREASE i
					cont = false;
			}

		}

		ArrayList<Point> result = new ArrayList<Point>();
		for (NodeVertexDS v : graph) {
			if (v.color.equals(Color.BLUE))
				result.add(v.p);
			if (v.color.equals(Color.BLACK))
				result.add(v.p);
		}
		return result;
	}
	
	public ArrayList<NodeVertexDS> distributedMIS2(ArrayList<NodeVertexDS> points) {
		ArrayList<NodeVertexDS> graph = (ArrayList<NodeVertexDS>) points.clone();
		for (int i = 0; i < graph.size(); i++) {
			graph.get(i).makeDSElement();
		}
				
		//BUILD A SPANNING TREE (KRUSKAL USING DISJOINT SET AND WITHOUT CARE OF EDGE LENGTH)
		for (NodeVertexDS v : graph) {
			for(NodeVertexDS vn : v.neighbors) {
				if(!v.disjointSetElem.find().equals(vn.disjointSetElem.find())) {
					v.neighborsTree.add(vn);
					vn.neighborsTree.add(v);
					v.disjointSetElem.union(vn.disjointSetElem);
				}
			}
		}
		
		//CHOOSE ARBITRARY ROOT NODE FOR THE TREE
		NodeVertexDS root = graph.get(0);
		
		//INITIATE TREE DATA STRUCTURE
		//(converts treeNeighbors into parent and children and instanciate the int rank)
		root.initiateRank(0);
		
		//root.checkRoot(); TEST
				
		//BUILD FINAL MIS USING COLORS AND SPANNING TREE + GRAPH STRUCTURE
		buildColorsMIS2(root);
		
		ArrayList<NodeVertexDS> result = new ArrayList<NodeVertexDS>();
		for(NodeVertexDS v : graph) {
			if(v.color.equals(Color.BLACK))
				result.add(v);
		}
		return result;
	}
	
	private static void buildColorsMIS2(NodeVertexDS root) {
		//ROOT IS MARKED BLACK
		root.color = Color.BLACK;
		ArrayList<NodeVertexDS> fifoNodes = new ArrayList<NodeVertexDS>();
		fifoNodes.add(root);
		
		//BREADTH-FIRST SEARCH
		while(!fifoNodes.isEmpty()) {
			NodeVertexDS node = fifoNodes.remove(0);
			
			boolean countedOneBlack = false;
			for(NodeVertexDS n : node.neighbors) {
				if(n.color == Color.BLACK) countedOneBlack = true;
			}
			if(countedOneBlack) {
				//IF THE NODE HAVE A BLACK NEIGHBOR -> CHANGE IT COLOR TO GRAY
				node.color = Color.GRAY;
				fifoNodes.addAll(node.children);
			} else {
				//ELSE IF ALL THE NEIGHBORS OF THE NODE THAT ARE CLOSER TO THE ROOT (IN THE TREE) ARE GRAY
				// -> CHANGE IT COLOR TO BLACK
				boolean allParentGray = true;
				for(NodeVertexDS n : node.neighbors) {
					if(n.rank < node.rank && !n.color.equals(Color.GRAY)) allParentGray = false;
				}
				if(allParentGray) {
					if(node.color.equals(Color.WHITE)) //TODO CHECK NOT NEEDED
						node.color = Color.BLACK;
					fifoNodes.addAll(node.children);
				}
			}
		}
	}

	public ArrayList<NodeVertexDS> distributedMIS1(ArrayList<NodeVertexDS> points) {
		ArrayList<NodeVertexDS> mis = new ArrayList<>();

		ArrayList<NodeVertexDS> rest = (ArrayList<NodeVertexDS>) points.clone();
		// active list
		ArrayList<NodeVertexDS> activeWhite = new ArrayList<>();

		NodeVertexDS leader = rest.get(0);
		int d = leader.degree();
		for (NodeVertexDS v : rest) {
			if (v.equals(leader)) continue;
			if (v.degree() > d) leader = v;
		}

		leader.isActive = true;

		while (!rest.isEmpty()) {
			// building active white collection
			activeWhite.clear();
			for (NodeVertexDS v : rest) {
				if (v.misColor == Color.white && v.isActive) {
					activeWhite.add(v);
				}
			}

			// no more active white
			if (activeWhite.size() == 0) break;

			// getting dominator vertex
			NodeVertexDS toBlack = activeWhite.get(0);
			d = toBlack.getEffDeg();
			for (NodeVertexDS v : activeWhite) {
				if (v.equals(toBlack)) continue;
				if (v.getEffDeg() > d) toBlack = v;
			}
			toBlack.misColor = Color.black;
			rest.remove(toBlack);

			// colouring all the white neighbors of black in gray
			for (NodeVertexDS toGray : toBlack.neighbors)
				if (toGray.misColor == Color.white) {
					toGray.misColor = Color.gray;
					//  setting white neighbours of gray nodes in active mode
					for (NodeVertexDS toActive : toGray.neighbors) {
						if (toActive.misColor == Color.white) {
							toActive.isActive = true;
						}
					}
					rest.remove(toGray);
				}
		}

		// returning the actual mis
		for (NodeVertexDS v : points) {
			if (v.misColor == Color.black)
				mis.add(v);
		}
		return mis;
	}
	
	public static boolean isValid(ArrayList<Point> points, ArrayList<Point> pts, int edgeTreshold) {
		
		//REBUILD THE GRAPH STRUCTURE
		ArrayList<NodeVertexDS> graphcds = new ArrayList<NodeVertexDS>();
		boolean ret = true;
		for (int i = 0; i < pts.size(); i++) {
			NodeVertexDS v = new NodeVertexDS(pts.get(i), i);
			graphcds.add(v);
			v.makeDSElement();
		}
		for (NodeVertexDS v : graphcds) {
			for (NodeVertexDS v2 : graphcds) {
				if (v == v2)
					continue;
				if (v.p.distance(v2.p) < edgeTreshold) {
					v.neighbors.add(v2);
				}
			}
		}
		
		//BUILD CONNEX COMPONENTS
		for(NodeVertexDS v : graphcds) {
			for(NodeVertexDS vn : v.neighbors) {
				v.disjointSetElem.union(vn.disjointSetElem);
			}
		}
		
		//ENUMERATE CONNEX COMPONENTS
		HashSet<DisjointSetElement<NodeVertexDS>> compconnex = new HashSet<DisjointSetElement<NodeVertexDS>>();
		for(NodeVertexDS v : graphcds) {
			compconnex.add(v.disjointSetElem.find());
		}
		if(compconnex.size() > 1) {
			System.err.println("Error connexity : " + compconnex.size());
			ret = false;
		}
		
		//CHECK DOMINATING (removing all neighbors of dominating set)
		ArrayList<Point> rest = (ArrayList<Point>) points.clone();
		rest.removeAll(pts);
		
		for(int i=0; i<rest.size(); ) {
			boolean removed = false;
			Point p = rest.get(i);
			for(Point p1 : pts) {
				if(p1.distance(p) < edgeTreshold) {
					rest.remove(p);
					removed = true;
					break;
				}
			}
			if(!removed) i++;
		}
		if(rest.size() > 0) {
			System.err.println("Error dominating : " + rest.size());
			ret = false;
		}
		
		return ret;
	}
	
	
	// verify mis is independent in points and all nodes in points are dominated
	public boolean isMIS(ArrayList<NodeVertexDS> points, ArrayList<NodeVertexDS> mis) {
		ArrayList<NodeVertexDS> rest = (ArrayList<NodeVertexDS>) points.clone();
		rest.removeAll(mis);
		for (NodeVertexDS u : mis) {
			for (NodeVertexDS v : mis) {
				if (u.neighbors.contains(v))
					return false;
			}
			rest.removeAll(u.neighbors);
		}
		if (rest.size() == 0)
			return true;
		return false;
	}
	
	////////////////////////////////////////////////////////////:////////////////////////////////////////
	
	// FILE PRINTER
		public void saveToFile(String filename, ArrayList<Point> result) {
			int index = 0;
			try {
				while (true) {
					BufferedReader input = new BufferedReader(
							new InputStreamReader(new FileInputStream(filename + Integer.toString(index) + ".points")));
					try {
						input.close();
					} catch (IOException e) {
						System.err.println(
								"I/O exception: unable to close " + filename + Integer.toString(index) + ".points");
					}
					index++;
				}
			} catch (FileNotFoundException e) {
				printToFile(filename + Integer.toString(index) + ".points", result);
			}
		}

		public void printToFile(String filename, ArrayList<Point> points) {
			try {
				PrintStream output = new PrintStream(new FileOutputStream(filename));
				int x, y;
				for (Point p : points)
					output.println(Integer.toString((int) p.getX()) + " " + Integer.toString((int) p.getY()));
				output.close();
			} catch (FileNotFoundException e) {
				System.err.println("I/O exception: unable to create " + filename);
			}
		}

		// FILE LOADER
		public ArrayList<Point> readFromFile(String filename) {
			String line;
			String[] coordinates;
			ArrayList<Point> points = new ArrayList<Point>();
			try {
				BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
				try {
					while ((line = input.readLine()) != null) {
						coordinates = line.split("\\s+");
						points.add(new Point(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1])));
					}
				} catch (IOException e) {
					System.err.println("Exception: interrupted I/O.");
				} finally {
					try {
						input.close();
					} catch (IOException e) {
						System.err.println("I/O exception: unable to close " + filename);
					}
				}
			} catch (FileNotFoundException e) {
				System.err.println("Input file not found.");
			}
			return points;
		}

}
