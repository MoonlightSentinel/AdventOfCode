import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

import static java.lang.Integer.parseInt;

/**
 * Output
 * Input: abcde 
 * A: baedc 
 * B: abcde 
 * 
 * Input: abcdefghijklmnop 
 * A:jkmflcgpdbonihea 
 * B: ajcdefghpkblmion
 */
public class Day16 {

	public static void main(String[] args) {
		solve(new Scanner("s1\n" + "x3/4\n" + "pe/b\n"), "abcde".toCharArray());

		try (Scanner in = new Scanner(new File("Day16.in"))) {
			solve(in, "abcdefghijklmnop".toCharArray());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Iterate until 1.000.000 because the period of my input is 3.000.000
	 * 
	 * 0: 		  abcdefghijklmnop 
	 * 1.000.000: ajcdefghpkblmion 
	 * 2.000.000: akcdefghnbjlmpoi
	 * 3.000.000: abcdefghijklmnop
	 * 
	 * 1.000.000.000 % 3.000.000 = 1.000.000
	 */
	private static void solve(final Scanner sc, final char[] list) {
		System.out.println("Input: " + new String(list));
		final List<Consumer<char[]>> cmds = parseInput(sc);

		// Part A
		cmds.forEach(c -> c.accept(list));
		System.out.println("A: " + new String(list));

		// Part B
		for (int i = 1; i < 1_000_000; i++) {
			cmds.forEach(c -> c.accept(list));
		}

		System.out.println("B: " + new String(list));
	}

	private static List<Consumer<char[]>> parseInput(final Scanner sc) {
		List<Consumer<char[]>> res = new ArrayList<>();

		while (sc.hasNext()) {
			String op = sc.next();
			char task = op.charAt(0);
			op = op.substring(1);

			switch (task) {
			case 's':
				int size = parseInt(op);
				res.add(list -> spin(list, size));
				break;
			case 'x':
				String[] ops = op.split("/");
				int arg1 = parseInt(ops[0]), arg2 = parseInt(ops[1]);
				res.add(list -> exchange(list, arg1, arg2));
				break;
			case 'p':
				char c1 = op.charAt(0), c2 = op.charAt(2); // X/Y
				res.add(list -> swap(list, c1, c2));
				break;
			}
		}
		sc.close();
		return res;
	}

	private static char[] back = new char[16];

	private static void spin(char[] list, int size) {
		int a = list.length - size, b = 0;

		while (b < size)
			back[b++] = list[a++];

		b = list.length - 1;
		a = list.length - size - 1;

		while (0 <= a)
			list[b--] = list[a--];

		a = b = 0;

		while (a < size)
			list[b++] = back[a++];
	}

	private static void exchange(char[] list, int a, int b) {
		char tmp = list[a];
		list[a] = list[b];
		list[b] = tmp;
	}

	private static void swap(char[] list, char a, char b) {
		int indA = -1, indB = -1;

		for (int i = 0; i < list.length; i++) {
			if (list[i] == a) {
				indA = i;
			} else if (list[i] == b) {
				indB = i;
			}
		}

		exchange(list, indA, indB);
	}
}
