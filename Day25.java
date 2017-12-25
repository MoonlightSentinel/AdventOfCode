import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Day25 {

	private static enum Move {
		Right, Left
	}
	
	private static enum State { 
		A, B, C, D, E, F 
	}
	
	private static class StateInfo {
		
		private int onZero, onOne;
		private Move onZeroM, onOneM;
		private State onZeroNext, onOneNext;
		
		
		private StateInfo(int onZero, Move onZeroM, State onZeroNext, int onOne, Move onOneM, State onOneNext) {
			this.onZero = onZero;
			this.onOne = onOne;
			this.onZeroM = onZeroM;
			this.onOneM = onOneM;
			this.onZeroNext = onZeroNext;
			this.onOneNext = onOneNext;
		}
	}
	
	private static final StateInfo 
	A = new StateInfo(1, Move.Right, State.B, 0, Move.Left, State.E),
	B = new StateInfo(1, Move.Left, State.C, 0, Move.Right, State.A),
	C = new StateInfo(1, Move.Left, State.D, 0, Move.Right, State.C),
	D = new StateInfo(1, Move.Left, State.E, 0, Move.Left, State.F),
	E = new StateInfo(1, Move.Left, State.A, 1, Move.Left, State.C),
	F = new StateInfo(1, Move.Left, State.E, 1, Move.Right, State.A);
	
	private static class Tape {
		private int cur = 0;
		private ArrayList<Integer> pos = new ArrayList<>(), neg = new ArrayList<>();
		
		public void set(final int value) {
			List<Integer> l;
			int ind;
			
			if(cur < 0) {
				l = neg;
				ind = -cur;
			} else {
				l = pos;
				ind = cur;
			}
			
			if(l.size() <= ind) {
				l.add(value);
			} else {
				l.set(ind, value);
			}
		}

		public int get() {
			List<Integer> l;
			int ind;
			
			if(cur < 0) {
				l = neg;
				ind = -cur;
			} else {
				l = pos;
				ind = cur;
			}
			
			if(l.size() <= ind) {
				return 0;
			} else {
				return l.get(ind);
			}
		}

		public void move(Move dir) {
			if(dir == Move.Left) {
				cur++;
			} else {
				cur--;
			}
		}

		public int checksum() {
			return count(pos) + count(neg);
		}
		
		private static int count(List<Integer> list) {
			int counter = 0;
			
			for(Integer i : list) {
				if( i== 1) counter++;
			}
			
			return counter;
		}
	}
	
	public static void main(String[] args) {
		Tape tape = new Tape();
		StateInfo cur = A;
		State next;
		
		for(int i = 0; i < 12208951; i++) {
			int val = tape.get();
			
			if(val == 0) {
				tape.set(cur.onZero);
				tape.move(cur.onZeroM);
				next = cur.onZeroNext;
			} else {
				tape.set(cur.onOne);
				tape.move(cur.onOneM);
				next = cur.onOneNext;
			}
			
			switch(next) {
			case A: cur = A; break;
			case B: cur = B; break;
			case C: cur = C; break;
			case D: cur = D; break;
			case E: cur = E; break;
			case F: cur = F; break;
			}
		}
		
		System.out.println(tape.checksum());
	}

}
