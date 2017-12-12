import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day12 {

	public static void main(String[] args) {
		solve(new Scanner("0 <-> 2\n" + "1 <-> 1\n" + "2 <-> 0, 3, 4\n" + "3 <-> 2, 4\n" + "4 <-> 2, 3, 6\n"
				+ "5 <-> 6\n" + "6 <-> 4, 5"));

		try (Scanner in = new Scanner(new File("/home/florian/workspace/AdventOfCode/src/Day12.in"))) {
			solve(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void solve(final Scanner in) {
		Map<Integer, List<Integer>> graph = new HashMap<>();

		while (in.hasNext()) {
			int cur = in.nextInt();
			in.next(); // Arrow
			List<Integer> list = new ArrayList<>();
			graph.put(cur, list);

			while (true) {
				String token = in.next();

				if (token.charAt(token.length() - 1) == ',') {
					token = token.substring(0, token.length() - 1);
					cur = Integer.parseInt(token);
					list.add(cur);
				} else {
					list.add(Integer.parseInt(token));
					break;
				}
			}
		}

		System.out.println("Counter: " + countGroups(graph));
	}

	private static int countGroups(final Map<Integer, List<Integer>> graph) {
		int counter = 0;

		while (!graph.isEmpty()) {
			countGroup(graph.keySet().iterator().next(), graph);
			counter++;
		}

		return counter;
	}

	private static int countGroup(final int start, final Map<Integer, List<Integer>> graph) {
		List<Integer> remaining = new ArrayList<>();
		remaining.add(start);
		int counter = 0;

		while (!remaining.isEmpty()) {
			int cur = remaining.remove(remaining.size() - 1);
			List<Integer> list = graph.remove(cur);

			if (list != null) {
				counter++;
				remaining.addAll(list);
			}
		}

		return counter;
	}
}
