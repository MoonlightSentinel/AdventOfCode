import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day21 {

	public static void main(String[] args) throws Exception {
		final List<Rule> rules = parse("src/Day21.in");
		final Grid grid = new Grid();

		next(grid, rules, 5);
		System.out.println("B: " + grid.onPixelCount());
		
		next(grid, rules, 13);
		System.out.println("B: " + grid.onPixelCount());
	}
	
	private static void next(final Grid grid, final List<Rule> rules, final int iterations) {
		for(int i = 0; i < iterations; i++) {
			
			for (Grid.View view : grid.step()) {
				for (Rule r : rules) {
					if (r.apply(view)) {
						break;
					}
				}
			}
		}
	}

	private static class Grid {
		private char[][] grid = { 
			".#.".toCharArray(), 
			"..#".toCharArray(), 
			"###".toCharArray() 
		};

		private int blockSize = 3;

		public List<View> step() {
			blockSize = (grid.length % 2 == 0) ? 2 : 3;
			int units = grid.length / blockSize, newSize = grid.length + units;

			expandArray(newSize);

			List<View> res = new ArrayList<>(units * units);
			
			for (int i = 0; i < newSize; i += blockSize + 1) {
				for (int j = 0; j < newSize; j += blockSize + 1) {
					res.add(new View(i, j));
				}
			}

			return res;
		}
		
		private void expandArray(int newSize) {
			char[][] newGrid = new char[newSize][newSize];

			for (int newRow = 0, oldRow = 0; newRow < newSize;) {

				for (int i = 0; i < blockSize; i++) {
					for (int oldCol = 0, newCol = 0; newCol < newSize;) {

						for (int j = 0; j < blockSize; j++) {
							newGrid[newRow][newCol] = grid[oldRow][oldCol];

							newCol++;
							oldCol++;
						}
						newCol++;
					}
					newRow++;
					oldRow++;
				}
				newRow++;
			}

			grid = newGrid;
		}

		private class View {
			private final int xoff, yoff;

			public View(int x, int y) {
				this.xoff = x;
				this.yoff = y;
			}

			public char get(int x, int y) {
				return grid[x + xoff][y + yoff];
			}

			public void set(int x, int y, char c) {
				grid[x + xoff][y + yoff] = c;
			}

			public int size() {
				return blockSize;
			}

			@Override
			public String toString() {
				int s = size();
				StringBuilder sb = new StringBuilder(s * s);

				for (int i = 0; i <= s; i++) {
					for (int j = 0; j <= s; j++) {
						char c = get(i, j);
						sb.append((c == '.' || c == '#' ? c : 'X'));
					}
					sb.append('\n');
				}
				return sb.toString();
			}
		}

		public int onPixelCount() {
			int count = 0;
			for (char[] row : grid) {
				for (char c : row) {
					if (c == '#') {
						count++;
					}
				}
			}
			return count;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder((grid.length + 1) * grid.length);
			for (char[] row : grid) {
				for (char c : row) {
					sb.append((c == '.' || c == '#' ? c : 'X'));
				}
				sb.append('\n');
			}
			return sb.toString();
		}
	}

	private static class Rule {

		final char[][] from, to;

		public Rule(String fromStr, String toStr) {
			this.from = parse(fromStr);
			this.to = parse(toStr);
		}

		private static char[][] parse(String s) {
			String[] rows = s.split("/");
			char[][] res = new char[rows.length][];

			for (int i = 0; i < rows.length; i++) {
				res[i] = rows[i].toCharArray();
			}

			return res;
		}

		private static interface View {
			public char get(char[][] arr, int x, int y, int len);
			
			public static final View
				ROTATE_000 = (arr, x, y, len) -> arr[x][y],
				ROTATE_090 = (arr, x, y, len) -> arr[y][len-x],		// 90
				ROTATE_180 = (arr, x, y, len) -> arr[len-x][len-y],	// 180
				ROTATE_270 = (arr, x, y, len) -> arr[len-y][x];		// 270;
				
			public static final View[] MODES = {
				ROTATE_000, ROTATE_090, ROTATE_180, ROTATE_270,
				flip(ROTATE_000), flip(ROTATE_090), flip(ROTATE_180), flip(ROTATE_270)
			};
			
			public static View flip(final View in) {
				return (arr, x, y, len) -> in.get(arr, len-x, y, len);
			}
		}
		
		public boolean apply(final Grid.View view) {
			if (view.size() != from.length) {
				return false;
			}
			for(View v : View.MODES) {
				if(matches(view, v)) {
					execute(view);
					return true;
				}
			}
			
			return false;
		}

		private boolean matches(Grid.View view, Rule.View fromView) {
			for (int i = 0; i < view.size(); i++) {
				for (int j = 0; j < view.size(); j++) {
					
					if (view.get(i, j) != fromView.get(from, i, j, from.length-1)) {
						return false;
					}
				}
			}
			return true;
		}

		private void execute(Grid.View view) {
			for (int i = 0; i <= view.size(); i++) {
				for (int j = 0; j <= view.size(); j++) {
					view.set(i, j, to[i][j]);
				}
			}
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < from.length; i++) {
				sb.append(from[i]).append('\t').append(to[i]).append('\n');
			}

			for (int i = 0; i < from[0].length; i++) {
				sb.append(' ');
			}
			
			sb.append('\t').append(to[from.length]);
			return sb.toString();
		}
	}

	private static List<Rule> parse(Scanner in) {
		List<Rule> res = new ArrayList<>();
		String from, to;

		while (in.hasNext()) {
			from = in.next();
			in.next(); // =>
			to = in.next();

			res.add(new Rule(from, to));
		}

		return res;
	}

	private static List<Rule> parse(String file) throws FileNotFoundException {
		try(Scanner in = new Scanner(new File(file))) {
			return parse(in);
		}
	}
}
