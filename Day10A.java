import java.util.Arrays;

public class Day10A {

	private static final int[] input =  {199,0,255,136,174,254,227,16,51,85,1,2,22,17,7,192};
	
	public static void main(String[] args) {
		System.out.println(solve(new int[] {3, 4, 1, 5}, new int[] {0,1,2,3,4}));
		System.out.println(solve(input));
	}

	private static int solve(int[] input) {
		int[] list = new int[256];
		Arrays.setAll(list, i -> i);
		return solve(input, list);
	}

	private static int solve(int[] input, int[] list) {
		int position = 0, skipSize = 0;
		
		for(int in : input) {
			reverse(list, position, in);
			position = (position + in + skipSize) % list.length;
			skipSize++;
		}
		return list[0] * list[1];
	}

	private static void reverse(int[] list, int position, int amaount) {
		int end = (position + amaount - 1) % list.length;
		
		while(amaount > 0) {
			int tmp = list[end];
			list[end] = list[position];
			list[position] = tmp;
				
			position = (position + 1) % list.length;
			end = (end + (list.length - 1)) % list.length;
			amaount -= 2;
		}
	}
}
