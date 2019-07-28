module aoc.y2016.d03;

import std;

void main() {
	version(unittest) {} else {
		const input = stdin.byLine.map!parseTriangle.array;
		const a = countValidTrianglesA(input);
		const b = countValidTrianglesB(input);
		writefln!"a: %d\nb: %d"(a, b);
	}
}

pure @safe:

alias Triangle = int[3];

Triangle parseTriangle(scope const(char)[] triangle) {
	typeof(return) result = void;
	
	foreach(ref cord; result) {
		triangle = triangle.stripLeft();
		cord = parse!int(triangle);
	}
	
	return result;
}

unittest {
	assert(parseTriangle("  1   2  3"		) == [1,2,3]);
	assert(parseTriangle("  221   112 4443"	) == [221,112,4443]);
}

nothrow:

auto countValidTrianglesA(R)(auto ref scope R range) {	
	return range.filter!isValid.count;
}

unittest {
	enum Triangle[] array = [
	[  4,   2,  3  ],
	[ 10,  10,  19 ],
	[  5,  10,  25 ],
	[  5, 100,  25 ],
	[ 50,  10,  25 ],
	];
	assert(2 == countValidTrianglesA(array));
}

bool isValid()(scope auto ref const Triangle triangle) {
	return isValid(triangle[0], triangle[1], triangle[2]);
}

bool isValid()(const int a, const int b, const int c) {
	return (a + b) > c
		&& (a + c) > b
		&& (b + c) > a;
}

unittest {
	assert(isValid([4, 2, 3]));
	assert(isValid([10, 10, 19]));
	assert(!isValid([5, 10, 25]));
	assert(!isValid([5, 100, 25]));
	assert(!isValid([50, 10, 25]));
}

uint countValidTrianglesB(const scope Triangle[] input) {	
	uint valid = 0;
	
	for(size_t idx = 0; idx < input.length - 2; idx += 3) {
		static foreach(const col; 0 .. 3) {
			if(isValid(
				input[idx][col],
				input[idx + 1][col],
				input[idx + 2][col]
			)) {
				valid++;
			}
		}
	}
	
	return valid;
}
unittest {
	enum Triangle[] input = [
		[101, 301, 501],
		[102, 302, 502],
		[103, 303, 503],
		[201, 401, 601],
		[202, 402, 602],
		[203, 403, 603]
	];
	
	assert(6 == countValidTrianglesB(input));
}