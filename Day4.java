import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class Day4 {
	public static void main(String[] args) {
		try(InputStream in = Day4.class.getResourceAsStream("Day4_Input.txt");
			Scanner sc = new Scanner(in)) {
			int counter = 0;
			
			while(sc.hasNext()) {
				
				if(valid(sc.nextLine())) {
					counter++;
				}
			}
			
			System.out.println(counter + " gueltige Passwoerter!");
		} catch (IOException e) {
			System.out.println("Exception of file: " + e.getMessage());
		}
	}
	
	private static boolean valid(String line) {
		Set<String> words = new TreeSet<>();

		for(String word : line.split(" ")) {
			if(words.contains(word)) {
				return false;
			}
			
			for(String cur : words) {
				if(isAnagram(cur, word)) {
					return false;
				}
			}
			
			words.add(word);
		}
		return true;
	}

	private static boolean isAnagram(String cur, String word) {		
		return cur.length() == word.length() 
			&& Arrays.equals(countLetters(cur), countLetters(word));
	}
	
	private static int[] countLetters(String s) {
		int[] arr = new int['z'-'a' + 1];
		
		for(int i = 0; i < s.length(); i++) {
			arr[s.charAt(i) - 'a']++;
		}
		
		return arr;
	}
}
