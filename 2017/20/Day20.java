import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day20 {

	private static class Vector {
		private int x,y,z;
	
		public Vector(String s){
			String[] vals = s.substring(1, s.length()-1).split(",");
			x = Integer.parseInt(vals[0]);
			y = Integer.parseInt(vals[1]);
			z = Integer.parseInt(vals[2]);
		}

		public Vector(Vector other) {
			this.x = other.x;
			this.y = other.y;
			this.z = other.z;
		}

		public String toString() {
			return "<" + x + ',' + y + ',' + z + '>';
		}

		public void  plus(final Vector other) {
			this.x += other.x;
			this.y += other.y;
			this.z += other.z;
		}

		public int manhattan() {
			return Math.abs(x) + Math.abs(y) + Math.abs(z);
		}
		
		public boolean equals(Vector other) {
			return x == other.x && y == other.y && z == other.z;
		}
	};

	private static class Point implements Comparable<Point> {
		private Vector position, velocity, acceleration;

		public Point(String p, String v, String a) {
			position = new Vector(p);
			velocity  = new Vector(v);
			acceleration = new Vector(a);
		}

		public Point(Point other) {
			position = new Vector(other.position);
			velocity = new Vector(other.velocity);
			acceleration = new Vector(other.acceleration);
		}
	
		public String toString() {
			return "p=" + position + ", v=" + velocity + ", a=" + acceleration;
		}

		public void update() {
			velocity.plus(acceleration);
			position.plus(velocity);
		}

		public int manhattan() {
			return position.manhattan();
		}
		
		public int compareTo(Point other) {
			return manhattan() - other.manhattan();
		}
		
		public boolean equals(Object o) {
			return ((Point) o).position.equals(position);
		}
	}

	public static void main(String[] args) {
		try (Scanner in = new Scanner(new File("src/Day20.in"))) {
			solve(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void solve(Scanner in) {
		List<Point> list = parse(in),
			copy = new ArrayList<>(list.size());
		list.forEach(p -> copy.add(new Point(p)));

		System.out.println("A: " + solveA(list));
		System.out.println("B: " + solveB(list));
	}
	
	private static int solveA(List<Point> list) {
		Point smallest = null, newSmallest;
		int counter = 0;
		
		while(counter < 5000) {
			list.forEach(Point::update);
			
			newSmallest = Collections.min(list);
			
			if(smallest == newSmallest) {
				counter++;
			} else {
				counter = 0;
				smallest = newSmallest;
			}
		}
		
		return list.indexOf(smallest);
	}

	private static int solveB(List<Point> list) {
		int counter = 0;
		
		while(counter < 1000) {			
			if(removeCollisions(list)) {
				counter = 0;
			}
			
			counter++;
			list.forEach(Point::update);
		}
		
		return list.size();
	}
	
	private static boolean removeCollisions(List<Point> list) {
		boolean res = false, found;
		
		for(int i = 0; i < list.size();) {
			Point cur = list.get(i);
			found = false;
			
			for(int j = i + 1; j < list.size();) {
				if(list.get(j).equals(cur)) {
					found = true;
					list.remove(j);
				} else {
					j++;
				}
			}
			
			if(found) {
				list.remove(i);
				res = true;
			} else {
				i++;
			}
		}
		return res;
	}

	private static List<Point> parse(final Scanner input) {
		List<Point> res = new ArrayList<>();
		
		Point point;
		String[] parts;
		
		while(input.hasNextLine()) {
			parts = input.nextLine().substring(2).split(", .=");

			point = new Point(parts[0], parts[1], parts[2]);
			res.add(point);
		}
		
		input.close();
		return res;
	}
}
