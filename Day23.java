import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class Day23 {

	public static void main(String[] args) {

		try (Scanner in = new Scanner(new File("src/Day23.in"))) {
			solve(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void solve(final Scanner sc) {
		final List<Consumer<Processor>> cmds = parseInput(sc);

		System.out.println(new Processor(false).execute(cmds));
	}

	private static class Processor {
		private Map<Character, Long> registers = new HashMap<>();
		private int programCounter = 0, mulcount = 0;

		public Processor(boolean debug) {
			registers.put('a', debug ? 0L : 1L);
		}
		public int execute(List<Consumer<Processor>> cmds) {
			
			while (0 <= programCounter && programCounter < cmds.size()) {
				cmds.get(programCounter++).accept(this);
				
//				print();
			}
			
			return mulcount;
		}

		private long getRegister(char c) {
			return registers.computeIfAbsent(c, unused -> 0L);
		}

		private void setRegister(char reg, long value) {
			registers.put(reg, value);
		}
		
		private static interface Updater { long calc(long l); }
		
		private void updateRegister(char reg, Updater u) {
			registers.compute(reg, (key, val) -> u.calc(val == null ? 0 : val));
		}

		public void set(char arg1, char arg2) {
			set(arg1, getRegister(arg2));
		}

		public void set(char arg1, long arg2) {
			setRegister(arg1, arg2);
		}

		public void subtract(char arg1, char arg2) {
			subtract(arg1, getRegister(arg2));
		}

		public void subtract(char arg1, long arg2) {
			updateRegister(arg1, old -> old - arg2);
		}

		public void multiply(char arg1, char arg2) {
			multiply(arg1, getRegister(arg2));
		}

		public void multiply(char arg1, long arg2) {
			mulcount++;
			updateRegister(arg1, old -> old * arg2);
		}

		public void jumpNotZero(char arg1, char arg2) {
			jumpNotZero(arg1, getRegister(arg2));
		}

		public void jumpNotZero(char arg1, long arg2) {
			jumpNotZero(getRegister(arg1), arg2);
		}
		
		public void jumpNotZero(long arg1, long arg2) {
			if (0 != arg1) {
				this.programCounter += arg2 - 1;
			}
		}

		public void print() {
			System.out.println("PC: " + this.programCounter);
			this.registers.entrySet().forEach(System.out::println);
			System.out.println();
		}
	}

	private static List<Consumer<Processor>> parseInput(final Scanner sc) {
		List<Consumer<Processor>> res = new ArrayList<>();

		while (sc.hasNext()) {
			res.add(parse(sc, sc.next(), sc.next()));
		}

		sc.close();
		return res;
	}

	private static Consumer<Processor> parse(final Scanner sc, final String op, final String arg) {

		char arg1 = arg.charAt(0);
		final String sarg2 = sc.next();

		if (isRegister(sarg2)) {
			char arg2 = sarg2.charAt(0);

			switch (op) {
			case "set":
				return p -> p.set(arg1, arg2);
			case "sub":
				return p -> p.subtract(arg1, arg2);
			case "mul":
				return p -> p.multiply(arg1, arg2);
			case "jnz":
				if(isRegister(arg)) {
					return p -> p.jumpNotZero(arg1, arg2);
				} else {
					int iarg1 = Integer.parseInt(arg);
					return p -> p.jumpNotZero(iarg1, arg2);
				}
			}

		} else {
			int arg2 = Integer.parseInt(sarg2);

			switch (op) {
			case "set":
				return p -> p.set(arg1, arg2);
			case "sub":
				return p -> p.subtract(arg1, arg2);
			case "mul":
				return p -> p.multiply(arg1, arg2);
			case "jnz":
				if(isRegister(arg)) {
					return p -> p.jumpNotZero(arg1, arg2);
				} else {
					int iarg1 = Integer.parseInt(arg);
					return p -> p.jumpNotZero(iarg1, arg2);
				}
			}
		}
		throw new IllegalArgumentException("Unknown operator! " + op);
	}

	private static boolean isRegister(final String s) {
		return s.length() == 1 && Character.isAlphabetic(s.charAt(0));
	}
}