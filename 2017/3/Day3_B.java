import java.util.HashMap;
import java.util.Map;

public class Day3_B {

	public static void main(String[] args) {
		int[] tests = {1, 12, 23, 1024, 361527};
		
		for(int t : tests) {
			System.out.println(t + ": " + steps(t));
		}
	}
	
	static class Square {
		class Point {
			int x, y;
			
			public Point() { this(0,0); }
			
			public Point(int x, int y) {
				this.x = x;
				this.y = y;
			}
			
			@Override
			public String toString() {
				return "(" + x + "," + y + ")";
			}
			
			@Override
			public int hashCode() {
				return x * y;
			}
			
			@Override
			public boolean equals(Object o) {
				Point p = (Point) o;
				return x == p.x && y == p.y;
			}
		}
		
		Map<Point, Integer> memory = new HashMap<>();
		
		private int xOffset = 0, yOffset = 0;
		private final int max;
		
		public Square(int max) {
			this.max = max;
			this.memory.put(new Point(0,0), 1);
		}
		
		private void sumNeighbours() {
			int sum = 0;
			
			Point neighbour = new Point();
			
			for(int i = -1; i <= 1; i++) {
				for(int j = -1; j <= 1; j++) {
					neighbour.x = xOffset + i;
					neighbour.y = yOffset + j;
					sum += memory.getOrDefault(neighbour, 0);
				}
			}
			
			neighbour.x = xOffset;
			neighbour.y = yOffset;
			
			memory.put(neighbour, sum);

			if(result == -1 && sum >= max) {
				result = sum;
			}
		}
		
		public void up(int stps) {			
			for(int i = 0; i < stps; i++) {
				yOffset++;
				sumNeighbours();				
			}
		}
		
		public void right(int stps) {
			for(int i = 0; i < stps; i++) {
				xOffset++;
				sumNeighbours();				
			}
		}
		
		public void down(int stps) {
			for(int i = 0; i < stps; i++) {
				yOffset--;
				sumNeighbours();				
			}
		}
		
		public void left(int stps) {
			for(int i = 0; i < stps; i++) {
				xOffset--;
				sumNeighbours();				
			}
		}

		private int result = -1;
		
		public boolean running() {
			return result == -1;
		}
		
		public int result() {
			return result;
		}
	}
	
	public static int steps(final int number) {
		Square pos = new Square(number);
		int size = 0;
		
		while(pos.running()){
			pos.right(1);
			pos.up(size + 1);
			
			size+=2;
			
			pos.left(size);
			pos.down(size);
			pos.right(size);
		}
		
		return pos.result();
	}
}
