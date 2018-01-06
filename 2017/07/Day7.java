import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day7 {
	
	public static void main(String[] args) {
		for (File in : new File("Day7.in").listFiles()) {
			try(Scanner s = new Scanner(in)) {
				System.out.println(in.getName());
				final Program root = parseTree(s);
				System.out.println("Root: " + root.name + "\nExpected weight: " + find(root));
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public static int find(final Program root) {
		Map<Integer, List<Program>> counter = new HashMap<>();
		
		for(Program sub : root.holdingPrograms) {
			int res = find(sub);
			
			if(res != -1) return res;
			
			counter.computeIfAbsent(sub.getWeight(), ArrayList::new).add(sub);
		}
		
		switch(counter.size()) {
			case 0:
			case 1: return -1;
			case 2:
				int right = 0, wrong = 0;
				Program wrongProg =null;
				
				for(Map.Entry<Integer, List<Program>> entry : counter.entrySet()) {
					List<Program> matched = entry.getValue();
					
					if(matched.size() == 1) {
						wrong = entry.getKey();
						wrongProg = matched.get(0);
					} else {
						right = entry.getKey();
					}
				}
				return wrongProg.weight + (right - wrong);
			default:
				throw new RuntimeException("Something went wrong...");
		}
	}
	
	static class Program {		
		String name;
		int weight, combinedWeight = -1;
		ArrayList<String> pendingPrograms = new ArrayList<>();
		ArrayList<Program> holdingPrograms = new ArrayList<>();
		Program root = null;
		
		public int getWeight() {
			return combinedWeight != -1 ? combinedWeight		
				: (combinedWeight = 
				holdingPrograms.stream()
					.map(Program::getWeight)
					.reduce(weight, (i, last) -> i + last)
				);
		}

	}

	public static Program parseTree(final Scanner s) {
		Map<String, Program> allPrograms = new HashMap<>();
		String cur;

		while(s.hasNext()) {
			Program p = new Program();
			
			// Name
			p.name = s.next();
			
			// Gewicht
			cur = s.next();
			p.weight = Integer.parseInt(cur.substring(1, cur.length() - 1));
			
			if(s.findInLine("->") != null) {
				while(true) {
					cur = s.next();
					
					if(cur.charAt(cur.length()-1) == ',') {
						addSubProgramm(cur.substring(0, cur.length() - 1), p, allPrograms);
					} else {
						addSubProgramm(cur, p, allPrograms);
						break;
					}
				}
			}
			
			allPrograms.put(p.name, p);
		}
		s.close();
		
		finishPending(allPrograms);
		
		return findRoot(allPrograms);
	} 

	private static void addSubProgramm(String name, Program p, Map<String, Program> allPrograms) {
		Program sub = allPrograms.get(name);
		
		if(sub == null) {
			p.pendingPrograms.add(name);
		} else {
			p.holdingPrograms.add(sub);
			sub.root = p;
		}
	}

	private static void finishPending(Map<String, Program> allPrograms) {
		for(Program p : allPrograms.values()) {
			for(String subName : p.pendingPrograms) {
				Program sub = allPrograms.get(subName);
				sub.root = p;
				p.holdingPrograms.add(sub);
			}
			
			p.pendingPrograms.clear();
		}
	}
	
	private static Program findRoot(Map<String, Program> allPrograms) {
		Program cur = allPrograms.values().iterator().next();
		
		while(cur.root != null) {
			cur = cur.root;
		}
		
		return cur;
	}
	
	@SuppressWarnings("unused")
	private static void print(final Program root, final int depth) {
		for(int i = 0; i < depth; i++) System.out.print('\t');
		System.out.println(root.name + " (" + root.combinedWeight + " [" + root.weight + "])");
		
		for(Program sub : root.holdingPrograms) {
			print(sub, depth +1);
		}
	}
}
