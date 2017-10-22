package algorithms;

import java.awt.Point;
import java.util.ArrayList;

public class Tests {

	public static void main(String[] args) {
		int[] nbpts = {100,500,1000,2000,3000,4000,5000,6000};
//		Object[][][] b1 = new Object[nbpts.length][][];
//		for(int i=0;i<nbpts.length;i++) {
//			b1[i] = test(1, nbpts[i], 50);
//		}
//		for(int i=0;i<nbpts.length;i++) {
//			System.out.println(
//					"1 - 1 - "+nbpts[i]+" points - Average size : " + b1[i][0][0] + " points - Average time : " + b1[i][0][1] + " s - Fails : " + b1[i][0][2] + " - degree : " + b1[i][0][3]);
//			System.out.println(
//					"1 - 2 - "+nbpts[i]+" points - Average size : " + b1[i][1][0] + " points - Average time : " + b1[i][1][1] + " s - Fails : " + b1[i][1][2] + " - degree : " + b1[i][1][3]);
//		}
		
		Object[][][] b2 = new Object[nbpts.length][][];
		for(int i=0;i<nbpts.length;i++) {
			b2[i] = test(2, nbpts[i], nbpts[i]/20);
		}
		for(int i=0;i<nbpts.length;i++) {
			System.out.println(
					"2 - 1 - "+nbpts[i]+" points - Average size : " + b2[i][0][0] + " points - Average time : " + b2[i][0][1] + " s - Fails : " + b2[i][0][2] + " - degree : " + b2[i][0][3]);
			System.out.println(
					"2 - 2 - "+nbpts[i]+" points - Average size : " + b2[i][1][0] + " points - Average time : " + b2[i][1][1] + " s - Fails : " + b2[i][1][2] + " - degree : " + b2[i][1][3]);
		}
		
//		Object[][][] b3 = new Object[nbpts.length][][];
//		for(int i=0;i<nbpts.length;i++) {
//			b3[i] = test(3, nbpts[i], nbpts[i]*1/36+200/9);
//		}
//		for(int i=0;i<nbpts.length;i++) {
//			System.out.println(
//					"3 - 1 - "+nbpts[i]+" points - Average size : " + b3[i][0][0] + " points - Average time : " + b3[i][0][1] + " s - Fails : " + b3[i][0][2] + " - degree : " + b3[i][0][3]);
//			System.out.println(
//					"3 - 2 - "+nbpts[i]+" points - Average size : " + b3[i][1][0] + " points - Average time : " + b3[i][1][1] + " s - Fails : " + b3[i][1][2] + " - degree : " + b3[i][1][3]);
//		}
	}

	public static Object[][] test(int id, int nb, int edgeTreshold) {
		DefaultTeam d = new DefaultTeam();
		int[] sizes = new int[100];
		double[] times = new double[100];
		int fails = 0;
		int maxdegs = 0;
		int[] sizes2 = new int[100];
		double[] times2 = new double[100];
		int fails2 = 0;
		int maxdegs2 = 0;
		for (int i = 0; i < 100; i++) {
			System.out.println("Test " + (i+1));
			ArrayList<Point> points = d.readFromFile("tests" + id + "-" + nb + "/test" + (i + 1) + ".points");
			//long time1 = System.currentTimeMillis();
			DefaultTeam.useMIS = 1;
			ArrayList<Point> pts = d.calculConnectedDominatingSet(points, edgeTreshold);
			long time2 = System.nanoTime();
			times[i] = (time2 - DefaultTeam.startedat);
			sizes[i] = pts.size();
			boolean valid = DefaultTeam.isValid(points, pts, edgeTreshold);
			System.out.println((i+1) + " END 1 " + nb + " points - size : " + pts.size() + " - time : " + times[i] / 1_000_000_000 + " s - valid : " + valid);
			if(!valid) fails++;
			maxdegs+=DefaultTeam.avdeg;

			//long time21 = System.currentTimeMillis();
			DefaultTeam.useMIS = 2;
			ArrayList<Point> pts2 = d.calculConnectedDominatingSet(points, edgeTreshold);
			long time22 = System.nanoTime();
			times2[i] = (time22 - DefaultTeam.startedat);
			sizes2[i] = pts2.size();
			boolean valid2 = DefaultTeam.isValid(points, pts2, edgeTreshold);

			System.out.println((i+1) + " END 2 " + nb + " points - size : " + pts2.size() + " - time : " + times2[i] / 1_000_000_000 + " s - valid : " + valid2);
			if(!valid2) fails2++;
			maxdegs2+=DefaultTeam.avdeg;
		}
		int avsize = 0;
		for (int s : sizes)
			avsize += s;
		avsize /= 100;
		double avtime = 0;
		for (double t : times)
			avtime += t;
		avtime /= (100.0 * 1_000_000_000.0);
		int avmdeg = maxdegs/100;
		
		int avsize2 = 0;
		for (int s : sizes2)
			avsize2 += s;
		avsize2 /= 100;
		double avtime2 = 0;
		for (double t : times2)
			avtime2 += t;
		avtime2 /= (100.0 * 1_000_000_000.0);
		int avmdeg2 = maxdegs2/100;

		System.out.println("Average size : " + avsize + " points - Average time : " + avtime + " s");
		return new Object[][] { {avsize, avtime, fails, avmdeg}, { avsize2, avtime2, fails2, avmdeg2 } };
	}

}
