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

public class DefaultTeam {
    public static void main(String[] args) {
	Object[] ret100 = test(1, 100, 50);
	Object[] ret500 = test(1, 500, 50);
	Object[] ret1000 = test(1, 1000, 50);
	Object[] ret5000 = test(1, 5000, 50);
	Object[] ret10000 = test(1, 10000, 50);

//	Object[] ret2100 = test(2, 100, 5);
//	Object[] ret2500 = test(2, 500, 25);
//	Object[] ret21000 = test(2, 1000, 50);
//	Object[] ret25000 = test(2, 5000, 250);
//	Object[] ret210000 = test(2, 10000, 500);

	System.out.println(
		"1 - 100 points - Average size : " + ret100[0] + " points - Average time : " + ret100[1] + " s - Fails : " + ret100[2]);

	System.out.println(
		"1 - 500 points - Average size : " + ret500[0] + " points - Average time : " + ret500[1] + " s - Fails : " + ret500[2]);

	System.out.println(
		"1 - 1000 points - Average size : " + ret1000[0] + " points - Average time : " + ret1000[1] + " s - Fails : " + ret1000[2]);

	System.out.println(
		"1 - 5000 points - Average size : " + ret5000[0] + " points - Average time : " + ret5000[1] + " s - Fails : " + ret5000[2]);

	System.out.println(
		"1 - 10000 points - Average size : " + ret10000[0] + " points - Average time : " + ret10000[1] + " s - Fails : " + ret10000[2]);

//	System.out.println(
//		"2 - 100 points - Average size : " + ret2100[0] + " points - Average time : " + ret2100[1] + " s");
//
//	System.out.println(
//		"2 - 500 points - Average size : " + ret2500[0] + " points - Average time : " + ret2500[1] + " s");
//
//	System.out.println(
//		"2 - 1000 points - Average size : " + ret21000[0] + " points - Average time : " + ret21000[1] + " s");
//
//	System.out.println(
//		"2 - 5000 points - Average size : " + ret25000[0] + " points - Average time : " + ret25000[1] + " s");
//
//	System.out.println("2 - 10000 points - Average size : " + ret210000[0] + " points - Average time : "
//		+ ret210000[1] + " s");
    }

    public static Object[] test(int id, int nb, int edgeTreshold) {
	DefaultTeam d = new DefaultTeam();
	int[] sizes = new int[100];
	double[] times = new double[100];
	int fails = 0;
	for (int i = 0; i < 100; i++) {
	    System.out.println("Test " + i);
	    ArrayList<Point> points = d.readFromFile("tests" + id + "-" + nb + "/test" + (i + 1) + ".points");
	    long time1 = System.currentTimeMillis();
	    ArrayList<Point> pts = d.calculConnectedDominatingSet(points, edgeTreshold);
	    long time2 = System.currentTimeMillis();
	    times[i] = (time2 - time1);
	    sizes[i] = pts.size();
	    boolean valid = isValid(points, pts, edgeTreshold);
	    System.out.println("END - size : " + pts.size() + " - time : " + times[i] / 1000 + " s - valid : " + valid);
	    if(!valid) fails++;
	}
	int avsize = 0;
	for (int s : sizes)
	    avsize += s;
	avsize /= 100;
	double avtime = 0;
	for (double t : times)
	    avtime += t;
	avtime /= 100.0 * 1000.0;
	System.out.println("Average size : " + avsize + " points - Average time : " + avtime + " s");
	return new Object[] { avsize, avtime, fails };
    }

    private static boolean isValid(ArrayList<Point> points, ArrayList<Point> pts, int edgeTreshold) {
    	ArrayList<VertexDS> graphcds = new ArrayList<VertexDS>();
    	boolean ret = true;
    	for (int i = 0; i < pts.size(); i++) {
    		VertexDS v = new VertexDS(pts.get(i), i);
    	    graphcds.add(v);
    	    v.makeBBComp();
    	}
    	for (VertexDS v : graphcds) {
    	    for (VertexDS v2 : graphcds) {
    		if (v == v2)
    		    continue;
    		if (v.p.distance(v2.p) < edgeTreshold) {
    		    v.neighbors.add(v2);
    		}
    	    }
    	}
    	
    	for(VertexDS v : graphcds) {
    		for(VertexDS vn : v.neighbors) {
    			v.bbcomp.union(vn.bbcomp);
    		}
    	}
    	
    	HashSet<DisjointSetElement<VertexDS>> compconnex = new HashSet<DisjointSetElement<VertexDS>>();
    	for(VertexDS v : graphcds) {
    		compconnex.add(v.bbcomp.find());
    	}
    	if(compconnex.size() > 1) {
    		System.err.println("Error connexity : " + compconnex.size());
    		ret = false;
    	}
    	ArrayList<Point> rest = (ArrayList<Point>) points.clone();
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

	public ArrayList<Point> calculConnectedDominatingSet(ArrayList<Point> points, int edgeTreshold) {

	ArrayList<VertexDS> graph = new ArrayList<VertexDS>();

	for (int i = 0; i < points.size(); i++) {
	    graph.add(new VertexDS(points.get(i), i));
	}
	for (VertexDS v : graph) {
	    for (VertexDS v2 : graph) {
		if (v == v2)
		    continue;
		if (v.p.distance(v2.p) < edgeTreshold) {
		    v.neighbors.add(v2);
		}
	    }
	}
	// START

	// CALCUL MIS
	ArrayList<VertexDS> rest = (ArrayList<VertexDS>) graph.clone();
	ArrayList<VertexDS> MIS = new ArrayList<VertexDS>();

	// while (!rest.isEmpty()) {
	// VertexDS v = rest.remove(0);
	// MIS.add(v);
	// rest.removeAll(v.neighbors);
	// }


	MIS = distributedMIS(graph);
//	System.out.println(isMIS(graph, MIS));

	// BLACK COMPONENTS
	for (VertexDS vi : MIS) {
	    vi.color = Color.BLACK;
	    vi.makeBBComp();
	}

	// GRAY NODES
	ArrayList<VertexDS> grayNodes = (ArrayList<VertexDS>) graph.clone();
	grayNodes.removeAll(MIS);
	// Collections.shuffle(grayNodes); TEST

	// TODO sort graynodes degree ++>--
	// grayNodes.sort((x1,x2) -> x1.degree() - x2.degree());

	for (int i = 5; i >= 2; i--) {
	    boolean cont = true;
	    // System.out.println(i);
	    while (cont) {
		int newblueId = -1;
		for (int j = 0; j < grayNodes.size(); j++) {
		    VertexDS vgray = grayNodes.get(j);
		    if (vgray.degree() < i)
			continue;

		    ArrayList<VertexDS> blackneighbors = new ArrayList<VertexDS>();
		    for (VertexDS vgraynei : vgray.neighbors)
			if (vgraynei.color == Color.BLACK)
			    blackneighbors.add(vgraynei);
		    if (blackneighbors.size() < i)
			continue;

		    ArrayList<DisjointSetElement<VertexDS>> neibbcompsroots = new ArrayList<DisjointSetElement<VertexDS>>();
		    for (VertexDS blackn : blackneighbors) {
			neibbcompsroots.add(blackn.bbcomp.find());
		    }

		    // TODO utiliser directement les indices
		    HashSet<Integer> idsbbcomps = new HashSet<Integer>();
		    for (DisjointSetElement<VertexDS> rootneibbcomp : neibbcompsroots) {
			idsbbcomps.add(rootneibbcomp.index);
		    }
		    if (idsbbcomps.size() < i)
			continue;

		    vgray.color = Color.BLUE;
		    newblueId = j;
		    // TODO optimisable en utilisant rootneibbcomp
		    for (int id = 1; id < neibbcompsroots.size(); id++) {
			blackneighbors.get(0).bbcomp.union(blackneighbors.get(id).bbcomp);
		    }
		    break;
		}
		if (newblueId != -1)
		    grayNodes.remove(newblueId);
		else
		    cont = false;
	    }

	}

	ArrayList<Point> result = new ArrayList<Point>();
	for (VertexDS v : graph) {
	    if (v.color.equals(Color.BLUE))
		result.add(v.p);
	    if (v.color.equals(Color.BLACK))
		result.add(v.p);
	}
	System.out.println(isValid(points, result, edgeTreshold));
	return result;
    }

    // FILE PRINTER
    private void saveToFile(String filename, ArrayList<Point> result) {
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

    private void printToFile(String filename, ArrayList<Point> points) {
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
    private ArrayList<Point> readFromFile(String filename) {
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

    // verify mis is independent in points and all nodes in points are dominated
    public boolean isMIS(ArrayList<VertexDS> points, ArrayList<VertexDS> mis) {
	ArrayList<VertexDS> rest = (ArrayList<VertexDS>) points.clone();
	rest.removeAll(mis);
	for (VertexDS u : mis) {
	    for (VertexDS v : mis) {
		if (u.neighbors.contains(v))
		    return false;
	    }
	    rest.removeAll(u.neighbors);
	}
	if (rest.size() == 0)
	    return true;
	return false;
    }

    public ArrayList<VertexDS> distributedMIS(ArrayList<VertexDS> points) {
	ArrayList<VertexDS> mis = new ArrayList<>();

	ArrayList<VertexDS> rest = (ArrayList<VertexDS>) points.clone();
	// active list
	ArrayList<VertexDS> activeWhite = new ArrayList<>();

	VertexDS leader = rest.get(0);
	int d = leader.degree();
	for (VertexDS v : rest) {
	    if (v.equals(leader)) continue;
	    if (v.degree() > d) leader = v;
	}
	
	leader.isActive = true;
	
	while (!rest.isEmpty()) {
	    // building active white collection
	    activeWhite.clear();
	    for (VertexDS v : rest) {
		if (v.misColor == Color.white && v.isActive) {
		    activeWhite.add(v);
		}
	    }
	    
	    // no more active white
	    if (activeWhite.size() == 0) break;
	    
	    // getting dominator vertex
	    VertexDS toBlack = activeWhite.get(0);
	    d = toBlack.getEffDeg();
	    for (VertexDS v : activeWhite) {
		if (v.equals(toBlack)) continue;
		if (v.getEffDeg() > d) toBlack = v;
	    }
	    toBlack.misColor = Color.black;
	    rest.remove(toBlack);
	    
	    // colouring all the white neighbors of black in gray
	    for (VertexDS toGray : toBlack.neighbors)
		if (toGray.misColor == Color.white) {
		    toGray.misColor = Color.gray;
		    //  setting white neighbours of gray nodes in active mode
		    for (VertexDS toActive : toGray.neighbors) {
			if (toActive.misColor == Color.white) {
			    toActive.isActive = true;
			}
		    }
		    rest.remove(toGray);
		}
	}
	
	// returning the actual mis
	for (VertexDS v : points) {
	    if (v.misColor == Color.black)
		mis.add(v);
	}
	return mis;
    }

}
