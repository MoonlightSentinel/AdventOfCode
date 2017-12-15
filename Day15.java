import java.util.Arrays;

public class Day15 {
	private static final int[][] input = { 
			{65, 8921},
			{873, 583 }
	};
	
	public static void main(String[] args) {
		for (int[] in : input) {
			System.out.println(Arrays.toString(in));
			System.out.println("A: " + solveA(in));
			System.out.println("B: " + solveB(in));
		}
	}

	private static class Generator {
		public static final int[] mult = {16807, 48271};
		private static final long MOD = 2147483647;

		private final long m;
		private long cur;
		
		public Generator(int cur, int m) {
			this.cur = cur;
			this.m = mult[m];
		}
		
		public int next() {
			cur = (cur * m) % MOD;
			return (int) cur;
		}
	}

	private static class SelectingGenerator extends Generator {
		private int div;
		
		public SelectingGenerator(int cur, int m, int div) {
			super(cur, m);
			this.div = div;
		}
	
		public int next() {
			int n;
			do {
				n = super.next();
			} while(n % div != 0);
			return n;
		}
	}
	
	private static int solveA(final int[] start) {
		return solve(
				40_000_000,
				new Generator(start[0],0),
				new Generator(start[1],1));
	}
	
	private static int solveB(final int[] start) {
		return solve(
				5_000_000,
				new SelectingGenerator(start[0],0, 4),
				new SelectingGenerator(start[1],1, 8));
	}
	
	private static int solve(int max, Generator a, Generator b) {
		int equal = 0;
		
		for (int i = 0; i < max; i++) {			
			if(0 == ((a.next() ^ b.next()) & 0b1111_1111_1111_1111)) {
				equal++;
			}
		}
		
		return equal;
	}
}
