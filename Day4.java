import java.util.Arrays;
import java.util.TreeSet;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Day4 {
	public static void main(String[] args) throws Exception {
		long counter =
		
		Files.lines(Paths.get("Day4_Input"))
				
		.map(line -> line.split(" "))
		.map(Arrays::stream)
		.filter(stream -> stream.allMatch(new TreeSet<>()::add)) // Aufgabe A
		.count();
		
		System.out.println(counter + " gueltige Passwoerter!");
		
	}
}
