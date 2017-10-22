import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		int[] nbpts = {100,500,1000,2000,3000,4000,5000,6000,7000,8000,9000,10000};
		try {
//			for(int i=0; i< nbpts.length; i++) {
//				Files.createDirectories(Paths.get("tests1-" + nbpts[i]));
//				for(int j=1; j<=100; j++)
//					export(new File("tests1-"+nbpts[i]+"/test"+j+".points"), generateGraph(1000, 1000, nbpts[i], 50));
//			}
			
			for(int i=0; i< nbpts.length; i++) {
				Files.createDirectories(Paths.get("tests2-" + nbpts[i]));
				for(int j=1; j<=100; j++)
					export(new File("tests2-"+nbpts[i]+"/test"+j+".points"), generateGraph(nbpts[i], nbpts[i], nbpts[i], nbpts[i]/20));
			}
//			for(int i=0; i< nbpts.length; i++) {
//				Files.createDirectories(Paths.get("tests3-" + nbpts[i]));
//				for(int j=1; j<=100; j++)
//					export(new File("tests3-"+nbpts[i]+"/test"+j+".points"), generateGraph(nbpts[i], nbpts[i], nbpts[i], nbpts[i]*1/36+200/9));
//			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void export(File f, ArrayList<Point> pts) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		for(Point p : pts) {
			bw.write(p.x + " " + p.y + "\n");
		}
		bw.close();
	}
	
	public static ArrayList<Point> generateGraph(int width, int height, int n, int edgeTreshold) {
		ArrayList<Point> result = new ArrayList<Point>();
		Random r = new Random(System.currentTimeMillis());

		while(result.size() != n) {
			while(result.size() != n) {
				result.add(new Point(r.nextInt(width), r.nextInt(height)));
			}

			ArrayList<VertexDS> graph = new ArrayList<VertexDS>();
			for(int i=0; i<result.size(); i++) {
				VertexDS v = new VertexDS(result.get(i), i);
				v.makeDSE();
				graph.add(v); 
			}
			for(VertexDS v : graph) {
				for(VertexDS v2 : graph) {
					if(v.p.distance(v2.p) < edgeTreshold)
						v.neighbors.add(v2);
				}
			}
			for(VertexDS v : graph) {
				for(VertexDS ne : v.neighbors) {
					v.dse.union(ne.dse);
				}
			}
			HashMap<Integer, ArrayList<VertexDS>> compcons = new HashMap<Integer, ArrayList<VertexDS>>();
			for(VertexDS v : graph) {
				int id = v.dse.find().index;
				if(!compcons.containsKey(id))
					compcons.put(id, new ArrayList<VertexDS>());
				compcons.get(id).add(v);
			}

			//System.out.println(compcons.size());
			int idmaxsizecomp = -1;
			int sizemaxcomp = Integer.MIN_VALUE;
			for(Entry<Integer, ArrayList<VertexDS>> comp : compcons.entrySet()) {
				//System.out.println(comp.getValue().size());
				if(comp.getValue().size() > sizemaxcomp) { sizemaxcomp = comp.getValue().size(); idmaxsizecomp = comp.getKey(); };
			}
			//System.out.println(idmaxsizecomp);
			result = convert(compcons.get(idmaxsizecomp));
		}
		System.out.println(result.size());
		return result;
	}

	private static ArrayList<Point> convert(ArrayList<VertexDS> arrayList) {
		ArrayList<Point> result = new ArrayList<Point>();
		//arrayList.forEach((x) -> result.add(x.p));
		for(VertexDS v : arrayList) {
			result.add(v.p);
		}
		return result;
	}

}
