import java.io.File;
import java.util.Scanner;

public class Day9 {

	public static void main(String[] args) {
		try (Scanner s = new Scanner(new File("Day9.in"))){
			while(s.hasNextLine()) {
				String line = s.nextLine();
				System.out.println(line);

				int[] res = score(line);
				System.out.println("Score: " + res[0] + ", " + res[1] + " Characters");
			}
		} catch(Exception e) {
			System.out.println(e);
		}
	}

	public static int[] score(final String list) {
		int sum = 0, depth = 0, counter = 0;
		boolean skipping = false, garbage = false;

		for (int i = 0; i < list.length(); i++) {
			if (skipping) {
				skipping = false;
				continue;
			} else if (garbage) {
				switch (list.charAt(i)) {
				case '!':
					skipping = true;
					break;
				case '>':
					garbage = false;
					break;
				default: 
					counter++;
					break;
				}
			} else {
				switch (list.charAt(i)) {
				case '{':
					depth++;
					break;
				case '}':
					sum += depth;
					depth--;
					break;
				case '!':
					skipping = true;
					break;
				case '<':
					garbage = true;
					break;
				}
			}
		}
		return new int[]{sum, counter};
	}
}
