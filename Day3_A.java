
public class Day3_A {

	public static void main(String[] args) {
		int[] tests = {1, 12, 23, 1024, 361527};
		
		for(int t : tests) {
			System.out.println(t + ": " + steps(t));
		}
	}
	
	static class Square {
		private int xOffset = 0, yOffset = 0, number = 1;
		private final int max;
		
		public Square(int max) {
			this.max = max;
		}
		
		public void up(int i) {
			yOffset += inkr(i);
		}
		
		public void right(int i) {
			xOffset += inkr(i);
		}
		
		public void down(int i) {
			yOffset -= inkr(i);
		}
		
		public void left(int i) {
			xOffset -= inkr(i);
		}
		
		private int inkr(int i) {
			int res = max - number - i;
			
			if(res < 0) {
				i = i + res;
			}
			
			number += i;
			return i;
		}
		
		public boolean running() {
			return number < max;
		}

		public int manhattan() {
			return betrag(xOffset) + betrag(yOffset);
		}
		
		private static int betrag(int x) {
			return x < 0 ? -x : x;
		}
		
		private void print() {
			System.out.printf("%d\t(%d,%d)\t%d Steps%n", number, xOffset, yOffset, number-1);
		}
	}
	
	public static int steps(final int number) {
		Square pos = new Square(number);
		int size = 0;
		
		pos.print();

		while(pos.running()){
			pos.right(1);		pos.print();
			pos.up(size + 1);	pos.print();
			
			size+=2;
			
			pos.left(size);		pos.print();
			pos.down(size);		pos.print();
			pos.right(size);	pos.print();
		}
		
		return pos.manhattan();
	}
}
