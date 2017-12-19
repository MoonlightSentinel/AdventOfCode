import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day19 {

	private static String test = 
			"     |          \n" + 
			"     |  +--+    \n" + 
			"     A  |  C    \n" + 
			" F---|----E|--+ \n" + 
			"     |  |  |  D \n" + 
			"     +B-+  +--+ ";
	
	public static void main(String[] args) {
		solve(new Scanner(test));

		try (Scanner in = new Scanner(new File("src/Day19.in"))) {
			solve(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private enum Direction {		
		Up(-1,0), Down(1, 0), Left(0, -1), Right(0, 1);
		
		public int x, y;
		
		private Direction(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public boolean notReversed(Direction other) {
			return (x + other.x) != 0 || (y + other.y) != 0;
		}
	};
	
	private static void solve(final Scanner input) {
		char[][] field = parse(input);
		
		Direction dir = Direction.Down;
		int x = 0, y = 0, steps = 0;
		
		// Start finden
		while(field[0][y] != '|') y++;
		
		String res = "";
		char cur;
		
		while(0 != (cur = safeAccess(field, x, y))) {
			if(Character.isAlphabetic(cur)) {
				res += cur;
			} 
			else if(cur == '+') {
				for(Direction newDir : Direction.values()) {
					cur = safeAccess(field, x + newDir.x, y + newDir.y);
					
					if(0 != cur && cur != ' ' && dir.notReversed(newDir)) {
						dir = newDir;
						break;
					}
				}
			} else if(cur != '|' && cur != '-') {
				break;
			}
			
			x += dir.x;
			y += dir.y;
			steps++;
		}
		
		System.out.println(res + " (" + steps + " Steps)");
	}

	private static char safeAccess(char[][] a, int x, int y) {
		if(0 <= x && x < a.length && 0 <= y && y < a[x].length) {
			return a[x][y];
		} else {
			return 0;
		}
	}
	
	private static char[][] parse(final Scanner sc) {
		List<char[]> lines = new ArrayList<>();
		while(sc.hasNext()) {
			lines.add(sc.nextLine().toCharArray());
		}
		sc.close();
		return lines.toArray(new char[lines.size()][]);		
	}
}
