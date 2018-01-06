
public class Day23_Translated {

	public static void main(String[] args) {
		step5();
	}
	
	@SuppressWarnings("unused")
	private static void base() {
		long a = 1, b = 0, c = 0, d = 0, e = 0, f = 0, g = 0, h = 0;

		b = 81;
		c = b;

		if (a != 0) {
			b *= 100;
			b -= -100_000;
			c = b;
			c -= -17_000;
		}

		do {
			f = 1;
			d = 2;

			do {
				e = 2;
				
				do {
					g = d;
					g *= e;
					g -= b;
	
					if (g == 0) {
						f = 0;
					}
	
					e -= -1;
					g = e;
					g -= b;
	
				} while (g != 0);

				d -= -1;
				g = d;
				g -= b;
			} while(g != 0);
			
			if(f == 0) {
				h -= -1;
			}
			
			g = b;
			g -= c;
			
			if(g == 0) {
				break;
			}
			
			b-= -17;
		} while (true);
		
		System.out.println(h);
	}
	
	// Remove a and unify expressions
	@SuppressWarnings("unused")
	private static void step1() {
		long b = 0, c = 0, d = 0, e = 0, f = 0, g = 0, h = 0;

		b = 81 * 100 - -100_000; // = 108100
		c = b - -17_000;		 // = 125100

		do {
			f = 1;
			d = 2;

			do {
				e = 2;
				
				do {
					g = d * e - b;
	
					if (g == 0) {
						f = 0;
					}
	
					e -= -1;
					g = e - b;
	
				} while (g != 0);

				d -= -1;
				g = d - b;
			} while(g != 0);
			
			if(f == 0) {
				h -= -1;
			}
			
			g = b - c;
			
			if(g == 0) {
				break;
			}
			
			b-= -17;
		} while (true);
		
		System.out.println(h);
	}
	
	// g is always used as 
	// g = (valA) - valB; 
	// if(g == 0) ...
	// => if((valA) == valB)
	//
	// make f a boolean (f € {0,1}) as it just decides to increment h
	@SuppressWarnings("unused")
	private static void step2() { 
		boolean ink;

		long b = 108100;
		final long c = 125100;

		long d = 0, e = 0, h = 0;
		
		do {
			ink = false;
			d = 2;

			do {
				e = 2;
				
				do {	
					if (d * e == b) {
						ink = true;
					}
	
					e -= -1;
	
				} while (e != b);

				d -= -1;
			} while(d != b);
			
			if(ink) {
				h -= -1;
			}
			
			if(b == c) {
				break;
			}
			
			b-= -17;
		} while (true);
		
		System.out.println(h);
	}
	
	// Reduce scope and remove x - -21 ...
	@SuppressWarnings("unused")
	private static void step3() { 
		boolean ink;

		long b = 108100;
		final long c = 125100;

		long h = 0;
		
		do {
			ink = false;
			long d = 2;

			do {
				long e = 2;
				
				do {	
					if (d * e == b) {
						ink = true;
					}
	
					e++;
	
				} while (e != b);

				d++;
			} while(d != b);
			
			if(ink) {
				h++;
			}
			
			if(b == c) {
				break;
			}
			
			b += 17;
		} while (true);
		
		System.out.println(h);
	}
	
	// Loop transformation
	@SuppressWarnings("unused")
	private static void step4() { 
		boolean ink;
		int b = 108100, h = 0;
		
		while(true) {
			ink = false;

			for(long d = 2; d != b; d++) {			// We can ignore b = 2
				
				for(long e = 2; e != b; e++) {		// We can ignore b = 2

					if (d * e == b) {
						ink = true;
					}
				}
			}
			
			if(ink) {
				h++;
			}
			
			if(b == 125100) {
				break;
			}

			b+=17;
		}
		
		System.out.println(h);
	}
	
	// Optimization
	private static void step5() { 
		long b = 108100, h = 0;
		
		while(true) {
			long max = b/2;
			
			Outer:
			for(long d = 2; d <= max; d++) {		// e starts at 2
				
				for(long e = 2; e <= max; e++) {	// d starts at 2
					long m = d * e;
					
					if (m == b) {
						h++;					// ink stays true, we can break the loops
						break Outer;
					} else if(b < m) {
						break;					// m will be even bigger on next iteration
					}
				}
			}
			
			if(b == 125100) {
				break;
			}

			b+=17;
		}
		
		System.out.println(h);
	}
}
