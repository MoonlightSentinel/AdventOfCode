void main() {
	version(unittest){} else {
		import std.stdio;

		enum input = 7139;

		const res1 = part1(input);

		writefln!"1: %d,%d (=> %d power level)"(res1.x, res1.y, res1.power);

		const res2 = part2(input);

		writefln!"2: %d,%d,%d (=> %d power level)"(res2.x, res2.y, res2.size, res2.power);
	}
}

struct Result { 
	int x, y, size, power; 
}

Result part1(const int serial, const int size = 3) pure nothrow @nogc @safe {

	Result result;
	result.size = size;
	result.power = int.min;

	foreach(const xStart; 1 .. 300 - size + 1) {
		foreach(const yStart; 1 .. 300 - size + 1) {			

			int power = 0;

			foreach(const x; xStart .. xStart + size) {
				foreach(const y; yStart .. yStart + size) {
					power += powerLevel(x, y, serial);
				}
			}

			if(result.power < power) {
				result.power = power;
				result.x = xStart;
				result.y = yStart;
			}
		}
	}

	return result;
}

unittest {
	with(part1(18)) {
		assertEquals(x, 33);
		assertEquals(y, 45);
		assertEquals(power, 29);
	}

	with(part1(42)) {
		assertEquals(x, 21);
		assertEquals(y, 61);
		assertEquals(power, 30);
	}
}

Result part2(const int serial) {
	import std.algorithm : maxElement;
	import std.parallelism : parallel;
	import std.range : iota;

	auto results = new Result[300];

	foreach(ind, ref result; parallel(results)) {
		result = part1(serial, ind + 1);
	}

	return results.maxElement!"a.power";
}

unittest {
	with(part2(18)) {
		assertEquals(x, 90);
		assertEquals(y, 269);
		assertEquals(power, 113);
		assertEquals(size, 16);
	}

	with(part2(42)) {
		assertEquals(x, 232);
		assertEquals(y, 251);
		assertEquals(power, 119);
		assertEquals(size, 12);
	}
}

int powerLevel(const int x, const int y, const int serial) pure nothrow @nogc @safe {
	const rackId = x + 10;
	
	int power = rackId * y;
	power += serial;
	power *= rackId;
	power = (power / 100) % 10;
	power -= 5;

	return power;
}

unittest {
	assertEquals(powerLevel(3, 5, 8), 4);
	assertEquals(powerLevel(122, 79, 57), -5);
	assertEquals(powerLevel(217, 196, 39), 0);
	assertEquals(powerLevel(101, 153, 71), 4);
}

version(unittest)
void assertEquals(A, B)(const A got, const B expected) {
	import std.format : format;

	assert(got == expected, format!"Expected %s but got %s!"(expected, got));
}