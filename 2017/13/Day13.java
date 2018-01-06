import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day13 {

	public static void main(String[] args) {
		solve(new Scanner("0: 3\n" + "1: 2\n" + "4: 4\n" + "6: 4"));

		try (Scanner in = new Scanner(new File("/home/florian/workspace/AdventOfCode/src/Day13.in"))) {
			solve(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void solve(final Scanner in) {
		List<Layer> firewall = parse(in);

		System.out.println("Severity: " + solve_A(copy(firewall)));
		System.out.println("Offset: " + solve_B(firewall));
	}

	private static class Layer {
		private static final boolean UP = true, DOWN = !UP;

		int current;
		final int max;
		boolean direction;

		public Layer() {
			this(-1, -1);
		}

		public Layer(int current, int max) {
			this.current = current;
			this.max = max;
			this.direction = UP;
		}

		public void set(final Layer l) {
			this.current = l.current;
			this.direction = l.direction;
		}

		public void update() {
			if (max != -1) {
				if (direction == UP && this.current == this.max - 1 || direction == DOWN && current == 0) {
					this.direction = !this.direction;
				}

				if (this.direction == UP) {
					current += 1;
				} else {
					current -= 1;
				}
			}
		}

		public String toString() {
			return current + " / " + max;
		}
	}

	private static List<Layer> parse(final Scanner in) {
		List<Layer> firewall = new ArrayList<>();

		while (in.hasNext()) {
			String n = in.next();
			int num = Integer.parseInt(n.substring(0, n.length() - 1));

			while (firewall.size() < num) {
				firewall.add(new Layer());
			}

			firewall.add(new Layer(0, in.nextInt()));
		}

		return firewall;
	}

	private static int solve_A(final List<Layer> firewall) {
		int sum = 0;
		for (int i = 0; i < firewall.size(); i++) {

			Layer l = firewall.get(i);

			if (l.current == 0) {
				sum += i * l.max;
			}

			firewall.forEach(Layer::update);
		}
		return sum;
	}

	private static int solve_B(final List<Layer> firewall) {
		List<Layer> copy = copy(firewall);

		int offset = 0;

		while (gotHit(copy)) {
			offset++;

			for (int j = 0; j < firewall.size(); j++) {
				Layer l = firewall.get(j);
				l.update();
				copy.get(j).set(l);
			}
		}

		return offset;
	}

	private static boolean gotHit(final List<Layer> firewall) {

		for (Layer l : firewall) {

			if (l.current == 0) {
				return true;
			}
			firewall.forEach(Layer::update);
		}

		return false;
	}

	private static List<Layer> copy(List<Layer> list) {
		List<Layer> copy = new ArrayList<>(list.size());

		list.forEach(l -> copy.add(new Layer(l.current, l.max)));

		return copy;
	}

	@SuppressWarnings("unused")
	private static void print(final int current, final List<Layer> firewall) {
		System.out.println("Picosecond " + current);

		for (int i = 0; i < firewall.size(); i++) {
			System.out.print(" " + i + "  ");
		}
		System.out.println();

		for (int i = 0;; i++) {
			boolean out = false;

			for (int j = 0; j < firewall.size(); j++) {
				Layer l = firewall.get(j);

				if (i >= l.max) {
					System.out.print(i == 0 && j == current ? "( ) " : "    ");
				} else {
					out = true;
					if (current == j && i == 0) {
						System.out.print(l.current == i ? "(S) " : "( ) ");
					} else if (l.current == i) {
						System.out.print("[S] ");
					} else {
						System.out.print("[ ] ");
					}
				}
			}
			System.out.println();
			if (!out) {
				break;
			}
		}
	}
}
