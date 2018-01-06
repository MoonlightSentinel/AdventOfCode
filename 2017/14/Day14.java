import java.util.List;
import java.math.BigInteger;
import java.util.ArrayList;

public class Day14 {

	private static final String[] input = { "flqrgnkx", "amgozmfv" };

	public static void main(String[] args) {
		for (String in : input) {
			System.out.println(in);
			System.out.println("A: " + solve_A(in));
			System.out.println("B: " + solve_B(in));
		}
	}

	static class Pair {
		int x, y;

		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}

	}

	private static int solve_B(String in) {
		boolean[][] grid = new boolean[128][128];

		for (int i = 0; i < 128; i++) {
			String toHash = in + '-' + i,
					hash = Day10B.solve(toHash);
			
			for(int cur = 0, j = 0; j < hash.length(); j++) {
				char c = hash.charAt(j);
				int val = Character.digit(c, 16);
				
				grid[i][cur++] = (0b1000 & val) != 0;
				grid[i][cur++] = (0b0100 & val) != 0;
				grid[i][cur++] = (0b0010 & val) != 0;
				grid[i][cur++] = (0b0001 & val) != 0;
			}
		}

//		print(grid);
		int regions = 0;

		for (int i = 0; i < 128; i++) {
			for (int j = 0; j < 128; j++) {
				if (grid[i][j]) {
					purgeRegion(grid, i, j);
					regions++;
				}
			}
		}
		return regions;
	}

	private static void purgeRegion(boolean[][] grid, int i, int j) {
		List<Pair> list = new ArrayList<>();
		list.add(new Pair(i, j));

		while (!list.isEmpty()) {
			Pair cur = list.remove(list.size() - 1);
			grid[cur.x][cur.y] = false;

			optAdd(list, grid, cur.x + 1, cur.y);
			optAdd(list, grid, cur.x - 1, cur.y);
			optAdd(list, grid, cur.x, cur.y + 1);
			optAdd(list, grid, cur.x, cur.y - 1);
		}
	}

	private static void optAdd(List<Pair> list, boolean[][] grid, int x, int y) {
		if (0 <= x && x < 128 && 0 <= y && y < 128 && grid[x][y]) {
			list.add(new Pair(x, y));
		}
	}
	
	private static void print(boolean[][] grid) {
		for(boolean[] line : grid) {
			for(boolean digit : line) {
				System.out.print(digit ? '#' : '.');
			}
			System.out.println();
		}
	}

	private static long solve_A(String in) {
		long result = 0;
		for (int i = 0; i < 128; i++) {
			String hex = Day10B.solve(in + '-' + i);
			BigInteger intValue = new BigInteger(hex, 16);
			String bin = intValue.toString(2);
			result += bin.chars().filter(dig -> dig == '1').count();
		}
		return result;
	}
}
