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
		try {
			Files.createDirectories(Paths.get("tests1-100"));
			Files.createDirectories(Paths.get("tests1-500"));
			Files.createDirectories(Paths.get("tests1-1000"));
			Files.createDirectories(Paths.get("tests1-5000"));
			Files.createDirectories(Paths.get("tests1-10000"));
			for(int i=1; i<=100; i++)
				export(new File("tests1-100/test"+i+".points"), generateGraph(1000, 1000, 100, 50));
			for(int i=1; i<=100; i++)
				export(new File("tests1-500/test"+i+".points"), generateGraph(1000, 1000, 500, 50));
			for(int i=1; i<=100; i++)
				export(new File("tests1-1000/test"+i+".points"), generateGraph(1000, 1000, 1000, 50));
			for(int i=1; i<=100; i++)
				export(new File("tests1-5000/test"+i+".points"), generateGraph(1000, 1000, 5000, 50));
			for(int i=1; i<=100; i++)
				export(new File("tests1-10000/test"+i+".points"), generateGraph(1000, 1000, 10000, 50));
			
			
			Files.createDirectories(Paths.get("tests2-100"));
			Files.createDirectories(Paths.get("tests2-500"));
			Files.createDirectories(Paths.get("tests2-1000"));
			Files.createDirectories(Paths.get("tests2-5000"));
			Files.createDirectories(Paths.get("tests2-10000"));
			for(int i=1; i<=100; i++)
				export(new File("tests2-100/test"+i+".points"), generateGraph(100, 100, 100, 5));
			for(int i=1; i<=100; i++)
				export(new File("tests2-500/test"+i+".points"), generateGraph(500, 500, 500, 25));
			for(int i=1; i<=100; i++)
				export(new File("tests2-1000/test"+i+".points"), generateGraph(1000, 1000, 1000, 50));
			for(int i=1; i<=100; i++)
				export(new File("tests2-5000/test"+i+".points"), generateGraph(5000, 5000, 5000, 250));
			for(int i=1; i<=100; i++)
				export(new File("tests2-10000/test"+i+".points"), generateGraph(10000, 10000, 10000, 500));

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
