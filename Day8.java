import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.File;

public class Day8 {

	public static void main(String[] args) {
		for (File in : new File("Day8.in").listFiles()) {
			try(Scanner s = new Scanner(in)) {
				System.out.println(in.getName());
				System.out.println(solve(s));
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private static class Result {
		int max, maxTemp;

		public Result(int m, int mT) {
			max = m;
			maxTemp = mT;
		}

		public String toString() {
			return "Max value: " + max + ", Max temporary value: " + maxTemp;
		}
	}

	public static Result solve(final Scanner s) {

		Map<String, Integer> register = new HashMap<>();
		int max = Integer.MIN_VALUE;

		while (s.hasNext()) {
			String reg = s.next(), // Register
					op = s.next(); // Inc / dec

			int count = s.nextInt(); // Count (10, -5 ..)

			s.next(); // if

			String boolReg = s.next(), // Boolean Reg1
					boolOp = s.next(), // Boolean operator
					boolParam = s.next(); // Boolean Reg1 or Literal

			int c1_value = register.computeIfAbsent(boolReg, unused -> 0);

			Integer c2_value = getIntOrNull(boolParam);

			if (null == c2_value) {
				c2_value = register.computeIfAbsent(boolParam, unused -> 0);
			}

			if (applyOp(c1_value, boolOp, c2_value)) {
				int n = register.compute(reg, (key, i) -> applyOperator(i == null ? 0 : i, op, count));

				if (max < n) max = n;
			}
		}

		s.close();

		return new Result(Collections.max(register.values()), max);
	}

	private static Integer getIntOrNull(String s) {
		return s.matches("-?[0-9]+") ? Integer.parseInt(s) : null;
	}

	private static int applyOperator(int old, String op, int val) {
		switch (op) {
			case "inc":
				return old + val;
			case "dec":
				return old - val;
		}
		throw new IllegalArgumentException("Should not happen, right ..?");
	}

	private static boolean applyOp(int a, String op, int b) {
		switch (op) {
			case "==":
				return a == b;
			case "!=":
				return a != b;
			case ">":
				return a > b;
			case ">=":
				return a >= b;
			case "<":
				return a < b;
			case "<=":
				return a <= b;
		}
		throw new IllegalArgumentException("Should not happen, right ..?");
	}
}