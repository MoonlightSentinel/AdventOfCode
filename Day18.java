import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.ArrayDeque;

public class Day18 {

	private static String testA = "set a 1\n" + "add a 2\n" + "mul a a\n" + "mod a 5\n" + "snd a\n" + "set a 0\n"
			+ "rcv a\n" + "jgz a -1\n" + "set a 1\n" + "jgz a -2",

			testB = "snd 1\n" + "snd 2\n" + "snd p\n" + "rcv a\n" + "rcv b\n" + "rcv c\n" + "rcv d",
			testC = "set i 31\n" + 
					"set a 1\n" + 
					"add b 1\n" + 
					"add a i\n" + 
					"mul a 2\n" + 
					"mul a b\n" + 
					"mod a 2\n" + 
					"mod b 3\n" + 
					"snd 2\n" + 
					"snd a\n" + 
					"jgz a 1\n" + 
					"jgz a b";

	public static void main(String[] args) {
//		solve(new Scanner(testA));
//		solve(new Scanner(testB));
//		solve(new Scanner(testC));

		try (Scanner in = new Scanner(new File("src/Day18.in"))) {
			solve(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void solve(final Scanner sc) {
		final List<Consumer<Processor>> cmds = parseInput(sc);

		Queue<Long> a = new ArrayDeque<>(), 
					b = new ArrayDeque<>();

		Processor p0 = new Processor(0, a, b), 
				  p1 = new Processor(1, b, a);
		
		boolean act0, act1;
		
		do {	
			act0 = p0.execute(cmds);			
			act1 = p1.execute(cmds);
			
		} while (act0 || act1);

		System.out.println("P1: " + p1.sendCount);
	}

	private static class Processor {
		private boolean isWaiting;
		private Map<Character, Long> registers = new HashMap<>();
		private int programCounter = 0, id;
		private Queue<Long> sendQueue, receiveQueue;
		
		long sendCount = 0;
		
		public Processor(int id, Queue<Long> s, Queue<Long> r) {
			this.sendQueue = s;
			this.receiveQueue = r;
			this.id = id;
			registers.put('p', (long) id);
		}

		public boolean execute(List<Consumer<Processor>> cmds) {
			System.out.println("------- Processor " + id + " --------");
			
			boolean didSomething = false;

			isWaiting = false;

			while (0 <= programCounter && programCounter < cmds.size()) {
				cmds.get(programCounter++).accept(this);

				if (isWaiting) break;

				print();

				didSomething = true;
			}

			return didSomething;
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

		public void send(char arg1) {
			send(getRegister(arg1));
		}
		
		public void send(long arg1) {
			sendCount++;
			sendQueue.add(arg1);
		}

		public void receive(char arg1) {
			Long val = receiveQueue.poll();

			if (val == null) {
				isWaiting = true;
				programCounter--;
			} else {
				setRegister(arg1, val);
			}
		}

		public void set(char arg1, char arg2) {
			set(arg1, getRegister(arg2));
		}

		public void set(char arg1, long arg2) {
			setRegister(arg1, arg2);
		}

		public void add(char arg1, char arg2) {
			add(arg1, getRegister(arg2));
		}

		public void add(char arg1, long arg2) {
			updateRegister(arg1, old -> old + arg2);
		}

		public void multiply(char arg1, char arg2) {
			multiply(arg1, getRegister(arg2));
		}

		public void multiply(char arg1, long arg2) {
			updateRegister(arg1, old -> old * arg2);
		}

		public void modulo(char arg1, char arg2) {
			modulo(arg1, getRegister(arg2));
		}

		public void modulo(char arg1, long arg2) {
			updateRegister(arg1, old -> old % arg2);
		}

		public void jumpGreaterZero(char arg1, char arg2) {
			jumpGreaterZero(arg1, getRegister(arg2));
		}

		public void jumpGreaterZero(char arg1, long arg2) {
			if (0 < getRegister(arg1)) {
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
		if ("snd".equals(op)) {
			if (isRegister(arg)) {
				return p -> p.send(arg.charAt(0));
			} else {
				return p -> p.send(Integer.parseInt(arg));
			}
		} else if ("rcv".equals(op)) {
			return p -> p.receive(arg.charAt(0));
		}

		char arg1 = arg.charAt(0);
		final String sarg2 = sc.next();

		if (isRegister(sarg2)) {
			char arg2 = sarg2.charAt(0);

			switch (op) {
			case "set":
				return p -> p.set(arg1, arg2);
			case "add":
				return p -> p.add(arg1, arg2);
			case "mul":
				return p -> p.multiply(arg1, arg2);
			case "mod":
				return p -> p.modulo(arg1, arg2);
			case "jgz":
				return p -> p.jumpGreaterZero(arg1, arg2);
			}

		} else {
			int arg2 = Integer.parseInt(sarg2);

			switch (op) {
			case "set":
				return p -> p.set(arg1, arg2);
			case "add":
				return p -> p.add(arg1, arg2);
			case "mul":
				return p -> p.multiply(arg1, arg2);
			case "mod":
				return p -> p.modulo(arg1, arg2);
			case "jgz":
				return p -> p.jumpGreaterZero(arg1, arg2);
			}
		}
		throw new IllegalArgumentException("Unknown operator!");
	}

	private static boolean isRegister(final String s) {
		return s.length() == 1 && Character.isAlphabetic(s.charAt(0));
	}
}