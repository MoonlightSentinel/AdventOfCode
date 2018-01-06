import java.util.ArrayList;
import java.util.List;

public class Day17 {

	public static void main(String[] args) {
		solve(366);
	}

	private static void solve(final int input) {
		solveA(input);
		solveB(input);
	}

	private static void solveA(final int input) {
		List<Integer> buffer = new ArrayList<>();
		buffer.add(0);
		int ind = 0, cur = 1;

		for (; cur <= 2017; cur++) {
			ind = (ind + input) % buffer.size() + 1;
			buffer.add(ind, cur);
		}

		System.out.println("A: " + buffer.get(ind + 1));
	}

	private static void solveB(final int input) {
		int zeroVal = 1, ind = 0, cur = 1;

		for (; cur <= 50000000; cur++) {
			ind = ((ind + input) % (cur)) + 1;

			// 0 is always at index 0 because a new value
			// after the current index
			if (ind == 1) {
				zeroVal = cur;
			}
		}

		System.out.println("B: " + zeroVal);
	}
}
