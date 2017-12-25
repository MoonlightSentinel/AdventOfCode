import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.BiFunction;

public class Day25 {

	public static void main(String[] args) throws Exception {
		try (Scanner in = new Scanner(new File("src/Day25.in"))) {
			solve(in);
		}
	}

	private static void solve(final Scanner in) {
		TuringTask task = parse(in);
		Tape tape = new Tape();
		State cur = task.start;

		for (int i = 0; i < task.iterations; i++) {
			Transition tran = cur.getTransition(tape.get());
			tape.apply(tran);
			cur = tran.next;
		}

		System.out.println(tape.checksum());
	}

	private static class Tape {
		private int cur = 0;
		private ArrayList<Integer> pos = new ArrayList<>(), neg = new ArrayList<>();

		public int get() {
			return access(List::get);
		}

		private int access(BiFunction<List<Integer>, Integer, Integer> accessor) {
			List<Integer> l;
			int ind;

			if (cur < 0) {
				l = neg;
				ind = -cur;
			} else {
				l = pos;
				ind = cur;
			}

			while (l.size() <= ind)
				l.add(0);

			return accessor.apply(l, ind);
		}

		public void apply(final Transition t) {
			access((list, ind) -> list.set(ind, t.toWrite));

			if (t.moveRight) {
				cur++;
			} else {
				cur--;
			}
		}

		public int checksum() {
			return count(pos) + count(neg);
		}

		private static int count(List<Integer> list) {
			int counter = 0;

			for (Integer i : list) {
				if (i == 1)
					counter++;
			}

			return counter;
		}
	}

	private static class State {
		private final Transition zero, one;

		public State(Transition z, Transition o) {
			zero = z;
			one = o;
		}

		public void finish(Map<String, State> states) {
			zero.finish(states);
			one.finish(states);
		}

		public Transition getTransition(int val) {
			return val == 0 ? zero : one;
		}
	}

	private static class Transition {

		public final int toWrite;
		public final boolean moveRight;
		public State next;
		public String nextStateString;

		public Transition(int val, boolean right, String line) {
			this.toWrite = val;
			this.moveRight = right;
			this.next = null;
			this.nextStateString = line;
		}

		public void finish(Map<String, State> states) {
			this.next = states.get(this.nextStateString);
			this.nextStateString = null;
		}
	}

	private static class TuringTask {
		@SuppressWarnings("unused")
		public final Map<String, State> states;
		public final State start;
		public final int iterations;

		public TuringTask(Map<String, State> states, State start, int iterations) {
			this.states = states;
			this.start = start;
			this.iterations = iterations;
		}
	}

	private static TuringTask parse(final Scanner in) {
		Map<String, State> states = new HashMap<>();

		skip(in, 3);
		String start = nextWithoutLast(in);

		skip(in, 5);
		int steps = in.nextInt();
		in.next();

		while (in.hasNext()) {
			skip(in, 2);
			String name = in.next();
			name = name.substring(0, name.length() - 1);

			Transition z = parseTransition(in), o = parseTransition(in);

			states.put(name, new State(z, o));
		}

		states.forEach((unused, state) -> state.finish(states));

		return new TuringTask(states, states.get(start), steps);
	}

	private static Transition parseTransition(final Scanner in) {
		while ("".equals(in.nextLine()))
			; // If val is x

		String line = in.nextLine();
		int val = line.charAt(line.length() - 2) - '0';

		line = in.nextLine();
		boolean right = line.contains("right");

		skip(in, 4);
		line = in.next();
		line = line.substring(0, line.length() - 1);

		return new Transition(val, right, line);
	}

	private static void skip(final Scanner in, final int count) {
		for (int i = 0; i < count; i++)
			in.next();
	}

	private static String nextWithoutLast(final Scanner in) {
		String s = in.next();
		s = s.substring(0, s.length() - 1);
		return s;
	}
}
