import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class Knapsack {

	public static HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

	public static int Q1(int[] val, int[] weight, int cap) {
		int max = 0;
		int length = val.length;
		int[][] memo = new int[length + 1][cap + 1];

		if (cap == 0 || length == 0) {
			return 0;
		}

		for (int n = 0; n <= length; n++) {

			for (int w = 0; w <= cap; w++) {
				if (n == 0 || w == 0) {
					memo[n][w] = 0;
				} else {
					if (weight[n - 1] <= w) {
						memo[n][w] = Math.max(memo[n - 1][w], val[n - 1] + memo[n - 1][w - weight[n - 1]]);
					} else {
						memo[n][w] = memo[n - 1][w];
					}
				}
			}
		}
		// for(int i =0; i < memo.length; i++){
		// for(int j=0; j<memo[0].length;j++){
		// System.out.println(memo[i][j]);
		// }
		// }

		max = memo[length][cap];
		return max;
	}

	public static int Q2(int[] val, int[] weight, int cap) {
		int result = 0;
		int max = val.length - 1; // assume the val array is sorted
		int n = val.length - 1; // n=4-1=3
		int minCost[][] = new int[n + 1][n * val[max] + 1];
		boolean take[][] = new boolean[n + 1][n * val[max] + 1];

		for (int i = 0; i <= n; i++) { // n=length or -1?
			minCost[i][0] = 0;
		}

		for (int t = 1; t <= val[1]; t++) {
			minCost[1][t] = weight[1];
			take[1][t] = true;
		}

		for (int t = val[1] + 1; t <= n * val[max]; t++) {
			minCost[1][t] = 10000000; // infinity
			take[1][t] = false;
		}

		for (int i = 2; i <= n; i++) {
			for (int t = 1; t <= n * val[max]; t++) {
				int nextT = Math.max(0, t - val[i]);
				if (minCost[i - 1][t] <= weight[i] + minCost[i - 1][nextT]) {
					minCost[i][t] = minCost[i - 1][t];
					take[i][t] = false;
				} else {
					minCost[i][t] = weight[i] + minCost[i - 1][nextT];
					take[i][t] = true;
				}
			}
		}
		outerloop: for (int a = minCost.length - 1; a >= 0; a--) {
			for (int b = minCost[0].length - 1; b >= 0; b--) {

				if (take[a][b] == true && minCost[a][b] <= cap) {
					// System.out.println("a="+a+" b="+b+" "+ minCost[a][b]);
					// System.out.println("a="+a+" b="+b+" "+ take[a][b]);
					result = b;
					break outerloop;
				}
			}
		}
		return result;
	}

	public static int Q3(int[] val, int[] weight, int cap) {
		int max = val[val.length - 1];
		int budget = cap;
		int[] G = new int[val.length];
		int n = 0;

		for (int v = 0; v < val.length; v++) {
			if (budget > 0) {
				if (weight[v] <= budget) {
					G[n] = val[v];
					n++;
					budget = budget - weight[v];
				}
			}
		}

		int sum = IntStream.of(G).sum();

		if (max > sum) {
			return max;
		}

		return sum;
	}

	public static int Q4(int[] val, int[] weight, int cap) {
		double f = val[val.length - 1] / val.length * 0.5;
		int max;
		int newVal[] = new int[val.length];

		for (int i = 0; i < val.length; i++) {
			newVal[i] = (int) (val[i] / f);
		}

		max = Q2(newVal, weight, cap);

		max = (int) (max * f);

		return max;
	}

	public static int[] rand(int nums) {
		int[] randList = new int[nums];
		for (int i = 0; i < nums; i++) {
			int randnum = (int) (Math.random() * (10000 - 10));
			randList[i] = randnum;
		}
		return randList;
	}

	public static void main(String[] args) {
		double[] d = new double[100];

		for (int i = 0; i < 100; i++) {
			// final long startTime = System.nanoTime();
			Random ran = new Random();
			int nums = ran.nextInt(10) + 90;
			int value[] = rand(nums);
			Arrays.sort(value);
			int weight[] = rand(nums);
			Arrays.sort(weight);
			int capacity = weight[nums - 1];
			int max1 = Q1(value, weight, capacity);
			int max2 = Q4(value, weight, capacity);
			d[i] = (double) max2 / max1;
			// final long duration = System.nanoTime() - startTime;
			// System.out.println((double)duration/1000000000);
			// d[i] = (double)duration/1000000000;
		}

		double sum = DoubleStream.of(d).sum();
		double avg = sum / d.length;
		Arrays.sort(d);
		double min = d[0];
		double max = d[d.length - 1];
		double mid = d[d.length / 2];

		System.out.println("avg = " + avg + " min = " + min + " max = " + max + " mid = " + mid);
	}

}