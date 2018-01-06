import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Idgaf {

	public static void main(String[] args) {
		Queue<Long> a = new ArrayDeque<>(), b = new ArrayDeque<>();
		Idgaf p0 = new Idgaf(0, a, b),
					p1 = new Idgaf(0, b, a);
		
		while(p0.execute() | p1.execute());
		
		System.out.println(p1.sends / 2);
	}	
	
	private int counter, sends, executed;
	private long a, b, f, i, p;
	private Queue<Long> sendQueue, receiveQueue;
	
	public Idgaf(int id, Queue<Long> sendQueue, Queue<Long> receiveQueue) {
		this.counter = 0;
		this.a = 0;
		this.b = 0;
		this.f = 0;
		this.i = 0;
		this.p = id;
		this.sends = 0;
		this.sendQueue = sendQueue;
		this.receiveQueue = receiveQueue;
	}

	private boolean execute() {
		executed = 0;
		
		try {
			run();
		} catch(NoSuchElementException e) {}
		
		return executed != 0;
	}
	
	private void send(long val) {
		sendQueue.add(val);
		sends++;
	}

	private long rcv() {
		return receiveQueue.remove();
	}
	
	private void jgz(long x, long y) {
		if(0 < x) {
			counter += y - 1;
		}
	}
	
	private void run() {
		while (true) {
			switch(counter) {
				case 0: i = 31;			break;
				case 1: a = 1;			break;
				case 2: p *= 17;		break;
				case 3: jgz(p, p);		break;
				case 4: a *= 2;			break;
				case 5: i += -1;		break;
				case 6: jgz(i, -2);		break;
				case 7: a += -1;		break;
				case 8: i = 127;		break;
				case 9: p = 464;		break;
				case 10: p *= 8505;		break;
				case 11: p %= a;		break;
				case 12: p *= 129749;	break;
				case 13: p += 12345;	break;
				case 14: p %= a;		break;
				case 15: b = p;			break;
				case 16: b %= 10000;	break;
				case 17: send(b);		break;
				case 18: i += -1;		break;
				case 19: jgz(i, -9);	break;
				case 20: jgz(a, 3);		break;
				case 21: b = rcv();		break;
				case 22: jgz(b, -1);	break;
				case 23: f = 0;			break;
				case 24: i = 126;		break;
				case 25: a = rcv();		break;
				case 26: b = rcv();		break;
				case 27: p = a;			break;
				case 28: p *= -1;		break;
				case 29: p += b;		break;
				case 30: jgz(p, 4);		break;
				case 31: send(a);		break;
				case 32: a = b;			break;
				case 33: jgz(1, 3);		break;
				case 34: send(b);		break;
				case 35: f = 1;			break;
				case 36: i += -1;		break;
				case 37: jgz(i, -11);	break;
				case 38: send(a);		break;
				case 39: jgz(f, -16);	break;
				case 40: jgz(a, -19);	break;
				default: return;
			}
			counter++;
			executed++;
		}
	}
}