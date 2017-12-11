import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Day11 {

	public static void main(String[] args) {		
		try(Scanner in = new Scanner(new File("/home/florian/workspace/AdventOfCode/src/Day11.in"))) {
			solve(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void solve(final Scanner in) {
		int x = 0, y = 0, max = Integer.MIN_VALUE, cur;
		
		while(in.hasNext()) {
			switch(in.next()) {
			case "n": y+=2; break;
			case "s": y-=2; break;
			case "sw": x--; y--; break;
			case "nw": x--; y++; break;
			case "se": x++; y--; break;
			case "ne": x++; y++; break;
			default:
				throw new RuntimeException("Unknown token!");
			}
			
			if(max < (cur = distance(x,y))) max = cur;
			
		}
		System.out.println("x=" + x + ", y=" + y + ", Result=" + distance(x,y) + ", Max=" +max);
	}
	
	private static int distance(int x, int y) {
		x = Math.abs(x);
		y = Math.abs(y);
		
		if(x > y) {
			int tmp = x;
			x = y;
			y = tmp;
		}
		
		return x + (y-x) / 2;
	}
	
	@SuppressWarnings("unused")
	private static void test() {
		String[] tests = {"ne ne ne", "ne ne sw sw", "ne ne s s", "se sw se sw sw"};
		
		for(String t : tests) {
			solve(new Scanner(t));
		}
	}
}
