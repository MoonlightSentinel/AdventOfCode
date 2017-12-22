import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Day22 {

	private final static String test = "..#\n" + "#..\n" + "...";

	public static void main(String[] args) throws FileNotFoundException {
		solve(new Scanner(test));
		try (Scanner in = new Scanner(new File("src/Day22.in"))) {
			solve(in);
		}
	}

	private static void solve(Scanner in) {
		final TreeMap<Point, State> map = new TreeMap<>();
		final Point start = parse(in, map);

		Ant a = new Ant_A(new TreeMap<>(map), start), b = new Ant_B(map, start);

		System.out.println("A: " + a.step(10_000));
		System.out.println("B: " + b.step(10_000_000));
	}

	private static class Point implements Comparable<Point> {
		public int x, y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Point(Point other) {
			this.x = other.x;
			this.y = other.y;
		}

		public void move(Direction dir) {
			switch (dir) {
			case UP:
				x--;
				break;
			case RIGHT:
				y++;
				break;
			case DOWN:
				x++;
				break;
			case LEFT:
				y--;
				break;
			}
		}

		@Override
		public int hashCode() {
			return x * 17 + y * 31;
		}

		@Override
		public boolean equals(Object other) {
			Point o = (Point) other;

			return x == o.x && y == o.y;
		}

		@Override
		public String toString() {
			return "(" + x + ", " + y + ")";
		}

		@Override
		public int compareTo(Point o) {
			return x != o.x ? x - o.x : y - o.y;
		}
	}

	private static enum Direction {
		UP, RIGHT, DOWN, LEFT;

		private static final Direction[] VALUES = Direction.values();

		public Direction left() {
			return VALUES[(ordinal() + 3) % VALUES.length];
		}

		public Direction right() {
			return VALUES[(ordinal() + 1) % VALUES.length];
		}

		public Direction reversed() {
			return VALUES[(ordinal() + 2) % VALUES.length];
		}
	}

	private static enum State {
		Clean, Weakened, Infected, Flagged;
	}

	private static abstract class Ant {

		private TreeMap<Point, State> world;
		private Point position;
		protected Direction dir;
		protected int infections;

		public Ant(TreeMap<Point, State> world, Point start) {
			this.world = world;
			this.position = new Point(start);
			this.dir = Direction.UP;
			this.infections = 0;
		}

		public int step(final int count) {
			for (int i = 0; i < count; i++) {
				step();
			}

			return infections;
		}

		public void step() {
			Point pos = this.position;
			State state = world.get(position);

			if (state == null) {
				state = State.Clean;
				pos = new Point(position);
			}

			state = changeState(state);
			world.put(pos, state);
			position.move(dir);

			if (position.y < miny)
				miny = position.y;
		}

		protected abstract State changeState(final State state);

		private int miny = 0;

		@Override
		public String toString() {

			StringBuilder sb = new StringBuilder(" ");
			int lastx = world.entrySet().iterator().next().getKey().x, lasty = miny;

			for (Map.Entry<Point, State> entry : world.entrySet()) {
				Point cur = entry.getKey();

				if (lastx != cur.x) {
					sb.append("\n ");
					lastx = cur.x;
					lasty = miny;
				}

				while (lasty < cur.y) {
					sb.append(" . ");
					lasty++;
				}

				switch (entry.getValue()) {
				case Clean:
					sb.append(" . ");
					break;
				case Weakened:
					sb.append(" W ");
					break;
				case Infected:
					sb.append(" # ");
					break;
				case Flagged:
					sb.append(" F ");
					break;
				}

				lasty++;

				if (cur.equals(position)) {
					sb.setCharAt(sb.length() - 3, '[');
					sb.setCharAt(sb.length() - 1, ']');
				}
			}

			sb.append("\n\nPosition: ").append(position).append("\nDirection: ").append(dir).append("\nInfections: ")
					.append(infections).append("\n--------------------------------");

			return sb.toString();
		}
	}

	private static class Ant_A extends Ant {

		public Ant_A(TreeMap<Point, State> world, Point start) {
			super(world, start);
		}

		protected State changeState(final State state) {
			switch (state) {
			case Clean:
				dir = dir.left();
				infections++;
				return State.Infected;

			case Infected:
				dir = dir.right();
				return State.Clean;

			default:
				throw new RuntimeException("Impossible");
			}
		}
	}

	private static class Ant_B extends Ant {

		public Ant_B(TreeMap<Point, State> world, Point start) {
			super(world, start);
		}

		protected State changeState(final State state) {
			switch (state) {
			case Clean:
				dir = dir.left();
				return State.Weakened;

			case Weakened:
				infections++;
				return State.Infected;

			case Infected:
				dir = dir.right();
				return State.Flagged;

			case Flagged:
				dir = dir.reversed();
				return State.Clean;
			}

			throw new RuntimeException("Impossible");
		}
	}

	private static Point parse(Scanner in, TreeMap<Point, State> res) {

		int x = 0, y = 0;
		String line;

		while (in.hasNext()) {
			y = 0;
			line = in.nextLine();

			while (y < line.length()) {
				char c = line.charAt(y);
				if (c == '#') {
					res.put(new Point(x, y), State.Infected);
				}
				y++;
			}
			x++;
		}

		return new Point(x / 2, y / 2);
	}
}
