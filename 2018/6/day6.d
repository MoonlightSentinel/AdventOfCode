import std.algorithm;

void main() {
	version(unittest){} else {

		import std.stdio, std.array : array;

		const input = File("day6.in", "r").byLine.map!(l => Point(l)).array;

		writefln("1: %d", part1(input));
		writefln("2: %d", part2(input));
	}
}

struct Point {
	int x, y;

	this(int x, int y) {
		this.x = x;
		this.y = y;
	}

	this(scope const(char)[] line) {
		import std.format : formattedRead;

		line.formattedRead!"%d, %d"(x, y);
	}

	int distanceTo(const ref Point other) {
		import std.math : abs;

		return abs(x - other.x) + abs(y - other.y);
	}
}

struct Counter {
	int value;
	bool infinite;
}

size_t part1(const Point[] input) {
	const maxX = input.map!"a.x".maxElement + 1,
		maxY = input.map!"a.y".maxElement + 1;

	auto counter = new Counter[](input.length);

	foreach(const int x; 0 .. maxX) {
		foreach(const int y; 0 .. maxY) {

			bool multi = false;
			int minDistance = int.max;
			size_t owner = -1;

			foreach(const id, ref const point; input) {

				const int distance = Point(x,y).distanceTo(point);

				if(distance < minDistance) {
					owner = id;
					minDistance = distance;
					multi = false;
				}
				else if(distance == minDistance) {
					multi = true;
					owner = -1;
				}
			}

			if(!multi) {
				counter[owner].value++;

				if(x == 0 || x == (maxX - 1) || y == 0 || y == (maxY - 1)) {
					counter[owner].infinite = true;
				}
			}
		}
	}

	return counter.filter!"!a.infinite".map!"a.value".maxElement;
}

uint part2(const Point[] input) {
	const maxX = input.maxElement!"a.x".x + 1,
		maxY = input.maxElement!"a.y".y + 1;

	uint counter = 0;

	foreach(const x;  0 .. maxX) {
		foreach(const y; 0 .. maxY) {
			uint distanceSum = 0;

			foreach(ref const point; input) {
				distanceSum += Point(x,y).distanceTo(point);
			}

			version(unittest) enum MAX = 32;
			else enum MAX = 10000;

			if(distanceSum < MAX) counter++;
		}
	}

	return counter;
}

unittest {
	import std.array : array;

	enum exampleStr = [
		"1, 1",
		"1, 6",
		"8, 3",
		"3, 4",
		"5, 5",
		"8, 9"
	];

	const example = exampleStr.map!(l => Point(l)).array;

	assertEquals(part1(example), 17);
	assertEquals(part2(example), 16);
}
version(unittest)
void assertEquals(A, B)(const A got, const B expected) {
	import std.format : format;

	assert(got == expected, format!"Expected %s but got %s!"(expected, got));
}