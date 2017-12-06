import java.util.ArrayList;
import java.util.Arrays;

public class Day6 {

	private static final int[][] list = {
			{0,2,7,0},
			{10,3,	15,	10,	5,	15,	5,	15,	9,	2,	5,	8,	5,	2,	3,	6},
	};
	
	public static void main(String[] args) {
		for (int[] l : list) {
			System.out.println(count(l));
		}
	}

	public static int count(final int[] list) {
		int count = 0;
		ArrayList<int[]> old = new ArrayList<>();
		
		for(int index;;){
			
			int cicle = 0;
			
			for(int[] oldState : old) {
				if(Arrays.equals(list, oldState)) return count - cicle;
				cicle++;
			}
			
			old.add(Arrays.copyOf(list, list.length));
			
			index = 0;
			
			for(int i = 1; i < list.length; i++) {
				if(list[i] > list[index]) index = i;
			}
			
			int toDistribute = list[index];
			list[index] = 0;
			
			while(toDistribute > 0) {
				index = (index+1) % list.length;
				list[index]++;
				toDistribute--;
			}
			
			count++;
		}
	} 
}
