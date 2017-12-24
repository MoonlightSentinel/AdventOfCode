import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day24 {

	private static String test = "0/2\n" + "2/2\n" + "2/3\n" + "3/4\n" + "3/5\n" + "0/1\n" + "10/1\n" + "9/10";

	public static void main(String[] args) throws Exception {
		solve(new Scanner(test));

		try (Scanner in = new Scanner(new File("src/Day24.in"))) {
			solve(in);
		}
	}

	private static class Part {
		public final int a, b;

		public Part(int a, int b) {
			this.a = a;
			this.b = b;
		}

		public int sum() {
			return a + b;
		}

		public int append(int end) {
			if (end == a) {
				return b;
			} else if (end == b) {
				return a;
			} else {
				return -1;
			}
		}

		public String toString() {
			return a + " / " + b;
		}
	}

	private static void solve(final Scanner in) {
		List<Part> parts = parse(in);

		System.out.println("A: " + findMax(parts, 0));
		System.out.println("B: " + findLongest(parts, 0));
	}

	private static int findMax(final List<Part> parts, final int end) {
		return findMax(parts, new boolean[parts.size()], end);
	}

	private static int findMax(final List<Part> parts, final boolean[] used, final int end) {
		int max = 0;

		for (int i = 0; i < parts.size(); i++) {
			if (!used[i]) {
				Part cand = parts.get(i);

				int tmp = cand.append(end);

				if (tmp != -1) {
					used[i] = true;
					tmp = findMax(parts, used, tmp) + cand.sum();
					used[i] = false;

					if (tmp > max)
						max = tmp;
				}
			}
		}

		return max;
	}

	private static class Path {
		public final int sum, len;

		public Path(final Path other, final Part p) {
			sum = other.sum + p.sum();
			len = other.len + 1;
		}

		public Path() {
			sum = len = 0;
		}
	}

	private static int findLongest(final List<Part> parts, final int end) {
		return findLongest(parts, new boolean[parts.size()], new Path(), end).sum;
	}

	private static Path findLongest(final List<Part> parts, final boolean[] used, final Path path, final int end) {
		Path max = path;

		for (int i = 0; i < parts.size(); i++) {
			if (!used[i]) {
				Part cand = parts.get(i);

				int tmp = cand.append(end);

				if (tmp != -1) {
					used[i] = true;
					Path pmax = findLongest(parts, used, new Path(path, cand), tmp);
					used[i] = false;

					if (pmax.len > max.len || (pmax.len == max.len && pmax.sum > max.sum)) {
						max = pmax;
					}
				}
			}
		}

		return max;
	}

	private static List<Part> parse(final Scanner in) {
		final List<Part> res = new ArrayList<>();

		while (in.hasNext()) {
			String[] val = in.next().split("/");
			res.add(new Part(Integer.parseInt(val[0]), Integer.parseInt(val[1])));
		}

		in.close();

		return res;
	}

}
