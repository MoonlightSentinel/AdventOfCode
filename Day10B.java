import java.util.Arrays;

public class Day10B {

	private static final String[] tasks = {"", "AoC 2017", "1,2,3", "1,2,4", "199,0,255,136,174,254,227,16,51,85,1,2,22,17,7,192"};
	
	public static void main(String[] args) {
		for(String s : tasks) {
			System.out.println(s);
			System.out.println(solve(s));
		}
	}

	private static char[] toAppend = {17, 31, 73, 47, 23};
	
	public static String solve(String input) {
		int[] list = new int[256];
		Arrays.setAll(list, i -> i);
		
		char[] lengths = Arrays.copyOf(input.toCharArray(), input.length() + toAppend.length);
		
		int pos = input.length();
		for(char toA : toAppend) lengths[pos++] = toA;
		
		return solve(lengths, list);
	}

	private static String solve(char[] input, int[] list) {
		int position = 0, skipSize = 0;
		
		for(int i = 0; i < 64; i++) {
			for(int in : input) {
				reverse(list, position, in);
				position = (position + in + skipSize) % list.length;
				skipSize++;
	//			System.out.println(Arrays.toString(list));
			}
		}
		return denseHash(list);
	}

	private static String denseHash(int[] list) {
		StringBuilder sb = new StringBuilder(list.length / 16);
		
		for(int i = 0; i < list.length;) {
			int res = 0;
			for(int j = 0; j < 16; j++) {
				res ^= list[i];
				i++;
			}
			
			String hex = Integer.toHexString(res);
			if(hex.length() == 1) sb.append('0'); 
			sb.append(hex);
		}
		
		return sb.toString();
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
