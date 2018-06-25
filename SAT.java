import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.stream.DoubleStream;

public class SAT {
	public static HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

	public static void print(Map<Integer, Integer> map) {
		if (map.isEmpty()) {
			System.out.println("map is empty");
		}

		else {
			System.out.println(map);
		}
	}

	public static void main(String[] args) {
		double[] d = new double[100];
		for (int i = 0; i < 100; i++) {
			final long startTime = System.nanoTime();
			Random ran = new Random();
			int nums = ran.nextInt(5) + 50;
			LinkedList<Clause> formula = new LinkedList<Clause>();
			for (int n = 0; n < nums; n++) {
				formula.add(new Clause(rand(), rand(), rand()));
			}
			int l = formula.size();

			map.clear();
			System.out.println("final: " + Q5(formula, l, 0));
			print(map);

			// Q6(formula, l);
			// print(map);

			final long duration = System.nanoTime() - startTime;
			d[i] = (double) duration / 1000000000;
			// System.out.println((double)duration/1000000000);
		}
		double sum = DoubleStream.of(d).sum();
		double avg = sum / d.length;
		Arrays.sort(d);
		double min = d[0];
		double max = d[d.length - 1];
		double mid = d[d.length / 2];

		System.out.println("avg = " + avg + " min = " + min + " max = " + max + " mid = " + mid);
	}

	public static int rand() {
		Random ran = new Random();
		int randn = ran.nextInt(1) + 9;

		int sign = getBoolean();
		if (sign == 1) {
			randn = -randn;
		}
		return randn;
	}

	public static int getBoolean() {
		Random rand = new Random();
		int range = 1;
		int rand1 = rand.nextInt(range);
		int rand2 = rand.nextInt(range);

		if (rand1 < rand2) {
			return 0;
		} else {
			return 1;
		}
	}

	public static boolean Q5(LinkedList<Clause> f, int l, int u) {
		boolean satisfiable = false;

		LinkedList<Clause> newf = unitProp(f, l, u);
		int size = newf.size();
		if (newf.size() == 0) {
			return satisfiable = true;
		}

		boolean emptyC = false;
		for (int i = 0; i < size; i++) {
			if (f.get(i).getA() == 0 && f.get(i).getB() == 0 && f.get(i).getC() == 0) {
				emptyC = true;
			}
		}

		if (emptyC) {
			return satisfiable = false;
		}

		// iterate though every clause to find the next unassigned val
		boolean hasnew = false;
		for (int i = 0; i < size; i++) {
			if (map.get(Math.abs(f.get(i).getA())) == null) {
				u = Math.abs(f.get(i).getA());
				map.put(u, 1);
				hasnew = true;
				break;
			}
			if (map.get(Math.abs(f.get(i).getB())) == null) {
				u = Math.abs(f.get(i).getB());
				map.put(u, 1);
				hasnew = true;
				break;
			}
			if (map.get(Math.abs(f.get(i).getC())) == null) {
				u = Math.abs(f.get(i).getC());
				map.put(u, 1);
				hasnew = true;
				break;
			}
		}

		if (!hasnew) {
			return satisfiable = false;
		}

		satisfiable = Q5(newf, l, u);

		if (satisfiable) {
			return true;
		} else {
			map.put(u, 0);
			u = -u;
			return Q5(newf, l, u);
		}

	}

	public static LinkedList<Clause> unitProp(LinkedList<Clause> f, int l, int u) {
		LinkedList<Clause> newf = f;

		if (u > 0) {
			System.out.println(u);
			map.put(u, 1);

			for (int i = 0; i < f.size(); i++) {
				if (f.get(i).getA() == -u) {
					f.get(i).setA(0);
				}
				if (f.get(i).getB() == -u) {
					f.get(i).setB(0);
				}
				if (f.get(i).getC() == -u) {
					f.get(i).setC(0);
				}

				if (f.get(i).getA() == u || f.get(i).getB() == u || f.get(i).getC() == u) {
					f.remove(i);
				}
			}
		} else if (u < 0) {
			map.put(Math.abs(u), 0);
			for (int i = 0; i < f.size(); i++) {
				if (f.get(i).getA() == -u) {
					f.get(i).setA(0);
				}
				if (f.get(i).getB() == -u) {
					f.get(i).setB(0);
				}
				if (f.get(i).getC() == -u) {
					f.get(i).setC(0);
				}
				if (f.get(i).getA() == u || f.get(i).getB() == u || f.get(i).getC() == u) {
					f.remove(i);
				}
			}
		} else {
			return f;
		}
		return newf;
	}

	public static void Q6(LinkedList<Clause> f, int l) {
		// 1=true 0=false
		map.clear();
		for (int m = 0; m < 100; m++) {
			HashMap<Integer, Integer> count = new HashMap<>();

			for (int i = 0; i < l; i++) {
				int a = Math.abs(f.get(i).getA());

				int b = Math.abs(f.get(i).getB());
				int c = Math.abs(f.get(i).getC());
				if (map.get(a) == null) {
					map.put(a, getBoolean());
				}
				if (map.get(b) == null) {
					map.put(b, getBoolean());
				}
				if (map.get(c) == null) {
					map.put(c, getBoolean());
				}
			}

			for (int i = 0; i < l; i++) {
				int aval = map.get(Math.abs(f.get(i).getA()));
				int a = f.get(i).getA();
				int a_r = torf(a, aval);
				if (a_r == 0) {
					if (count.put(Math.abs(a), 1) == null) {
						count.put(Math.abs(a), 1);
					} else {
						count.put(Math.abs(a), count.get(Math.abs(a)) + 1);
					}
				}

				int bval = map.get(Math.abs(f.get(i).getB()));
				int b = f.get(i).getB();
				int b_r = torf(b, bval);
				if (b_r == 0) {
					if (count.put(Math.abs(b), 1) == null) {
						count.put(Math.abs(b), 1);
					} else {
						count.put(Math.abs(b), count.get(Math.abs(b)) + 1);
					}
				}

				int cval = map.get(Math.abs(f.get(i).getC()));
				int c = f.get(i).getC();
				int c_r = torf(c, cval);
				if (c_r == 0) {
					if (count.put(Math.abs(c), 1) == null) {
						count.put(Math.abs(c), 1);
					} else {
						count.put(Math.abs(c), count.get(Math.abs(c)) + 1);
					}
				}
			}

			int worst = 0;
			int index = -1;
			for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
				if (entry.getValue() > worst) {
					worst = entry.getValue();
					index = entry.getKey();
				}
			}

			if (index != -1) {
				if (map.get(index) == 0) {
					map.put(index, 1);
				} else {
					map.put(index, 0);
				}
			} else {
				// System.out.println("we found the optinmal solution");
				break;
			}

		} //
	}

	public static int torf(int n, int m) {
		int result = -1;
		if (m == 0) { // false
			if (n < 0) { // false
				result = 1;
			} else if (n > 0) { // true
				result = 0;
			} else {
				System.out.println("error");
			}
		} else { // true
			if (n < 0) { // false
				result = 0;
			} else if (n > 0) { // true
				result = 1;
			} else {
				System.out.println("error");
			}
		}
		return result;
	}

	public static void Q7(LinkedList<Clause> f, int l) {
		// 1=true 0=false
		map.clear();
		boolean solved = false;
		while (!solved) {

			// randomly generate truth assignments
			for (int i = 0; i < l; i++) {
				int a = Math.abs(f.get(i).getA());

				int b = Math.abs(f.get(i).getB());
				int c = Math.abs(f.get(i).getC());
				if (map.get(a) == null) {
					map.put(a, getBoolean());
				}
				if (map.get(b) == null) {
					map.put(b, getBoolean());
				}
				if (map.get(c) == null) {
					map.put(c, getBoolean());
				}
			}

			// check if 7/8 satisfies

			int count = 0;
			for (int i = 0; i < l; i++) {
				int aval = map.get(Math.abs(f.get(i).getA()));
				int a = f.get(i).getA();
				int a_r = torf(a, aval);

				int bval = map.get(Math.abs(f.get(i).getB()));
				int b = f.get(i).getB();
				int b_r = torf(b, bval);

				int cval = map.get(Math.abs(f.get(i).getC()));
				int c = f.get(i).getC();
				int c_r = torf(c, cval);

				if (a_r == 1 || b_r == 1 || c_r == 1) {
					count++;
				}
			}

			double requirement = (l / 8) * 7;

			if (count >= requirement) {
				solved = true;
			}
		} //
	}

}
