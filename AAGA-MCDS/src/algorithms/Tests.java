package algorithms;

import java.awt.Point;
import java.util.ArrayList;

public class Tests {

	public static void main(String[] args) {
		Object[][] ret100 = test(1, 100, 50);
		Object[][] ret500 = test(1, 500, 50);
		Object[][] ret1000 = test(1, 1000, 50);
		Object[][] ret5000 = test(1, 5000, 50);
		Object[][] ret10000 = test(1, 10000, 50);

		Object[][] ret2100 = test(2, 100, 5);
		Object[][] ret2500 = test(2, 500, 25);
		Object[][] ret21000 = test(2, 1000, 50);
		Object[][] ret25000 = test(2, 5000, 250);
		Object[][] ret210000 = test(2, 10000, 500);

		System.out.println(
				"1 - 1 - 100 points - Average size : " + ret100[0][0] + " points - Average time : " + ret100[0][1] + " s - Fails : " + ret100[0][2]);
		System.out.println(
				"1 - 2 - 100 points - Average size : " + ret100[1][0] + " points - Average time : " + ret100[1][1] + " s - Fails : " + ret100[1][2]);

		System.out.println(
				"1 - 1 - 500 points - Average size : " + ret500[0][0] + " points - Average time : " + ret500[0][1] + " s - Fails : " + ret500[0][2]);
		System.out.println(
				"1 - 2 - 500 points - Average size : " + ret500[1][0] + " points - Average time : " + ret500[1][1] + " s - Fails : " + ret500[1][2]);

		System.out.println(
				"1 - 1 - 1000 points - Average size : " + ret1000[0][0] + " points - Average time : " + ret1000[0][1] + " s - Fails : " + ret1000[0][2]);
		System.out.println(
				"1 - 2 - 1000 points - Average size : " + ret1000[1][0] + " points - Average time : " + ret1000[1][1] + " s - Fails : " + ret1000[1][2]);

		System.out.println(
				"1 - 1 - 5000 points - Average size : " + ret5000[0][0] + " points - Average time : " + ret5000[0][1] + " s - Fails : " + ret5000[0][2]);
		System.out.println(
				"1 - 2 - 5000 points - Average size : " + ret5000[1][0] + " points - Average time : " + ret5000[1][1] + " s - Fails : " + ret5000[1][2]);

		System.out.println(
				"1 - 1 - 10000 points - Average size : " + ret10000[0][0] + " points - Average time : " + ret10000[0][1] + " s - Fails : " + ret10000[0][2]);
		System.out.println(
				"1 - 2 - 10000 points - Average size : " + ret10000[1][0] + " points - Average time : " + ret10000[1][1] + " s - Fails : " + ret10000[1][2]);



		System.out.println(
				"2 - 1 - 100 points - Average size : " + ret2100[0][0] + " points - Average time : " + ret2100[0][1] + " s - Fails : " + ret2100[0][2]);
		System.out.println(
				"2 - 2 - 100 points - Average size : " + ret2100[1][0] + " points - Average time : " + ret2100[1][1] + " s - Fails : " + ret2100[1][2]);

		System.out.println(
				"2 - 1 - 500 points - Average size : " + ret2500[0][0] + " points - Average time : " + ret2500[0][1] + " s - Fails : " + ret2500[0][2]);
		System.out.println(
				"2 - 2 - 500 points - Average size : " + ret2500[1][0] + " points - Average time : " + ret2500[1][1] + " s - Fails : " + ret2500[1][2]);

		System.out.println(
				"2 - 1 - 1000 points - Average size : " + ret21000[0][0] + " points - Average time : " + ret21000[0][1] + " s - Fails : " + ret21000[0][2]);
		System.out.println(
				"2 - 2 - 1000 points - Average size : " + ret21000[1][0] + " points - Average time : " + ret21000[1][1] + " s - Fails : " + ret21000[1][2]);

		System.out.println(
				"2 - 1 - 5000 points - Average size : " + ret25000[0][0] + " points - Average time : " + ret25000[0][1] + " s - Fails : " + ret25000[0][2]);
		System.out.println(
				"2 - 2 - 5000 points - Average size : " + ret25000[1][0] + " points - Average time : " + ret25000[1][1] + " s - Fails : " + ret25000[1][2]);

		System.out.println(
				"2 - 1 - 10000 points - Average size : " + ret210000[0][0] + " points - Average time : " + ret210000[0][1] + " s - Fails : " + ret210000[0][2]);
		System.out.println(
				"2 - 2 - 10000 points - Average size : " + ret210000[1][0] + " points - Average time : " + ret210000[1][1] + " s - Fails : " + ret210000[1][2]);
	}

	public static Object[][] test(int id, int nb, int edgeTreshold) {
		DefaultTeam d = new DefaultTeam();
		int[] sizes = new int[100];
		double[] times = new double[100];
		int fails = 0;
		int[] sizes2 = new int[100];
		double[] times2 = new double[100];
		int fails2 = 0;
		for (int i = 0; i < 100; i++) {
			System.out.println("Test " + (i+1));
			ArrayList<Point> points = d.readFromFile("tests" + id + "-" + nb + "/test" + (i + 1) + ".points");
			long time1 = System.currentTimeMillis();
			DefaultTeam.useMIS = 1;
			ArrayList<Point> pts = d.calculConnectedDominatingSet(points, edgeTreshold);
			long time2 = System.currentTimeMillis();
			times[i] = (time2 - time1);
			sizes[i] = pts.size();
			boolean valid = DefaultTeam.isValid(points, pts, edgeTreshold);
			System.out.println((i+1) + " END 1 " + nb + " points - size : " + pts.size() + " - time : " + times[i] / 1000 + " s - valid : " + valid);
			if(!valid) fails++;

			long time21 = System.currentTimeMillis();
			DefaultTeam.useMIS = 2;
			ArrayList<Point> pts2 = d.calculConnectedDominatingSet(points, edgeTreshold);
			long time22 = System.currentTimeMillis();
			times2[i] = (time22 - time21);
			sizes2[i] = pts2.size();
			boolean valid2 = DefaultTeam.isValid(points, pts2, edgeTreshold);

			System.out.println((i+1) + " END 2 " + nb + " points - size : " + pts2.size() + " - time : " + times2[i] / 1000 + " s - valid : " + valid2);
			if(!valid2) fails2++;
		}
		int avsize = 0;
		for (int s : sizes)
			avsize += s;
		avsize /= 100;
		double avtime = 0;
		for (double t : times)
			avtime += t;
		avtime /= 100.0 * 1000.0;
		
		int avsize2 = 0;
		for (int s : sizes2)
			avsize2 += s;
		avsize2 /= 100;
		double avtime2 = 0;
		for (double t : times2)
			avtime2 += t;
		avtime2 /= 100.0 * 1000.0;
		System.out.println("Average size : " + avsize + " points - Average time : " + avtime + " s");
		return new Object[][] { {avsize, avtime, fails}, { avsize2, avtime2, fails2 } };
	}

}
